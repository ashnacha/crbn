package csci499.crbn;

import java.util.ArrayList;
import java.util.List;

public class User {

    String name;
    String email;
    String password;
    String level;


    List<String> pictures;

    public User(){

    }

    public User(String n, String e, String p, String l){
        name = n;
        email = e;
        password =p;
        level=l;
        pictures = new ArrayList<>();
    }

    public User(String n, String e, String p, String l, List<String> pics){
        name = n;
        email = e;
        password =p;
        level=l;
        pictures = pics;
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


    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> p) {
        this.pictures = p;
    }
}
