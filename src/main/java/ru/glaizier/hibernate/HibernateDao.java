package ru.glaizier.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.glaizier.domain.City;

public class HibernateDao {

    public City testApp() {
//        SessionFactory sessionFactory = new Configuration().configure()
//                .buildSessionFactory();

//        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().
//                configure().loadProperties("hibernate.cfg.xml").build();
//        SessionFactory sessionFactory = new Configuration().buildSessionFactory(serviceRegistry);

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        SessionFactory sessionFactory = new MetadataSources( registry )
                .addAnnotatedClass(City.class)
                .buildMetadata().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            City city = session.get(City.class, 1);
            System.out.println(city.getCityId());
            System.out.println(city.getCityName());
            return city;
        }

    }


}
