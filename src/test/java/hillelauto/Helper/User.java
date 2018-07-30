package hillelauto.Helper;


public class User {
        private String id;
        private String name;
        private String phone;
        private String role;
        private Integer strikes;
        private String location;

    public User(String id, String name, String phone, String role, Integer strikes, String location) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.strikes = strikes;
        this.location = location;
    }

    public User(String id, String name, String phone, String role, String location) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.location = location;
    }

    public User(String id, String name, String phone, String role, Integer strikes) {

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.strikes = strikes;
    }

    public User(String id, String name, String phone, String role) {

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public User(String name, String phone, String location) {
        this.name = name;
        this.phone = phone;
        this.location = location;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public Integer getStrikes() {
        return strikes;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
