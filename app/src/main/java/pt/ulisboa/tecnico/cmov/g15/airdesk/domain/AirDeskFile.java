package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import android.util.Log;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;

/**
 * Created by MSC on 02/04/2015.
 */
public class AirDeskFile {
    private String name;
    private String path; // Must be a canonical path
    private int version;
    private FileState state;
    private long size;
    private Workspace workspace;

    public AirDeskFile(String name, String path, Workspace workspace) { // When a empty AirFile is created
        this.name = name;
        this.version = 0;
        this.path = path;
        this.size = 0;
        this.state = FileState.IDLE;
        this.workspace = workspace;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public FileState getState() {
        return state;
    }

    public void setState(FileState state) {
        this.state = state;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void incrementVersion() {
        this.version += 1;
    }

    public boolean delete() {
        boolean remoteDeleteStatus = NetworkServiceClient.deleteFile(getWorkspace(), this);
        boolean localDeleteStatus = FileSystemManager.deleteFile(getPath());
        if(!remoteDeleteStatus)
            Log.e("Error", "could not delete remote file");
        if(!localDeleteStatus)
            Log.e("Error", "could not delete local file");
        return remoteDeleteStatus && localDeleteStatus;
    }

    public boolean deleteNoNetwork() {
        return FileSystemManager.deleteFile(getPath());
    }

    public boolean write(String content) {
        int contentSize = content.getBytes().length;

        if (getWorkspace().remainingSpace() + getSize() < contentSize) return false;

        if (NetworkServiceClient.notifyIntention(getWorkspace(), this, FileState.WRITE)) {
            incrementVersion();
            setSize(contentSize);
            return NetworkServiceClient.sendFile(getWorkspace(), this, content) &&
                    FileSystemManager.setFileContent(getPath(), content);
        }else
            return false;
    }

    public boolean writeNoNetwork(String content) {
        int contentSize = content.getBytes().length;

        if (getWorkspace().remainingSpace() + getSize() < contentSize) return false;

        return FileSystemManager.setFileContent(getPath(), content);

    }

    public String read() {
        if (NetworkServiceClient.getFileVersion(getWorkspace(), this) > getVersion())
            writeNoNetwork(NetworkServiceClient.getFile(getWorkspace(), this));

        return FileSystemManager.getFileContent(getPath());

    }

    public String readNoNetwork() {
        return FileSystemManager.getFileContent(getPath());
    }

}
