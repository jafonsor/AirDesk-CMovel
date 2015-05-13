package pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions;

/**
 * Created by joao on 13-05-2015.
 */
public class JsonServerCallException extends AirDeskException {
    public JsonServerCallException(Throwable e) { super(e); }
    public JsonServerCallException(String msg) { super(msg); }
}
