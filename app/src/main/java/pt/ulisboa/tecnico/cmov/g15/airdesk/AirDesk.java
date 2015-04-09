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
        NetworkServiceClient.setAirDesk(this);
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

    public OwnerWorkspace getOwnerWorkspace(String workspaceName) {
        for (OwnerWorkspace ow : getOwnerWorkspaces()) {
            if (ow.getName().equals(workspaceName)) {
                return ow;
            }
        }
        return null;
    }

    public ForeignWorkspace getForeignWorkspace(String workspaceName) {
        for (ForeignWorkspace fw : getForeignWorkspaces()) {
            if (fw.getName().equals(workspaceName)) {
                return fw;
            }
        }
        return null;
    }

    public void getAllowedWorkspaces() {
        List<ForeignWorkspace> foreignWSList = NetworkServiceClient.getAllowedWorkspaces(getUser(), getUser().getUserTags());
        for (ForeignWorkspace fw : foreignWSList) {
            if (getForeignWorkspace(fw.getName()) == null) {
                //updates only if there is a new workspace
                getForeignWorkspaces().add(fw);
            }
        }
    }


}
