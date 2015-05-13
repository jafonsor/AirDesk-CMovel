package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

/**
 * Created by joao on 12-05-2015.
 */
public interface RemoteCommunicatorI {
    public void send(String msg);
    public String receive();
}
