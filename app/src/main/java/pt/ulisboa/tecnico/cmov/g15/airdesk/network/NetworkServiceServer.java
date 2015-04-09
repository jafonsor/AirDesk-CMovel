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
            if (workspace.userHasPermissions(user)) {

                //TODO alterar, muito feio
                Workspace wTemp = (Workspace) workspace;
                allowedWorkspacesR.add((ForeignWorkspace) wTemp);
                continue;
            }

            if (checkTags(workspace.getTags(), tags)) {
                Workspace wTemp = (Workspace) workspace;
                allowedWorkspacesR.add((ForeignWorkspace) wTemp);
                continue;
            }
        }

        return allowedWorkspacesR;
    }


    public boolean notifyIntentionS(Workspace workspace, AirDeskFile file, FileState intention) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());

        if (ws != null) {
            AirDeskFile f = ws.getFile(file.getName());
            if (f.getState() != FileState.WRITE) {
                f.setState(intention);
                return true;
            }
        }
        return false;
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


    public String getFileS(Workspace workspace, AirDeskFile file) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());

        if (ws != null) {
            AirDeskFile f = ws.getFile(file.getName());
            return f.read();
        }

        return null;
    }


    public boolean sendFileS(Workspace workspace, AirDeskFile file, String fileContent) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());
        if (ws == null) return false;
        AirDeskFile f = ws.getFile(file.getName());
        return f.write(fileContent);
    }


    public boolean changeQuotaS(Workspace workspace, long quota) {
        //TODO Broadcast new quota --> all clients
        ForeignWorkspace ws = airDesk.getForeignWorkspaceByName(workspace.getName(),workspace.getOwner());
        if (ws == null) return false;
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

    public boolean inviteUserS(Workspace workspace, User user) {
        return this.airDesk.getForeignWorkspaces().add((ForeignWorkspace) workspace);
    }

    public boolean removeWorkspaceS(OwnerWorkspace ownerWorkspace) {
        return airDesk.deleteForeignWorkspace(ownerWorkspace.getName(), ownerWorkspace.getOwner());
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

    public boolean deleteFileS(Workspace workspace, AirDeskFile airDeskFile) {
        AirDeskFile f;
        if (workspace.isOwner()) {
            f = airDesk.getOwnerWorkspaceByName(workspace.getName()).getFile(airDeskFile.getName());
        } else {
            f = airDesk.getForeignWorkspaceByName(workspace.getName(),workspace.getOwner()).getFile(airDeskFile.getName());
        }
        return f.deleteNoNetwork();
    }
}
