package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import junit.framework.TestCase;

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
        assertEquals("{'methodName':'method','args':[{'class':'int','valeu':'3'} ]", jsonedCall);
    }
}
