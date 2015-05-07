package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileDoesNotExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceDoesNotExistException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.Utils;

/**
 * Created by MSC on 02/04/2015.
 */
public class NetworkServiceServer {

    private AirDesk airDesk;

    public NetworkServiceServer() {
    }

    public NetworkServiceServer(AirDesk airDesk) {
        this.airDesk = airDesk;
    }


    public List<ForeignWorkspace> getAllowedWorkspacesS(User user, List<String> tags) {
        List<ForeignWorkspace> allowedWorkspacesR = new ArrayList<ForeignWorkspace>();
        for (OwnerWorkspace workspace : airDesk.getOwnerWorkspaces()) {

            if (workspace.userInAccessList(user)) {
                if (workspace.userHasPermissions(user)) {
                    allowedWorkspacesR.add(Utils.OwnerToForeignWorkspace(workspace));
                    continue;
                } else break;
            }

            if (workspace.getVisibility() == WorkspaceVisibility.PUBLIC) {
                if (checkTags(workspace.getTags(), tags)) {
                    allowedWorkspacesR.add(Utils.OwnerToForeignWorkspace(workspace));
                    workspace.addUserToAccessList(user.getEmail());
                    continue;
                }
            }
        }

        return allowedWorkspacesR;
    }


    public boolean notifyIntentionS(String workspaceName, String fileName, FileState intention) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspaceName);

        if (ws == null)
            throw new WorkspaceDoesNotExistException(workspaceName);

        AirDeskFile f = ws.getFile(fileName);
        if( f == null)
            f = ws.createFileNoNetwork(fileName);

        if (f.getState() == FileState.WRITE)
            return false;
        else {
            f.setState(intention);
            return true;
        }
    }


    public int getFileVersionS(Workspace workspace, AirDeskFile file) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());

        if (ws != null) {
            AirDeskFile f = ws.getFile(file.getName());
            return f.getVersion();
        }

        return -1;
    }


    public FileState getFileStateS(Workspace workspace, AirDeskFile file) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());

        if (ws != null) {
            AirDeskFile f = ws.getFile(file.getName());
            return f.getState();
        }

        return null;
    }


    public String getFileS(String workspaceName, String fileName) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspaceName);

        if (ws == null)
            throw new WorkspaceDoesNotExistException(workspaceName);

        AirDeskFile f = ws.getFile(fileName);
        if(f == null)
            throw new FileDoesNotExistsException(fileName);

        return f.readNoNetwork();
    }


    public void sendFileS(String workspaceName, String fileName, String fileContent) {
        OwnerWorkspace workspace = airDesk.getOwnerWorkspaceByName(workspaceName);

        AirDeskFile f = workspace.getFile(fileName);
        if(f == null)
            f = workspace.createFileNoNetwork(fileName);

        f.incrementVersion();
        f.setState(FileState.IDLE);
        f.writeNoNetwork(fileContent);
    }


    public boolean changeQuotaS(Workspace workspace, long quota) {
        //TODO Broadcast new quota --> all clients
        ForeignWorkspace ws = airDesk.getForeignWorkspaceByName(workspace.getOwner().getEmail(), workspace.getName());

        if (ws == null) return true;
        return ws.setQuota(quota);
    }

    public boolean checkTags(List<String> workspaceTags, List<String> userTags) {
        for (String wt : workspaceTags) {
            for (String us : userTags) {
                if (wt.equalsIgnoreCase(us)) return true;
            }
        }
        return false;
    }

    public void inviteUserS(OwnerWorkspace workspace, User user) {
        ForeignWorkspace fw = Utils.OwnerToForeignWorkspace(workspace);
        fw.create();
        if (airDesk.isForeignWorkspaceBlocked(fw.getOwner().getEmail(), fw.getName()))
            return;
        if (user.equals(airDesk.getUser()))
            this.airDesk.getForeignWorkspaces().add(fw);
    }

    public void removeWorkspaceS(OwnerWorkspace ownerWorkspace) {
        if (airDesk.isForeignWorkspaceBlocked(ownerWorkspace.getOwner().getEmail(), ownerWorkspace.getName()))
            return;
        if (airDesk.getOwnerWorkspaceByName(ownerWorkspace.getName()) == null)
            return;
        airDesk.deleteForeignWorkspace(ownerWorkspace.getOwner().getEmail(), ownerWorkspace.getName());
    }

    public AirDesk getAirDesk() {
        return airDesk;
    }

    public void setAirDesk(AirDesk airDesk) {
        this.airDesk = airDesk;
    }

    public void workspaceCreatedS() {
        //TODO temporary
        airDesk.getAllowedWorkspaces();
    }

    public void deleteFileS(Workspace workspace, AirDeskFile airDeskFile) {
        AirDeskFile f;
        if (!workspace.isOwner()) {
            f = airDesk.getOwnerWorkspaceByName(workspace.getName()).getFile(airDeskFile.getName());
            if(f!=null){
                airDesk.getOwnerWorkspaceByName(workspace.getName()).getFiles().remove(f);
            }
        } else {
            ForeignWorkspace fw = airDesk.getForeignWorkspaceByName(workspace.getOwner().getEmail(), workspace.getName());
            if(fw == null)
                return;
            f = fw.getFile(airDeskFile.getName());
            if(f!=null){
                airDesk.getForeignWorkspaceByName(workspace.getOwner().getEmail(), workspace.getName()).getFiles().remove(f);
            }
        }
        f.deleteNoNetwork();
    }

    public boolean refreshWorkspacesS() {
        getAirDesk().getAllowedWorkspaces();
        return true;
    }
}
