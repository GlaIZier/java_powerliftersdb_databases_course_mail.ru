package ru.glaizier.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.glaizier.domain.City;
import ru.glaizier.domain.FirstPowerlifterAfterDate;
import ru.glaizier.domain.Powerlifter;
import ru.glaizier.domain.Powerlifter_;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

/**
 * This class contains methods to get data using Hibernate specific API (hibernate.cfg.xml)
 * and JPA API (persistance.xml)
 */
public class HibernateDao {

    private final StandardServiceRegistry registry;

    private final SessionFactory sessionFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final EntityManager entityManager;

    public HibernateDao() {
        registry = new StandardServiceRegistryBuilder()
                .configure() // get settings from hibernate.cfg.xml from classpath
                .build();
        sessionFactory = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();

        entityManagerFactory =
                Persistence.createEntityManagerFactory("ru.glaizier.powerliftersdb.jpa");

        entityManager = entityManagerFactory.createEntityManager();
    }

    public City testHibernateCityMapping() {
        try (Session session = sessionFactory.openSession()) {
            return session.get(City.class, 1);
        }
    }

    /**
     * Because we have lazy initialization we need to get city within session or we'll get LazyInitializationException \
     * because of the no session
     */
    public FirstPowerlifterAfterDate testHibernatePowerliftingMapping() {
        try (Session session = sessionFactory.openSession()) {
            Powerlifter powerlifter = session.get(Powerlifter.class, 1);
            return new FirstPowerlifterAfterDate(powerlifter.getPowerlifterId(), powerlifter.getLastName(), powerlifter.getFirstName(),
                    powerlifter.getSex(), powerlifter.getBirthdate(), powerlifter.getCity().getCityName());
        }
    }

    public void testJpa() {
        List<City> result = entityManager.createQuery("from City", City.class).getResultList();
        for (City city : result) {
            System.out.println("City (" + city.getCityId() + ") : " + city.getCityName());
        }
    }

    public void testQueryApi() {
        List<City> cities = entityManager.createQuery(
                "select c " +
                        "from City c " +
                        "where c.cityName like :name", City.class
        ).setParameter("name", "City1%")
                .getResultList();
        for (City city : cities) {
            System.out.println("City (" + city.getCityId() + ") : " + city.getCityName());
        }
    }

    public void testQueryApiObject() {
        List<Powerlifter> objects = entityManager.createQuery(
                "select p " +
                        "from Powerlifter p " +
                        "join p.city c " +
                        "where p.powerlifterId = :powerlifterId"
                , Powerlifter.class
        )
                .setParameter("powerlifterId", 1)
                .getResultList();
        for (Powerlifter objs : objects) {
            System.out.println("Object (" + objs.getPowerlifterId() + ")");
        }
    }

    /*
    * select powerlifter.last_name, city.city_name
    * from powerlifter
    * join city
    * on (powerlifter.city_id = city.city_id);
    * */
    public void testJpaCriteriaJoin() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Get powerlifter
        CriteriaQuery<Powerlifter> powerlifterCriteriaQuery = builder.createQuery(Powerlifter.class);
        Root<Powerlifter> powerlifterRoot = powerlifterCriteriaQuery.from(Powerlifter.class);
        // Join city to it
        Join<Powerlifter, City> powerlifterCityJoin = powerlifterRoot.join(Powerlifter_.city);
        powerlifterCriteriaQuery.where(builder.equal(powerlifterRoot.get(Powerlifter_.powerlifterId), 1));

        Powerlifter powerlifter = entityManager.createQuery(powerlifterCriteriaQuery).getSingleResult();
        System.out.println("powerlifter.getLastName() = " + powerlifter.getLastName());
        System.out.println("powerlifter.getCity().getCityName() = " + powerlifter.getCity().getCityName());

        /**
         * Bad variant because it use two queries. First it fetches city join powerlifter where powerlifter_id =?
         * Then fetches all powerlifters with city_id = city_id from 1st query.
         * So we get all powerlifters with city_id = city_id from first query
         *
         *   CriteriaQuery<City> criteria = builder.createQuery(City.class);
         Root<City> cityRoot = criteria.from(City.class);

         Join<City, Powerlifter> cityPowerlifterJoin = cityRoot.join(City_.powerlifters);
         criteria.where(builder.equal(cityPowerlifterJoin.get(Powerlifter_.powerlifterId), 1));

         City city = entityManager.createQuery(criteria).getSingleResult();
         System.out.println("city.getPowerlifters().get(0).getLastName() = " + city.getPowerlifters().get(0).getLastName());
         System.out.println("city.getCityName() = " + city.getCityName());
         System.out.println("city.getPowerlifters().get(0).getCountryId() = " + city.getPowerlifters().get(0).getCountryId());
         System.out.println("city.getPowerlifters().get(1).getLastName() = " + city.getPowerlifters().get(1).getLastName());
         */



    }

    /*
    * select *
    * from powerlifter
    * where powerlifter_id =
    *   (select powerlifter_id
    *   from powerlifter p
    *   where p.birthdate > :date
    *   )
    * order by birthdate
    * limit 1;
    * */
    public void testJpaCriteriaSubquery(Date date) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Powerlifter> mainCriteriaQuery = builder.createQuery(Powerlifter.class);
        Root<Powerlifter> powerlifterRoot = mainCriteriaQuery.from(Powerlifter.class); // from powerlifter
        CriteriaQuery<Powerlifter> select = mainCriteriaQuery.select(powerlifterRoot); // select *

        Subquery<Integer> powerlifterSubquery = mainCriteriaQuery.subquery(Integer.class);
        Root<Powerlifter> powerlifterSubqueryRoot = powerlifterSubquery.from(Powerlifter.class);
        Path<Integer> subqueryPowerlifterIdPath = powerlifterSubqueryRoot.get(Powerlifter_.powerlifterId);
        powerlifterSubquery.select(subqueryPowerlifterIdPath); // (select powerlifter_id
        Path<Date> subqueryPowerlifterBirthdatePath = powerlifterSubqueryRoot.get(Powerlifter_.birthdate);
        powerlifterSubquery.where(builder.greaterThanOrEqualTo(subqueryPowerlifterBirthdatePath, date)); // where p.birthdate > :date

        Path<Integer> powerlifterIdPath = powerlifterRoot.get(Powerlifter_.powerlifterId);
        select.where(powerlifterIdPath.in(powerlifterSubquery)); // where powerlifter_id in

        Path<Date> powerlifterBirthdatePath = powerlifterRoot.get(Powerlifter_.birthdate);
        select.orderBy(builder.asc(powerlifterBirthdatePath)); // order by birthdate

        List<Powerlifter> powerlifters = entityManager.createQuery(select).setMaxResults(1).getResultList(); // limit 1

        for (Powerlifter pw : powerlifters) {
            System.out.println("pw.getLastName() = " + pw.getLastName());
            System.out.println("pw.getBirthdate() = " + pw.getBirthdate());
        }
    }

    /**
     * Just used this overkill query to check joins and subqueries in hql, criteria and native '
     *
     * @param date first born powerlifter after this date
     */
    public FirstPowerlifterAfterDate getFirstPowerlifterAfterDateHql(Date date) {
        Object[] object = entityManager.createQuery(
                "select p.powerlifterId, p.lastName, p.firstName, p.sex, p.birthdate, c.cityName " +
                        "from Powerlifter p " +
                        "join p.city c " +
                        "where p.powerlifterId in (" +
                        "   select pin.powerlifterId " +
                        "   from Powerlifter pin" +
                        "   where pin.birthdate >= :date " +
                        "   order by pin.powerlifterId" +
                        ") " +
                        "order by p.birthdate"
                , Object[].class)
                .setParameter("date", date)
                .setMaxResults(1)
                .getSingleResult();

        return new FirstPowerlifterAfterDate((Integer) object[0], (String) object[1],
                (String) object[2], (Short) object[3], (Date) object[4], (String) object[5]);
    }

    /*
    * select powerlifter.last_name, city.city_name
    * from powerlifter
    * join city
    * on (powerlifter.city_id = city.city_id)
    * where powerlifter_id =
    *   (select powerlifter_id
    *   from powerlifter p
    *   where p.birthdate >= ':date
    *   )
    * order by powerlifter_id
    * limit 1
    * */
    public FirstPowerlifterAfterDate getFirstPowerlifterAfterDateJpaCriteria(Date date) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Get powerlifter
        CriteriaQuery<Powerlifter> mainCriteriaQuery = builder.createQuery(Powerlifter.class);
        Root<Powerlifter> powerlifterRoot = mainCriteriaQuery.from(Powerlifter.class);
        // Join city to it
        Join<Powerlifter, City> powerlifterCityJoin = powerlifterRoot.join(Powerlifter_.city); // join city on (powerlifter.city_id = city.city_id)
        CriteriaQuery<Powerlifter> select = mainCriteriaQuery.select(powerlifterRoot); // select *

        Subquery<Integer> powerlifterSubquery = mainCriteriaQuery.subquery(Integer.class);
        Root<Powerlifter> powerlifterSubqueryRoot = powerlifterSubquery.from(Powerlifter.class);
        Path<Integer> subqueryPowerlifterIdPath = powerlifterSubqueryRoot.get(Powerlifter_.powerlifterId);
        powerlifterSubquery.select(subqueryPowerlifterIdPath); // (select powerlifter_id
        Path<Date> subqueryPowerlifterBirthdatePath = powerlifterSubqueryRoot.get(Powerlifter_.birthdate);
        powerlifterSubquery.where(builder.greaterThanOrEqualTo(subqueryPowerlifterBirthdatePath, date)); // where p.birthdate > :date

        Path<Integer> powerlifterIdPath = powerlifterRoot.get(Powerlifter_.powerlifterId);
        select.where(powerlifterIdPath.in(powerlifterSubquery)); // where powerlifter_id in

        Path<Date> powerlifterBirthdatePath = powerlifterRoot.get(Powerlifter_.birthdate);
        select.orderBy(builder.asc(powerlifterBirthdatePath)); // order by birthdate

        List<Powerlifter> powerlifters = entityManager.createQuery(select).setMaxResults(1).getResultList(); // limit 1

        return new FirstPowerlifterAfterDate(powerlifters.get(0).getPowerlifterId(), powerlifters.get(0).getLastName(),
                powerlifters.get(0).getFirstName(), powerlifters.get(0).getSex(), powerlifters.get(0).getBirthdate(),
                powerlifters.get(0).getCity().getCityName());
    }


    public FirstPowerlifterAfterDate getFirstPowerlifterAfterDateNative(Date date) {
        List<Object[]> object = entityManager.createNativeQuery(
                "select p.powerlifter_id, p.last_name, p.first_name, p.sex, p.birthdate, c.city_name " +
                        "from powerlifter p " +
                        "join city c " +
                        "on (p.city_id = c.city_id) " +
                        "where p.powerlifter_id in (" +
                        "   select pin.powerlifter_id " +
                        "   from powerlifter pin" +
                        "   where pin.birthdate >= :date " +
                        "   order by pin.powerlifter_id" +
                        ") " +
                        "order by p.birthdate " +
                        "limit 1"
        )
                .setParameter("date", date)
                .getResultList();

        return new FirstPowerlifterAfterDate((Integer) object.get(0)[0], (String) object.get(0)[1],
                (String) object.get(0)[2], (Short) object.get(0)[3], (Date) object.get(0)[4], (String) object.get(0)[5]);
    }

    public void destroy() {
        StandardServiceRegistryBuilder.destroy(registry);
        sessionFactory.close();

        entityManager.close();
        entityManagerFactory.close();
    }

}
