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

    public OwnerWorkspace(User owner, String name, Long quota, WorkspaceVisibility visibility, List<String> tags) {
        super(owner, name, quota);
        this.visibility = visibility;
        this.tags = tags;
        this.accessList = new ArrayList<AccessListItem>();
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

    public boolean setVisibility(WorkspaceVisibility newVisibility) {
        if(this.visibility != newVisibility) {
            this.visibility = newVisibility;
            //TODO - NetworkServiceClient.changeVisibility(this, newVisibility)
            return true;
        }
        return true;
    }

    public List<String> getTags() {
        return tags;
    }

    private boolean areListsEqual(List l1, List l2) {
        for(Object o1 : l1)
            for(Object o2 : l2)
                if(!o1.equals(o2))
                    return false;
        return true;
    }

    public boolean setTags(List<String> newTags) {
        if (!areListsEqual(this.tags, newTags)) {
            this.tags = newTags;
            //TODO - NetworkServiceClient.changeTags(this, newTags);
            return true;
        }
        return true;
    }

    public boolean setQuota(long newQuota) {
        if (newQuota >= this.workspaceUsage()) {
            this.quota = newQuota;
            return NetworkServiceClient.changeQuota(this, newQuota);
        }
        return false;
    }

    public boolean addUserToAccessList(AccessListItem item) {
        boolean returnValue = true;
        if (item.isInvited()) returnValue = NetworkServiceClient.inviteUser(this, item.getUser());
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
        returnValue = NetworkServiceClient.removeUserFromAccessList(this, user);
        return returnValue && getAccessList().remove(itemToRemove);
    }

    public boolean userInAccessList(User user) {
        for (AccessListItem item : getAccessList()) {
            if (item.getUser().equals(user)) {
                return true;
            }
        }
        return false;
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
            /*Apenas se envia para a rede se for publico
            Neste caso é enviado um alerta a dizer que existe um novo workspace
            e os subscritores depois actualizaram os seus workspaces chamando o getAllowedWorkspaces */
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
