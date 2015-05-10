package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by MSC on 05/04/2015.
 */
public class NetworkServiceClient {
    //TODO when WIFIDirect is implemented, use its handler
    private static NetworkServiceServer networkServiceServer = new NetworkServiceServer();
    private static List<String> userEmails;

    public NetworkServiceClient() {
        userEmails = new ArrayList<String>();
    }

    public static boolean notifyIntention(Workspace workspace, AirDeskFile file, FileState intention) {
        return networkServiceServer.notifyIntentionS(workspace.getName(), file.getName(), intention);
    }


    public static int getFileVersion(Workspace workspace, AirDeskFile file) {
        return networkServiceServer.getFileVersionS(workspace, file);
    }


    public static FileState getFileState(Workspace workspace, AirDeskFile file) {
        return networkServiceServer.getFileStateS(workspace, file);
    }


    public static String getFile(String workspaceName, String fileName) {
        return networkServiceServer.getFileS(workspaceName, fileName);
    }


    public static void sendFile(String workspaceName, String fileName, String fileContent) {
        networkServiceServer.sendFileS(workspaceName, fileName, fileContent);
    }


    public static boolean changeQuota(Workspace workspace, long quota) {
        //TODO Broadcast new quota --> all clients except it self
        return networkServiceServer.changeQuotaS(workspace, quota);
    }

    public static void inviteUser(OwnerWorkspace workspace, User user) {
        networkServiceServer.inviteUserS(workspace, user);
    }

    //TODO temporary
    public static void setAirDesk(AirDesk airDesk) {
        networkServiceServer.setAirDesk(airDesk);
        userEmails.add(airDesk.getUser().getEmail());
    }

    public static void removeWorkspace(OwnerWorkspace workspace) {
        //TODO broadcast to accessList
        networkServiceServer.removeWorkspaceS(workspace);
    }

    public static void removeUserFromAccessList(OwnerWorkspace ownerWorkspace, User user) {
        networkServiceServer.removeWorkspaceS(ownerWorkspace);
    }

    public static void deleteFile(Workspace workspace, AirDeskFile airDeskFile) {
        //TODO broadcast to accessList
        networkServiceServer.deleteFileS(workspace, airDeskFile);
    }

    public static Map<String, List<String>> searchWorkspaces(String email, List<String> tags) {
        Map<String,List<String>> searchResult = new HashMap<String,List<String>>();
        List<String> workspaceNames = networkServiceServer.searchWorkspacesS(email, tags);
        List<String> foreignWorkspaces = new ArrayList<String>();
        for(String workspaceName : workspaceNames) {
            foreignWorkspaces.add(workspaceName);
        }
        searchResult.put(email, foreignWorkspaces);
        return searchResult;
    }

    public static long getWorkspaceQuota(String ownerEmail, String workspaceName) {
        return networkServiceServer.getWorkspaceQuotaS(ownerEmail, workspaceName);
    }
}
