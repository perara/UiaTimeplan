package no.persoft.uiatimeplan;

import no.persoft.uiatimeplan.Fetcher.Fetcher;
import no.persoft.uiatimeplan.Fetcher.HibernateUtil;
import no.persoft.uiatimeplan.Fetcher.Models.Course;
import org.hibernate.*;

import java.io.IOException;

import java.util.LinkedHashSet;


public class Main {

    public static void main(String[] args){

        Session session = HibernateUtil.getInstance().openSession();
        LinkedHashSet<Course> result = new LinkedHashSet(session.createCriteria(Course.class).setFetchMode("courseItems", FetchMode.JOIN).list());

        for(Course i : result)
        {
            System.out.println(i.getName() + " - " + i.getCourseItems().size());
        }
        System.out.println("......");


        Fetcher fetchCourse = new Fetcher(false);
        Fetcher fetchSingle = new Fetcher(true);
        try {
            fetchCourse.run();
            fetchSingle.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.close();

    }
}