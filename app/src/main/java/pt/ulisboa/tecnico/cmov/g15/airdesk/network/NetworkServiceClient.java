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
public class NetworkServiceClient {
    //TODO when WIFIDirect is implemented, use its handler
    private static NetworkServiceServer networkServiceResponse = new NetworkServiceServer();

    public NetworkServiceClient(){}

    public static List<Workspace> getAllowedWorkspaces(User user, List<String> tags) {
        List<Workspace> allowedWorkspaces = new ArrayList<Workspace>();

        //broadcast this message and collects all workspaces
        for(Workspace workspace: networkServiceResponse.getAllowedWorkspacesS(user, tags)){
            allowedWorkspaces.add(workspace);
        }
        return allowedWorkspaces;
    }
    
    public static boolean notifyIntention(User user, Workspace workspace, AirDeskFile file, FileState intention) {
        return networkServiceResponse.notifyIntentionS(user, workspace, file, intention);
    }

    
    public static int getFileVersion(Workspace workspace, AirDeskFile file) {
        return networkServiceResponse.getFileVersionS(workspace, file);
    }

    
    public static FileState getFileState(Workspace workspace, AirDeskFile file) {
        return networkServiceResponse.getFileStateS(workspace, file);
    }

    
    public static String getFile(Workspace workspace, AirDeskFile file) {
        return networkServiceResponse.getFileS(workspace, file);
    }

    
    public static boolean sendFile(Workspace workspace, AirDeskFile file, String fileContent) {
        return networkServiceResponse.sendFileS(workspace, file, fileContent);
    }

    
    public static boolean changeQuota(Workspace workspace, long quota) {
        return networkServiceResponse.changeQuotaS(workspace, quota);
    }

}
