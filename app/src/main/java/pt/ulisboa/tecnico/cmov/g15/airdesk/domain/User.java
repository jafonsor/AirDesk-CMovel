package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by MSC on 02/04/2015.
 */
public class User {
    private String userName;
    private String email;
    private List<String> userTags;

    public User(String email) {
        this.userName = "NULL";
        this.email = email;
        userTags = new ArrayList<String>();
    }

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        userTags = new ArrayList<String>();
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getUserTags() {
        return userTags;
    }

    public void setUserTags(List<String> userTags) {
        this.userTags = userTags;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof User) {
            User u = (User) object;
            return this.getEmail().equals(u.getEmail());
        }
        return false;
    }
}
