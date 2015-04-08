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
public class NetworkServiceClient implements INetworkServiceClient {
    //TODO when WIFIDirect is implemented, use its handler
    private NetworkServiceServer networkServiceResponse;

    public NetworkServiceClient(){}

    public NetworkServiceClient(NetworkServiceServer networkServiceResponse){
        this.networkServiceResponse=networkServiceResponse;
    }

    @Override
    public List<Workspace> getAllowedWorkspaces(User user, List<String> tags) {
        List<Workspace> allowedWorkspaces = new ArrayList<Workspace>();

        //broadcast this message and collects all workspaces
        for(Workspace workspace: networkServiceResponse.getAllowedWorkspacesS(user, tags)){
            allowedWorkspaces.add(workspace);
        }
        return allowedWorkspaces;
    }

    @Override
    public boolean notifyIntention(User user, Workspace workspace, AirDeskFile file, FileState intention) {
        return networkServiceResponse.notifyIntentionS(user, workspace, file, intention);
    }

    @Override
    public int getFileVersion(Workspace workspace, AirDeskFile file) {
        return networkServiceResponse.getFileVersionS(workspace, file);
    }

    @Override
    public FileState getFileState(Workspace workspace, AirDeskFile file) {
        return networkServiceResponse.getFileStateS(workspace, file);
    }

    @Override
    public String getFile(Workspace workspace, AirDeskFile file) {
        return networkServiceResponse.getFileS(workspace, file);
    }

    @Override
    public boolean sendFile(Workspace workspace, AirDeskFile file, String fileContent) {
        return networkServiceResponse.sendFileS(workspace, file, fileContent);
    }

    @Override
    public boolean changeQuota(Workspace workspace, long quota) {
        //TODO contact Storage Service to check if is possible to change quota.
        //If it is, change and return true, otherwise return false
        return false;
    }

    //TEMPORARIO
    public NetworkServiceServer getNetworkServiceResponse() {
        return networkServiceResponse;
    }

    //TEMPORARIO
    public void setNetworkServiceResponse(NetworkServiceServer networkServiceResponse) {
        this.networkServiceResponse = networkServiceResponse;
    }
}
