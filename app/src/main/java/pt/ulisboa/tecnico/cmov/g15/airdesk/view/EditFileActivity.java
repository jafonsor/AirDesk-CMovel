package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.SwipeActivity;


public class EditFileActivity extends ActionBarActivity {

    public final static String EXTRA_WORKSPACE_OWNER
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity.EXTRA_WORKSPACE_OWNER";

    public final static String EXTRA_WORKSPACE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity.EXTRA_WORKSPACE_NAME";

    public final static String EXTRA_FILE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity.EXTRA_FILE_NAME";

    public final static String EXTRA_TYPE_OF_WORKSPACE
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity.EXTRA_TYPE_OF_WORKSPACE";

    private AirDesk mAirDesk;
    private String mWorkspaceName;
    private String mWorkspaceOwner;
    private String mFileName;
    private TextView fileContentView;
    private WorkspaceType mWorkspaceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);

        mAirDesk = (AirDesk) getApplication();

        Intent intent = getIntent();

        mWorkspaceOwner = intent.getStringExtra(EXTRA_WORKSPACE_OWNER);
        mWorkspaceName = intent.getStringExtra(EXTRA_WORKSPACE_NAME);
        mFileName = intent.getStringExtra(EXTRA_FILE_NAME);
        mWorkspaceType = (WorkspaceType) intent.getSerializableExtra(EXTRA_TYPE_OF_WORKSPACE);

        if(mWorkspaceOwner==null ||  mWorkspaceName==null  ||
           mFileName == null     || mWorkspaceType == null)
            if(savedInstanceState != null){
                mWorkspaceOwner = savedInstanceState.getString(EXTRA_WORKSPACE_OWNER);
                mWorkspaceName = savedInstanceState.getString(EXTRA_WORKSPACE_NAME);
                mFileName = savedInstanceState.getString(EXTRA_FILE_NAME);
                mWorkspaceType = (WorkspaceType) savedInstanceState.getSerializable(EXTRA_TYPE_OF_WORKSPACE);
            }else{
                Toast.makeText(getApplicationContext(), "Invalid workspace attributes", Toast.LENGTH_SHORT).show();
                this.setResult(0); //Destroy o swipe anterior
                startActivity(new Intent(this, SwipeActivity.class));
                finish();
            }

        TextView fileNameView = (TextView) findViewById(R.id.filename_box);
        fileNameView.setText(mWorkspaceType + " > " + mWorkspaceName + ": " + mFileName);

        fileContentView = (TextView) findViewById(R.id.file_content_box);
        fileContentView.setText(mAirDesk.viewFileContent(mWorkspaceOwner, mWorkspaceName, mFileName, mWorkspaceType));
    }


    public void onClickSave (View v) {
        try {
            mAirDesk.saveFileContent(mWorkspaceOwner, mWorkspaceName, mFileName, fileContentView.getText().toString(), mWorkspaceType);
            Intent intent = new Intent(this, FileListActivity.class);
            intent.putExtra(FileListActivity.EXTRA_OWNER_EMAIL, mWorkspaceOwner);
            intent.putExtra(FileListActivity.EXTRA_WORKSPACE_NAME, mWorkspaceName);
            intent.putExtra(FileListActivity.EXTRA_TYPE_OF_WORKSPACE, mWorkspaceType);
            Toast.makeText(this, "File successfully saved.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        } catch (AirDeskException e){
            Log.e("exception", e.toString());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_WORKSPACE_OWNER, mWorkspaceOwner);
        outState.putString(EXTRA_WORKSPACE_NAME, mWorkspaceName);
        outState.putString(EXTRA_FILE_NAME, mFileName);
        outState.putSerializable(EXTRA_TYPE_OF_WORKSPACE, mWorkspaceType);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        mAirDesk.notifyIntention(mWorkspaceOwner, mWorkspaceName, mFileName, FileState.IDLE, mWorkspaceType, true);
        super.onStop();
    }
}

