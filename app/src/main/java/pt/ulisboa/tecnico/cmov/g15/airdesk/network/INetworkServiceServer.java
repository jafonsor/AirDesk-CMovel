package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by MSC on 02/04/2015.
 */
public interface INetworkServiceServer {
    List<Workspace> getAllowedWorkspacesS(User user, List<String> tags);

    //returns true if the intention can be fullfilled
    boolean notifyIntentionS(User user, Workspace workspace, AirDeskFile file, FileState intention);

    int getFileVersionS(Workspace workspace, AirDeskFile file);
    FileState getFileStateS(Workspace workspace, AirDeskFile file);

    String getFileS(Workspace workspace, AirDeskFile file);
    //returns true if everything's fine
    boolean sendFileS(Workspace workspace, AirDeskFile file, String fileContent);

    // Broadcast new quota --> all clients
    boolean changeQuotaS(Workspace workspace, long quota);


}
