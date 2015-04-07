package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import java.io.File;
import java.io.IOException;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by diogo on 03-04-2015.
 */

/* This Service provides functions to create Workspaces (Directories) and AirDeskFiles (Files)
 * on an external (by default) or an internal storage.
 */
public class StorageService {

    /*  Creates a totally new empty File on the target Workspace (which is a real Directory)
     *  Return an airDeskFile with the direct canonical path setted to the real file
     */
    public AirDeskFile newAirDeskFile(Workspace wsTarget, String fileName) {
        FileSystemManager FS = new FileSystemManager();
        String dirPath = wsTarget.getPath();

        File file = FS.createFile(dirPath, fileName);

        AirDeskFile airFile = null;
        try {
            airFile = new AirDeskFile(fileName, file.getCanonicalPath()); //This constructor has AirDeskFile version 0
        } catch (IOException e) {
            e.printStackTrace();
        }
        return airFile;
    }

    /*  Creates an AirDeskFile for a specified local file chosen by the user
     *  Return an airDeskFile with the direct canonical path setted to the real file
     *  Note: The receive path as argument has to be canonical.
     */
    public AirDeskFile createAirDeskFile(Workspace wsTarget, String fileName, String path) {
        long remainingSize = wsTarget.getQuota()-wsTarget.workspaceUsage();
        FileSystemManager FS = new FileSystemManager();

        File file = FS.getFile(path);
        AirDeskFile airFile = null;
        if(file!=null){
            if(remainingSize >= file.length())
                airFile = new AirDeskFile(fileName,path);
        }
        return airFile;
    }

    /*  From a AirDeskFile access to the real File and reads his content
     *  Return a String with the content of the file specified on the AirDeskFile path
     */
    public String readAirDeskFile(AirDeskFile file) {
        FileSystemManager FS = new FileSystemManager();
        File f = FS.getFile(file.getPath());

        return FS.readFile(f);
    }
}
