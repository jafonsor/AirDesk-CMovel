package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;

/**
 * Created by diogo on 03-04-2015.
 */
public class OwnerWorkspace extends Workspace {
    private List<AccessListItem> accessList;
    private WorkspaceVisibility visibility;
    private List<String> tags;

    public OwnerWorkspace(User owner, String name, long quota) {
        super(owner, name, quota);
        tags = new ArrayList<String>();
        accessList = new ArrayList<AccessListItem>();
    }

    public List<AccessListItem> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<AccessListItem> accessList) {
        this.accessList = accessList;
    }

    public WorkspaceVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(WorkspaceVisibility visibility) {
        this.visibility = visibility;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean setQuota(long newQuota) {
        if (newQuota >= this.workspaceUsage()) {
            this.quota = newQuota;
            return true;
        }
        return NetworkServiceClient.changeQuota(this, newQuota);
    }

    public boolean addUserToAccessList(AccessListItem item) {
        boolean returnValue = true;
        if (item.isInvited()) returnValue = NetworkServiceClient.inviteUser(this, item.getUser());
        //if(item.isInvited()) returnValue = NetworkServiceClient.inviteUser(this, item.getUser());
        //TODO verify if it needs another condition to add User in Network when it's not invited
        return getAccessList().add(item) && returnValue;
    }

    public boolean removeUserFromAccessList(User user) {

        AccessListItem itemToRemove = null;
        for (AccessListItem item : getAccessList())
            if (item.getUser().equals(user)) {
                itemToRemove = item;
                break;
            }
        if (itemToRemove == null) return false;
        boolean returnValue = true;
        //returnValue = NetworkServiceClient.removeUserFromAccessList(this, user);
        return getAccessList().remove(itemToRemove);
    }

    public boolean userHasPermissions(User user) {
        for (AccessListItem item : getAccessList()) {
            if (item.getUser().equals(user)) {
                return item.isAllowed();
            }
        }
        return false;
    }

    //Faz-se override porque só tem que propagar na rede se for criação de Owner
    @Override
    public boolean create() {
        if (!super.create()) return false;

        //Network
        if (getVisibility() == WorkspaceVisibility.PUBLIC) {
            //Apenas se envia para a rede se for publico
            //Neste caso é enviado um alerta a dizer que existe um novo workspace
            //e os subscritores depois actualizaram os seus workspaces chamando o getAllowedWorkspaces
            NetworkServiceClient.workspaceCreated();
        }
        return true;
    }

    //Faz-se override porque só tem que propagar na rede se for criação de Owner
    @Override
    public boolean delete() {
        if (!super.delete()) return false;
        return NetworkServiceClient.removeWorkspace(this);
    }

    @Override
    public boolean isOwner() {
        return true;
    }
}
