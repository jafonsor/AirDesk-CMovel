package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;

/**
 * Created by diogo on 03-04-2015.
 */

/* This Service provides functions to create Workspaces (Directories) and AirDeskFiles (Files)
 * on an external (by default) or an internal storage.
 */
public class StorageService {
    private FileSystemManager FS = new FileSystemManager();
    private WorkspaceManager WM = new WorkspaceManager();

                          /****                              ****
                            **      AirDeskFile Methods       **
                             ****                          ****/

    /*  Creates a totally new empty File on the target Workspace (which is a real Directory)
     *  Return an airDeskFile with the direct canonical path setted to the real file
     */
    public AirDeskFile newAirDeskFile(Workspace wsTarget, String fileName) {
        String dirPath = wsTarget.getPath();

        File file = FS.createFile(dirPath, fileName);

        AirDeskFile airFile = null;
        try {
            airFile = new AirDeskFile(fileName, file.getCanonicalPath()); //This constructor has AirDeskFile version 0
        } catch (IOException e) {
            Log.e("exception", e.toString());
        }
        return airFile;
    }

    /*  Creates an AirDeskFile for a specified local file chosen by the user
     *  Return an airDeskFile with the direct canonical path setted to the real file
     *  Note: The receive path as argument has to be canonical.
     */
    public AirDeskFile createAirDeskFile(Workspace wsTarget, String fileName, String path) {
        long remainingSpace = wsTarget.remainingSpace();
        File file;
        AirDeskFile airFile = null;

        try {
            file = FS.getFile(path);

            if(remainingSpace >= file.length())
                airFile = new AirDeskFile(fileName,path);
            //TODO - BROADCAST to the network
        }  catch (IOException e) {
            Log.e("exception", e.toString());
        }
        return airFile;
    }

    /*  From a AirDeskFile, accesses the real stored File and reads his content
     *  Return a String with the content of the file specified on the AirDeskFile path
     */
    public String readAirDeskFile(AirDeskFile airFile) {
        File file;

        try {
            file = FS.getFile(airFile.getPath());
            //TODO - BROADCAST to the network
            return FS.readFile(file);

        } catch (IOException e) {
            Log.e("exception", e.toString());
        }

        return null;
    }

    /*  From a AirDeskFile, accesses the real stored File and deletes it
     *  Return true if the AirFile it was successful removed from the specified Workspace
     */
    public boolean deleteAirDeskFile(AirDeskFile airFile, Workspace ws) {
        File file;

        try {
            file = FS.getFile(airFile.getPath());

            if(FS.deleteFile(file)) {

                //TODO - BROADCAST to the network
                return ws.removeAirDeskFile(airFile);
            }

        } catch (IOException e) {
            Log.e("exception", e.toString());
        }

        return false;
    }

    /*  Write the new content on the desired local file and increments the AirFile version
     *  Return true if it was successful
     */
    public boolean writeAirDeskFile(AirDeskFile airFile, String content, Workspace ws) {
        File file;
        long remainingSpace = ws.remainingSpace();

        //TODO - Needs to know if someone is editing this file

        if(Integer.parseInt(content.getBytes().toString()) <= remainingSpace) { // If the content fits in the remaining quota space
            try {
                file = FS.getFile(airFile.getPath());

                FS.writeFile(file, content);
                airFile.incrementVersion();

                //TODO - BROADCAST to the network

                return true;
            } catch (IOException e) {
                Log.e("exception", e.toString());
            }
        }
        return false;

    }


                             /****                           ****
                               **     Workspace Methods       **
                                ****                       ****/

    public Workspace createWorkspace(User appUser,User wsOwner, String name, long quota) {
        return WM.createWorkspace(appUser,wsOwner, name, quota);
    }

    public void deleteWorkspace(Workspace workspace) {
        if(workspace instanceof OwnerWorkspace) {
            //TODO - tem que enviar para rede que o Workspace já não existe
        } else {
            // Se for Foreign apenas remove localmente
        }

        WM.deleteWorkspace(workspace);

    }

    public boolean changeWorkspaceQuota(Workspace ws, long newQuota, User owner) {
        boolean bool= ws.changeQuota(owner, newQuota);

        if(bool) {
            //TODO - broadcast the newQuota to the network
        }

        return bool;
    }

    public boolean updateWorkspaceTags(List<String> tags, Workspace ws, User owner) {
        boolean bool = ws.changeTags(owner,tags);

        if(bool) {
            //TODO - broadcast the new Tags to the network
        }

        return bool;
    }

}
