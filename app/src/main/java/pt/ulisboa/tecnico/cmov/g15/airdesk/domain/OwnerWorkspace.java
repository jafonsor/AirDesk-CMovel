package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.Map;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;

/**
 * Created by diogo on 03-04-2015.
 */
public class OwnerWorkspace extends Workspace {
    private Boolean mounted;
    private Map<User, Boolean> accessList;
    private WorkspaceVisibility visibility;

    public Boolean userHasPermissions(User user) {
        return accessList.get(user);
    }
}
