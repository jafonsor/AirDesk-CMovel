package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by MSC on 02/04/2015.
 */
public class AirDeskFile {
    private String name;
    private String path; // Must be a canonical path
    private int version;
    private FileState state;
    private long size;


    public AirDeskFile(String name, String path){ // When a empty AirFile is created
        this.name = name;
        this.version = 0;
        this.path = path;
        this.size = 0;
    }

    public AirDeskFile(String name, String path, FileState state) { // When a AirFile with content is created
        this.name = name;
        this.version = 1;
        this.path = path;
        this.state = state;
        this.size = 0;
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
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void incrementVersion() {
        this.version += 1;
    }
}
