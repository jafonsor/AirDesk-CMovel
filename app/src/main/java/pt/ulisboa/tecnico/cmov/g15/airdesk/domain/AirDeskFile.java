package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.io.Serializable;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.DeleteFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.LocalDeleteAirDeskFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.RemoteDeleteAirDeskFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;

/**
 * Created by MSC on 02/04/2015.
 */
public class AirDeskFile implements Serializable {
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

    public void delete() throws RemoteDeleteAirDeskFileException, LocalDeleteAirDeskFileException {

        //Delete remotely
        try {
            NetworkServiceClient.deleteFile(getWorkspace(), this);
        } catch (AirDeskException e) {
            throw new RemoteDeleteAirDeskFileException(this.getName());
        }

        //Delete locally
        try {
            FileSystemManager.deleteFile(getPath());
        } catch (AirDeskException e) {
            throw new LocalDeleteAirDeskFileException(this.getName());
        }

    }

    public void deleteNoNetwork() throws DeleteFileException {
        FileSystemManager.deleteFile(getPath());
    }

    public boolean write(String content) {
        int contentSize = content.getBytes().length;

        if (getWorkspace().remainingSpace() + getSize() < contentSize) return false;

        if (NetworkServiceClient.notifyIntention(getWorkspace(), this, FileState.WRITE)) {
            incrementVersion();
            setSize(contentSize);
            NetworkServiceClient.sendFile(getWorkspace(), this, content);
            return FileSystemManager.setFileContent(getPath(), content);
        } else
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
