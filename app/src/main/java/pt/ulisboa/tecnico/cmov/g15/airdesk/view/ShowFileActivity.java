package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;

public class ShowFileActivity extends ActionBarActivity {

    public final static String EXTRA_WORKSPACE_OWNER
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.EXTRA_WORKSPACE_OWNER";

    public final static String EXTRA_WORKSPACE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.EXTRA_WORKSPACE_NAME";

    public final static String EXTRA_FILE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.EXTRA_FILE_NAME";

    private AirDesk mAirDesk;
    private String mWorkspaceName;
    private String mWorkspaceOwner;
    private String mFileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file);

        mAirDesk = (AirDesk) getApplication();
        Intent intent = getIntent();

        mWorkspaceOwner = intent.getStringExtra(EXTRA_WORKSPACE_OWNER);
        mWorkspaceName = intent.getStringExtra(EXTRA_WORKSPACE_NAME);
        mFileName = intent.getStringExtra(EXTRA_FILE_NAME);


        TextView fileNameView = (TextView) findViewById(R.id.filename_box);
        fileNameView.setText(mWorkspaceName + ": " + mFileName);

        TextView fileContentView = (TextView) findViewById(R.id.file_content_box);
        fileContentView.setText(mAirDesk.viewFileContent(mWorkspaceOwner, mWorkspaceName, mFileName));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickEditFile(View v) {
        Intent intent = new Intent(this, EditFileActivity.class);
        intent.putExtra(EditFileActivity.EXTRA_WORKSPACE_NAME, mWorkspaceName);
        intent.putExtra(EditFileActivity.EXTRA_WORKSPACE_OWNER, mWorkspaceOwner);
        intent.putExtra(EditFileActivity.EXTRA_FILE_NAME, mFileName);
        startActivity(intent);
        finish();
    }
}
