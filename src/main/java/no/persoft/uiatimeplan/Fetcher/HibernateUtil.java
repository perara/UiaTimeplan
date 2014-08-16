package no.persoft.uiatimeplan.Fetcher;

import no.persoft.uiatimeplan.Fetcher.Models.Course;
import no.persoft.uiatimeplan.Fetcher.Models.CourseItem;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static SessionFactory instance;


    private static SessionFactory createFactory()
    {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();

        SessionFactory sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(Course.class)
                .addAnnotatedClass(CourseItem.class)
                .buildSessionFactory(serviceRegistry);


        return sessionFactory;
    }

    public static SessionFactory getInstance(){
        if(HibernateUtil.instance == null){
            HibernateUtil.instance = HibernateUtil.createFactory();
        }
        return HibernateUtil.instance;
    }

}