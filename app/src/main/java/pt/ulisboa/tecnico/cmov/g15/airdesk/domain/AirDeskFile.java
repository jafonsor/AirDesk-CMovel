package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;

/**
 * Created by MSC on 02/04/2015.
 */
public class AirDeskFile {
    private String name;
    private String path;
    private int version;
    private FileState state;


    public AirDeskFile(String name, String path, FileState state){
        this.name = name;
        this.version = 1;
        this.path = path;
        this.state = state;
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
}
