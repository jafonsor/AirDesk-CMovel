package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by joao on 10-05-2015.
 */
public interface NetworkServiceServerI {
    String getEmail();

    boolean notifyIntentionS(String workspaceName, String fileName, FileState intention, boolean force);

    int getFileVersionS(String workspaceName, String fileName);

    FileState getFileStateS(String workspaceName, String fileName);

    String getFileS(String workspaceName, String fileName);

    void sendFileS(String workspaceName, String fileName, String fileContent);

    boolean checkTags(List<String> workspaceTags, List<String> userTags);

    void deleteFileS(String workspaceName, String fileName);

    List<String> searchWorkspacesS(String clientEmail, List<String> clientTags);

    List<String> getFileList(String workspaceName);

    // i don't know if when we add wifi to this the ownerEmail will be needed
    long getWorkspaceQuotaS(String ownerEmail, String workspaceName);

    void workspaceRemovedS(String userEmail, String workspaceName);

    void fileRemovedS(String userEmail, String workspaceName, String fileName);
}
