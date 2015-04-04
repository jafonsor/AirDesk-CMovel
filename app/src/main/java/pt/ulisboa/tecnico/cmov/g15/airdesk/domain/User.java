package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSC on 02/04/2015.
 */
public class User {
    private String userName;
    private String email;
    private List<Workspace> ownerWorkspaces;
    private List<Workspace> foreignWorkspaces;

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        this.ownerWorkspaces = new ArrayList<Workspace>();
        this.foreignWorkspaces = new ArrayList<Workspace>();
    }

    public String getUserName() { return userName; }
    public String getEmail() { return email; }

    public List<Workspace> getOwnerWorkspaces()   { return ownerWorkspaces; }
    public List<Workspace> getForeignWorkspaces() { return foreignWorkspaces; }

    public void addOwnerWorkspace(Workspace workspace) {
        ownerWorkspaces.add(workspace);
    }

    public void foreignOwnerWorkspace(Workspace workspace) {
        foreignWorkspaces.add(workspace);
    }
}
