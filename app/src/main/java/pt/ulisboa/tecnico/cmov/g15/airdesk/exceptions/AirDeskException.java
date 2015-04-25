package pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions;

public abstract class AirDeskException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String msg;

    public AirDeskException(String msg){
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ":" + msg + "\n";
    }

}// End AirDeskException class
