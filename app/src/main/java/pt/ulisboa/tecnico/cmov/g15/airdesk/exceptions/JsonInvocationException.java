package pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions;

/**
 * Created by joao on 13-05-2015.
 */
public class JsonInvocationException extends AirDeskException {
    public JsonInvocationException(Throwable e) { super(e); }
    public JsonInvocationException(String msg)  { super(msg); }
}
