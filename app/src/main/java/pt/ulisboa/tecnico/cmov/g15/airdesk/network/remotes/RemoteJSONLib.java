package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.JsonCallException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.JsonResultGenerationException;

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

    /*
        normal return
        { methodName: "<name>",
          return: { class: "<class name>"
                    value: <value> },
        }

        returning lists. lists are allways of strings
        { methodName: "<name>",
          return: { class: "array"
                    value: JSONArray },
        }

        exception return
        { methodName: "<name>",
          exception: { class: "<class name>"
                       stack-trace: "<stack-trace>" }
        }
     */

    public static Object generateReturnFromJson(String response) throws AirDeskException {
        Object result = null;
        try {
            JSONObject obj = new JSONObject(response);
            JSONObject jsonReturn = obj.getJSONObject("return");
            if(jsonReturn != null) {
                String returnClassName = jsonReturn.getString("class");
                result = createValueFromClassName(returnClassName, jsonReturn);
            } else {
                JSONObject jsonException = obj.getJSONObject("exception");
                if(jsonException == null)
                    throw new JsonResultGenerationException("no exception nor result on the json");
                String exceptionClassName  = jsonException.getString("class");
                String exceptionStackTrace = jsonException.getString("stack-trace");
                createAndThrowException(exceptionClassName, exceptionStackTrace);
            }
        } catch (JSONException e) {
            throw new JsonResultGenerationException(e);
        }
        return result;
    }

    private static Object createValueFromClassName(String className, JSONObject jsonReturn) throws JSONException {
        switch (className) {
            case "array" : return arrayToList(jsonReturn.getJSONArray("value"));
            case "java.lang.Integer" : return jsonReturn.getInt("value");
            case "java.lang.Long"    : return jsonReturn.getLong("value");
            case "java.lang.Boolean" : return jsonReturn.getBoolean("value");
            case "java.lang.String"  : return jsonReturn.getString("value");
            case "pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState" : return createFileState(jsonReturn.getString("value"));
            default: throw new JsonResultGenerationException("can't create object of class: " + className);
        }
    }

    // returns ArrayList<String>
    private static Object arrayToList(JSONArray array) throws JSONException {
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < array.length(); i++) {
            result.add(array.getString(i));
        }
        return result;
    }

    private static Object createFileState(String fileStateStr) {
        switch (fileStateStr) {
            case "IDLE"  : return FileState.IDLE;
            case "READ"  : return FileState.READ;
            case "WRITE" : return FileState.WRITE;
            default: throw new JsonResultGenerationException("can't create FileState: " + fileStateStr);
        }
    }

    private static void createAndThrowException(String exceptionClass, String exceptionStackTrace) throws AirDeskException {
        try {
            Class<?> clazz = Class.forName(exceptionClass);
            Constructor<?> constructor = clazz.getConstructor(String.class);
            AirDeskException e = (AirDeskException)constructor.newInstance(exceptionStackTrace);
            throw e;
        } catch (ClassNotFoundException e) {
            throw new JsonResultGenerationException(e);
        } catch (NoSuchMethodException e) {
            throw new JsonResultGenerationException(e);
        } catch (InvocationTargetException e) {
            throw new JsonResultGenerationException(e);
        } catch (InstantiationException e) {
            throw new JsonResultGenerationException(e);
        } catch (IllegalAccessException e) {
            throw new JsonResultGenerationException(e);
        }
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

