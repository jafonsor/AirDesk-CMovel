package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.JsonCallException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.JsonExceptionStringifyingException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.JsonInvocationException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.JsonResultGenerationException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.JsonResultStringifyingException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.JsonServerCallException;

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
                argObj.put("class", jsonedClassName(o.getClass()));
                argObj.put("value", convertToArrayIfNeeded(o));
                argArray.put(argObj);
            }
            obj.put("args", argArray);
        } catch (JSONException e) {
            throw new JsonCallException(e.toString());
        }
        String res = obj.toString();
        Log.e("json", "createJsonCall: " + res);
        return res;
    }

    private static String jsonedClassName(Class<?> clazz) {
        if(List.class.isAssignableFrom(clazz)) {
            return "array";
        } else {
            return clazz.getName();
        }
    }

    private static Object convertToArrayIfNeeded(Object o) throws JSONException {
        if(List.class.isAssignableFrom(o.getClass())) {
            return new JSONArray((List)o);
        } else {
            return o;
        }
    }

    /*
        normal return
        { methodName: "<name>",
          return: { class: "<class name>"
                    value: <value> },
        }

        returning lists. lists are allways of strings
        {
          return: { class: "array"
                    value: JSONArray },
        }

        exception return
        {
          exception: { class: "<class name>"
                       message: "<message>" }
        }
     */

    public static Object generateReturnFromJson(String response) throws AirDeskException {
        Log.e("json", "generateReturnFromJson: " + response);
        Object result = null;
        try {
            JSONObject obj = new JSONObject(response);
            JSONObject jsonReturn = obj.getJSONObject("return");
            String returnClassName = jsonReturn.getString("class");
            result = createValueFromClassName(returnClassName, jsonReturn);
        } catch (JSONException e) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONObject jsonException = obj.getJSONObject("exception");
                String exceptionClassName = jsonException.getString("class");
                String exceptionMessage   = jsonException.getString("message");
                createAndThrowException(exceptionClassName, exceptionMessage);
            } catch (JSONException e2) {
                throw new JsonResultGenerationException(e2);
            }
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
            case "pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState":
                return createFileState(jsonReturn.getString("value"));
            case "pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType":
                return createWorkspaceType(jsonReturn.getString("value"));
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

    private static Object createWorkspaceType(String workspaceTypeStr) {
        switch (workspaceTypeStr) {
            case "OWNER"   : return WorkspaceType.OWNER;
            case "FOREIGN" : return WorkspaceType.FOREIGN;
            default: throw new JsonResultGenerationException("can't create WorkspaceType: " + workspaceTypeStr);
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
        Object invocationResult = null;
        try {
            JSONObject obj = new JSONObject(jsonCall);
            String methodName = obj.getString("methodName");
            JSONArray jsonArgsArray = obj.getJSONArray("args");

            Class<?>[] argTypes = new Class<?>[jsonArgsArray.length()];
            ArrayList<Object> parsedArgs = new ArrayList<Object>();
            for(int i = 0; i < jsonArgsArray.length(); i++) {
                JSONObject argObj = jsonArgsArray.getJSONObject(i);
                String className = argObj.getString("class");

                Class<?> argClazz = toPrimitive(Class.forName(unJsonClassName(className)));
                argTypes[i] = argClazz;

                Object parsedValue = createValueFromClassName(className, argObj);
                parsedArgs.add(parsedValue);
            }
            Object[] argArray = parsedArgs.toArray();
            Class<?> clazz = instance.getClass();
            Method method = clazz.getMethod(methodName, argTypes);
            invocationResult = method.invoke(instance, argArray);
        } catch (JSONException e) {
            throw new JsonServerCallException(e);
        } catch (ClassNotFoundException e) {
            throw new JsonInvocationException(e);
        } catch (NoSuchMethodException e) {
            throw new JsonInvocationException(e);
        } catch (InvocationTargetException e) {
            throw new JsonInvocationException(e);
        } catch (IllegalAccessException e) {
            throw new JsonInvocationException(e);
        }
        return invocationResult;
    }

    private static String unJsonClassName(String className) {
        if(className.equals("array")) {
            return List.class.getName();
        } else {
            return className;
        }
    }

    private static Class<?> toPrimitive(Class<?> clazz) {
        if (clazz == Integer.class) {
            return int.class;
        }
        if (clazz == Long.class) {
            return long.class;
        }
        if (clazz == Double.class) {
            return double.class;
        }
        if (clazz == Boolean.class) {
            return boolean.class;
        }
        return clazz;
    }

    public static String generateJsonFromResult(final Object result) {
        try {
            JSONObject jsonedObj = new JSONObject() {{
                put("return", new JSONObject() {{
                    put("class", jsonedClassName(result.getClass()));
                    put("value", convertToArrayIfNeeded(result));
                }});
            }};
            String res = jsonedObj.toString();
            Log.e("json", "generateJsonFromResult: " + res);
            return res;
        } catch(JSONException e) {
            throw new JsonResultStringifyingException(e);
        }
    }

    public static String generateJsonFromException(final AirDeskException e) {
        try {
            JSONObject jsonedObj = new JSONObject() {{
                put("exception", new JSONObject() {{
                    put("class", e.getClass().getName());
                    put("message", e.getMessage());
                }});
            }};
            String res = jsonedObj.toString();
            Log.e("json", "generateJsonFromException: " + res);
            return res;
        } catch(JSONException e2) {
            throw new JsonExceptionStringifyingException(e2);
        }
    }
}

