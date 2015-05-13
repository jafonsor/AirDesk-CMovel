package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.SocketReceiveMessageException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.SocketSendMessageException;

/**
 * Created by ist169580 on 13-05-2015.
 */
public class SocketCommunicator implements RemoteCommunicatorI {

    SimWifiP2pSocket socket;

    public static String MSG_TERMINATOR = "####??$$";

    public SocketCommunicator(SimWifiP2pSocket socket){
        this.socket = socket;
    }

    @Override
    public void send(String msg) {
        try {
            StringBuilder sb = new StringBuilder(msg);
            sb.append("\n");
            sb.append(MSG_TERMINATOR);
            socket.getOutputStream().write(sb.toString().getBytes());
        } catch (IOException e) {
            throw new SocketSendMessageException(e.toString());
        }
    }

    @Override
    public String receive() {
        BufferedReader sockIn = null;
        String st;
        StringBuilder sb = new StringBuilder();
        try {
            sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((st = sockIn.readLine()) != null) {
                if(st.equals(MSG_TERMINATOR)) break;
                sb.append(st);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new SocketReceiveMessageException(e.toString());
        }


    }
}
