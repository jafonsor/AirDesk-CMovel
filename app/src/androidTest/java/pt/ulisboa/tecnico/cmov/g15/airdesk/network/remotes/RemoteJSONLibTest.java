package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import android.util.Log;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
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

    public void testGenerateMethodCall() throws JSONException {
        String jsonedCall = RemoteJSONLib.createJsonCall("method", 3);

        JSONObject obj = new JSONObject();
        try {
            obj.put("methodName", "method");
            obj.put("args", new JSONArray() {{
                put(new JSONObject() {{
                    put("class", Integer.class.getName());
                    put("value", 3);
                }});
            }});
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals(obj.toString(), jsonedCall);
    }

    public void testGenerateIntResult() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("methodName", "method");
        obj.put("return", new JSONObject() {{
            put("class", Integer.class.getName());
            put("value", 3);
        }});

        int result = (int)RemoteJSONLib.generateReturnFromJson(obj.toString());
        assertEquals(3, result);
    }

    public void testGenerateLongResult() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("methodName", "method");
        obj.put("return", new JSONObject() {{
            put("class", Long.class.getName());
            put("value", 3L);
        }});

        long result = (long)RemoteJSONLib.generateReturnFromJson(obj.toString());
        assertEquals(3, result);
    }

    public void testGenerateBooleanResult() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("methodName", "method");
        obj.put("return", new JSONObject() {{
            put("class", Boolean.class.getName());
            put("value", true);
        }});

        boolean result = (boolean)RemoteJSONLib.generateReturnFromJson(obj.toString());
        assertEquals(true, result);
    }

    public void testGenerateListResult() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("methodName", "method");
        obj.put("return", new JSONObject() {{
            put("class", "array");
            put("value", new JSONArray() {{
                put("string1");
                put("string2");
            }});
        }});

        List<String> expected = new ArrayList<String>() {{
            add("string1");
            add("string2");
        }};

        List<String> result = (List<String>)RemoteJSONLib.generateReturnFromJson(obj.toString());
        assertEquals(expected, result);
    }

    public void testGenerateFileStateResult() throws JSONException {
        JSONObject objIdle = new JSONObject();
        objIdle.put("methodName", "method");
        objIdle.put("return", new JSONObject() {{
            put("class", FileState.class.getName());
            put("value", "IDLE");
        }});

        JSONObject objRead = new JSONObject();
        objRead.put("methodName", "method");
        objRead.put("return", new JSONObject() {{
            put("class", FileState.class.getName());
            put("value", "READ");
        }});

        JSONObject objWrite = new JSONObject();
        objWrite.put("methodName", "method");
        objWrite.put("return", new JSONObject() {{
            put("class", FileState.class.getName());
            put("value", "WRITE");
        }});

        FileState resultIdle  = (FileState)RemoteJSONLib.generateReturnFromJson(objIdle.toString());
        FileState resultRead  = (FileState)RemoteJSONLib.generateReturnFromJson(objRead.toString());
        FileState resultWrite = (FileState)RemoteJSONLib.generateReturnFromJson(objWrite.toString());
        assertEquals(FileState.IDLE,  resultIdle);
        assertEquals(FileState.READ,  resultRead);
        assertEquals(FileState.WRITE, resultWrite);
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
