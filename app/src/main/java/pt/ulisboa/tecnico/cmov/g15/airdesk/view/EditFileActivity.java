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


public class EditFileActivity extends ActionBarActivity {

    public final static String EXTRA_FILE_ID
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity.FILE_ID";

    private AirDesk mAirDesk;
    private Integer mFileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);

        mAirDesk = (AirDesk) getApplication();

        Intent intent = getIntent();
        mFileId = intent.getIntExtra(EXTRA_FILE_ID, -1);

        //AirDeskFile file = mAirDesk.getFileById(mFileId);

        TextView fileNameView = (TextView) findViewById(R.id.filename_box);
        //fileNameView.setText(file.getName());

        TextView fileContentView = (TextView) findViewById(R.id.file_content_box);
        //fileContentView.setText(SS.readAirDeskFile(file));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_file, menu);
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

    public void onClickSave (View v) {

    }
}

