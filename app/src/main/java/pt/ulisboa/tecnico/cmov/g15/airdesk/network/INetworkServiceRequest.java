package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by MSC on 02/04/2015.
 */
public interface INetworkServiceRequest {
    List<Workspace> getAllowedWorkspaces(User user, List<String> tags);

    //returns true if the intention can be fullfilled
    boolean notifyIntention(User user, Workspace workspace, AirDeskFile file, FileState intention);

    int getFileVersion(Workspace workspace, AirDeskFile file);
    FileState getFileState(Workspace workspace, AirDeskFile file);

    String getFile(Workspace workspace, AirDeskFile file);
    //returns true if everything's fine
    boolean sendFile(Workspace workspace, AirDeskFile file, String fileContent);
}
