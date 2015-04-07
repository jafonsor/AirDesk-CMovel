package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by MSC on 02/04/2015.
 */
public class AirDeskFile {
    private Integer id = AirDeskFile.generateId(this);
    private String name;
    private String path; // Must be a canonical path
    private int version;
    private FileState state;
    private long size;

    private static Integer currentId = 0;
    private static Map<Integer,AirDeskFile> instanceMap = new HashMap<Integer,AirDeskFile>();
    synchronized private static Integer generateId(AirDeskFile newInstance) {
        Integer newId = AirDeskFile.currentId++;
        AirDeskFile.instanceMap.put(newId, newInstance);
        return newId;
    }
    synchronized public static AirDeskFile getById(Integer workspaceId) {
        return AirDeskFile.instanceMap.get(workspaceId);
    }

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

    public Integer getId() { return id; }

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

    public void incrementVersion() { this.version += 1; }
}
