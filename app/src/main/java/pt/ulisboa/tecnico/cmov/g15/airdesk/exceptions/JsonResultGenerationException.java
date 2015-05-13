package pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions;

/**
 * Created by ist169408 on 13-05-2015.
 */
public class JsonResultGenerationException extends AirDeskException {
    public JsonResultGenerationException(Throwable e) { super(e);}
    public JsonResultGenerationException(String msg) { super(msg); }
}
