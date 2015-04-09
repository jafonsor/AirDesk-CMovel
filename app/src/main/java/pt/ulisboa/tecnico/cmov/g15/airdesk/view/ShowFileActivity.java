package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;

public class ShowFileActivity extends ActionBarActivity {

    public final static String EXTRA_FILE_ID
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.FILE_ID";

    public final static String EXTRA_WORKSPACE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.WORKSPACE_NAME";

    public final static String EXTRA_IS_OWNER
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.EXTRA_IS_OWNER";

    private AirDesk mAirDesk;
    private Integer mFileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file);

        mAirDesk = (AirDesk) getApplication();

        Intent intent = getIntent();
        mFileId = intent.getIntExtra(EXTRA_FILE_ID, -1);
        String workspaceName = intent.getStringExtra(EXTRA_WORKSPACE_NAME);
        boolean isOwner = intent.getBooleanExtra(EXTRA_IS_OWNER, false);

        //AirDeskFile file = mAirDesk.getFileById(mFileId);

        TextView fileNameView = (TextView) findViewById(R.id.filename_box);
        //fileNameView.setText( "isOwner=" + isOwner + "; " + workspaceName + ": " + file.getName());

        TextView fileContentView = (TextView) findViewById(R.id.file_content_box);
        //fileContentView.setText(SS.readAirDeskFile(file));
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

    public void onClickEditFile(View v){

    }
}
