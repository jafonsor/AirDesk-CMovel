package pt.ulisboa.tecnico.cmov.g15.airdesk.network.wifi;

import java.io.IOException;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.ServerSocketException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteCommunicatorI;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.SocketCommunicator;

public class WifiProviderServer implements WifiProviderI {

    SimWifiP2pSocketServer mSrvSocket = null;

    public WifiProviderServer() {
        try {
            mSrvSocket = new SimWifiP2pSocketServer(Integer.parseInt(String.valueOf(PORT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RemoteCommunicatorI acceptConnection() {
        try {
            SimWifiP2pSocket sock = mSrvSocket.accept();
            return new SocketCommunicator(sock);
        } catch (IOException ex) {
            throw new ServerSocketException(ex.toString());
        }

    }
}
