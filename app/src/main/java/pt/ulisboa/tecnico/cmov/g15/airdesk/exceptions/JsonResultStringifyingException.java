package pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions;

/**
 * Created by ist169408 on 14-05-2015.
 */
public class JsonResultStringifyingException extends AirDeskException {
    public JsonResultStringifyingException(Throwable e) { super(e); }
    public JsonResultStringifyingException(String msg) { super(msg); }
}
