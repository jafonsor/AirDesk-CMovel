package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AccessListItem;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.INetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceServer;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.WorkspaceManager;


/**
 * Created by MSC on 02/04/2015.
 */
public class AirDesk extends Application {

    private User mUser;

    private List<OwnerWorkspace> mOwnerWorkspaces;
    private List<ForeignWorkspace> mForeignWorkspaces;

    private WorkspaceManager mWorkspaceManager = new WorkspaceManager();

    private INetworkServiceClient networkServiceClient;

    public AirDesk() {
        mOwnerWorkspaces = new ArrayList<OwnerWorkspace>();
        mForeignWorkspaces = new ArrayList<ForeignWorkspace>();
        this.networkServiceClient = new NetworkServiceClient(new NetworkServiceServer(this));

        // fake init
        OwnerWorkspace ow = new OwnerWorkspace(mUser, "workspace1", 4);
        ow.getAccessList().add(new AccessListItem(new User("xpto", "xpto@gmail.com"), true));
        ow.getAccessList().add(new AccessListItem(new User("joao", "joao@gmail.com"), true));
        ow.getAccessList().add(new AccessListItem(new User("marco", "marco@gmail.com"), false));
        mOwnerWorkspaces.add(ow);
        mOwnerWorkspaces.add(new OwnerWorkspace(mUser, "workspace2", 4));

        mForeignWorkspaces.add(new ForeignWorkspace(mUser, "workspace2", 4));
        mForeignWorkspaces.add(new ForeignWorkspace(mUser, "workspace3", 4));


    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public List<OwnerWorkspace> getOwnerWorkspaces() {
        return mOwnerWorkspaces;
    }

    public void setOwnerWorkspaces(List<OwnerWorkspace> mOwnerWorkspaces) {
        this.mOwnerWorkspaces = mOwnerWorkspaces;
    }

    public List<ForeignWorkspace> getForeignWorkspaces() {
        return mForeignWorkspaces;
    }

    public void setForeignWorkspaces(List<ForeignWorkspace> foreignWorkspaces) {
        this.mForeignWorkspaces = foreignWorkspaces;
    }

    public OwnerWorkspace getOwnerWorkspace(Workspace ow) {
        for (OwnerWorkspace ows : getOwnerWorkspaces()) {
            if (ows.getName().equals(ow.getName()))
                if (ows.getOwner().getEmail().equals(ow.getOwner().getEmail()))
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

    public Workspace getWorkspaceById(Integer workspaceId) {
        return Workspace.getById(workspaceId);
    }

    public AirDeskFile getFileById(Integer fileId) {
        return AirDeskFile.getById(fileId);
    }

    public void deleteOwnerWorkspace(Integer workspaceId) {
        // TO DO: verificar workspace id
        Workspace workspace = getWorkspaceById(workspaceId);
        getOwnerWorkspaces().remove(workspace);
        mWorkspaceManager.deleteWorkspace(workspace);
    }

    public void deleteForeignWorkspace(Integer workspaceId) {
        // TO DO: verificar workspace id
        Workspace workspace = getWorkspaceById(workspaceId);
        getForeignWorkspaces().remove(workspace);
        //mWorkspaceManager.deleteWorkspace(workspace);
    }

    public void deleteFile(Integer fileId) {
        // TO DO
    }

    public Integer createFile(String fileName) {
        // TO DO
        return 0;
    }

    public List<AirDeskFile> getWorkspaceFiles(Integer workspaceId) {
        return new ArrayList<AirDeskFile>();
    }

    public void createOwnerWorkspace(String workspaceName, int workspaceQuota, WorkspaceVisibility workspaceVisibility, ArrayList<String> workspaceTags) {
        OwnerWorkspace ownerWorkspace = new OwnerWorkspace(getUser(), workspaceName, workspaceQuota);
        ownerWorkspace.changeVisibilityTo(getUser(), workspaceVisibility);
        ownerWorkspace.setTags(workspaceTags);

        getOwnerWorkspaces().add(ownerWorkspace);
    }
}
