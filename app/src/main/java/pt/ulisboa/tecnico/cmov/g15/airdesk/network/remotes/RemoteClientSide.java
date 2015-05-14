package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import android.util.Log;

import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceServerI;

/**
 * Created by joao on 10-05-2015.
 */
public class RemoteClientSide implements NetworkServiceServerI {

    RemoteCommunicatorI communicator;

    public RemoteClientSide(RemoteCommunicatorI communicator) {
        this.communicator = communicator;
    }

    private Object remoteInvocation(String methodName, Object... args ) {
        Log.e("json", "remoteCall: " + methodName);
        String jsonCall = RemoteJSONLib.createJsonCall(methodName, args);
        communicator.send(jsonCall);
        String response = communicator.receive();
        Object result = RemoteJSONLib.generateReturnFromJson(response);
        return result;
    }

    @Override
    public String getEmail() {
        return (String) remoteInvocation("getString");
    }

    @Override
    public boolean notifyIntentionS(String workspaceName, String fileName, FileState intention, boolean force) {
        return (boolean) remoteInvocation("notifyIntentionS", workspaceName, fileName, intention, force);
    }

    @Override
    public int getFileVersionS(String workspaceName, String fileName) {
        return (int) remoteInvocation("getFileVersionS", workspaceName, fileName);
    }

    @Override
    public FileState getFileStateS(String workspaceName, String fileName) {
        return (FileState) remoteInvocation("getFileStateS", workspaceName, fileName);
    }

    @Override
    public String getFileS(String workspaceName, String fileName) {
        return (String) remoteInvocation("getFileS", workspaceName, fileName);
    }

    @Override
    public void sendFileS(String workspaceName, String fileName, String fileContent) {
        remoteInvocation("sendFileS", workspaceName, fileName, fileContent);
    }

    @Override
    public boolean checkTags(List<String> workspaceTags, List<String> userTags) {
        return (boolean) remoteInvocation("checkTags", workspaceTags, userTags);
    }

    @Override
    public void removeWorkspaceS(String userEmail, String workspaceName) {
        remoteInvocation("removeWorkspaceS", userEmail, workspaceName);
    }

    @Override
    public void deleteFileS(String workspaceName, String fileName) {
        remoteInvocation("deleteFileS", workspaceName, fileName);
    }

    @Override
    public List<String> searchWorkspacesS(String clientEmail, List<String> clientTags) {
        return (List<String>)remoteInvocation("searchWorkspace", clientEmail, clientTags);
    }

    @Override
    public List<String> getFileList(String workspaceName) {
        return (List<String>)remoteInvocation("getFileList", workspaceName);
    }

    @Override
    public long getWorkspaceQuotaS(String ownerEmail, String workspaceName) {
        return (long)remoteInvocation("getWorkspaceQuotaS", ownerEmail, workspaceName);
    }
}
