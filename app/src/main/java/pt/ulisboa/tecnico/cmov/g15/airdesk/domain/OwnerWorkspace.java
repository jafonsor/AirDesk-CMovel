package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.InvalidQuotaException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AccessListItemNotFoundException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;

/**
 * Created by diogo on 03-04-2015.
 */
public class OwnerWorkspace extends Workspace implements Serializable {

    private long quota;
    private List<AccessListItem> accessList;
    private WorkspaceVisibility visibility;
    private List<String> tags;

    public OwnerWorkspace(User owner, String name, Long quota, WorkspaceVisibility visibility, List<String> tags) {
        super(owner, name);
        this.quota = quota;
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

    public void setVisibility(WorkspaceVisibility newVisibility) {
        if(this.visibility != newVisibility) {
            this.visibility = newVisibility;
            removeUsersFromAccessListExceptInvited();
        }
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
            return true;
        }
        return true;
    }

    @Override
    public long getQuota() { return this.quota; }

    public void setQuota(long newQuota) throws InvalidQuotaException {
        if (newQuota >= this.workspaceUsage()) {
            this.quota = newQuota;
        } else {
            throw new InvalidQuotaException("Quota is invalid");
        }
    }

    public boolean addUserToAccessList(String userEmail) {
        AccessListItem item = new AccessListItem(new User(userEmail));
        return getAccessList().add(item);
    }

    public void inviteUser(String userEmail) {
        AccessListItem item = new AccessListItem(new User(userEmail));
        item.setInvited(true);

        if(getAccessListItemByEmail(userEmail) == null)
            getAccessList().add(item);
    }

    public AccessListItem getAccessListItemByEmail(String userEmail) {
        for(AccessListItem item : getAccessList())
            if(item.getUser().getEmail().equals(userEmail))
                return item;
        return null;
    }

    public void blockUserFromAccessList(AccessListItem itemToBlock) {
        // check if an item for the same user already exists.
        // this avoids that more than one item exists for the same user
        AccessListItem itemOnList = getAccessListItemByEmail(itemToBlock.getUser().getEmail());
        if(itemOnList != null)
            itemToBlock = itemOnList;
        else
            getAccessList().add(itemToBlock);

        itemToBlock.setAllowed(false);
    }

    public void allowUserFromAccessList(AccessListItem itemToAllow) {
        // check if an item for the same user already exists.
        // this avoids that more than one item exists for the same user
        AccessListItem itemOnList = getAccessListItemByEmail(itemToAllow.getUser().getEmail());
        if(itemOnList != null) {
            itemToAllow = itemOnList;
        } else {
			getAccessList().add(itemToAllow);
        }

        itemToAllow.setAllowed(true);
    }

    public boolean userInAccessList(String userEmail) {
        for (AccessListItem item : getAccessList()) {
            if (item.getUser().getEmail().equals(userEmail)) {
                return true;
            }
        }
        return false;
    }

    public boolean userHasPermissions(String userEmail) {
        for (AccessListItem item : getAccessList()) {
            if (item.getUser().getEmail().equals(userEmail)) {
                return item.isAllowed();
            }
        }
        return false;
    }

    public void toggleUserPermissions(String userEmail, boolean oldPermission) {
        AccessListItem item = getAccessListItemByEmail(userEmail);
        if(item==null) {
            throw new AccessListItemNotFoundException(userEmail);
        }
        if(oldPermission) {
            blockUserFromAccessList(item);
        } else {
            allowUserFromAccessList(item);
        }
    }

    public WorkspaceType getType() {
        return WorkspaceType.OWNER;
    }

    //Faz-se override porque só tem que propagar na rede se for criação de Owner
    @Override
    public void delete() {
        super.delete();
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
