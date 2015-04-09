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
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.OwnerFragment;

public class EditAccessListActivity extends ActionBarActivity {

    public final static String EXTRA_WORKSPACE_ID
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditAccessListActivity.EXTRA_WORKSPACE_ID";

    private AirDesk mAirDesk;
    private ListAdapter<AccessListItem> mListAdapter;
    private ListView mListView;
    private Button mAddUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_access_list);

        mAirDesk = (AirDesk) getApplication();

        Intent intent = getIntent();

        Integer workspaceId = intent.getIntExtra(EXTRA_WORKSPACE_ID, -1);

        mListView = (ListView) findViewById(R.id.user_accesslistLV);

        //final Workspace workspace = mAirDesk.getWorkspaceById(workspaceId);
        final Workspace workspace = null;

        //List<AccessListItem> elements = workspace.getAccessList();
        List<AccessListItem> elements = new ArrayList<AccessListItem>() {{
            add(new AccessListItem(new User("só","para compilar")));
        }};

        mListAdapter = new ListAdapter<AccessListItem>(getApplicationContext(), R.layout.accesslist_user_item, elements) {
            @Override
            public void initItemView(final AccessListItem accessListItem, View view, final int position) {
                String email = accessListItem.getUser().getEmail();
                //boolean allowed = accessListItem.getAllowed();
                TextView emailTV = (TextView) view.findViewById(R.id.accesslist_user_emailTV);
                emailTV.setText(email);

                Button accessListButton = (Button) view.findViewById(R.id.user_accesslistBtn);
                accessListButton.setText("IMPLEMENT GET ALLOWED");
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
                onClickAddUserButton(workspace, v);
            }
        });
    }

    public void onClickAccessListButton(AccessListItem accessListItem, View v, int position) {
        //accessListItem.setAllowed(!accessListItem.getAllowed());
        mListAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "User's permissions changed", Toast.LENGTH_SHORT).show();
    }


    public void onClickAddUserButton(final Workspace workspace, View view) {
        final EditText input = new EditText(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add User")
                .setMessage("E-mail: ")
                .setView(input)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int which) {
                        String email = input.getText().toString();
                        if (!email.equals("")) {
                            AccessListItem acl = new AccessListItem(new User(email));
                            acl.setInvited(true);
                            //workspace.getAccessList().add(acl);
                            mListAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "User has been added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid e-mail", Toast.LENGTH_SHORT).show();
                        }
                        di.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int which) {
                        di.dismiss();
                    }
                })
                .create();
        dialog.show();

    }

}
