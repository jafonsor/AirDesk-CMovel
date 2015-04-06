package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import java.io.File;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;

/**
 * Created by diogo on 03-04-2015.
 */

/* This Service provides functions to create Workspaces (Directories) and AirDeskFiles (Files)
 * on an external (by default) or a internal storage.
 */
public class StorageService {

    public AirDeskFile createAirDeskFile(Workspace wsTarget, String fileName) {
        FileSystemManager FS = new FileSystemManager();
        String dirPath = wsTarget.getPath();

        File file = FS.createFile(dirPath, fileName);

        return null;
    }
}
