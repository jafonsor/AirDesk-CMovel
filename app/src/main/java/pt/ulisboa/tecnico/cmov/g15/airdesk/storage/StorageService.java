package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;

/**
 * Created by diogo on 03-04-2015.
 */

/* This Service provides functions to create Workspaces (Directories) and AirDeskFiles (Files)
 * on an external (by default) or an internal storage.
 */
public class StorageService {
    private FileSystemManager FS = new FileSystemManager();

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
        long remainingSpace = wsTarget.getQuota() - wsTarget.workspaceUsage();
        File file;
        AirDeskFile airFile = null;

        try {
            file = FS.getFile(path);

            if(remainingSpace >= file.length())
                airFile = new AirDeskFile(fileName,path);
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

            //if(FS.deleteFile(file))                   // If is to remove the file locally uncomment this line
            return ws.removeAirDeskFile(airFile);

        } catch (IOException e) {
            Log.e("exception", e.toString());
        }

        return false;
    }

    /*  Write the new content on the desired local file and increments the AirFile version
     *  Return true if it was successful
     */
    public boolean writeAirDeskFile(AirDeskFile airFile, String content) {
        File file;

        try {
            file = FS.getFile(airFile.getPath());

            FS.writeFile(file, content);
            airFile.incrementVersion();

            return true;
        } catch (IOException e) {
            Log.e("exception", e.toString());
        }
        return false;

    }
}
