package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.HashMap;

/**
 * Created by MSC on 02/04/2015.
 */
public abstract class Workspace {
    private User owner;
    private Integer id = Workspace.generateId(this);
    private String name;
    private long quota;
    private List<String> tags;
    private List<AirDeskFile> files;
    private String path;
    private List<AccessListItem> accessList;
    private WorkspaceVisibility visibility;

    private static Integer currentId = 0;
    private static Map<Integer, Workspace> instanceMap = new HashMap<Integer, Workspace>();

    synchronized private static Integer generateId(Workspace newInstance) {
        Integer newId = Workspace.currentId++;
        Workspace.instanceMap.put(newId, newInstance);
        return newId;
    }

    synchronized public static Workspace getById(Integer workspaceId) {
        return Workspace.instanceMap.get(workspaceId);
    }

    public Workspace() {
        tags = new ArrayList<String>();
        files = new ArrayList<AirDeskFile>();
        accessList = new ArrayList<AccessListItem>();
    }

    public Workspace(User owner, String name, long quota) {
        this();
        this.owner = owner;
        this.name = name;
        this.quota = quota;
    }

    public AirDeskFile getAirDeskFile(AirDeskFile file) {
        AirDeskFile result = null;
        for (AirDeskFile f : files) {
            if (f.getName().equals(file.getName())) {
                result = f;
            }
        }
        return result;
    }

    public boolean removeAirDeskFile(AirDeskFile file) {
        for (Iterator<AirDeskFile> iter = this.files.listIterator(); iter.hasNext(); ) {
            AirDeskFile aFile = iter.next();
            if (file.getName().equals(aFile.getName())) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    public long workspaceUsage() {
        long result = 0;
        for (AirDeskFile f : files)
            result += f.getSize();
        return result;
    }

    public long remainingSpace(){
        return getQuota()-workspaceUsage();
    }


    public Boolean userHasPermissions(User user) {
        for (AccessListItem aci : accessList) {
            if (aci.getUser().getEmail().equals(user.getEmail())) {
                return aci.getAllowed();
            }
        }
        return false;
    }

    public void changeQuota(User owner, long newQuota) {
        if (userHasPermissions(owner))
            if (newQuota >= this.workspaceUsage())
                this.quota = newQuota;
    }

    public void changeVisibilityTo(User owner, WorkspaceVisibility status) {
        if (userHasPermissions(owner))
            this.visibility = status;
    }

    public Integer getId() {
        return this.id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getTags() {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setQuota(long quota) {
        this.quota = quota;
    }

    public List<AccessListItem> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<AccessListItem> accessList) {
        this.accessList = accessList;
    }

    public WorkspaceVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(WorkspaceVisibility visibility) {
        this.visibility = visibility;
    }
}
