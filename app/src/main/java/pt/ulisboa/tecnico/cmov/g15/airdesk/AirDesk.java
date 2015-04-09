package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;


/**
 * Created by MSC on 02/04/2015.
 */
public class AirDesk extends Application {

    private User user;

    private List<OwnerWorkspace> ownerWorkspaces;
    private List<ForeignWorkspace> foreignWorkspaces;

    public AirDesk() {
        ownerWorkspaces = new ArrayList<OwnerWorkspace>();
        foreignWorkspaces = new ArrayList<ForeignWorkspace>();
        //TODO temporary
        //NetworkServiceClient.setAirDesk(this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OwnerWorkspace> getOwnerWorkspaces() {
        return ownerWorkspaces;
    }

    public void setOwnerWorkspaces(List<OwnerWorkspace> ownerWorkspaces) {
        this.ownerWorkspaces = ownerWorkspaces;
    }

    public List<ForeignWorkspace> getForeignWorkspaces() {
        return foreignWorkspaces;
    }

    public void setForeignWorkspaces(List<ForeignWorkspace> foreignWorkspaces) {
        this.foreignWorkspaces = foreignWorkspaces;
    }


    public void populate() {
        OwnerWorkspace ow = new OwnerWorkspace(getUser(), "hollday_at_lodon", 2000);
        getOwnerWorkspaces().add(ow);
        ow.createFile("my_little_file");
    }


    // ---- Services for activities ----

    public OwnerWorkspace getOwnerWorkspaceByName(String workspaceName) {
        for (OwnerWorkspace workspace : getOwnerWorkspaces())
            if (workspace.getName().equals(workspaceName))
                return workspace;
        return null;
    }

    public ForeignWorkspace getForeignWorkspaceByName(String workspaceName) {
        for (ForeignWorkspace workspace : getForeignWorkspaces())
            if (workspace.getName().equals(workspaceName))
                return workspace;
        return null;
    }

    public boolean deleteOwnerWorkspace(String workspaceName) {
        OwnerWorkspace ow = getOwnerWorkspaceByName(workspaceName);
        if (ow == null)
            return false;

        return getOwnerWorkspaces().remove(ow) && ow.delete();
    }

    public boolean deleteForeignWorkspace(String workspaceName) {
        ForeignWorkspace fw = getForeignWorkspaceByName(workspaceName);
        if (fw == null)
            return false;

        return getForeignWorkspaces().remove(fw) && fw.delete();
    }

    public void getAllowedWorkspaces() {
        List<ForeignWorkspace> foreignWSList = NetworkServiceClient.getAllowedWorkspaces(getUser(), getUser().getUserTags());
        for (ForeignWorkspace fw : foreignWSList) {
            if (getForeignWorkspaceByName(fw.getName()) == null) {
                //updates only if there is a new workspace
                getForeignWorkspaces().add(fw);
            }
        }
    }

}
