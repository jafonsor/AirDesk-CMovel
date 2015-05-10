package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.io.File;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AccessListItem;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.FileDoesNotExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceDoesNotExistException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceFullException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.ForeignFragment;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<AirDesk> {

    final String OWNERUSERNAME = "owner";
    final String OWNEREMAIL = "owner@tecnico.ulisboa.pt";

    public ApplicationTest() {
        super(AirDesk.class);
    }

    AirDesk airDesk;
    User user;

    long workpaceQuota = 2000L;

    public void setUp() {
        FileSystemManager.deleteStorage();
        createApplication();
        airDesk = getApplication();


        user = new User(OWNERUSERNAME,OWNEREMAIL);
        airDesk.setUser(user);

        airDesk.reset();
        airDesk.setUser(user);

        List<String> tags = new ArrayList<String>();
        tags.add("hollyday");

        user.setUserTags(tags);

        airDesk.createOwnerWorkspace("workspace", workpaceQuota, WorkspaceVisibility.PUBLIC, tags);
    }

    // -- Foreign Workspace

    public void testCreateForeignWorkspace() {
        int sizeForeignWorkspaceList = airDesk.getForeignWorkspaces().size();
        airDesk.createForeignWorkspace(new User(OWNERUSERNAME, OWNEREMAIL), "ForeignWorkspace1", workpaceQuota);
        assertEquals("sizeWorskpaces wasn't incremented", sizeForeignWorkspaceList + 1, airDesk.getForeignWorkspaces().size());
        File dir = FileSystemManager.workspaceDir(OWNEREMAIL, "ForeignWorkspace1", WorkspaceType.FOREIGN);
        assertTrue("Workspace was not created in storage", dir.exists());
    }

    public void testDeleteForeignWorkspace() {
        airDesk.createForeignWorkspace(new User(OWNERUSERNAME, OWNEREMAIL), "workspace", workpaceQuota);
        airDesk.deleteForeignWorkspace(OWNEREMAIL, "workspace");
        File dir = FileSystemManager.workspaceDir(OWNEREMAIL, "workspace", WorkspaceType.FOREIGN);
        assertFalse(dir.exists());
        assertNull(airDesk.getForeignWorkspaceByName(OWNEREMAIL, "workspace"));
    }

    public void testDeleteForeignWorkspaceThatDoesNotExist() {
        File dir = FileSystemManager.workspaceDir(OWNEREMAIL, "workspace", WorkspaceType.FOREIGN);
        assertFalse(dir.exists());
        assertNull(airDesk.getForeignWorkspaceByName(OWNEREMAIL, "workspace"));
        try {
            airDesk.deleteForeignWorkspace(OWNEREMAIL, "workspace");
            assertTrue(false);
        } catch(WorkspaceDoesNotExistException e) {
            assertTrue(true);
        }
    }

    // -- Owner Workspace

    public void testCreateOwnerWorkspace() {
        List<String> tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        int sizeOwnerWorkspaceList = airDesk.getOwnerWorkspaces().size();
        airDesk.createOwnerWorkspace("Workspace1", 200L, WorkspaceVisibility.PUBLIC, tags);
        assertEquals("sizeWorskpaces wasn't incremented", sizeOwnerWorkspaceList + 1, airDesk.getOwnerWorkspaces().size());
        File dir = FileSystemManager.workspaceDir(OWNEREMAIL, "Workspace1", WorkspaceType.OWNER);
        assertTrue("Workspace was not created in storage", dir.exists());
    }

    public void testDeleteOwnerWorkspace() {
        List<OwnerWorkspace> workspaces = airDesk.getOwnerWorkspaces();
        OwnerWorkspace w = workspaces.get(0);
        File dir = FileSystemManager.workspaceDir(airDesk.getUser().getEmail(), w.getName(), WorkspaceType.OWNER);
        assertTrue(dir.exists());
        airDesk.deleteOwnerWorkspace(w.getName());
        List<OwnerWorkspace> resultWorkspaces = airDesk.getOwnerWorkspaces();
        assertFalse(resultWorkspaces.contains(w));
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

    // -- file
    public void testFileExists() {
        airDesk.createFile(OWNEREMAIL, "workspace", "new_file", WorkspaceType.OWNER);

        assertTrue(airDesk.fileExists(OWNEREMAIL, "workspace", "new_file", WorkspaceType.OWNER));

        OwnerWorkspace ow = airDesk.getOwnerWorkspaceByName("workspace");
        File file = new File(ow.getPath(), "new_file.txt");
        assertTrue(file.exists());
    }

    // -- Foreign file
    public void testCreateForeignFile() {
        airDesk.createForeignWorkspace(new User(OWNERUSERNAME, OWNEREMAIL), "workspace", workpaceQuota);
        airDesk.createFile(OWNEREMAIL, "workspace", "new_file", WorkspaceType.FOREIGN);
        assertTrue(airDesk.fileExists(OWNEREMAIL, "workspace", "new_file", WorkspaceType.FOREIGN));
        assertTrue(airDesk.fileExists(OWNEREMAIL, "workspace", "new_file", WorkspaceType.OWNER));
    }

    public void testChangeContentOwnerFile() {
        airDesk.createFile(OWNEREMAIL, "workspace", "new_file", WorkspaceType.OWNER);
        AirDeskFile file = airDesk.getOwnerWorkspaceByName("workspace").getFile("new_file");
        int fileInitialVersion = file.getVersion();
        airDesk.saveFileContent(OWNEREMAIL, "workspace", "new_file", "content", WorkspaceType.OWNER);
        assertEquals(fileInitialVersion + 1, file.getVersion());
        String readContent = airDesk.viewFileContent(OWNEREMAIL, "workspace", "new_file", WorkspaceType.OWNER);
        assertEquals("content\n", readContent);
    }

    // -- Owner file

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

    public void testSearchWorkspaces() {
        List<String> userTags = user.getUserTags();
        List<String> wrongTags = new ArrayList<String>() {{
            add("wrongTag");
        }};

        airDesk.getForeignWorkspaces().clear();

        airDesk.createOwnerWorkspace("workspace2", 1000L, WorkspaceVisibility.PRIVATE, userTags);
        airDesk.createOwnerWorkspace("workspace3", 1000L, WorkspaceVisibility.PUBLIC, userTags);
        airDesk.createOwnerWorkspace("workspace4", 1000L, WorkspaceVisibility.PUBLIC, wrongTags);
        airDesk.createOwnerWorkspace("workspace5", 1000L, WorkspaceVisibility.PUBLIC, userTags);
        airDesk.createOwnerWorkspace("workspace6", 1000L, WorkspaceVisibility.PUBLIC, userTags);
        airDesk.createOwnerWorkspace("workspace7", 1000L, WorkspaceVisibility.PRIVATE, wrongTags);

        OwnerWorkspace ow1 = airDesk.getOwnerWorkspaceByName("workspace5");
        ow1.allowUserFromAccessList(new AccessListItem(user));

        OwnerWorkspace ow2 = airDesk.getOwnerWorkspaceByName("workspace6");
        ow2.blockUserFromAccessList(new AccessListItem(user));

        OwnerWorkspace ow3 = airDesk.getOwnerWorkspaceByName("workspace7");
        AccessListItem invitedItem = new AccessListItem(user);
        invitedItem.setInvited(true);
        ow3.allowUserFromAccessList(invitedItem);


        NetworkServiceClient.addForeignUser(OWNEREMAIL);

        airDesk.searchWorkspaces();

        assertNull(airDesk.getForeignWorkspaceByName(OWNEREMAIL, "workspace2"));
        assertNotNull(airDesk.getForeignWorkspaceByName(OWNEREMAIL, "workspace3"));
        assertNull(airDesk.getForeignWorkspaceByName(OWNEREMAIL, "workspace4"));
        assertNotNull(airDesk.getForeignWorkspaceByName(OWNEREMAIL, "workspace5"));
        assertNull(airDesk.getForeignWorkspaceByName(OWNEREMAIL, "workspace6"));
        assertNotNull(airDesk.getForeignWorkspaceByName(OWNEREMAIL, "workspace7"));
    }

    @Override
    protected void tearDown() throws Exception {
        FileSystemManager.deleteStorage();
        airDesk.reset();
    }
}