package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
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
    File internal = Environment.getDataDirectory();
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
        File newDir = null;
        if(state.equals(Environment.MEDIA_MOUNTED)) { // If has memory card
            newDir = new File(sdcard + "/AirDesk/", name);
        } else {
            newDir = new File(internal + "/AirDesk/", name);
        }
        newDir.mkdir();
        try {
            ws.setPath(newDir.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ws;
    }

    public void deleteWorkspace(Workspace workspace) {
        /*
            If it is to delete all the files when a workspace is destroyed, uncomment the follow code.
         */

        /*
        File ws = null;

        if(state.equals(Environment.MEDIA_MOUNTED)) { // If has memory card
            ws = new File(sdcard + "/AirDesk/", workspace.getName());
            if(ws.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                ws.delete();
            }
        } else {
            ws = new File(internal + "/AirDesk/", workspace.getName());
            if(ws.isDirectory()){
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                ws.delete();
            }
        }*/
    }
}
