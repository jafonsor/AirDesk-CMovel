package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.GlobalContext;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.MainActivity;


public class WorkspaceListActivity extends ActionBarActivity {

    public final static String EXTRA_FILE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.WorkspaceListActivity.FILE_NAME";

    class WorkspaceListAdapter extends BaseExpandableListAdapter {
        private Activity context;
        private List<String> workspaces;
        private Map<String, List<String>> workspaceFiles;

        public WorkspaceListAdapter(Activity context, List<String> workspaces, Map<String, List<String>> workspaceFiles) {
            this.context = context;
            this.workspaces = workspaces;
            this.workspaceFiles = workspaceFiles;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return workspaceFiles.get(workspaces.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String fileName = (String) getChild(groupPosition, childPosition);

            if(convertView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                convertView = inflater.inflate(R.layout.file_item, null);
            }

            TextView item = (TextView) convertView.findViewById(R.id.file_name);
            item.setText(fileName);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return workspaceFiles.get(workspaces.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return workspaces.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return workspaces.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String workspaceName = (String) getGroup(groupPosition);
            if(convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.workspace_item, null);
            }

            TextView item = (TextView) convertView.findViewById(R.id.workspace_name);
            item.setTypeface(null, Typeface.BOLD);
            item.setText(workspaceName);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_LOGIN_EMAIL);
        setContentView(R.layout.activity_workspace_list);

        TextView textView = (TextView) findViewById(R.id.user_info);
        textView.setText("User: " + message);

        List<String> workspaces = new ArrayList<String>() {{
            add("Workspace1");
            add("Workspace2");
        }};

        Map<String,List<String>> fileNames = new HashMap<String, List<String>>() {{
            put("Workspace1", new ArrayList<String>() {{
                add("file1");
                add("file3");
            }});

            put("Workspace2", new ArrayList<String>() {{
                add("file4");
                add("file5");
            }});
        }};



        ExpandableListAdapter adapter = new WorkspaceListAdapter(this, workspaces, fileNames);
        Log.d("adaptorTest:", "groupCount=" + adapter.getGroupCount());
        Log.d("adaptorTest:", "childrenCount(0)=" + adapter.getChildrenCount(0));
        Log.d("adaptorTest:", "childrenCount(1)=" + adapter.getChildrenCount(1));
        Log.d("adaptorTest:", (String)adapter.getGroup(0) + " - " + (String)adapter.getChild(0,0));
        Log.d("adaptorTest:", (String)adapter.getGroup(0) + " - " + (String)adapter.getChild(0,1));
        Log.d("adaptorTest:", (String)adapter.getGroup(1) + " - " + (String)adapter.getChild(1,0));
        Log.d("adaptorTest:", (String)adapter.getGroup(1) + " - " + (String)adapter.getChild(1,1));
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
}
