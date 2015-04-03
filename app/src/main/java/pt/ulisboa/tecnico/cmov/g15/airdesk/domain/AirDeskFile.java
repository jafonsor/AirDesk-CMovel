package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

/**
 * Created by MSC on 02/04/2015.
 */
public class AirDeskFile {
    private String name;
    private String path;
    private int version;

    public AirDeskFile(String name){
        this.name = name;
        this.version = 1;
    }

    public String getName(){
        return name;
    }

    public int getVersion(){
        return version;
    }
}
