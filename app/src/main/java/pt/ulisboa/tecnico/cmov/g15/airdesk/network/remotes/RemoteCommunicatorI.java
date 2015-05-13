package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;

/**
 * Created by joao on 12-05-2015.
 */
public interface RemoteCommunicatorI {
    public void send(String msg) throws AirDeskException;
    public String receive() throws AirDeskException;
}
