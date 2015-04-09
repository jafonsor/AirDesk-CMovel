package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

/**
 * Created by diogo on 03-04-2015.
 */
public class ForeignWorkspace extends Workspace {

    public ForeignWorkspace(User owner, String name, long quota) {
        super(owner, name, quota);
    }

    /*
    method create to change quota in foreign workspaces when
    the owner changes the workspace quota
     */

    public boolean setQuota(long newQuota) {
        this.quota = newQuota;
        return true;
    }


}
