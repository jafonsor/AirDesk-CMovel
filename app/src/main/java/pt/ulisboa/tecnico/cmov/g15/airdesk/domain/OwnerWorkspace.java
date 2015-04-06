package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.Map;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;

/**
 * Created by diogo on 03-04-2015.
 */
public class OwnerWorkspace extends Workspace {
    private Map<User, Boolean> accessList;
    private WorkspaceVisibility visibility;

    public OwnerWorkspace(User owner, String name, double quota) {
        super(owner,name,quota);
    }

    public Boolean userHasPermissions(User user) {
        return accessList.get(user);
    }

    public void changeVisibilityTo(WorkspaceVisibility status) { this.visibility = status; }

    public void changeQuota(User owner, Double newQuota) {
        if(this.getOwner().equals(owner.getEmail()))
            if(newQuota >= this.workspaceUsage())
                this.setQuota(newQuota);
    }
}
