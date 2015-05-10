package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AccessListItem;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AnotherUserEditingFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileDoesNotExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.InvalidQuotaException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceDoesNotExistException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;


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

    private Map<String, User> foreignUsers = new HashMap<String, User>();
    private User getForeignUser(String email) {
        if(foreignUsers.containsKey(email)) {
            return foreignUsers.get(email);
        } else {
            User newUser = new User(email);
            foreignUsers.put(email, newUser);
            return newUser;
        }
    }

    /*
    @Override
    public void onCreate() {
        try {
            FileInputStream streamIn = new FileInputStream(Environment.getExternalStorageDirectory() + "/backup.airdesk");
            ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
            ArrayList<OwnerWorkspace> ownerWorkspaces = (ArrayList<OwnerWorkspace>) objectinputstream.readObject();
            setOwnerWorkspaces(ownerWorkspaces);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (OptionalDataException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            Log.i("info", "backup file not found");
        } catch (StreamCorruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
*/
    public void backup() {
        Log.i("info", "saving state");
        List<OwnerWorkspace> ownerWorkspaces = getOwnerWorkspaces();
        try  {
            FileOutputStream fout = new FileOutputStream(Environment.getExternalStorageDirectory() + "/backup.airdesk");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(ownerWorkspaces);
            oos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        //FileSystemManager.deleteRecursively(new File(Environment.getExternalStorageDirectory() + "/AirDesk/" + getUser().getEmail()));
        List<String> tags = new ArrayList<String>();
        tags.add("hollyday");

        String workspaceName = "Workspace"+System.currentTimeMillis();
        createOwnerWorkspace(workspaceName, 2000L, WorkspaceVisibility.PUBLIC, tags);
        OwnerWorkspace ow = getOwnerWorkspaceByName(workspaceName);
        ow.createFileNoNetwork("file" + System.currentTimeMillis()).write("ola\n");
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
            if (workspace.getName().equals(wsName)
                && workspace.getOwner().getEmail().equals(ownerEmail))
                    return workspace;
        return null;
    }

    public ForeignWorkspace getBlockedWorkspaceByName(String workspaceOwnerEmail, String workspaceName) {
        return findForeignWorkspaceByName(getBlockedWorkspaces(), workspaceOwnerEmail, workspaceName);
    }

    public void deleteOwnerWorkspace(String workspaceName) {
        OwnerWorkspace ow = getOwnerWorkspaceByName(workspaceName);
        if (ow == null) {
            Log.e("bad error", "owner workspace not found: " + workspaceName);
            throw new WorkspaceDoesNotExistException(workspaceName);
        }

        getOwnerWorkspaces().remove(ow);
        ow.delete();
    }

    public void deleteForeignWorkspace(String userEmail, String workspaceName) {
        ForeignWorkspace fw = getForeignWorkspaceByName(userEmail, workspaceName);
        if (fw == null) {
            throw new WorkspaceDoesNotExistException(workspaceName);
        }
        if(!getForeignWorkspaces().remove(fw)) {
            Log.e("bad error", "could not remove from list: " + workspaceName);
        }
        fw.delete();
    }

    public void searchWorkspaces() {
        Map<String, List<String>> foreignWSMap = NetworkServiceClient.searchWorkspaces(getUser().getEmail(), getUser().getUserTags());
        List<ForeignWorkspace> wsListToAdd = new ArrayList<ForeignWorkspace>();
        for(Map.Entry<String, List<String>> entry : foreignWSMap.entrySet()) {
            String wsOwner = entry.getKey();
            List<String> workspaces = entry.getValue();
            for(String workspaceName : workspaces) {
                if (!isForeignWorkspaceBlocked(wsOwner, workspaceName)) {
                    // don't add blocked workspaces
                    long quota = NetworkServiceClient.getWorkspaceQuota(wsOwner, workspaceName);
                    ForeignWorkspace fw = getForeignWorkspaceByName(wsOwner, workspaceName);
                    // don't create a new workspace if the workspace already exists
                    if (fw != null) {
                        fw = new ForeignWorkspace(getForeignUser(wsOwner), workspaceName, quota);
                        wsListToAdd.add(fw);
                    } else {
                        fw.setQuota(fw.getQuota());
                    }
                }
            }
        };
        getForeignWorkspaces().addAll(wsListToAdd);
    }

    public List<AirDeskFile> getWorkspaceFiles(String userEmail, String workspaceName, WorkspaceType workspaceType) {
        Workspace workspace = null;
        if (workspaceType==WorkspaceType.OWNER) {
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

    public void createOwnerWorkspace(String name, Long quota, WorkspaceVisibility visibility, List<String> tags) {
        OwnerWorkspace ow = new OwnerWorkspace(getUser(), name, quota, visibility, tags);
        if (getOwnerWorkspaceByName(name) != null) {
            throw new WorkspaceAlreadyExistsException(name);
        }
        getOwnerWorkspaces().add(ow);
        ow.create();
    }

    public void createForeignWorkspace(User ownerWorkspace, String workspaceName, Long quota) {
        ForeignWorkspace fw = new ForeignWorkspace(ownerWorkspace, workspaceName, quota);
        if (getForeignWorkspaceByName(ownerWorkspace.getEmail(),workspaceName) != null) {
            throw new WorkspaceAlreadyExistsException(workspaceName);
        }
        getForeignWorkspaces().add(fw);
        fw.create();
    }

    public void editOwnerWorkspace(String name, Long quota, WorkspaceVisibility visibility, List<String> tags)
                throws InvalidQuotaException {
        OwnerWorkspace ow = getOwnerWorkspaceByName(name);
        ow.setQuota(quota);
        ow.setVisibility(visibility);
        ow.setTags(tags);
    }

    public boolean toggleUserPermissions(String workspaceName, String userEmail, boolean oldStatus) {
        OwnerWorkspace workspace = getOwnerWorkspaceByName(workspaceName);
        return workspace.toggleUserPermissions(userEmail, oldStatus);
    }

    public List<AccessListItem> getWorkspaceAccessList(String workspaceName) {
        OwnerWorkspace workspace = getOwnerWorkspaceByName(workspaceName);
        return workspace.getAccessList();
    }

    public void inviteUser(String workspaceName, String userEmail) {
        OwnerWorkspace workspace = getOwnerWorkspaceByName(workspaceName);
        workspace.inviteUser(userEmail);
    }

    public void changeUserTags(List<String> tags) {
        User user = getUser();
        user.setUserTags(tags);
        searchWorkspaces();
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

    /*
    *
    * Chamar esta funcao apenas da activity. Para criar a lista de ficheiros usar createFileNoNetwork.
     */

    public void createFile(String wsOwner, String wsName, String filename, WorkspaceType workspaceType) {
        if (workspaceType == WorkspaceType.OWNER) {
            OwnerWorkspace ow  = getOwnerWorkspaceByName(wsName);
            ow.createFileNoNetwork(filename);
        } else {
           ForeignWorkspace fw= getForeignWorkspaceByName(wsOwner, wsName);
           fw.createFileOnNetwork(filename);
        }
    }

    public String viewFileContent(String wsOwner, String wsName, String filename, WorkspaceType workspaceType) {
        Workspace ws = null;
        if (workspaceType == WorkspaceType.OWNER) {
            ws = getOwnerWorkspaceByName(wsName);
        } else {
            ws = getForeignWorkspaceByName(wsOwner, wsName);
        }

        if(ws == null)
            throw new WorkspaceDoesNotExistException(wsName);

        AirDeskFile mFile = ws.getFile(filename);
        if(mFile == null)
            throw new FileDoesNotExistsException(filename);

        return mFile.read();
    }

    public void saveFileContent(String wsOwner, String wsName, String filename, String content, WorkspaceType workspaceType)
                throws AnotherUserEditingFileException{

        AirDeskFile mFile = null;

        if (workspaceType == WorkspaceType.OWNER) {
            mFile = getOwnerWorkspaceByName(wsName).getFile(filename);
            if(mFile == null)
                throw new FileDoesNotExistsException(filename);
            mFile.writeNoNetwork(content);
            mFile.incrementVersion();
        } else {
            mFile = getForeignWorkspaceByName(wsOwner, wsName).getFile(filename);
            if(mFile == null)
                throw new FileDoesNotExistsException(filename);
            mFile.write(content);
        }
    }

    public boolean isForeignWorkspaceBlocked(String userEmail, String foreignWorkspaceName) {
        ForeignWorkspace fw = getBlockedWorkspaceByName(userEmail, foreignWorkspaceName);
        return fw != null;
    }

    public boolean fileExists(String wsOwner, String wsName, String filename, WorkspaceType workspaceType) {
        AirDeskFile mFile = null;
        if (workspaceType == WorkspaceType.OWNER) {
            mFile = getOwnerWorkspaceByName(wsName).getFile(filename);
        } else {
            mFile = getForeignWorkspaceByName(wsOwner, wsName).getFile(filename);
        }

        if(mFile==null) return false;
        else return true;
    }

    public void deleteFile(String ownerEmail, String workspaceName, String fileName, WorkspaceType mWorkspaceType) {
        Workspace w = null;
        if(mWorkspaceType == WorkspaceType.FOREIGN) {
            w = getForeignWorkspaceByName(ownerEmail, workspaceName);
        } else {
            w = getOwnerWorkspaceByName(workspaceName);
        }

        if(w == null) {
            throw new WorkspaceDoesNotExistException("error deleting file on " + workspaceName);
        }

        w.deleteFile(fileName);
    }

    public boolean notifyIntention(String ownerEmail, String workspaceName, String fileName, FileState fileIntention, WorkspaceType workspaceType) {
        Workspace w = null;
        if(workspaceType == WorkspaceType.FOREIGN) {
            w = getForeignWorkspaceByName(ownerEmail, workspaceName);
        } else {
            w = getOwnerWorkspaceByName(workspaceName);
        }

        if(w == null) {
            throw new WorkspaceDoesNotExistException("workspace " + workspaceName + " does not exist");
        }

        AirDeskFile file = w.getFile(fileName);

        return NetworkServiceClient.notifyIntention(w, file, fileIntention);
    }

}
