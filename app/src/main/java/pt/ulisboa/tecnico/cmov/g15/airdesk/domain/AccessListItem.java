package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

/**
 * Created by MSC on 07/04/2015.
 */
public class AccessListItem {
    User user;
    Boolean allowed;

    public AccessListItem(User user, Boolean allowed) {
        this.user = user;
        this.allowed = allowed;
    }

    public AccessListItem(User user) {
        this.user = user;
        this.allowed = true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }
}
