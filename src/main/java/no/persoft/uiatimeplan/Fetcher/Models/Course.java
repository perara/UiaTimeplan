package no.persoft.uiatimeplan.Fetcher.Models;

import com.google.gson.annotations.Expose;

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
    @Expose private int id;
    @Expose private String name;
    @Expose private Set<CourseItem> courseItems = new HashSet<CourseItem>(0);


    @Override
    public String toString() {
        return "Course [name=" + name + ", items=" + courseItems + "]";
    }


    public Course(){}
    public Course(String name)
    {
        this.name = name;
    }
    public Course(String name, Set<CourseItem> courseItems)
    {
        this.name = name;
        this.courseItems = courseItems;
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
     * One to Many Relation - Items
     *
     ************************************************************/
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    public Set<CourseItem> getCourseItems() {
        return this.courseItems;
    }

    public void setCourseItems(Set<CourseItem> courseItems){
        this.courseItems = courseItems;
    }




}
