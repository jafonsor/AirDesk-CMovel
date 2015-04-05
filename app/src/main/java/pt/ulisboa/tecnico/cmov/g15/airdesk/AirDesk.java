package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.INetworkServiceRequest;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.INetworkServiceResponse;

/**
 * Created by MSC on 02/04/2015.
 */
public class AirDesk extends Application {

    private User user;

    private List<OwnerWorkspace> ownerWorkspaces;
    private List<ForeignWorkspace> foreignWorkspaces;

    private INetworkServiceRequest networkServiceRequest;
    private INetworkServiceResponse networkServiceResponse;

    public AirDesk(){

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

    public INetworkServiceRequest getNetworkServiceRequest() {
        return networkServiceRequest;
    }

    public void setNetworkServiceRequest(INetworkServiceRequest networkServiceRequest) {
        this.networkServiceRequest = networkServiceRequest;
    }

    public INetworkServiceResponse getNetworkServiceResponse() {
        return networkServiceResponse;
    }

    public void setNetworkServiceResponse(INetworkServiceResponse networkServiceResponse) {
        this.networkServiceResponse = networkServiceResponse;
    }
}
