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
 * Created by MSC on 05/04/2015.
 */
public class NetworkServiceClient {
    //TODO when WIFIDirect is implemented, use its handler
    private static NetworkServiceServer networkServiceServer = new NetworkServiceServer();

    public NetworkServiceClient() {
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
    }

    public static void removeUserFromAccessList(OwnerWorkspace ownerWorkspace, User user) {
        networkServiceServer.removeWorkspaceS(ownerWorkspace);
    }

    public static void workspaceCreated() {
        //TODO broadcast
        networkServiceServer.workspaceCreatedS();
    }

    public static void removeWorkspace(OwnerWorkspace workspace) {
        //TODO broadcast to accessList
        networkServiceServer.removeWorkspaceS(workspace);
    }


    public static void deleteFile(Workspace workspace, AirDeskFile airDeskFile) {
        //TODO broadcast to accessList
        networkServiceServer.deleteFileS(workspace, airDeskFile);
    }

    public static boolean refreshWorkspacesC() {
        return networkServiceServer.refreshWorkspacesS();
    }

    public static List<ForeignWorkspace> searchWorkspaces(String email, List<String> tags) {
        List<String> workspaceNames = networkServiceServer.searchWorkspacesS(email, tags);
        List<ForeignWorkspace> foreignWorkspaces = new ArrayList<ForeignWorkspace>();
        for(String workspaceName : workspaceNames) {
            long quota = getWorkspaceQuota(email, workspaceName);
            foreignWorkspaces.add(new ForeignWorkspace(new User(email), workspaceName, quota));
        }
        return foreignWorkspaces;
    }

    public static long getWorkspaceQuota(String ownerEmail, String workspaceName) {
        return networkServiceServer.getWorkspaceQuotaS(ownerEmail, workspaceName);
    }
}
