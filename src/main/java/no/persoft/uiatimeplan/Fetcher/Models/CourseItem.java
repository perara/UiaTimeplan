package no.persoft.uiatimeplan.Fetcher.Models;

import com.google.gson.annotations.Expose;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "courseItem", uniqueConstraints = @UniqueConstraint(columnNames = "COURSEITEM_ID"))
public class CourseItem implements java.io.Serializable {

    @Expose private Integer itemId;
    @Expose private Date fromDate;
    @Expose private Date toDate;
    @Expose private String activity;
    @Expose private String room;
    @Expose private String educator;

    @Override
    public String toString() {
        return "course [id=" + itemId + ", from_date=" + fromDate + ", to_date=" + toDate + ", activity=" + activity + ", room=" + room + ", educator=" + educator + "]";
    }

    @ManyToOne
    @JoinColumn(name = "COURSE_NAME")
    private Course course; // DO NOT @Expose this as it causes GSON to do circual reference Course --> CourseItem --> Course .....



    public CourseItem() {}
    public CourseItem(Course course, Date fromDate, Date toDate, String activity, String room, String educator) {
        this.course = course;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.activity = activity;
        this.room = room;
        this.educator = educator;
    }

    /***********************************************************
     *
     * ID Field
     *
     ************************************************************/
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "COURSEITEM_ID", unique = true, nullable = false)
    public Integer getItemId() {
        return this.itemId;
    }

    public void setItemId(Integer id) {
        this.itemId = id;
    }

    /***********************************************************
     *
     * Course Field
     *
     ************************************************************/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_ID", nullable = false)
    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    /***********************************************************
     *
     * From Date Field
     *
     ************************************************************/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COURSEITEM_FROMDATE", nullable = false, length = 10)
    public Date getFromDate() {
        return this.fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /***********************************************************
     *
     * Date Field
     *
     ************************************************************/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COURSEITEM_TODATE", length = 10)
    public Date getToDate() {
        return this.toDate;
    }
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    /***********************************************************
     *
     * Activity Field
     *
     ************************************************************/
    @Column(name = "COURSEITEM_ACTIVITY", nullable = false, length = 1000)
    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    /***********************************************************
     *
     * Room Field
     *
     ************************************************************/
    @Column(name = "COURSEITEM_ROOM", nullable = false, columnDefinition = "TEXT")
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    /***********************************************************
     *
     * Educator Field
     *
     ************************************************************/
    @Column(name = "COURSEITEM_EDUCATOR", nullable = false, length = 255)
    public String getEducator() {
        return educator;
    }

    public void setEducator(String educator) {
        this.educator = educator;
    }











}