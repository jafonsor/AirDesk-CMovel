package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceServer;

/**
 * Created by joao on 10-05-2015.
 */
public class RemoteServerSide {

    //TODO the method implementations are only drafts. should be reviewed and actualized once wifi service is implemented

    // to be called to initialize the server. no instance of NetworkServiceSErver should be called outside this class
    public static void initRemoteServer(NetworkServiceServer server) {
        /* create thread to wait for connections
            thread ->
              try {
                while(true) {
                    socket = Wifi.acceptSocket();
                    RemoteServerSide remoteServer = new RemoteServerSide(socket, server);
                    remoteServer.listenRemoteCalls();
                }
              } catch(SomeException e) {
                // do somthing
              }
         */
    }


    // Socket socket;
    NetworkServiceServer server;

    public RemoteServerSide(/*Socket socket,*/ NetworkServiceServer server) {
        // this.socket = socket;
        this.server = server;
    }


    public void listenRemoteCalls() {
        /* create thread to listen the socket and return
            thread  ->

              try {
                while(true) {
                    msg = socket.read
                    try {
                        result = RemoteJSONLib.makeInvocationFromJson(server, msg);
                        socket.write RemoteJSONLib.generateJsonFromResult(result);
                    } catch(AirDeskException e) {
                        socket.write RemoteJSONLib.generateJsonFromException(e);
                    }
                }
              } catch( SomeSocketException e) {
                close connection;
              }

         */

    }
}
