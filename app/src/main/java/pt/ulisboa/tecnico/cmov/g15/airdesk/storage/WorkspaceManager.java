package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import android.os.Environment;

import java.io.File;
import java.security.acl.Owner;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;


/**
 * Created by diogo on 03-04-2015.
 */
public class WorkspaceManager {
    File sdcard = Environment.getExternalStorageDirectory();
    String state = Environment.getExternalStorageState();

    public Workspace createWorkspace(User appUser,User wsOwner, String name, double quota) {
        Workspace ws = null;
        if(appUser.getEmail().equals(wsOwner.getEmail()))
            ws = new OwnerWorkspace(wsOwner,name,quota);
        else
            ws = new ForeignWorkspace(wsOwner, name, quota);

        /*
            Creates a directory inside /sdcard/AirDesk/ with the Workspace name
            Note: This approach is useful to have different files with the
                  same name in different workspaces.
         */
        if(state.equals(Environment.MEDIA_MOUNTED)) { // If has memory card
            File newDirectory = new File(sdcard + "/AirDesk/", name);
            newDirectory.mkdir();
        }

        return ws;
    }

    public void deleteWorkspace(Workspace workspace) {

    }
}
