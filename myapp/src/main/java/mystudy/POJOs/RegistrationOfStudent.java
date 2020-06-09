package mystudy.POJOs;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class RegistrationOfStudent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    public RegistrationOfStudent() {
    }

    public RegistrationOfStudent(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public RegistrationOfStudent student(Student student) {
        this.student = student;
        return this;
    }

    public RegistrationOfStudent course(Course course) {
        this.course = course;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RegistrationOfStudent)) {
            return false;
        }
        RegistrationOfStudent registrationPK = (RegistrationOfStudent) o;
        return Objects.equals(student, registrationPK.student) && Objects.equals(course, registrationPK.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }

    @Override
    public String toString() {
        return "{" + " student='" + getStudent() + "'" + ", course='" + getCourse() + "'" + "}";
    }

}