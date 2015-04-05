package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by MSC on 05/04/2015.
 */
public class NetworkServiceRequest implements INetworkServiceRequest {
    //TODO when WIFIDirect is implemented, use its handler
    NetworkServiceResponse networkServiceResponse = new NetworkServiceResponse();

    @Override
    public List<Workspace> getAllowedWorkspaces(User user, List<String> tags) {
        List<Workspace> allowedWorkspaces = new ArrayList<Workspace>();

        //broadcast this message and collects all workspaces
        for(Workspace workspace: networkServiceResponse.getAllowedWorkspacesR(user, tags)){
            allowedWorkspaces.add(workspace);
        }
        return allowedWorkspaces;
    }

    @Override
    public boolean notifyIntention(User user, Workspace workspace, AirDeskFile file, FileState intention) {
        return networkServiceResponse.notifyIntentionR(user,workspace,file,intention);
    }

    @Override
    public int getFileVersion(Workspace workspace, AirDeskFile file) {
        return networkServiceResponse.getFileVersionR(workspace,file);
    }

    @Override
    public FileState getFileState(Workspace workspace, AirDeskFile file) {
        return networkServiceResponse.getFileStateR(workspace,file);
    }

    @Override
    public String getFile(Workspace workspace, AirDeskFile file) {
        return networkServiceResponse.getFileR(workspace,file);
    }

    @Override
    public boolean sendFile(Workspace workspace, AirDeskFile file, String fileContent) {
        return networkServiceResponse.sendFileR(workspace,file,fileContent);
    }
}
