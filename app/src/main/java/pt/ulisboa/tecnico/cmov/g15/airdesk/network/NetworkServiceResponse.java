package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by MSC on 02/04/2015.
 */
public class NetworkServiceResponse implements INetworkServiceResponse {

    AirDesk airDesk;
    @Override
    public List<Workspace> getAllowedWorkspacesR(User user, List<String> tags) {
        List<Workspace> allowedWorkspacesR = new ArrayList<Workspace>();
        for(Workspace workspace: airDesk.getOwnerWorkspaces()){
            if(workspace.userHasPermissions(user)){
                allowedWorkspacesR.add(workspace);
                continue;
            }

            if(checkTags(workspace.getTags(), tags)){
                allowedWorkspacesR.add(workspace);
                continue;
            }
        }
    }

    @Override
    public boolean notifyIntentionR(User user, Workspace workspace, AirDeskFile file, FileState intention) {
        if(airDesk.getOwnerWorkspace(workspace))
        return false;
    }

    @Override
    public int getFileVersionR(Workspace workspace, AirDeskFile file) {
        return 0;
    }

    @Override
    public FileState getFileStateR(Workspace workspace, AirDeskFile file) {
        return null;
    }

    @Override
    public String getFileR(Workspace workspace, AirDeskFile file) {
        return null;
    }

    @Override
    public boolean sendFileR(Workspace workspace, AirDeskFile file, String fileContent) {
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
}
