package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;

/**
 * Created by MSC on 02/04/2015.
 */
public abstract class Workspace {
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

    public boolean create() {
        String path = FileSystemManager.createWorkspace(getOwner().getEmail(), getName());

        if (path == null) return false;

        setPath(path);
        return true;
    }

    public boolean delete() {
        return FileSystemManager.deleteWorkspace(getPath());
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

    public AirDeskFile createFile(String filename) {
        //WARNING This method never contacts Network
        if (remainingSpace() <= 0) {
            Log.e("workspace", "creating file but there is no space left");
            return null;
        }

        if (getFile(filename) != null) return null;

        String path = FileSystemManager.createFile(getPath(), filename);
        if (path == null)
            return null;

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

}
