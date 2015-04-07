package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by diogo on 03-04-2015.
 */
public class FileSystemManager {
    File sdcard = Environment.getExternalStorageDirectory();
    String state = Environment.getExternalStorageState();

    public File createFile(String dirPath, String name) {
        File f = null;
        if (name != null && dirPath != null) {
            //if (state.equals(Environment.MEDIA_MOUNTED)) { //by default
                f = new File(dirPath, name + ".txt");
                if (!f.exists())
                    try {
                        f.createNewFile();

                    } catch (IOException e) {
                        Log.e("Error", "Already exists the file with name: " + e.toString());
                    }
            return f;
           /* } else {
                f = new File(dirPath, name + ".txt");
                if (!f.exists())
                    try {
                        f.createNewFile();
                        return f;
                    } catch (IOException e) {
                        Log.e("Error", "Already exists the file with name: " + e.toString());
                    }*/
        }
        return null; // Já existe um ficheiro com esse nome
    }

    public File getFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (filePath != null) {
            if (file.exists())
                return file;
            else
                throw new IOException("The desired file does not exist.");
        } else
            throw new IOException("Missing the file path.");

        //return null; // O file que procura não existe ou não foi introduzido nenhum file path
    }

    public boolean deleteFile(File file) {
        return file.delete();
    }

    public String readFile(File file) {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            Log.e("read", "Can not read file: " + e.toString());
        }

        return text.toString();
    }

    public boolean writeFile(File file, String content) {
        try {
        Writer writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.close();

        return true;
        } catch (IOException e) {
            Log.e("write", "Can not write file: " + e.toString());
        }
        return false;
    }

}