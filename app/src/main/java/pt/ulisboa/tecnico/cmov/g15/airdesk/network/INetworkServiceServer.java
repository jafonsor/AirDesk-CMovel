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
    List<Workspace> getAllowedWorkspacesR(User user, List<String> tags);

    //returns true if the intention can be fullfilled
    boolean notifyIntentionR(User user, Workspace workspace, AirDeskFile file, FileState intention);

    int getFileVersionR(Workspace workspace, AirDeskFile file);
    FileState getFileStateR(Workspace workspace, AirDeskFile file);

    String getFileR(Workspace workspace, AirDeskFile file);
    //returns true if everything's fine
    boolean sendFileR(Workspace workspace, AirDeskFile file, String fileContent);


}
