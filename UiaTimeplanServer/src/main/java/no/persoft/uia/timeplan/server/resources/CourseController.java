package no.persoft.uia.timeplan.server.resources;

import no.persoft.uia.timeplan.model.Course;
import no.persoft.uia.timeplan.model.CourseItem;
import no.persoft.uia.timeplan.model.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by PerArne on 19.12.2014.
 */

@Path("/course")
public class CourseController {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public List<Course> GET_CourseList(@Context HttpServletRequest req, @Context HttpServletResponse resp){
        Session session = EntityManagerFactory.getSessionFactory().openSession();
        List<Course> courseList = session.createCriteria(Course.class)
                .list();
        session.close();

        return courseList;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Course GET_CourseById(@Context HttpServletRequest req,
                                 @Context HttpServletResponse resp,
                                 @PathParam("id") Integer id){

        Session session = EntityManagerFactory.getSessionFactory().openSession();
        Course course = (Course) session.get(Course.class, id);
        session.close();

        return course;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/items/{id}")
    public HashMap<Long, List<CourseItem>> GET_CourseItems(@Context HttpServletRequest req,
                                 @Context HttpServletResponse resp,
                                 @PathParam("id") Integer id){

        Session session = EntityManagerFactory.getSessionFactory().openSession();

        List<CourseItem> courseItems = session.createCriteria(CourseItem.class)
                .add(Restrictions.eq("course.id", id))
                .list();


        session.close();


        HashMap<Long, List<CourseItem>> map = new HashMap<Long, List<CourseItem>>();
        for (CourseItem courseItem : courseItems) {

            // Strip time
            Calendar cal = Calendar.getInstance();
            cal.setTime(courseItem.getFromDate());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            if(!map.containsKey(cal.getTimeInMillis())){
                map.put(cal.getTimeInMillis(), new ArrayList<CourseItem>());
            }

            map.get(cal.getTimeInMillis()).add(courseItem);
        }




        return map;
    }


}
