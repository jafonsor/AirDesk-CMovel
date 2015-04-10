package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.io.Serializable;

/**
 * Created by MSC on 07/04/2015.
 */
public class AccessListItem implements Serializable {
    private User user;
    private boolean allowed;
    private boolean invited;

    public AccessListItem(User user) {
        this.user = user;
        this.allowed = true;
        this.invited = false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public void toggleAllowed() { this.setAllowed(!this.isAllowed()); }

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }
}
