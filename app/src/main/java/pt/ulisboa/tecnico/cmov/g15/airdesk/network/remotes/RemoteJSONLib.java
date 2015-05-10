package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.JsonCallException;

/**
 * Created by joao on 10-05-2015.
 */
public class RemoteJSONLib {

    /*
        method call structure
        { methodName: "<name>",
          args: [
            { class: "<class name>",
              value: <value> },
            ...
          ]
        }
     */
    public static String createJsonCall(String methodName, Object ...args) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("methodName", methodName);
            JSONArray argArray = new JSONArray();
            for(Object o : args) {
                JSONObject argObj = new JSONObject();
                argObj.put("class", o.getClass().getName());
                argObj.put("value", o);
                argArray.put(argObj);
            }
            obj.put("args", argArray);
        } catch (JSONException e) {
            throw new JsonCallException(e.toString());
        }
        return obj.toString();
    }

    public static Object generateReturnFromJson(String response) throws AirDeskException {

        return null;
    }

    public static Object makeInvocationFromJson(Object instance, String jsonCall) {
        return null;
    }

    public static String generateJsonFromResult(Object result) {

        return null;
    }

    public static String generateJsonFromException(AirDeskException e) {
        return null;
    }
}

