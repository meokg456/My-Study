package mystudy.POJOs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "courseId", nullable = false, length = 10)
    private String courseId;

    @Column(name = "courseName", nullable = false, length = 30)
    private String courseName;

    @Column(name = "RoomId", nullable = false, length = 10)
    private String roomId;

    public Course() {
    }

    public Course(String courseId, String courseName, String roomId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.roomId = roomId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object obj) {
        Course course = (Course) obj;
        if (course == null)
            return false;
        return course.courseId.equals(courseId);
    }

    @Override
    public String toString() {
        return courseId + " - " + courseName;
    }
}