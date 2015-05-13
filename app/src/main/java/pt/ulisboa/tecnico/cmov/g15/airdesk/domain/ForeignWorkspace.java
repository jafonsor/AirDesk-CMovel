package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;

/**
 * Created by diogo on 03-04-2015.
 */
public class ForeignWorkspace extends Workspace {

    public ForeignWorkspace(User owner, String name) {
        super(owner, name);
    }

    @Override
    public boolean isOwner() {
        return false;
    }

    @Override
    public long getQuota() {
        return NetworkServiceClient.getWorkspaceQuota(getOwner().getEmail(), getName());
    }

    /*
    method create to change quota in foreign workspaces when
    the owner changes the workspace quota
     */

    @Override
    public WorkspaceType getType() {
        return WorkspaceType.FOREIGN;
    }
/*
    @Override
    public AirDeskFile getFile(String fileName) {
        AirDeskFile file = super.getFile(fileName);
        if(file == null) {
            file = createFileNoNetwork(fileName);
        }
        file.getVersion();
        //NetworkServiceClient.getFileVersion()
        String fileContent = NetworkServiceClient.getFile(this.getName(), fileName);
        return null;
    }
*/

}
