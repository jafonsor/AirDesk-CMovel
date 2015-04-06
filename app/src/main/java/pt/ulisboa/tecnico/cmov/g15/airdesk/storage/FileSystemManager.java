package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import android.os.Environment;

import java.io.File;

/**
 * Created by diogo on 03-04-2015.
 */
public class FileSystemManager {
    File sdcard = Environment.getExternalStorageDirectory();

    public File createFile(String dirPath, String name) {
        if(name != null)
            if(dirPath == null)
                return new File(dirPath,name+".txt");
            else //by default
                return new File(sdcard+"AirDesk/",name+".txt");

        return null;
    }

    public File getFile(String filePath) {
        if(filePath != null)
            return new File(filePath);

        return null;
    }

    public boolean deleteFile(File file){
        return file.delete();
    }
}
