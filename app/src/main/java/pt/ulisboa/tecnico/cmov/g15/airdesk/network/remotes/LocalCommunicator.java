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
    LocalCommunicator otherEnd = null;

    public void setOtherEnd(LocalCommunicator otherEnd) {
        this.otherEnd = otherEnd;
    }

    public void stashMessage(String msg) {
        msgs.add(msg);
    }

    @Override
    public void send(String msg) throws AirDeskException {
        otherEnd.stashMessage(msg);
    }

    @Override
    public String receive() throws AirDeskException {
        while(msgs.size() <= 0) {
            ; // do nothing
        }
        String str = msgs.get(0);
        msgs.remove(0);
        return str;
    }
}
