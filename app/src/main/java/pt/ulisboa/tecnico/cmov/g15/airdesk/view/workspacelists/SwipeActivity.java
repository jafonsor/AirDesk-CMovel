package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.NetworkServiceServer;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteServerSide;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.wifi.WifiProviderI;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.wifi.WifiProviderServer;
import pt.ulisboa.tecnico.cmov.g15.airdesk.storage.FileSystemManager;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.WifiP2pManagerService;


public class SwipeActivity extends FragmentActivity {

    public final static String EXTRA_FILE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.WorkspaceListActivity.FILE_NAME";

    public final static String WIFI_P2P_MANAGER_SERVICE
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.SwipeActivity.WIFI_P2P_MANAGER_SERVICE";

    private boolean startManagerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspaces_list);

        startManagerService = true;

        if(savedInstanceState!=null){
            startManagerService = savedInstanceState.getBoolean(WIFI_P2P_MANAGER_SERVICE);
        }


        if(startManagerService) {
           startWifiP2pManagerService();
           WifiProviderI wifiProvider = new WifiProviderServer();
           RemoteServerSide.initRemoteServer(wifiProvider, new NetworkServiceServer((AirDesk) getApplication()));
        }

        MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());

        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
    }

    public void createWorkspace(View view) {
        // TO DO
    }

    public void onClickEditFileButton(String fileName, View v) {
        Intent intent = new Intent(this, EditFileActivity.class);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        startActivity(intent);
    }

    public void onClickDeleteFileButton(String fileName, final View v) {
        Activity self = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete the file '" + fileName + "'?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "TO DO: delete file", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog deleteFileDialog = alertDialogBuilder.create();
        deleteFileDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //matches the result code passed from ChildActivity
        if(resultCode == 0)
        {
            //kill self
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        AirDesk airDesk = (AirDesk) getApplication();
        airDesk.backup();

        savedInstanceState.putBoolean(WIFI_P2P_MANAGER_SERVICE, startManagerService);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    // Start the service
    public void startWifiP2pManagerService() {
        startService(new Intent(this, WifiP2pManagerService.class));
    }

    // Stop the service
    public void stopWifiP2pManagerService() {
        stopService(new Intent(this, WifiP2pManagerService.class));
    }
}
