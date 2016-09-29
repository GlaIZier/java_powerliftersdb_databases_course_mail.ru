package ru.glaizier.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.glaizier.domain.City;
import ru.glaizier.domain.Powerlifter;
import ru.glaizier.domain.Powerlifter_;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

// TODO one session for request (one transaction)
// TODO one session factory for the whole application
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
//                .addAnnotatedClass(City.class)
                .buildMetadata().buildSessionFactory();

        entityManagerFactory =
                Persistence.createEntityManagerFactory("ru.glaizier.powerliftersdb.jpa");

        entityManager = entityManagerFactory.createEntityManager();
    }

    public City testCityMapping() {
        try (Session session = sessionFactory.openSession()) {
            City city = session.get(City.class, 1);
            return city;
        }
    }

    public Powerlifter testPowerlifterMapping() {
        try (Session session = sessionFactory.openSession()) {
            Powerlifter powerlifter = session.get(Powerlifter.class, 1);
            return powerlifter;
        }
    }

    public void testJpa() {
        entityManager.getTransaction().begin();
        List<City> result = entityManager.createQuery("from City", City.class).getResultList();
        for (City city : result) {
            System.out.println("City (" + city.getCityId() + ") : " + city.getCityName());
        }
        entityManager.getTransaction().commit();
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

    /**
     * Just used this overkill query to check joins and subqueries in hql, criteria and native '
     *
     * @param date first born powerlifter after this date
     */
    public void getFirstPowerlifterAfterDateHql(Date date) {
        Object[] object = entityManager.createQuery(
                "select p.lastName, p.birthdate, c.cityName " +
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
        System.out.println("Hql: obj[0] = " + object[0]);
        System.out.println("Hql: obj[1] = " + object[1]);
        System.out.println("Hql: obj[2] = " + object[2]);
    }

    /*
    * select powerlifter.last_name, city.city_name
    * from powerlifter
    * join city
    * on (powerlifter.city_id = city.city_id)
    * where powerlifter_id =
    *   (select powerlifter_id
    *   from powerlifter
    *   where powerlifter.birthday >= '1970-01-01'::date
    *   order by powerlifter_id
    *   limit 1)
    * */
    public void getFirstPowerlifterAfterDateCriteria(Date date) {
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
         */

//        CriteriaQuery<City> criteria = builder.createQuery(City.class);
//        Root<City> cityRoot = criteria.from(City.class);
//
//        Join<City, Powerlifter> cityPowerlifterJoin = cityRoot.join(City_.powerlifters);
//        criteria.where(builder.equal(cityPowerlifterJoin.get(Powerlifter_.powerlifterId), 1));
//
//        City city = entityManager.createQuery(criteria).getSingleResult();
//        System.out.println("city.getPowerlifters().get(0).getLastName() = " + city.getPowerlifters().get(0).getLastName());
//        System.out.println("city.getCityName() = " + city.getCityName());
//        System.out.println("city.getPowerlifters().get(0).getCountryId() = " + city.getPowerlifters().get(0).getCountryId());
//        System.out.println("city.getPowerlifters().get(1).getLastName() = " + city.getPowerlifters().get(1).getLastName());


//        CriteriaQuery<Powerlifter> powerlifterCriteriaSub = builder.createQuery(Powerlifter.class);
//        DetachedCriteria subCriteria = DetachedCriteria.forClass(Powerlifter.class)
//                .add(Property.forName("birthday").gt(date))
//                .addOrder(Order.asc("birthday"));

    }

    public void getFirstPowerlifterAfterDateCriteriaSubquery(Date date) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Powerlifter> mainCriteriaQuery = builder.createQuery(Powerlifter.class);
        Root<Powerlifter> powerlifterRoot = mainCriteriaQuery.from(Powerlifter.class);
        CriteriaQuery<Powerlifter> select = mainCriteriaQuery.select(powerlifterRoot);

        Subquery<Powerlifter> powerlifterSubquery = mainCriteriaQuery.subquery(Powerlifter.class);
        Root<Powerlifter> powerlifterSubqueryRoot = powerlifterSubquery.from(Powerlifter.class);
        powerlifterSubquery.select(powerlifterSubqueryRoot.get("powerlifterId"));
        powerlifterSubquery.where(builder.equal(powerlifterSubqueryRoot.get("powerlifterId"), 1));
//        powerlifterSubquery.where(builder.greaterThanOrEqualTo(powerlifterSubqueryRoot.<Date>get("birthdate"), date));

//        mainCriteriaQuery.orderBy(builder.asc(powerlifterSubqueryRoot.get("birthdate")));

        select.where(builder.equal(powerlifterRoot.get(Powerlifter_.powerlifterId), powerlifterSubquery));

        List<Powerlifter> powerlifters = entityManager.createQuery(select).getResultList();

        for (Powerlifter pw : powerlifters) {
            System.out.println("pw.getLastName() = " + pw.getLastName());
            System.out.println("pw.getCity().getCityName() = " + pw.getBirthdate());
        }
    }


    public void getFirstPowerlifterAfterDateNative(Date date) {
        List<Object[]> object = entityManager.createNativeQuery(
                "select p.last_name, p.birthdate, c.city_name " +
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
        System.out.println("Native: obj[0] = " + object.get(0)[0]);
        System.out.println("Native: obj[1] = " + object.get(0)[1]);
        System.out.println("Native: obj[2] = " + object.get(0)[2]);
    }

    /*
    * select powerlifter.last_name, city.city_name
    * from powerlifter
    * join city
    * on (powerlifter.city_id = city.city_id)
    * where powerlifter_id =
    *   (select powerlifter_id
    *   from powerlifter
    *   where powerlifter.birthday >= '1970-01-01'::date
    *   order by powerlifter_id
    *   limit 1)
    * */
    /*
    select *
    from city
    where city_id =
        (select city_id
        from powerlifter
        group by city_id
        order by count(city_id)
        limit 1)
     */

//    public Powerlifter getEldestPowerlifter() {
//        DetachedCriteria userSubquery = DetachedCriteria.forClass(Powerlifter.class, "powerlifter")
//                // Filter the Subquery
//                .add(Restrictions.eq(Powerlifter, domain))
//                // SELECT The User Id
//                .setProjection( Projections.property("ud.userId") );
//    }

//    public void testCriteria() {
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory()
//
//
//        sessionFactory.openSession().createCr
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//
//        CriteriaQuery<Powerlifter> criteria = builder.createQuery( Powerlifter.class );
//        Root<Powerlifter> root = criteria.from( Powerlifter.class );
//        criteria.select( root );
//        criteria.where( builder.equal( root.get( Powerlifter_.city_id ), "John Doe" ) );
//        criteria.subquery()
//
//        List<Powerlifter> persons = entityManager.createQuery( criteria ).getResultList();
//    }


    public void destroy() {
        StandardServiceRegistryBuilder.destroy(registry);
        entityManagerFactory.close();
    }

}
