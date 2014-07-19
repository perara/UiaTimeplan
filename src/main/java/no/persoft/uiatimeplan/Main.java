package no.persoft.uiatimeplan;

import no.persoft.uiatimeplan.Fetcher.Fetcher;
import no.persoft.uiatimeplan.Fetcher.HibernateUtil;
import no.persoft.uiatimeplan.Fetcher.Models.Course;
import no.persoft.uiatimeplan.Server.WebServer;
import org.hibernate.*;

import java.io.IOException;

import java.util.LinkedHashSet;


public class Main {

    public static void main(String[] args){
    /*
        Session session = HibernateUtil.createFactory().openSession();
        LinkedHashSet<Course> result = new LinkedHashSet(session.createCriteria(Course.class).setFetchMode("courseItems", FetchMode.JOIN).list());
        session.close();

        for(Course i : result)
        {
            System.out.println(i.getName() + " - " + i.getCourseItems().size());
        }
        System.out.println("......");


        Fetcher fetch = new Fetcher();
        try {
            fetch.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        WebServer webServer = new WebServer();





    }
}