package mystudy.POJOs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Student> readStudentsFromCSV(InputStreamReader fileReader) throws IOException {
        List<Student> students = new ArrayList<>();
        BufferedReader reader = new BufferedReader(fileReader);
        // Đọc tên lớp
        Class fromClass = new Class(reader.readLine().split(",")[0]);
        // Đọc bỏ dòng tên cột
        String line = reader.readLine();
        line = reader.readLine();
        while (line != null) {
            // Đọc từng dòng
            String[] args = line.split(",");
            Student student = new Student(args[1], args[2], args[3], args[4], fromClass);
            students.add(student);
            line = reader.readLine();
        }
        reader.close();
        return students;
    }
}