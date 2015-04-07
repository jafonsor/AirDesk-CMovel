package pt.ulisboa.tecnico.cmov.g15.airdesk;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.security.acl.Owner;

import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.OwnerFragment;


public class WorkspaceSettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_settings);

        Intent intent = getIntent();
        String workspaceName = intent.getStringExtra(OwnerFragment.EXTRA_WORKSPACE_NAME);

        TextView workspaceNameView = (TextView)findViewById(R.id.workspace_name);
        workspaceNameView.setText(workspaceName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workspace_setings, menu);
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
