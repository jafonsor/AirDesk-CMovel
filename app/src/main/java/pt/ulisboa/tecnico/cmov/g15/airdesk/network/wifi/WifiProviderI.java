package pt.ulisboa.tecnico.cmov.g15.airdesk.network.wifi;

import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteCommunicatorI;

/**
 * Created by joao on 13-05-2015.
 */
public interface WifiProviderI {

    public final static int PORT = 10001;
    // blocking call that waits for a client to make a connection.
    public RemoteCommunicatorI acceptConnection();
}
