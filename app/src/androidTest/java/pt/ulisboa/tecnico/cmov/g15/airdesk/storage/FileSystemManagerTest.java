package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import android.os.Environment;

import junit.framework.TestCase;

import java.io.File;

/**
 * Created by joao on 26-04-2015.
 */
public class FileSystemManagerTest extends TestCase {
    public void testResetStorageFolder() {
        File file = new File(Environment.getExternalStorageDirectory(), "/AirDesk");
        file.mkdirs();
        FileSystemManager.resetStorageFolder();
        assertFalse(FileSystemManager.getApplicationFolder().exists());
    }
}
