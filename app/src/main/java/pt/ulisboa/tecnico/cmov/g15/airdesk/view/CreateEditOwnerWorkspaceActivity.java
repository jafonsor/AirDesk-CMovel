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

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceVisibility;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.WorkspaceAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.SwipeActivity;

public class CreateEditOwnerWorkspaceActivity extends ActionBarActivity {

    public final static String EXTRA_WORKSPACE_ID
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.CreateOwnerWorkspaceActivity.WORKSPACE_ID";

    private final static WorkspaceVisibility[] spinnerVisibilityChoices
        = {WorkspaceVisibility.PUBLIC, WorkspaceVisibility.PRIVATE};

    private AirDesk mAirDesk;
    private Spinner mVisibilitySpinner;

    // if intent has workspace id then this activity
    private Workspace mWorkspace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_owner_workspace);

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

        // check if this is an edit or a create
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_WORKSPACE_ID)) {
            Integer workspaceId = intent.getIntExtra(EXTRA_WORKSPACE_ID, -1);
            mWorkspace = mAirDesk.getWorkspaceById(workspaceId);

            // inflate view with values to edit
            EditText nameText  = (EditText) findViewById(R.id.workspace_name_input);
            EditText quotaText = (EditText) findViewById(R.id.workspace_quota_input);
            EditText tagsText  = (EditText) findViewById(R.id.workspace_tags_input);

            nameText.setText(mWorkspace.getName());
            quotaText.setText(mWorkspace.getQuota()+"");

            StringBuilder strBuilder = new StringBuilder();
            for(String tag : mWorkspace.getTags()) {
                strBuilder.append(tag);
                strBuilder.append(';');
            }
            tagsText.setText(strBuilder.toString());

            int visibilityPosition = -1;
            if(mWorkspace.getVisibility() == spinnerVisibilityChoices[0]) {
                visibilityPosition = 0;
            } else {
                visibilityPosition = 1;
            }
            mVisibilitySpinner.setSelection(visibilityPosition);

        }
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
        if(workspaceName == null || workspaceName.isEmpty()) {
            Toast.makeText(this, "invalid name", Toast.LENGTH_SHORT).show();
            return;
        }

        int workspaceQuota;
        try {
            workspaceQuota = Integer.parseInt(quotaText.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "invalid quota", Toast.LENGTH_SHORT).show();
            return;
        }

        String tagsString = tagsText.getText().toString();
        if(tagsString == null) {
            Toast.makeText(this, "invalid tags", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] tagsArr = tagsString.split(";");
        // filter empty strings
        ArrayList<String> workspaceTags = new ArrayList<>();
        for(String tag : tagsArr) {
            if (!tag.isEmpty())
                workspaceTags.add(tag);
        }

        WorkspaceVisibility workspaceVisibility = (WorkspaceVisibility)mVisibilitySpinner.getSelectedItem();

        if(mWorkspace == null) {
            try {
                mAirDesk.createOwnerWorkspace(workspaceName, workspaceQuota, workspaceVisibility, workspaceTags);
                Toast.makeText(this, "workspace '" + workspaceName + "' created", Toast.LENGTH_SHORT).show();
            } catch (WorkspaceAlreadyExistsException e) {
                Toast.makeText(this, "There is already a workspace with that name.", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            mAirDesk.editOwnerWorkspace(mWorkspace.getId(), workspaceName, workspaceQuota, workspaceVisibility, workspaceTags);
            Toast.makeText(this, "settings saved", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(this, SwipeActivity.class));
        finish();
    }
}
