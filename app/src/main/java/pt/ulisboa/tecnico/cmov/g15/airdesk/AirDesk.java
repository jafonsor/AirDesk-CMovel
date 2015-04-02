package pt.ulisboa.tecnico.cmov.g15.airdesk;

import java.util.Collection;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.INetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.INetworkServiceServer;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceServer;


/**
 * Created by MSC on 02/04/2015.
 */
public class AirDesk extends Application {

    private User user;

    private List<OwnerWorkspace> ownerWorkspaces;
    private List<ForeignWorkspace> foreignWorkspaces;

    private INetworkServiceClient networkServiceClient;

    public AirDesk(){
        ownerWorkspaces = new ArrayList<OwnerWorkspace>();
        foreignWorkspaces = new ArrayList<ForeignWorkspace>();
        this.networkServiceClient = new NetworkServiceClient(new NetworkServiceServer(this));
    }

    public AirDesk(User user){
        this.user = user;
        ownerWorkspaces = new ArrayList<OwnerWorkspace>();
        foreignWorkspaces = new ArrayList<ForeignWorkspace>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OwnerWorkspace> getOwnerWorkspaces() {
        return ownerWorkspaces;
    }

    public void setOwnerWorkspaces(List<OwnerWorkspace> ownerWorkspaces) {
        this.ownerWorkspaces = ownerWorkspaces;
    }

    public List<ForeignWorkspace> getForeignWorkspaces() {
        return foreignWorkspaces;
    }

    public void setForeignWorkspaces(List<ForeignWorkspace> foreignWorkspaces) {
        this.foreignWorkspaces = foreignWorkspaces;
    }

    public OwnerWorkspace getOwnerWorkspace(Workspace ow){
        for(OwnerWorkspace ows: getOwnerWorkspaces()){
            if(ows.getName().equals(ow.getName()))
                if(ows.getOwner().getEmail().equals(ow.getOwner().getEmail()))
                    return ows;
        }

        return null;
    }

    public INetworkServiceClient getNetworkServiceClient() {
        return networkServiceClient;
    }

    public void setNetworkServiceClient(INetworkServiceClient networkServiceClient) {
        this.networkServiceClient = networkServiceClient;
    }
}
