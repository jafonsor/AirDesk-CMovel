package pt.ulisboa.tecnico.cmov.g15.airdesk.network.wifi;

import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteCommunicatorI;

/**
 * Created by ist169408 on 14-05-2015.
 */
public class LocalWifiProvider implements WifiProviderI {
    RemoteCommunicatorI communicator;

    public LocalWifiProvider(RemoteCommunicatorI communicator) {
        this.communicator = communicator;
    }

    @Override
    public RemoteCommunicatorI acceptConnection() {
        if(communicator != null) {
            RemoteCommunicatorI result = communicator;
            communicator = null;
            return result;
        } else {
            while(true) {
                int a = 1;
            }
        }
    }
}
