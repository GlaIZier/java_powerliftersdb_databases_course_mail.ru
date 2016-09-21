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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    public void getFirstPowerlifterAfterDateCriteria(Date date) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Powerlifter> criteria = builder.createQuery(Powerlifter.class);
        Root<Powerlifter> root = criteria.from(Powerlifter.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get(Powerlifter_.powerlifterId), 1));

        List<Powerlifter> powerlifters = entityManager.createQuery(criteria).getResultList();

        for (Powerlifter powerlifter : powerlifters) {
            System.out.println("powerlifter.getLastName() = " + powerlifter.getLastName());
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
