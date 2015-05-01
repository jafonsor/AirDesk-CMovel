package pt.ulisboa.tecnico.cmov.g15.airdesk.domain;

import android.test.ApplicationTestCase;

import junit.framework.TestCase;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.DeleteFileException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceFullException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;

/**
 * Created by joao on 25-04-2015.
 */
public class WorkspaceTest extends TestCase {

    User user;
    Workspace workspace;

    @Override
    public void setUp() {
        try {
            FileSystemManager.resetStorageFolder();
        } catch (DeleteFileException e) {
            // ignore. the folder may no be there
        }
        user = new User("someEmail");
        workspace = new Workspace(user, "workspaceName", 1000) {
            @Override
            public boolean isOwner() {
                return false;
            }
        };
        workspace.create(WorkspaceType.FOREIGN);
    }

    public void testCreateFileOnEmptyWorkspace() {
        String fileName = "fileName";
        AirDeskFile file = workspace.createFile(fileName);
        assertEquals(fileName, file.getName());
        assertEquals(workspace.getPath() + "/" + fileName + ".txt", file.getPath());
        assertSame(workspace, file.getWorkspace());
    }

    public void testCreateFileOnFullWorkspace() {
        StringBuilder contentBuilder = new StringBuilder();
        for (long i = workspace.getQuota(); i > 0; i--) {
            contentBuilder.append('a');
        }
        String fileContent = contentBuilder.toString();

        AirDeskFile file = workspace.createFile("bigFile");
        file.write(fileContent);

        assertEquals(0, workspace.remainingSpace());

        try {
            workspace.createFile("extraFile");
            assertTrue(false);
        } catch (WorkspaceFullException e) {
            assertTrue (true);
        }
    }
}
