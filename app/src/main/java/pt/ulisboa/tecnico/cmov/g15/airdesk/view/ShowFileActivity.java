package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.g15.airdesk.R;

public class ShowFileActivity extends ActionBarActivity {

    public final static String EXTRA_FILE_NAME
        = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.FILE_NAME";

    public final static String EXTRA_WORKSPACE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.WORKSPACE_NAME";

    public final static String EXTRA_IS_OWNER
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.ShowFileActivity.EXTRA_IS_OWNER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file);

        Intent intent = getIntent();
        String fileName      = intent.getStringExtra(EXTRA_FILE_NAME);
        String workspaceName = intent.getStringExtra(EXTRA_WORKSPACE_NAME);
        boolean isOwner = intent.getBooleanExtra(EXTRA_IS_OWNER, false);

        TextView fileNameView = (TextView) findViewById(R.id.file_name);
        fileNameView.setText( "isOwner=" + isOwner + "; " + workspaceName + ": " + fileName);
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
}
