package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceServer;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceServerI;

/**
 * Created by joao on 10-05-2015.
 */
public class RemoteClientSide implements NetworkServiceServerI {



    // this method will not be needed when we start to use sockets
    private String getResponse() {
        return "something just to compile";
    }
    private Object remoteInvocation(String methodName, Object... args ) {
        String jsonCall = RemoteJSONLib.createJsonCall(methodName, args);
        // send jsonCall
        // wait for repsonse
        String response = getResponse();
        Object result = RemoteJSONLib.generateReturnFromJson(response);
        return result;
    }

    @Override
    public boolean notifyIntentionS(String workspaceName, String fileName, FileState intention, boolean force) {
        return (boolean) remoteInvocation("notifyIntentionS", workspaceName, fileName, intention, force);
    }

    @Override
    public int getFileVersionS(Workspace workspace, AirDeskFile file) {
        return (int) remoteInvocation("getFileVersionS");
    }

    @Override
    public FileState getFileStateS(Workspace workspace, AirDeskFile file) {
        return (FileState) remoteInvocation("getFileStateS");
    }

    @Override
    public String getFileS(String workspaceName, String fileName) {
        return (String) remoteInvocation("getFileS");
    }

    @Override
    public void sendFileS(String workspaceName, String fileName, String fileContent) {
        remoteInvocation("sendFileS");
    }

    @Override
    public boolean changeQuotaS(Workspace workspace, long quota) {
        return (boolean) remoteInvocation("changeQuotaS");
    }

    @Override
    public boolean checkTags(List<String> workspaceTags, List<String> userTags) {
        return (boolean) remoteInvocation("checkTags");
    }

    @Override
    public void inviteUserS(OwnerWorkspace workspace, User user) {
        remoteInvocation("inviteUserS");
    }

    @Override
    public void removeWorkspaceS(OwnerWorkspace ownerWorkspace) {
        remoteInvocation("removeWorkspaceS");
    }

    @Override
    public void deleteFileS(String workspaceName, String fileName) {
        remoteInvocation("deleteFileS");
    }

    @Override
    public List<String> searchWorkspacesS(String clientEmail, List<String> clientTags) {
        return null;
    }

    @Override
    public List<String> getFileList(String workspaceName) {
        return null;
    }

    @Override
    public long getWorkspaceQuotaS(String ownerEmail, String workspaceName) {
        return 0;
    }
}
