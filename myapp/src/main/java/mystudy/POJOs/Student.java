package mystudy.POJOs;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class Student implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "studentId")
    private String studentId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "gender", nullable = false, length = 5)
    private String gender;

    @Column(name = "personalId", nullable = false, length = 10)
    private String personalId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "className")
    private Class className;

    public Student() {
    }

    public Student(String studentId, String name, String gender, String personalId, Class className) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.personalId = personalId;
        this.className = className;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonalId() {
        return this.personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public Class getClassName() {
        return this.className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

}