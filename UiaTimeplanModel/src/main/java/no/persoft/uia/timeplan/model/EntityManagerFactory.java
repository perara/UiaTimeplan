package no.persoft.uia.timeplan.model;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;


public class EntityManagerFactory {
    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EntityManagerFactory.class.getName());
    private static StandardServiceRegistry serviceRegistry;
    private static SessionFactory INSTANCE = null;


    private synchronized static void createSessionFactory(){
        if(INSTANCE!=null){return;}
        Configuration configuration = new Configuration();
        configuration.addPackage("model");

        // Use Development CFG if hostname is production

        log.info("Production Environment - Hibernate using production configuration");
        configuration.configure("hibernate.cfg.xml");



        // Add Mapping Dynamically
        Reflections reflections = new Reflections("no.persoft.uia.timeplan.model");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);

        for(Class<?> clazz : classes)
        {
            configuration.addAnnotatedClass(clazz);
        }


        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        INSTANCE = configuration.buildSessionFactory(serviceRegistry);
    }


    public static SessionFactory getSessionFactory() {
        if(INSTANCE==null){
            createSessionFactory();
        }
        return EntityManagerFactory.INSTANCE;
    }


}
