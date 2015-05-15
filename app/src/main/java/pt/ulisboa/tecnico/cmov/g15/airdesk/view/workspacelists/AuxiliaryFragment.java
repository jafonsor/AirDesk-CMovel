package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;

/**
 * Created by MSC on 06/04/2015.
 */

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.GroupInfoListener;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceClient;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceServer;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteCommunicatorI;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteServerSide;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.SocketCommunicator;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.wifi.WifiProviderI;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.wifi.WifiProviderServer;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.SimWifiP2pBroadcastReceiver;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.WifiP2pManagerService;

public class AuxiliaryFragment extends Fragment implements
        PeerListListener, GroupInfoListener {

    public static final String TAG = "airDesk";

    private AirDesk mAirdesk;
    private Button mPopulateButton;
    private Button mDisconnectNetworkButton;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private boolean mBound = false;
    private SimWifiP2pSocketServer mSrvSocket = null;
    private ReceiveCommTask mComm = null;
    private SimWifiP2pSocket mCliSocket = null;
    private TextView mTextInput;
    private TextView mTextOutput;
    SimWifiP2pBroadcastReceiver receiver;
    View rootView;

    public SimWifiP2pManager getManager() {
        return mManager;
    }

    public SimWifiP2pManager.Channel getChannel() {
        return mChannel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.auxiliary_layout, container, false);

        guiSetButtonListeners(rootView);
        guiUpdateInitState(rootView);

        mPopulateButton = (Button) rootView.findViewById(R.id.populateAuxiliaryBtn);
        mDisconnectNetworkButton = (Button) rootView.findViewById(R.id.disconnectAuxiliaryBtn);

        mAirdesk = (AirDesk) getActivity().getApplication();
        mPopulateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPopulateAirDesk(v);
            }
        });
        mDisconnectNetworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDisconnectNetwork(v);
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        //getActivity().unbindService(mConnection);
        //mBound = false;
        guiUpdateInitState(rootView);
        super.onStop();
    }

    public void  onClickPopulateAirDesk(View v) {
        mAirdesk.populate();
        Toast.makeText(getActivity().getApplicationContext(),
                "Workspace created", Toast.LENGTH_SHORT).show();
    }

    public void  onClickDisconnectNetwork(View v) {
        //TODO fazer disconnect
        Toast.makeText(getActivity().getApplicationContext(),
                "Not Implemented", Toast.LENGTH_SHORT).show();
    }

    /*
	 * Listeners associated to buttons
	 */

    private View.OnClickListener listenerWifiOnButton = new View.OnClickListener() {
        public void onClick(View v){

            Intent intent = new Intent(v.getContext(), SimWifiP2pService.class);
            getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            mBound = true;

            AirDesk airDesk = (AirDesk)getActivity().getApplication();
            airDesk.startService(new Intent(airDesk, WifiP2pManagerService.class));
            WifiProviderI wifiProvider = new WifiProviderServer();
            RemoteServerSide.initRemoteServer(wifiProvider, new NetworkServiceServer(airDesk));

            Log.d("future", "WIFI ON");

            // spawn the chat server background task
            //new IncommingCommTask().executeOnExecutor(
                    //AsyncTask.THREAD_POOL_EXECUTOR);

            guiUpdateDisconnectedState(rootView);
        }
    };

    private View.OnClickListener listenerWifiOffButton = new View.OnClickListener() {
        public void onClick(View v){
            if (mBound) {
                getActivity().unbindService(mConnection);
                mBound = false;
                guiUpdateInitState(rootView);
            }
        }
    };

    private View.OnClickListener listenerInRangeButton = new View.OnClickListener() {
        public void onClick(View v){
            if (mBound) {
                mManager.requestPeers(mChannel, (PeerListListener) AuxiliaryFragment.this);
            } else {
                Toast.makeText(v.getContext(), "Service not bound",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener listenerInGroupButton = new View.OnClickListener() {
        public void onClick(View v){
            if (mBound) {
                mManager.requestGroupInfo(mChannel, (GroupInfoListener) AuxiliaryFragment.this);
            } else {
                Toast.makeText(v.getContext(), "Service not bound",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener listenerConnectButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rootView.findViewById(R.id.idConnectButton).setEnabled(false);
            new OutgoingCommTask().executeOnExecutor(
               AsyncTask.THREAD_POOL_EXECUTOR,
               mTextInput.getText().toString());
        }
    };

    private View.OnClickListener listenerDisconnectButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rootView.findViewById(R.id.idDisconnectButton).setEnabled(false);
            if (mCliSocket != null) {
                try {
                    mCliSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCliSocket = null;
            guiUpdateDisconnectedState(rootView);
        }
    };

    private View.OnClickListener listenerSendButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rootView.findViewById(R.id.idSendButton).setEnabled(false);
           // try {
             //   mCliSocket.getOutputStream().write( (mTextInput.getText().toString()+"\n").getBytes());
            //} catch (IOException e) {
             //   e.printStackTrace();
            //}
            mTextInput.setText("");
            rootView.findViewById(R.id.idSendButton).setEnabled(true);
            rootView.findViewById(R.id.idDisconnectButton).setEnabled(true);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mManager = new SimWifiP2pManager(mService);
            mChannel = mManager.initialize(getActivity().getApplication(), getActivity().getMainLooper(), null);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };


	/*
	 * Classes implementing chat message exchange
	 */

    public class IncommingCommTask extends AsyncTask<Void, SimWifiP2pSocket, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");

            try {
                mSrvSocket = new SimWifiP2pSocketServer(
                        Integer.parseInt(getString(R.string.port)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SimWifiP2pSocket sock = mSrvSocket.accept();
                    if (mCliSocket != null && mCliSocket.isClosed()) {
                        mCliSocket = null;
                    }
                    if (mCliSocket != null) {
                        Log.d(TAG, "Closing accepted socket because mCliSocket still active.");
                        sock.close();
                    } else {
                        publishProgress(sock);
                    }
                } catch (IOException e) {
                    Log.d("Error accepting socket:", e.getMessage());
                    break;
                    //e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(SimWifiP2pSocket... values) {
            mCliSocket = values[0];
            mComm = new ReceiveCommTask();
            mComm.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCliSocket);
        }
    }

    public class OutgoingCommTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            mTextOutput.setText("Connecting...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.d("future", "I will create a new client socket");
                mCliSocket = new SimWifiP2pSocket(params[0],
                        Integer.parseInt(getString(R.string.port)));

                RemoteCommunicatorI rem = new SocketCommunicator(mCliSocket);
                NetworkServiceClient.addNewElementOffNetwork(rem);
                Log.d("future","New Client Socket created");
            } catch (UnknownHostException e) {
                Log.d("future", "Unknown Host:" + e.getMessage());
                return "Unknown Host:" + e.getMessage();
            } catch (IOException e) {
                Log.d("future","IO error:" + e.getMessage());
                return "IO error:" + e.getMessage();
            }
            return null;
        }

        /*@Override
        protected void onPostExecute(String result) {
            if (result != null) {
                mTextOutput.setText(result);
                rootView.findViewById(R.id.idConnectButton).setEnabled(true);
            }
            else {
                mComm = new ReceiveCommTask();
                mComm.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mCliSocket);
            }
        }*/
    }

    public class ReceiveCommTask extends AsyncTask<SimWifiP2pSocket, String, Void> {
        SimWifiP2pSocket s;

        @Override
        protected Void doInBackground(SimWifiP2pSocket... params) {
            BufferedReader sockIn;
            String st;

            s = params[0];
            try {
                sockIn = new BufferedReader(new InputStreamReader(s.getInputStream()));

                while ((st = sockIn.readLine()) != null) {
                    publishProgress(st);
                }
            } catch (IOException e) {
                Log.d("Error reading socket:", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mTextOutput.setText("");
            rootView.findViewById(R.id.idSendButton).setEnabled(true);
            rootView.findViewById(R.id.idDisconnectButton).setEnabled(true);
            rootView.findViewById(R.id.idConnectButton).setEnabled(false);
            mTextInput.setHint("");
            mTextInput.setText("");

        }

        @Override
        protected void onProgressUpdate(String... values) {
            mTextOutput.append(values[0]+"\n");
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!s.isClosed()) {
                try {
                    s.close();
                }
                catch (Exception e) {
                    Log.d("Error closing socket:", e.getMessage());
                }
            }
            s = null;
            if (mBound) {
                guiUpdateDisconnectedState(rootView);
            } else {
                guiUpdateInitState(rootView);
            }
        }
    }

	/*
	 * Listeners associated to WDSim
	 */

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        StringBuilder peersStr = new StringBuilder();

        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ")\n";
            peersStr.append(devstr);
        }

        // display list of devices in range
        new AlertDialog.Builder(getActivity())
                .setTitle("Devices in WiFi Range")
                .setMessage(peersStr.toString())
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices,
                                     SimWifiP2pInfo groupInfo) {

        // compile list of network members
        StringBuilder peersStr = new StringBuilder();
        for (String deviceName : groupInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = devices.getByName(deviceName);
            String devstr = "" + deviceName + " (" +
                    ((device == null)?"??":device.getVirtIp()) + ")\n";
            peersStr.append(devstr);
        }

        // display list of network members
        new AlertDialog.Builder(getActivity())
                .setTitle("Devices in WiFi Network")
                .setMessage(peersStr.toString())
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /*
	 * Helper methods for updating the interface
	 */

    private void guiSetButtonListeners(View rootView) {

        rootView.findViewById(R.id.idConnectButton).setOnClickListener(listenerConnectButton);
        rootView.findViewById(R.id.idDisconnectButton).setOnClickListener(listenerDisconnectButton);
        rootView.findViewById(R.id.idSendButton).setOnClickListener(listenerSendButton);
        rootView.findViewById(R.id.idWifiOnButton).setOnClickListener(listenerWifiOnButton);
        rootView.findViewById(R.id.idWifiOffButton).setOnClickListener(listenerWifiOffButton);
        rootView.findViewById(R.id.idInRangeButton).setOnClickListener(listenerInRangeButton);
        rootView.findViewById(R.id.idInGroupButton).setOnClickListener(listenerInGroupButton);
    }

    private void guiUpdateInitState(View rootView) {

        mTextInput = (TextView) rootView.findViewById(R.id.editText1);
        mTextInput.setHint("type remote virtual IP (192.168.0.0/16)");
        mTextInput.setEnabled(false);

        mTextOutput = (TextView) rootView.findViewById(R.id.editText2);
        mTextOutput.setEnabled(false);
        mTextOutput.setText("");

        rootView.findViewById(R.id.idConnectButton).setEnabled(false);
        rootView.findViewById(R.id.idDisconnectButton).setEnabled(false);
        rootView.findViewById(R.id.idSendButton).setEnabled(false);
        rootView.findViewById(R.id.idWifiOnButton).setEnabled(true);
        rootView.findViewById(R.id.idWifiOffButton).setEnabled(false);
        rootView.findViewById(R.id.idInRangeButton).setEnabled(false);
        rootView.findViewById(R.id.idInGroupButton).setEnabled(false);
    }

    private void guiUpdateDisconnectedState(View rootView) {

        mTextInput.setEnabled(true);
        mTextInput.setHint("type remote virtual IP (192.168.0.0/16)");
        mTextOutput.setEnabled(true);
        mTextOutput.setText("");

        rootView.findViewById(R.id.idSendButton).setEnabled(false);
        rootView.findViewById(R.id.idConnectButton).setEnabled(true);
        rootView.findViewById(R.id.idDisconnectButton).setEnabled(false);
        rootView.findViewById(R.id.idWifiOnButton).setEnabled(false);
        rootView.findViewById(R.id.idWifiOffButton).setEnabled(true);
        rootView.findViewById(R.id.idInRangeButton).setEnabled(true);
        rootView.findViewById(R.id.idInGroupButton).setEnabled(true);
    }


}
