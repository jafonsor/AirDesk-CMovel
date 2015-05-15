package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
    static List<RemoteServerSide> servers = new ArrayList<>();

    // to be called to initialize the server. no instance of NetworkServiceSErver should be called outside this class
    public static void initRemoteServer(final WifiProviderI wifiProvider, final NetworkServiceServerI server) {
        if(connectionWaiterThread != null)
            throw new ServerAlreadyRunningException("");

        connectionWaiterThread = new Thread(new Runnable() {
            @Override
            public void run() {
            try {
                while (!connectionWaiterThread.isInterrupted()) {
                    RemoteCommunicatorI socket = wifiProvider.acceptConnection();
                    Log.e("json", "server.new client connection");
                    RemoteServerSide remoteServer = new RemoteServerSide(socket, server);
                    servers.add(remoteServer);
                    remoteServer.listenRemoteCalls();
                }
            } catch(AirDeskCommunicationException e) {
                Log.e("RemoteServerSide", "communication exception on accept: " + e.getMessage());
            }
            }
        });
        connectionWaiterThread.start();
    }

    public static void closeServer() {
        connectionWaiterThread.interrupt();
        connectionWaiterThread = null;
        for(RemoteServerSide s: servers) {
            s.closeServerInstance();
        }
        servers.clear();
    }

    private RemoteCommunicatorI socket;
    private NetworkServiceServerI server;
    private Thread remoteCallListener;

    public RemoteServerSide(RemoteCommunicatorI socket, NetworkServiceServerI server) {
        this.socket = socket;
        this.server = server;
    }

    public void closeServerInstance() {
        remoteCallListener.interrupt();
    }

    public void listenRemoteCalls() {
        remoteCallListener =  new Thread(new Runnable() {
            @Override
            public void run() {
            Object result;
            String jsonedCall;
            String jsonedResult;
            try {
                while(!remoteCallListener.isInterrupted()) {
                    Log.e("json", "server.waiting for call");
                    jsonedCall = socket.receive();
                    Log.e("json", "server.received call: " + jsonedCall);
                    try {
                        result = RemoteJSONLib.makeInvocationFromJson(server, jsonedCall);
                        Log.e("json", "server.toString of invocation result: " + result);
                        jsonedResult = RemoteJSONLib.generateJsonFromResult(result);
                    } catch(AirDeskException e) {
                        jsonedResult = RemoteJSONLib.generateJsonFromException(e);
                    }
                    Log.e("json", "server.will send result: " + jsonedResult);
                    socket.send(jsonedResult);
                    Log.e("json", "server.sent result: " + jsonedResult);
                }
            } catch (AirDeskCommunicationException e) {
                Log.e("RemoteServerSide", "communication exception on call: " + e.getMessage());
            }
            }
        });
        remoteCallListener.start();
    }
}
