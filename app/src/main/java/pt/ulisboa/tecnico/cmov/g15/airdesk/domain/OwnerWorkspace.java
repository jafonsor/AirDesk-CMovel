package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.Map;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;

/**
 * Created by diogo on 03-04-2015.
 */
public class OwnerWorkspace extends Workspace {

    public OwnerWorkspace(User owner, String name, long quota) {
        super(owner,name,quota);
    }
    
}
