package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;

/**
 * Created by diogo on 03-04-2015.
 */
public class OwnerWorkspace extends Workspace implements Serializable {
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
            removeUsersFromAccessListExceptInvited();
            NetworkServiceClient.refreshWorkspacesC();
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
            removeUsersFromAccessListExceptInvited();
            NetworkServiceClient.refreshWorkspacesC();
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

    public boolean addUserToAccessList(String userEmail) {
        AccessListItem item = new AccessListItem(new User(userEmail));
        return getAccessList().add(item);
    }

    public void inviteUser(String userEmail) {
        AccessListItem item = new AccessListItem(new User(userEmail));
        item.setInvited(true);

        NetworkServiceClient.inviteUser(this, item.getUser());
        if(getAccessListItemByEmail(userEmail) == null)
            getAccessList().add(item);
    }

    public AccessListItem getAccessListItemByEmail(String userEmail) {
        for(AccessListItem item : getAccessList())
            if(item.getUser().getEmail().equals(userEmail))
                return item;
        return null;
    }

    public boolean blockUserFromAccessList(AccessListItem itemToBlock) {
        NetworkServiceClient.removeUserFromAccessList(this, itemToBlock.getUser());
        itemToBlock.setAllowed(false);
        return true;
    }

    public boolean allowUserFromAccessList(AccessListItem itemToAllow) {
        removeUsersFromAccessListExceptInvited();
        boolean returnValue = NetworkServiceClient.refreshWorkspacesC();
        if(!returnValue) {
            Log.e("Error", "Could not refresh workspaces when  allowing  " + getName() + ", " + itemToAllow.getUser().getEmail());
            return false;
        }
        if(itemToAllow.isInvited()) {
            NetworkServiceClient.inviteUser(this, getOwner());
        }
        itemToAllow.setAllowed(true);
        return true;
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

    public boolean toggleUserPermissions(String userEmail, boolean oldPermission) {
        AccessListItem item = getAccessListItemByEmail(userEmail);
        if(item==null) {
            Log.e("Error", "Could not find accesslist item by email: " + userEmail);
            return false;
        }
        if(oldPermission) {
            return blockUserFromAccessList(item);
        } else {
            return allowUserFromAccessList(item);
        }
    }

    public WorkspaceType getType() {
        return WorkspaceType.OWNER;
    }

    //Faz-se override porque só tem que propagar na rede se for criação de Owner
    @Override
    public void create() {
        super.create();

        //Network
        if (getVisibility() == WorkspaceVisibility.PUBLIC) {
            /*Apenas se envia para a rede se for publico
            Neste caso é enviado um alerta a dizer que existe um novo workspace
            e os subscritores depois actualizaram os seus workspaces chamando o getAllowedWorkspaces */
            NetworkServiceClient.workspaceCreated();
        }
    }

    //Faz-se override porque só tem que propagar na rede se for criação de Owner
    @Override
    public void delete() {
        super.delete();
        NetworkServiceClient.removeWorkspace(this);
    }

    @Override
    public boolean isOwner() {
        return true;
    }

    public void removeUsersFromAccessListExceptInvited(){
        List<AccessListItem> acListToRemove = new ArrayList<AccessListItem>();
        for(AccessListItem ac: getAccessList()){
            if(!ac.isInvited()) acListToRemove.add(ac);
        }
        getAccessList().removeAll(acListToRemove);
    }


}
