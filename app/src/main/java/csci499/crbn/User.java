package csci499.crbn;

public class User {

    String name;
    String email;
    String password;
    String level;

    public User(){

    }

    public User(String n, String e, String p, String l){
        name = n;
        email = e;
        password =p;
        level=l;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
