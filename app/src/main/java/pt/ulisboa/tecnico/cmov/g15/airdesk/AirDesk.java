package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;


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


    public void populate() {
        FileSystemManager.deleteRecursively(new File(Environment.getExternalStorageDirectory() + "/AirDesk/" + getUser().getEmail()));
        OwnerWorkspace ow = new OwnerWorkspace(getUser(), "hollday_at_lodon", 2000);
        ow.create();
        getOwnerWorkspaces().add(ow);
        ow.create();
        ow.createFile("my_little_file");
    }


    // ---- Services for activities ----

    public OwnerWorkspace getOwnerWorkspaceByName(String workspaceName) {
        for (OwnerWorkspace workspace : getOwnerWorkspaces())
            if (workspace.getName().equals(workspaceName))
                return workspace;
        return null;
    }

    public ForeignWorkspace getForeignWorkspaceByName(String workspaceOwnerEmail, String workspaceName) {
        for (ForeignWorkspace workspace : getForeignWorkspaces())
            if (workspace.getName().equals(workspaceName))
                if (workspace.getOwner().getEmail().equals(workspaceOwnerEmail))
                    return workspace;
        return null;
    }

    public boolean deleteOwnerWorkspace(String workspaceName) {
        OwnerWorkspace ow = getOwnerWorkspaceByName(workspaceName);
        if (ow == null) {
            Log.e("bad error", "owner workspace not found: " + workspaceName);
            return false;
        }

        return getOwnerWorkspaces().remove(ow) && ow.delete();
    }

    public boolean deleteForeignWorkspace(String userEmail, String workspaceName) {
        ForeignWorkspace fw = getForeignWorkspaceByName(userEmail, workspaceName);
        if (fw == null) {
            Log.e("bad error", "foreign workspace not found: " + workspaceName);
            return false;
        }

        return getForeignWorkspaces().remove(fw) && fw.delete();
    }

    public void getAllowedWorkspaces() {
        List<ForeignWorkspace> foreignWSList = NetworkServiceClient.getAllowedWorkspaces(getUser(), getUser().getUserTags());
        for (ForeignWorkspace fw : foreignWSList) {
            if (getForeignWorkspaceByName(fw.getOwner().getUserName(), fw.getName()) == null) {
                //updates only if there is a new workspace
                getForeignWorkspaces().add(fw);
            }
        }
    }

    public List<AirDeskFile> getWorkspaceFiles(String userEmail, String workspaceName) {
        Workspace workspace = null;
        if(getUser().getEmail().equals(userEmail)) {
            workspace = getOwnerWorkspaceByName(workspaceName);
        } else {
            workspace = getForeignWorkspaceByName(userEmail, workspaceName);
        }

        if(workspace == null) {
            Log.e("bad error", "getWorkspaceFiles for " + userEmail + "," + workspaceName);
            return null;
        }

        return workspace.getFiles();
    }

}
