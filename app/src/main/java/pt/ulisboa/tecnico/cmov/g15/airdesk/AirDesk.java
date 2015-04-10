package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AccessListItem;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;


/**
 * Created by MSC on 02/04/2015.
 */
public class AirDesk extends Application {

    private User user;

    private List<OwnerWorkspace> ownerWorkspaces;
    private List<ForeignWorkspace> foreignWorkspaces;
    private List<ForeignWorkspace> blockedWorkspaces;

    public AirDesk() {
        ownerWorkspaces = new ArrayList<OwnerWorkspace>();
        foreignWorkspaces = new ArrayList<ForeignWorkspace>();
        blockedWorkspaces = new ArrayList<ForeignWorkspace>();
        //TODO temporary
        NetworkServiceClient.setAirDesk(this);
    }

    public void reset() {
        ownerWorkspaces = new ArrayList<OwnerWorkspace>();
        foreignWorkspaces = new ArrayList<ForeignWorkspace>();
        blockedWorkspaces = new ArrayList<ForeignWorkspace>();
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

    public List<ForeignWorkspace> getBlockedWorkspaces() {
        return blockedWorkspaces;
    }


    public void populate() {
        FileSystemManager.deleteRecursively(new File(Environment.getExternalStorageDirectory() + "/AirDesk/" + getUser().getEmail()));
        List<String> tags = new ArrayList<String>() {{
            add("hollyday");
        }};

        if (createOwnerWorkspace("hollyday_at_lodon", 2000L, WorkspaceVisibility.PUBLIC, tags)) {
            OwnerWorkspace ow = getOwnerWorkspaceByName("hollyday_at_lodon");
            ow.createFile("my_little_file").write("ola\n\n\n\n\n\n\n\n\n\n\n\n\n\nn\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "OLAAOO" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n\n\n\n\n\n\nolaaaaa\n");
        }
    }


    // ---- Services for activities ----

    public OwnerWorkspace getOwnerWorkspaceByName(String workspaceName) {
        for (OwnerWorkspace workspace : getOwnerWorkspaces())
            if (workspace.getName().equals(workspaceName))
                return workspace;
        return null;
    }

    public ForeignWorkspace getForeignWorkspaceByName(String workspaceOwnerEmail, String workspaceName) {
        return findForeignWorkspaceByName(getForeignWorkspaces(), workspaceOwnerEmail, workspaceName);
    }

    private ForeignWorkspace findForeignWorkspaceByName(List<ForeignWorkspace> workspaces, String ownerEmail, String wsName) {
        for (ForeignWorkspace workspace : workspaces)
            if (workspace.getName().equals(wsName))
                if (workspace.getOwner().getEmail().equals(ownerEmail))
                    return workspace;
        return null;
    }

    public ForeignWorkspace getBlockedWorkspaceByName(String workspaceOwnerEmail, String workspaceName) {
        return findForeignWorkspaceByName(getBlockedWorkspaces(), workspaceOwnerEmail, workspaceName);
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
        List<ForeignWorkspace> wsListToRemove = new ArrayList<ForeignWorkspace>();
        for (ForeignWorkspace fw : foreignWSList) {
            if (isForeignWorkspaceBlocked(fw.getOwner().getEmail(), fw.getName()))
                wsListToRemove.add(fw);
        }
        foreignWSList.removeAll(wsListToRemove);
        setForeignWorkspaces(foreignWSList);
    }

    public List<AirDeskFile> getWorkspaceFiles(String userEmail, String workspaceName) {
        Workspace workspace = null;
        if (getUser().getEmail().equals(userEmail)) {
            workspace = getOwnerWorkspaceByName(workspaceName);
        } else {
            workspace = getForeignWorkspaceByName(userEmail, workspaceName);
        }

        if (workspace == null) {
            Log.e("bad error", "getWorkspaceFiles for " + userEmail + "," + workspaceName);
            return null;
        }

        return workspace.getFiles();
    }

    public boolean createOwnerWorkspace(String name, Long quota, WorkspaceVisibility visibility, List<String> tags) {
        if (getOwnerWorkspaceByName(name) != null) {
            Log.e("Error", "trying to create a workspace with a name that already exists");
            return false;
        }
        OwnerWorkspace ow = new OwnerWorkspace(getUser(), name, quota, visibility, tags);
        getOwnerWorkspaces().add(ow);
        NetworkServiceClient.workspaceCreated();
        return ow.create();
    }

    public boolean editOwnerWorkspace(String name, Long quota, WorkspaceVisibility visibility, List<String> tags) {
        OwnerWorkspace ow = getOwnerWorkspaceByName(name);
        boolean changedQuota = ow.setQuota(quota);
        boolean changedVisibility = ow.setVisibility(visibility);
        boolean changedTags = ow.setTags(tags);

        Log.i("info", "quota: " + changedQuota + ", visibility: " + changedVisibility + ", tags: " + changedTags);

        return changedQuota && changedVisibility && changedTags;
    }

    public boolean toggleUserPermissions(String workspaceName, String userEmail, boolean oldStatus) {
        OwnerWorkspace workspace = getOwnerWorkspaceByName(workspaceName);
        return workspace.toggleUserPermissions(userEmail, oldStatus);
    }

    public List<AccessListItem> getWorkspaceAccessList(String workspaceName) {
        OwnerWorkspace workspace = getOwnerWorkspaceByName(workspaceName);
        return workspace.getAccessList();
    }

    public boolean inviteUser(String workspaceName, String userEmail) {
        OwnerWorkspace workspace = getOwnerWorkspaceByName(workspaceName);
        return workspace.inviteUser(userEmail);
    }

    public void changeUserTags(List<String> tags) {
        User user = getUser();
        user.setUserTags(tags);
        getAllowedWorkspaces();
    }

    public boolean blockForeignWorkspace(String userEmail, String foreignWorkspaceName) {
        ForeignWorkspace fw = getForeignWorkspaceByName(userEmail, foreignWorkspaceName);
        if (fw == null) {
            Log.e("Error", "blocking a foreign workspace that doesn't exist");
            return false;
        }
        if (!isForeignWorkspaceBlocked(userEmail, foreignWorkspaceName))
            return getBlockedWorkspaces().add(fw);
        else
            return true;
    }

    public boolean isOwner(String userEmail) {
        if (user.getEmail().equals(userEmail))
            return true;

        return false;
    }

    public boolean createFile(String wsOwner, String wsName, String filename, WorkspaceType workspaceType) {
        if (workspaceType == WorkspaceType.OWNER) {
            if (getOwnerWorkspaceByName(wsName).createFile(filename) != null)
                return true;
        } else {
            if (getForeignWorkspaceByName(wsOwner, wsName).createFile(filename) != null)
                return true;
        }
        return false;
    }

    public String viewFileContent(String wsOwner, String wsName, String filename) {
        AirDeskFile mFile = null;

        if (isOwner(wsOwner)) {
            mFile = getOwnerWorkspaceByName(wsName).getFile(filename);
        } else {
            mFile = getForeignWorkspaceByName(wsOwner, wsName).getFile(filename);
        }

        return mFile.read();
    }

    public Boolean saveFileContent(String wsOwner, String wsName, String filename, String content) {
        AirDeskFile mFile = null;

        if (isOwner(wsOwner)) {
            mFile = getOwnerWorkspaceByName(wsName).getFile(filename);
        } else {
            mFile = getForeignWorkspaceByName(wsOwner, wsName).getFile(filename);
        }

        return mFile.write(content);
    }

    public boolean isForeignWorkspaceBlocked(String userEmail, String foreignWorkspaceName) {
        ForeignWorkspace fw = getBlockedWorkspaceByName(userEmail, foreignWorkspaceName);
        return fw != null;
    }

}
