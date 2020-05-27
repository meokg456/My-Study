package mystudy.User;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4859206593107291357L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "username")
    private String Username;

    @Column(name = "password")
    private String Password;

    @Column(name = "permission")
    private Permission permission;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "CMND")
    private String id;

    public User() {
    }

    public User(String Username, String Password, Permission permission, String name, Gender gender, String id) {
        this.Username = Username;
        this.Password = Password;
        this.permission = permission;
        this.name = name;
        this.gender = gender;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return Password;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getUsername() {
        return Username;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void setUsername(String username) {
        Username = username;
    }
}