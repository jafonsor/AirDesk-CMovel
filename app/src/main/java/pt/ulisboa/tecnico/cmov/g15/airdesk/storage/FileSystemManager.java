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

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.CanonicalPathException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.CreateFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.DeleteFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.DeleteWorkspaceException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileDoesNotExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.InvalidFileContentException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceAlreadyExistsException;

/**
 * Created by diogo on 03-04-2015.
 */
public class FileSystemManager {

    private static File sdcard0 = Environment.getExternalStorageDirectory();
    private static File appDir = new File(sdcard0, "/AirDesk");

    // recursively delete files. used to delete all folders and subfolders
    public static boolean deleteRecursively(File file) throws DeleteFileException {

        if (file.isDirectory()) {
            for (String subFileName : file.list()) {
                deleteRecursively(new File(file, subFileName));
                Log.i("info", "deleted file " + file.getAbsolutePath() + "/" + subFileName);
            }
        }
        if (!file.delete()) {
            Log.e("Error", "failed to delete file " + file.getAbsolutePath());
            throw new DeleteFileException(file.getAbsolutePath());
        }
        return true;
    }

    public static String createFile(String dirPath, String filename)
            throws CreateFileException, DeleteFileException, CanonicalPathException {

        File f = null;
        if (filename != null && dirPath != null && !filename.isEmpty() && !dirPath.isEmpty()) {
            f = new File(dirPath, filename + ".txt");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    Log.i("Yay", "Encontrei o dir: " + f.getCanonicalPath());
                } catch (IOException e) {
                    Log.e("Error", "Could not create file " + f.getAbsolutePath() + " :" + e.getMessage());
                    throw new CreateFileException(f.getAbsolutePath());
                }
            } else {
                Log.e("Error", "Already exists the file with that name");
                throw new DeleteFileException("Already exists the file with that name:" + filename);
            }
            try {
                Log.i("Info", f.getCanonicalPath());
                return f.getCanonicalPath();
            } catch (IOException e) {
                Log.e("Error", "Could not get cannonical paht. " + e.toString());
                throw new CanonicalPathException(f.getAbsolutePath());
            }
        } else throw new CreateFileException(filename);
    }

    private static File getFile(String filePath) throws FileDoesNotExistsException {

        File file = new File(filePath);

        if (filePath != null && !filePath.isEmpty())
            if (file.exists())
                return file;

        throw new FileDoesNotExistsException(filePath); // O file que procura não existe ou não foi introduzido nenhum file path
    }

    public static String getFileContent(String filePath)
            throws FileDoesNotExistsException, InvalidFileContentException {

        File file = getFile(filePath);

        if (file == null)
            throw new FileDoesNotExistsException(filePath);

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
            throw new InvalidFileContentException(filePath);
        }

        return text.toString();
    }

    public static boolean setFileContent(String filePath, String content)
            throws FileDoesNotExistsException, InvalidFileContentException {

        File file = getFile(filePath);

        if (file == null)
            throw new FileDoesNotExistsException(filePath);

        try {
            Writer writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();

            return true;
        } catch (IOException e) {
            Log.e("write", "Can not write file: " + e.toString());
            throw new InvalidFileContentException(filePath + "\n content:" + content);
        }
    }

    public static File workspaceDir(String userEmail, String wsName, WorkspaceType workspaceType) {
        String dirName = workspaceType.toString() + "/" + userEmail + "/" + wsName;

        return new File(appDir, dirName);
    }

    public static String createWorkspace(String userEmail, String wsName, WorkspaceType workspaceType)
            throws WorkspaceAlreadyExistsException, CanonicalPathException {
        /*
            Creates a directory inside /sdcard/AirDesk/ with the workspaceType +  Workspace name
            Note: This approach is useful to have different files with the
                  same name in different workspaces.
         */

        File newDir = workspaceDir(userEmail, wsName, workspaceType);

        if (!newDir.exists()) {
            newDir.mkdirs();
            Log.i("Yay", "Criei o dir: " + wsName);
        } else {
            throw new WorkspaceAlreadyExistsException(wsName);
        }

        try {
            return newDir.getCanonicalPath();
        } catch (IOException e) {
            throw new CanonicalPathException(newDir.getAbsolutePath());
        }
    }

    public static void deleteWorkspace(String dirPath)
            throws DeleteFileException, DeleteWorkspaceException {

        File ws = new File(dirPath);
        if (ws.isDirectory()) {
            String[] children = ws.list();
            for (int i = 0; i < children.length; i++) {
                if (!new File(ws, children[i]).delete()) {
                    throw new DeleteFileException(children[i]);
                }
            }
            if(!ws.delete()) {
                throw new DeleteWorkspaceException(dirPath + " could not remove dir");
            }
        } else {
            throw new DeleteWorkspaceException(dirPath + " is not dir");
        }
    }

    public static void deleteFile(String filePath)
            throws DeleteFileException {

        File file = getFile(filePath);

        if(file == null) {
            throw new DeleteFileException("invalid path");
        }

        if (!file.delete()) {
            throw new DeleteFileException("could not delete file");
        }
    }

    public static void deleteStorage() {
        deleteRecursively(appDir);
    }

}