package ru.glaizier.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.glaizier.domain.City;

public class HibernateDao {

    private final StandardServiceRegistry registry;

    private final SessionFactory sessionFactory;

    public HibernateDao() {
        registry = new StandardServiceRegistryBuilder()
                .configure() // get settings from hibernate.cfg.xml from classpath
                .build();
        sessionFactory = new MetadataSources(registry)
//                .addAnnotatedClass(City.class)
                .buildMetadata().buildSessionFactory();
    }

    public City testApp() {
        try (Session session = sessionFactory.openSession()) {
            City city = session.get(City.class, 1);
            System.out.println(city.getCityId());
            System.out.println(city.getCityName());
            return city;
        }
    }

    public void destroy() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

}
