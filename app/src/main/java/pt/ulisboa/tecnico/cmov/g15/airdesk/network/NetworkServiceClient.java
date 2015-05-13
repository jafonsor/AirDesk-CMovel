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
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteClientSide;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteCommunicatorI;

/**
 * Created by MSC on 05/04/2015.
 */
public class NetworkServiceClient {
    //TODO when WIFIDirect is implemented, use its handler
    private static Map<String, NetworkServiceServerI> servers;

    public static void init() {
        servers = new HashMap<String, NetworkServiceServerI>();
    }

    public static void addNetworkServiceServer(String userEmail, NetworkServiceServerI server) {
        servers.put(userEmail, server);
    }

    public static void addNewElementOffNetwork(RemoteCommunicatorI communicator) {
        NetworkServiceServerI server = new RemoteClientSide(communicator);
        String serverEmail = server.getEmail();
        servers.put(serverEmail, server);
    }

    private static NetworkServiceServerI getWorkspaceOwnerServer(Workspace workspace) {
        return servers.get(workspace.getOwner().getEmail());
    }

    private static NetworkServiceServerI getUserServer(User user) {
        return servers.get(user.getEmail());
    }

    public static boolean notifyIntention(Workspace workspace, AirDeskFile file, FileState intention, boolean force) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        return fileOwnerServer.notifyIntentionS(workspace.getName(), file.getName(), intention, force);
    }


    public static int getFileVersion(Workspace workspace, AirDeskFile file) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        return fileOwnerServer.getFileVersionS(workspace, file);
    }


    public static FileState getFileState(Workspace workspace, AirDeskFile file) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        return fileOwnerServer.getFileStateS(workspace, file);
    }


    public static String getFile(Workspace workspace, String fileName) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        return fileOwnerServer.getFileS(workspace.getName(), fileName);
    }


    public static void sendFile(Workspace workspace, String fileName, String fileContent) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        fileOwnerServer.sendFileS(workspace.getName(), fileName, fileContent);
    }

    public static void inviteUser(OwnerWorkspace workspace, User user) {
        NetworkServiceServerI invitedUserServer = getUserServer(user);
        invitedUserServer.inviteUserS(workspace, user);
    }

    //TODO temporary
    /*public static void setAirDesk(AirDesk airDesk) {
        networkServiceServer.setAirDesk(airDesk);
    }*/

    // this method is no longer used. the workspaces are refreshed on the foreign workspace activity
    /*
    public static void removeWorkspace(OwnerWorkspace workspace) {
        //TODO broadcast to accessList
        String email = workspace.getOwner().getEmail();
        networkServiceServer.removeWorkspaceS(workspace);
    }*/

    // this method is no longer used. the workspaces are refreshed on the foreign workspace activity
    /*
    public static void removeUserFromAccessList(OwnerWorkspace ownerWorkspace, User user) {
        networkServiceServer.removeWorkspaceS(ownerWorkspace);
    }
    */

    public static void deleteFile(Workspace workspace, AirDeskFile airDeskFile) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        fileOwnerServer.deleteFileS(workspace.getName(), airDeskFile.getName());
    }

    public static Map<String, List<String>> searchWorkspaces(String email, List<String> tags) {
        Map<String,List<String>> searchResult = new HashMap<String,List<String>>();
        for(String foreignEmail : servers.keySet()) {
            NetworkServiceServerI workspaceOwnerServer = servers.get(foreignEmail);
            List<String> workspaceNames = workspaceOwnerServer.searchWorkspacesS(email, tags);
            searchResult.put(foreignEmail, workspaceNames);
        }
        return searchResult;
    }

    public static List<String> searchFiles(String userEmail, String workspaceName) {
        NetworkServiceServerI fileOwnerServer = servers.get(userEmail);
        return fileOwnerServer.getFileList(workspaceName);
    }

    public static long getWorkspaceQuota(String ownerEmail, String workspaceName) {
        NetworkServiceServerI workspaceOwnerServer = servers.get(ownerEmail);
        return workspaceOwnerServer.getWorkspaceQuotaS(ownerEmail, workspaceName);
    }
}
