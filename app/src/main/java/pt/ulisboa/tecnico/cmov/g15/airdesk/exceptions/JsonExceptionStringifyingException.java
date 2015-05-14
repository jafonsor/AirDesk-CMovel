package pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions;

/**
 * Created by ist169408 on 14-05-2015.
 */
public class JsonExceptionStringifyingException extends AirDeskException {
    public JsonExceptionStringifyingException(Throwable e) { super(e); }
    public JsonExceptionStringifyingException(String msg) { super(msg); }
}
