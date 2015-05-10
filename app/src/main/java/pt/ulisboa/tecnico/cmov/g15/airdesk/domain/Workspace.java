package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.DeleteFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.DeleteWorkspaceException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileDoesNotExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.LocalDeleteAirDeskFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.RemoteDeleteAirDeskFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceFullException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;

/**
 * Created by MSC on 02/04/2015.
 */
public abstract class Workspace implements Serializable {
    protected long quota;
    private User owner;
    private String name;
    private List<AirDeskFile> files;
    private String path;

    public Workspace() {
        files = new ArrayList<AirDeskFile>();
    }

    public Workspace(User owner, String name, long quota) {
        this();
        this.owner = owner;
        this.name = name;
        this.quota = quota;
    }

    public void create() {
        String path = FileSystemManager.createWorkspace(getOwner().getEmail(), getName(), getType());
        setPath(path);
    }

    public abstract WorkspaceType getType();

    public void delete() throws DeleteFileException, DeleteWorkspaceException {
        FileSystemManager.deleteWorkspace(getPath());
    }

    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQuota() {
        return quota;
    }

    public List<AirDeskFile> getFiles() {
        return files;
    }

    public void setFiles(List<AirDeskFile> files) {
        this.files = files;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long workspaceUsage() {
        long result = 0;
        for (AirDeskFile f : files)
            result += f.getSize();
        return result;
    }

    public AirDeskFile createFileOnNetwork(String filename) throws WorkspaceFullException, FileAlreadyExistsException {
        AirDeskFile file = createFileNoNetwork(filename);
        file.write("");
        return file;
    }

    public AirDeskFile createFileNoNetwork(String filename) throws WorkspaceFullException, FileAlreadyExistsException {
        //WARNING This method never contacts Network
        if (remainingSpace() <= 0) {
            throw new WorkspaceFullException("there is no space left on this workspace");
        }

        if (getFile(filename) != null) {
            throw new FileAlreadyExistsException("file: " + filename + "already exists.");
        };

        String path = FileSystemManager.createFile(getPath(), filename);

        /*
        if (path == null){
            //TEMPORARY 1a entrega
            path = getPath()+"/"+filename+".txt";
            //return null;
        }
        */

        AirDeskFile newFile = new AirDeskFile(filename, path, this);
        getFiles().add(newFile);


        return newFile;
    }

    public AirDeskFile getFile(String filename) {
        for (AirDeskFile af : files)
            if (af.getName().equals(filename))
                return af;
        return null;
    }

    public long remainingSpace() {
        return getQuota() - workspaceUsage();
    }

    public abstract boolean isOwner();

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void deleteFile(String fileName, WorkspaceType workspaceType) throws FileDoesNotExistsException, RemoteDeleteAirDeskFileException, LocalDeleteAirDeskFileException {
        AirDeskFile file = getFile(fileName);
        if(file == null) {
            throw new FileDoesNotExistsException("deleting file '" + fileName + "' that does not exist.");
        }
        file.delete(workspaceType);
        getFiles().remove(file);
    }
}
