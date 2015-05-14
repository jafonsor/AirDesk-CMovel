package pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;

/**
 * Created by ist169408 on 14-05-2015.
 */
public class LocalCommunicator implements RemoteCommunicatorI {
    List<String> msgs = new LinkedList<String>();

    @Override
    public synchronized void send(String msg) throws AirDeskException {
        Log.e("json", "send: " + msg);
        msgs.add(msg);
    }

    @Override
    public synchronized String receive() throws AirDeskException {
        while(msgs.size() <= 0) {
            ; // do nothing
        }
        String str = msgs.get(0);
        msgs.remove(0);
        Log.e("json", "receive: " + str);
        return str;
    }
}
