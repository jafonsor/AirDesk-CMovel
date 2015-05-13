package pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;

public abstract class AirDeskException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String msg;

    public AirDeskException(Throwable e) {
        super(e);
        this.msg = e.getMessage();
    }
    public AirDeskException(String msg){
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ":" + msg + "\n";
    }

}// End AirDeskException class
