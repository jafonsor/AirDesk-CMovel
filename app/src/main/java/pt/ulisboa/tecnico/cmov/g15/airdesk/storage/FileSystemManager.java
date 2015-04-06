package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import android.os.Environment;

import java.io.File;

/**
 * Created by diogo on 03-04-2015.
 */
public class FileSystemManager {
    File sdcard = Environment.getExternalStorageDirectory();
    String state = Environment.getExternalStorageState();

    public File createFile(String dirPath, String name) {
        File f = null;
        if(name != null)
            if(dirPath != null && state.equals(Environment.MEDIA_MOUNTED)) { //by default
                f = new File(sdcard + "/AirDesk/", name + ".txt");
                if(!f.exists())
                    return f;
            } else {
                f = new File(dirPath, name + ".txt");
                if(!f.exists())
                    return f;
            }
        return null; // Já existe um ficheiro com esse nome
    }

    public File getFile(String filePath) {
        File file = new File(filePath);

        if(filePath != null)
            if(file.exists())
                return file;
        return null; // O file que procura não existe ou não foi introduzido nenhum file path
    }

    public boolean deleteFile(File file){
        return file.delete();
    }
}
