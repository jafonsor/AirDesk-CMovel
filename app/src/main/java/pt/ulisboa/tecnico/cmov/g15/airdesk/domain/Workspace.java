package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;

/**
 * Created by MSC on 02/04/2015.
 */
public abstract class Workspace {
    private User owner;
    private String name;
    private long quota;
    private List<String> tags;
    private List<AirDeskFile> files;
    private String path;
    private Map<User, Boolean> accessList;
    private WorkspaceVisibility visibility;

    public Workspace(){}

    public Workspace(User owner, String name, long quota){
        this.owner = owner;
        this.name = name;
        this.quota = quota;
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

    public boolean removeAirDeskFile(AirDeskFile file){
        for (Iterator<AirDeskFile> iter = this.files.listIterator(); iter.hasNext(); ) {
            AirDeskFile aFile = iter.next();
            if (file.getName().equals(aFile.getName())) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    public long workspaceUsage(){
        long result = 0;
        for(AirDeskFile f : files)
            result += f.getSize();
        return result;
    }

    public Boolean userHasPermissions(User user) {
        return accessList.get(user);
    }

    public void changeQuota(User owner, long newQuota) {
        if(userHasPermissions(owner))
            if(newQuota >= this.workspaceUsage())
                this.quota = newQuota;
    }

    public void changeVisibilityTo(User owner, WorkspaceVisibility status) {
        if(userHasPermissions(owner))
            this.visibility = status;
    }

    public String getPath() { return this.path; }

    public void setPath(String path) { this.path = path; }

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

    public long getQuota() {
        return quota;
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
