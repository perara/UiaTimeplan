package no.persoft.uiatimeplan.Server.Servlets;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.persoft.uiatimeplan.Fetcher.HibernateUtil;
import no.persoft.uiatimeplan.Fetcher.Models.Course;
import no.persoft.uiatimeplan.Fetcher.Models.CourseItem;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;
import java.util.*;


/**
 * Created by PerArne on 16.07.2014.
 */

@Path("/course")
public class CourseServlet{

    private static ArrayList<Course>  courseData = null;
    private static ArrayList<String> courseNames = null;

    public static void Cache()
    {
        Session session = HibernateUtil.createFactory().openSession();
        LinkedHashSet<Course> result = new LinkedHashSet(session.createCriteria(Course.class).setFetchMode("courseItems", FetchMode.JOIN).list());
        session.close();

        CourseServlet.courseData = new ArrayList<Course>();
        CourseServlet.courseNames = new ArrayList<String>();

        for(Course i : result)
        {
            // Cache complete data
            CourseServlet.courseData.add(i);

            // Cache name only
            courseNames.add(i.getName());
        }
    }

    @GET
    @Path("/course/{course}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getCourse(@PathParam("course") int course){

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        String data = gson.toJson(CourseServlet.courseData.get(course));
        return data;
    }



    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getCourses()
    {

        System.out.println(CourseServlet.courseData.size());

        // TODO - StackOverflow :(
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String data = gson.toJson(CourseServlet.courseData);
        return data;
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getCourseList() throws Exception
    {
        Gson gson = new Gson();
        String data = gson.toJson(CourseServlet.courseNames);

        return data;
    }


}
