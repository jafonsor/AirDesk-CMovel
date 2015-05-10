package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import android.util.Log;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.WildcardType;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;

/**
 * Created by joao on 10-05-2015.
 */
public class RemoteJSONLibTest extends TestCase {
    public class TestClass {
        public int method(int i) {
            return i;
        }
    }

    TestClass testInstance;

    @Override
    public void setUp() {
        testInstance = new TestClass();
    }

    public void testGenerateMethodCall() {
        String jsonedCall = RemoteJSONLib.createJsonCall("method", 3);
        assertEquals("{\"methodName\":\"method\",\"args\":[{\"class\":\"int\",\"valeu\":3}]", jsonedCall);
    }

    public void testSomeJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("num", 1);
            obj.put("type", WorkspaceType.OWNER);
            String msg = obj.toString();
            JSONObject msgObj = new JSONObject(msg);

            //WorkspaceType type = (WorkspaceType)msgObj.get("type");
            //assertSame(WorkspaceType.OWNER, type);
            long i = msgObj.getLong("num");
            assertEquals(1, i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //assertEquals("{\"num\":1}",obj.toString());
    }
}
