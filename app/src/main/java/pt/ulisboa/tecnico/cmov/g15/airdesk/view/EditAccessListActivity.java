package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AccessListItem;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.OwnerFragment;

public class EditAccessListActivity extends ActionBarActivity {

    public final static String EXTRA_WORKSPACE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditAccessListActivity.EXTRA_WORKSPACE_NAME";

    private AirDesk mAirDesk;
    private String mWorkspaceName;
    private ListAdapter<AccessListItem> mListAdapter;
    private ListView mListView;
    private Button mAddUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_access_list);

        mAirDesk = (AirDesk) getApplication();

        Intent intent = getIntent();

        mWorkspaceName = intent.getStringExtra(EXTRA_WORKSPACE_NAME);

        mListView = (ListView) findViewById(R.id.user_accesslistLV);

        final List<AccessListItem> accessList = mAirDesk.getWorkspaceAccessList(mWorkspaceName);

        mListAdapter = new ListAdapter<AccessListItem>(getApplicationContext(), R.layout.accesslist_user_item, accessList) {
            @Override
            public void initItemView(final AccessListItem accessListItem, View view, final int position) {
                String email = accessListItem.getUser().getEmail();
                //boolean allowed = accessListItem.getAllowed();
                TextView emailTV = (TextView) view.findViewById(R.id.accesslist_user_emailTV);
                emailTV.setText(email);

                Button accessListButton = (Button) view.findViewById(R.id.user_accesslistBtn);
                accessListButton.setText((accessListItem.isAllowed())? "block" : "allow");
                accessListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickAccessListButton(accessListItem, v, position);
                    }
                });
            }
        };

        mListView.setAdapter(mListAdapter);

        mAddUserBtn = (Button) findViewById(R.id.add_user_accesslistBtn);
        mAddUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddUserButton(mWorkspaceName, v);
            }
        });
    }

    private String toggleAllowanceText(AccessListItem item) {
        return (item.isAllowed())? "block" : "allow";
    }

    public void onClickAccessListButton(AccessListItem accessListItem, View v, int position) {
        boolean status  = mAirDesk.toggleUserPermissions(mWorkspaceName, accessListItem.getUser().getEmail(), accessListItem.isAllowed());
        if(status) {
            mListAdapter.setItems(mAirDesk.getWorkspaceAccessList(mWorkspaceName));
            mListAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "User's permissions changed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Could not change user permissions.", Toast.LENGTH_SHORT).show();
        }
    }


    public void onClickAddUserButton(final String mWorkspaceName, View view) {
        final EditText input = new EditText(this);

        EditText userEmailText = (EditText) findViewById(R.id.new_user_email);
        String userEmail = userEmailText.getText().toString();

        if (!userEmail.equals("")) {
            try {
                mAirDesk.inviteUser(mWorkspaceName, userEmail);
                mListAdapter.setItems(mAirDesk.getWorkspaceAccessList(mWorkspaceName));
                mListAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "User has been added", Toast.LENGTH_SHORT).show();
            } catch(AirDeskException e) {
                Toast.makeText(getApplicationContext(), "Could not add user!!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Invalid e-mail", Toast.LENGTH_SHORT).show();
        }

    }

}
