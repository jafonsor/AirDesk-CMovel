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
public class NetworkServiceServer implements INetworkServiceServer {

    private AirDesk airDesk;

    public NetworkServiceServer(){}

    public NetworkServiceServer(AirDesk airDesk){
        this.airDesk=airDesk;
    }

   @Override
    public List<Workspace> getAllowedWorkspacesS(User user, List<String> tags) {
        List<Workspace> allowedWorkspacesR = new ArrayList<Workspace>();
        for(OwnerWorkspace workspace: airDesk.getOwnerWorkspaces()){
            if(workspace.userHasPermissions(user)){
                allowedWorkspacesR.add(workspace);
                continue;
            }

            if(checkTags(workspace.getTags(), tags)){
                allowedWorkspacesR.add(workspace);
                continue;
            }
        }

        return allowedWorkspacesR;
    }

    @Override
    public boolean notifyIntentionS(User user, Workspace workspace, AirDeskFile file, FileState intention) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());

        if(ws!=null){
            AirDeskFile f = ws.getFile(file.getName());
            if(f.getState() != FileState.WRITE){
                f.setState(intention);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getFileVersionS(Workspace workspace, AirDeskFile file) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());

        if(ws!=null) {
            AirDeskFile f = ws.getFile(file.getName());
            return f.getVersion();
        }

        return -1;
    }

    @Override
    public FileState getFileStateS(Workspace workspace, AirDeskFile file) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());

        if(ws!=null) {
            AirDeskFile f = ws.getFile(file.getName());
            return f.getState();
        }

        return null;
    }

    @Override
    public String getFileS(Workspace workspace, AirDeskFile file) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());

        if(ws!=null) {
            AirDeskFile f = ws.getFile(file.getName());
            return f.read();
        }

        return null;
    }

    @Override
    public boolean sendFileS(Workspace workspace, AirDeskFile file, String fileContent) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspaceByName(workspace.getName());
        if(ws == null) return false;
        AirDeskFile f = ws.getFile(file.getName());
        return f.write(fileContent);
    }

    @Override
    public boolean changeQuotaS(Workspace workspace, long quota) {
        //TODO Broadcast new quota --> all clients
        ForeignWorkspace ws = airDesk.getForeignWorkspaceByName(workspace.getName());
        if(ws==null) return false;
        return ws.setQuota(quota);
    }

    public boolean checkTags(List<String> workspaceTags, List<String> userTags){
        for(String wt: workspaceTags){
            for(String us: userTags){
                if(wt.equalsIgnoreCase(us)) return true;
            }
        }
        return false;
    }

    public AirDesk getAirDesk() {
        return airDesk;
    }

    public void setAirDesk(AirDesk airDesk) {
        this.airDesk = airDesk;
    }
}
