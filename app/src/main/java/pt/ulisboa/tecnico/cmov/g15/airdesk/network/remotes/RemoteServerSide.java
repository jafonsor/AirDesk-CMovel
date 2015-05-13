package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import android.util.Log;

import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskCommunicationException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.ServerAlreadyRunningException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceServerI;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.wifi.WifiProviderI;

/**
 * Created by joao on 10-05-2015.
 */
public class RemoteServerSide {
    static Thread connectionWaiterThread = null;

    // to be called to initialize the server. no instance of NetworkServiceSErver should be called outside this class
    public static void initRemoteServer(final WifiProviderI wifiProvider, final NetworkServiceServerI server) {
        if(connectionWaiterThread != null)
            throw new ServerAlreadyRunningException("");

        connectionWaiterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        RemoteCommunicatorI socket = wifiProvider.acceptConnection();
                        RemoteServerSide remoteServer = new RemoteServerSide(socket, server);
                        remoteServer.listenRemoteCalls();
                    }
                } catch(AirDeskCommunicationException e) {
                    Log.e("RemoteServerSide", "communication exception on accept: " + e.getMessage());
                }
            }
        });
        connectionWaiterThread.start();
    }

    private RemoteCommunicatorI socket;
    private NetworkServiceServerI server;
    private Thread remoteCallListener;

    public RemoteServerSide(RemoteCommunicatorI socket, NetworkServiceServerI server) {
        this.socket = socket;
        this.server = server;
    }

    public void listenRemoteCalls() {
        remoteCallListener =  new Thread(new Runnable() {
            @Override
            public void run() {
                Object result;
                String jsonedCall;
                String jsonedResult;
                try {
                    while(true) {
                        jsonedCall = socket.receive();
                        try {
                            result = RemoteJSONLib.makeInvocationFromJson(server, jsonedCall);
                            jsonedResult = RemoteJSONLib.generateJsonFromResult(result);
                        } catch(AirDeskException e) {
                            jsonedResult = RemoteJSONLib.generateJsonFromException(e);
                        }
                        socket.send(jsonedResult);
                    }
                } catch (AirDeskCommunicationException e) {
                    Log.e("RemoteServerSide", "communication exception on call: " + e.getMessage());
                }
            }
        });
        remoteCallListener.start();
    }
}
