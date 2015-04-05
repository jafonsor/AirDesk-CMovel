package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by MSC on 02/04/2015.
 */
public class NetworkServiceResponse implements INetworkServiceResponse {

    private AirDesk airDesk;

    public NetworkServiceResponse(){}

    public NetworkServiceResponse(AirDesk airDesk){
        this.airDesk=airDesk;
    }

   @Override
    public List<Workspace> getAllowedWorkspacesR(User user, List<String> tags) {
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
    public boolean notifyIntentionR(User user, Workspace workspace, AirDeskFile file, FileState intention) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspace(workspace);

        if(ws!=null){
            AirDeskFile f = ws.getAirDeskFile(file);
            if(f.getState() != FileState.WRITE){
                f.setState(intention);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getFileVersionR(Workspace workspace, AirDeskFile file) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspace(workspace);

        if(ws!=null) {
            AirDeskFile f = ws.getAirDeskFile(file);
            return f.getVersion();
        }

        return -1;
    }

    @Override
    public FileState getFileStateR(Workspace workspace, AirDeskFile file) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspace(workspace);

        if(ws!=null) {
            AirDeskFile f = ws.getAirDeskFile(file);
            return f.getState();
        }

        return null;
    }

    @Override
    public String getFileR(Workspace workspace, AirDeskFile file) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspace(workspace);

        if(ws!=null) {
            AirDeskFile f = ws.getAirDeskFile(file);
            //TODO contact storage service and retrieve content of file f
        }

        return null;
    }

    @Override
    public boolean sendFileR(Workspace workspace, AirDeskFile file, String fileContent) {
        OwnerWorkspace ws = airDesk.getOwnerWorkspace(workspace);
        //TODO contact storage service and set content of file
        //file.setContent(fileContent);
        return false;
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
