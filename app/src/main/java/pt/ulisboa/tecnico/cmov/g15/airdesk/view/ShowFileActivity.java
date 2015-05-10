package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;

public class ShowFileActivity extends ActionBarActivity {

    public final static String EXTRA_WORKSPACE_OWNER
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.EXTRA_WORKSPACE_OWNER";

    public final static String EXTRA_WORKSPACE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.EXTRA_WORKSPACE_NAME";

    public final static String EXTRA_FILE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.EXTRA_FILE_NAME";

    public final static String EXTRA_TYPE_OF_WORKSPACE
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.EXTRA_TYPE_OF_WORKSPACE";

    private AirDesk mAirDesk;
    private String mWorkspaceName;
    private String mWorkspaceOwner;
    private String mFileName;
    private WorkspaceType mWorkspaceType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file);

        mAirDesk = (AirDesk) getApplication();
        Intent intent = getIntent();

        mWorkspaceOwner = intent.getStringExtra(EXTRA_WORKSPACE_OWNER);
        mWorkspaceName = intent.getStringExtra(EXTRA_WORKSPACE_NAME);
        mFileName = intent.getStringExtra(EXTRA_FILE_NAME);
        mWorkspaceType = (WorkspaceType) intent.getSerializableExtra(EXTRA_TYPE_OF_WORKSPACE);

        TextView fileNameView = (TextView) findViewById(R.id.filename_box);
        fileNameView.setText(mWorkspaceType + " > " + mWorkspaceName + ": " + mFileName);

        TextView fileContentView = (TextView) findViewById(R.id.file_content_box);
        fileContentView.setText(mAirDesk.viewFileContent(mWorkspaceOwner, mWorkspaceName, mFileName, mWorkspaceType));
    }

    @Override
    protected void onResume() {
        mAirDesk.notifyIntention(mWorkspaceOwner, mWorkspaceName, mFileName, FileState.READ, mWorkspaceType);
        super.onResume();
    }

    public void onClickEditFile(View v) {
        if(mAirDesk.notifyIntention(mWorkspaceOwner, mWorkspaceName, mFileName, FileState.WRITE, mWorkspaceType)){
            Intent intent = new Intent(this, EditFileActivity.class);
            intent.putExtra(EditFileActivity.EXTRA_WORKSPACE_NAME, mWorkspaceName);
            intent.putExtra(EditFileActivity.EXTRA_WORKSPACE_OWNER, mWorkspaceOwner);
            intent.putExtra(EditFileActivity.EXTRA_FILE_NAME, mFileName);
            intent.putExtra(EditFileActivity.EXTRA_TYPE_OF_WORKSPACE, mWorkspaceType);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Not allowed to edit this file", Toast.LENGTH_LONG).show();
        }

    }

}
