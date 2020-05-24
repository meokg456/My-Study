public class User {
    private String Username;
    private String Password;
    private Permission permission;

    public User() {
    }

    public User(String Username, String Password, Permission permission) {
        this.Username = Username;
        this.Password = Password;
        this.permission = permission;
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

    public User Username(String Username) {
        this.Username = Username;
        return this;
    }

    public User Password(String Password) {
        this.Password = Password;
        return this;
    }

    public User permission(Permission permission) {
        this.permission = permission;
        return this;
    }

}