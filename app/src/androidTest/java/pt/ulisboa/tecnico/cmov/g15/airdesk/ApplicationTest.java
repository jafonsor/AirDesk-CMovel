package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.io.File;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileDoesNotExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceDoesNotExistException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceFullException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<AirDesk> {
    public ApplicationTest() {
        super(AirDesk.class);
    }

    AirDesk airDesk;

    long workpaceQuota = 2000L;

    public void setUp() {
        createApplication();
        FileSystemManager.deleteStorage();
        airDesk = getApplication();
        airDesk.reset();

        airDesk.setUser(new User("name","email"));

        List<String> tags = new ArrayList<String>();
        tags.add("hollyday");

        airDesk.createOwnerWorkspace("workspace", workpaceQuota, WorkspaceVisibility.PUBLIC, tags);
    }

    public void testDeleteOwnerWorkspace() {
        List<OwnerWorkspace> workspaces = airDesk.getOwnerWorkspaces();
        OwnerWorkspace w = workspaces.get(0);
        airDesk.deleteOwnerWorkspace(w.getName());
        List<OwnerWorkspace> resultWorkspaces = airDesk.getOwnerWorkspaces();
        assertFalse(resultWorkspaces.contains(w));
        File dir = FileSystemManager.workspaceDir(airDesk.getUser().getEmail(), w.getName(), WorkspaceType.OWNER);
        assertFalse(dir.exists());
    }

    public void testDeleteOwnerWorkspaceThatDoesNotExist() {
        List<OwnerWorkspace> workspaces = airDesk.getOwnerWorkspaces();
        OwnerWorkspace w = new OwnerWorkspace(airDesk.getUser(), "non existent workspace", 10L, WorkspaceVisibility.PUBLIC, new ArrayList<String>());
        try {
            airDesk.deleteOwnerWorkspace(w.getName());
            assertTrue(false);
        } catch (WorkspaceDoesNotExistException e) {
            assertTrue(true);
        }
    }

    public void testCreateFile() {
        airDesk.createFile("name", "workspace", "new_file", WorkspaceType.OWNER);
        assertTrue(airDesk.fileExists("name", "workspace", "new_file", WorkspaceType.OWNER));
    }

    public void testCreateFileFullWorkspace() {
        airDesk.createFile("name", "workspace", "new_file", WorkspaceType.OWNER);

        StringBuilder stringBuilder = new StringBuilder();
        for(long i = workpaceQuota; i > 0; i--) {
            stringBuilder.append('a');
        }
        String fileContent = stringBuilder.toString();

        airDesk.saveFileContent("name", "workspace", "new_file", fileContent, WorkspaceType.OWNER);

        try {
            airDesk.createFile("name", "workspace", "other_file", WorkspaceType.OWNER);
            assertTrue(false);
        } catch (WorkspaceFullException e) {
            assertTrue(true);
        }
    }

    public void testCreateFileWithSameName() {
        airDesk.createFile("name", "workspace", "new_file", WorkspaceType.OWNER);
        try {
            airDesk.createFile("name", "workspace", "new_file", WorkspaceType.OWNER);
            assertTrue(false);
        } catch (FileAlreadyExistsException e) {
            assertTrue(true);
        }
    }

    public void testSaveFile() {
        airDesk.createFile("name", "workspace", "new_file", WorkspaceType.OWNER);
        airDesk.saveFileContent("name", "workspace", "new_file", "fileContent", WorkspaceType.OWNER);
        String savedContent = airDesk.viewFileContent("name", "workspace", "new_file", WorkspaceType.OWNER);
        assertEquals("fileContent\n", savedContent);
    }

    public void testDeleteFile() {
        airDesk.createFile("name", "workspace", "new_file", WorkspaceType.OWNER);
        assertTrue(airDesk.fileExists("name", "workspace", "new_file", WorkspaceType.OWNER));
        airDesk.deleteFile("name", "workspace", "new_file", WorkspaceType.OWNER);
        assertFalse(airDesk.fileExists("name", "workspace", "new_file", WorkspaceType.OWNER));
    }

    public void testDeleteFileThatDoesNotExist() {
        try {
            airDesk.deleteFile("name", "workspace", "new_file", WorkspaceType.OWNER);
            assertTrue(false);
        } catch (FileDoesNotExistsException e) {
            assertTrue(true);
        }
    }

    public void testDeleteFileFromWorkspaceThatDoesNotExist() {
        try {
            airDesk.deleteFile("name", "workspace2", "new_file", WorkspaceType.OWNER);
            assertTrue(false);
        } catch (WorkspaceDoesNotExistException e) {
            assertTrue(true);
        }
    }
}