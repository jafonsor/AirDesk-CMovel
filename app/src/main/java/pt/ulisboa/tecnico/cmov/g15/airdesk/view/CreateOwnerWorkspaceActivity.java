package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.OwnerFragment;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.SwipeActivity;

public class CreateOwnerWorkspaceActivity extends ActionBarActivity {

    private final static WorkspaceVisibility[] spinnerVisibilityChoices
        = {WorkspaceVisibility.PUBLIC, WorkspaceVisibility.PRIVATE};

    private AirDesk mAirDesk;
    private Spinner mVisibilitySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_owner_workspace);

        mAirDesk = (AirDesk) getApplication();

        mVisibilitySpinner = (Spinner) findViewById(R.id.visibility_spinner);
        ArrayAdapter<WorkspaceVisibility> spinnerAdapter
                = new ArrayAdapter<WorkspaceVisibility>(this, android.R.layout.simple_spinner_item, spinnerVisibilityChoices);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mVisibilitySpinner.setAdapter(spinnerAdapter);

        final Button createWorkspaceButton = (Button) findViewById(R.id.create_workspace_button);
        createWorkspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateWorkspace();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_owner_workspace, menu);
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

    public void onClickCreateWorkspace() {
        EditText nameText  = (EditText) findViewById(R.id.workspace_name_input);
        EditText quotaText = (EditText) findViewById(R.id.workspace_quota_input);
        EditText tagsText  = (EditText) findViewById(R.id.workspace_tags_input);


        String workspaceName = nameText.getText().toString();
        int workspaceQuota = Integer.parseInt(quotaText.getText().toString());

        String tagsString = tagsText.getText().toString();
        String[] tagsArr = tagsString.split(";");
        // filter empty strings
        ArrayList<String> workspaceTags = new ArrayList<>();
        for(String tag : tagsArr) {
            if (!tag.isEmpty())
                workspaceTags.add(tag);
        }

        WorkspaceVisibility workspaceVisibility = (WorkspaceVisibility)mVisibilitySpinner.getSelectedItem();

        mAirDesk.createOwnerWorkspace(workspaceName, workspaceQuota, workspaceVisibility, workspaceTags);

        Toast.makeText(this, "workspace '" + workspaceName + "' created", Toast.LENGTH_SHORT);

        startActivity(new Intent(this, SwipeActivity.class));
    }
}
