package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.MainActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListExpandableListAdapter;


public class WorkspaceListActivity extends ActionBarActivity {

    public final static String EXTRA_FILE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.WorkspaceListActivity.FILE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_LOGIN_EMAIL);
        setContentView(R.layout.activity_workspace_list);

        TextView textView = (TextView) findViewById(R.id.user_info);
        textView.setText("User: " + message);


        List<Pair<String,List<String>>> elements = new ArrayList<Pair<String,List<String>>>() {{
            add(new Pair<String,List<String>>("Workspace1", new ArrayList<String>() {{
                add("file1");
                add("file2");
            }}));
            add(new Pair<String,List<String>>("Workspace2", new ArrayList<String>() {{
                add("file3");
                add("file4");
            }}));
        }};


        ExpandableListAdapter adapter = new ListExpandableListAdapter<String,String>(this, elements,
                R.layout.workspace_item, R.layout.file_item) {
            @Override
            public void editChildView(final String fileName, View convertView) {
                TextView item = (TextView) convertView.findViewById(R.id.file_name);
                item.setText(fileName);

                Button editFileButton = (Button) convertView.findViewById(R.id.edit_file_button);
                editFileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickEditFileButton(fileName, v);
                    }
                });

                Button deleteFileButton = (Button) convertView.findViewById(R.id.delete_file_button);
                deleteFileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        onClickDeleteFileButton(fileName, v);
                    }
                });
            }

            @Override
            public void editGroupView(String workspaceName, View convertView) {
                TextView item = (TextView) convertView.findViewById(R.id.workspace_name);
                item.setTypeface(null, Typeface.BOLD);
                item.setText(workspaceName);
            }
        };

        ExpandableListView list = (ExpandableListView) findViewById(R.id.owned_workspaces_list);
        list.setAdapter(adapter);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workspace_list, menu);
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

    public void createWorkspace(View view) {
        // TO DO
    }

    public void onClickEditFileButton(String fileName, View v) {
        Intent intent = new Intent(this, EditFileActivity.class);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        startActivity(intent);
    }

    public void onClickDeleteFileButton(String fileName, final View v) {
        Activity self = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete the file '" + fileName + "'?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "TO DO: delete file", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog deleteFileDialog = alertDialogBuilder.create();
        deleteFileDialog.show();
    }
}
