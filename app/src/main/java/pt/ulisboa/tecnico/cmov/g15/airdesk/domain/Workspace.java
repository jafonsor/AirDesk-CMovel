package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.List;

/**
 * Created by MSC on 02/04/2015.
 */
public class Workspace {
    private User owner;
    private String name;
    private double quote;
    private List<String> tags;
    private List<AirDeskFile> files;

    public Workspace(){}

    public Workspace(User owner, String name, double quote){
        this.owner = owner;
        this.name = name;
        this.quote = quote;
    }

    public AirDeskFile getAirDeskFile(AirDeskFile file) {
        AirDeskFile result = null;
        for(AirDeskFile f : files){
            if(f.getName().equals(file.getName())) {
                result = f;
            }
        }
        return result;
    }


    public List<String> getTags(){
        return tags;
    }


}
