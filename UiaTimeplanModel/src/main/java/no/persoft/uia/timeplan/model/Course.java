package no.persoft.uia.timeplan.model;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by PerArne on 07.07.2014.
 */
@Entity
@Table(name = "course", uniqueConstraints = {
        @UniqueConstraint(columnNames = "COURSE_NAME")
})
@XmlRootElement
public class Course implements Serializable {
    private int id;
    private String name;
    private Set<CourseItem> courseItems = new HashSet<CourseItem>(0);
    private boolean isSubject;
    private String lastUpdate;

    @Override
    public String toString() {
        return "Course [name=" + name + ", items=" + courseItems + "]";
    }


    public Course(){}
    public Course(String name)
    {
        this.name = name;
    }
    public Course(String name, Set<CourseItem> courseItems , boolean isSubject, String lastUpdate)
    {
        this.name = name;
        this.courseItems = courseItems;
        this.isSubject = isSubject;
        this.lastUpdate = lastUpdate;
    }

    /***********************************************************
     *
     * Name Field
     *
     ************************************************************/

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "COURSE_NAME", unique = true, nullable = false, length = 255)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /***********************************************************
     *
     * Last updated field
     *
     ************************************************************/
    @Column(name = "COURSE_LAST_UPDATED", unique = true, nullable = true, length = 255)
    public String getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /***********************************************************
     *
     * Bool if its subject or course
     *
     ************************************************************/
    @Column(name = "COURSE_ISSUBJECT", nullable = false)
    public boolean getIsSubject() {
        return this.isSubject;
    }
    public void setIsSubject(boolean isSubject){this.isSubject = isSubject;}


}
