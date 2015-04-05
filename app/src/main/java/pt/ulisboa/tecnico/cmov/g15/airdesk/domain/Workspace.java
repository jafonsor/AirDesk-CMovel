package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.List;

/**
 * Created by MSC on 02/04/2015.
 */
public abstract class Workspace {
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuote() {
        return quote;
    }

    public void setQuote(double quote) {
        this.quote = quote;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<AirDeskFile> getFiles() {
        return files;
    }

    public void setFiles(List<AirDeskFile> files) {
        this.files = files;
    }
}
