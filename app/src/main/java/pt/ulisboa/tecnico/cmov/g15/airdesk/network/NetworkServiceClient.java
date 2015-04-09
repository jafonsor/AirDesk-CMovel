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
    private static List<ForeignWorkspace> blockedForeignWorkspaces = new ArrayList<ForeignWorkspace>();

    public NetworkServiceClient() {
    }

    public static List<ForeignWorkspace> getAllowedWorkspaces(User user, List<String> tags) {
        List<ForeignWorkspace> allowedWorkspaces = new ArrayList<ForeignWorkspace>();

        //broadcast this message and collects all workspaces
        for (ForeignWorkspace workspace : networkServiceServer.getAllowedWorkspacesS(user, tags)) {
            if(!isWorkspaceBlocked(workspace)) allowedWorkspaces.add(workspace);
        }
        return allowedWorkspaces;
    }

    public static boolean notifyIntention(Workspace workspace, AirDeskFile file, FileState intention) {
        return networkServiceServer.notifyIntentionS(workspace, file, intention);
    }


    public static int getFileVersion(Workspace workspace, AirDeskFile file) {
        return networkServiceServer.getFileVersionS(workspace, file);
    }


    public static FileState getFileState(Workspace workspace, AirDeskFile file) {
        return networkServiceServer.getFileStateS(workspace, file);
    }


    public static String getFile(Workspace workspace, AirDeskFile file) {
        return networkServiceServer.getFileS(workspace, file);
    }


    public static boolean sendFile(Workspace workspace, AirDeskFile file, String fileContent) {
        return networkServiceServer.sendFileS(workspace, file, fileContent);
    }


    public static boolean changeQuota(Workspace workspace, long quota) {
        //TODO Broadcast new quota --> all clients except it self
        return networkServiceServer.changeQuotaS(workspace, quota);
    }

    public static boolean inviteUser(Workspace workspace, User user) {
        return networkServiceServer.inviteUserS(workspace, user);
    }

    //TODO temporary
    public static void setAirDesk(AirDesk airDesk) {
        networkServiceServer.setAirDesk(airDesk);
    }

    public static boolean removeUserFromAccessList(OwnerWorkspace ownerWorkspace, User user) {
        return networkServiceServer.removeWorkspaceS(ownerWorkspace);
    }

    public static void workspaceCreated() {
        //TODO broadcast
        networkServiceServer.workspaceCreatedS();
    }

    public static boolean removeWorkspace(OwnerWorkspace workspace) {
        //TODO broadcast to accessList
        return networkServiceServer.removeWorkspaceS(workspace);
    }


    public static boolean deleteFile(Workspace workspace, AirDeskFile airDeskFile) {
        //TODO broadcast to accessList
        return networkServiceServer.deleteFileS(workspace,airDeskFile);
    }

    public static boolean isWorkspaceBlocked(ForeignWorkspace foreignWorkspace) {
        for (ForeignWorkspace fw : blockedForeignWorkspaces) {
            if (fw.getName().equals(foreignWorkspace.getName()) &&
                    fw.getOwner().equals(foreignWorkspace.getOwner()))
                return true;
        }
        return false;
    }

    public static boolean refreshWorkspacesC() {
        return networkServiceServer.refreshWorkspacesS();
    }
}
