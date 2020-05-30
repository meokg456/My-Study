package mystudy.POJOs;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class TimeTablePK implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @ManyToOne
    @JoinColumn(name = "className")
    private Class className;
    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    public TimeTablePK() {
    }

    public TimeTablePK(Class className, Course course) {
        this.className = className;
        this.course = course;
    }

    public Class getClassName() {
        return this.className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public TimeTablePK className(Class className) {
        this.className = className;
        return this;
    }

    public TimeTablePK course(Course course) {
        this.course = course;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TimeTablePK)) {
            return false;
        }
        TimeTablePK timeTablePK = (TimeTablePK) o;
        return Objects.equals(className, timeTablePK.className) && Objects.equals(course, timeTablePK.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, course);
    }

    @Override
    public String toString() {
        return "{" + " className='" + getClassName() + "'" + ", course='" + getCourse() + "'" + "}";
    }

}