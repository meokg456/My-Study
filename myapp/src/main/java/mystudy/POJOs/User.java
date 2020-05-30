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
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "permission", nullable = false)
    private Permission permission;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    private Student student;

    public User() {
    }

    public User(String Username, String Password, Permission permission, Student student) {
        this.username = Username;
        this.password = Password;
        this.permission = permission;
        this.student = student;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String Username) {
        this.username = Username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String Password) {
        this.password = Password;
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