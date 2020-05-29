package mystudy.POJOs;

import java.io.Serializable;

import javax.persistence.*;

import mystudy.Enum.Permission;

@Entity
@Table(name = "users")
public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4859206593107291357L;

    @Id
    @Column(name = "username", nullable = false, length = 30)
    private String Username;

    @Column(name = "password", nullable = false, length = 200)
    private String Password;

    @Column(name = "permission", nullable = false)
    private Permission permission;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studentId")
    private Student student;

    public User() {
    }

    public User(String Username, String Password, Permission permission, Student student) {
        this.Username = Username;
        this.Password = Password;
        this.permission = permission;
        this.student = student;
    }

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public Permission getPermission() {
        return this.permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}