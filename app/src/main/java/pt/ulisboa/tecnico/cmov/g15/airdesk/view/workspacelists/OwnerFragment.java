package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;

/**
 * Created by MSC on 06/04/2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.WorkspaceSettingsActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.CreateOwnerWorkspaceActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.WorkspaceFileListActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;

public class OwnerFragment extends Fragment {
    public final static String EXTRA_WORKSPACE_NAME
        = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.ForeignFragment.FILE_NAME";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.owner_layout, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.owner_workspaces_list);
        List<String> elements = new ArrayList<String>() {{
            add("workspace1");
            add("workspace2");
        }};

        final ListAdapter<String> listAdapter = new ListAdapter<String>(getActivity(), R.layout.owner_workspace_item, elements) {
            @Override
            public void initItemView(final String workspaceName, View view) {
                TextView textView = (TextView) view.findViewById(R.id.workspace_name);
                textView.setText(workspaceName);

                Button deleteWorkspaceButton = (Button) view.findViewById(R.id.delete_workspace_button);
                deleteWorkspaceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickDeleteWorkspace(workspaceName, v);
                    }
                });

                Button workspaceStingsButton = (Button) view.findViewById(R.id.workspace_settings_button);
                workspaceStingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickWorkspaceSettings(workspaceName, v);
                    }
                });
            }
        };
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String workspaceName = listAdapter.getItem(position);
                onClickListWorkspaceFiles(workspaceName, view);
            }
        });

        Button button = (Button)rootView.findViewById(R.id.create_workspace_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateWorkspace(v);
            }
        });

        return rootView;
    }

    public void onClickCreateWorkspace(View v) {
        Intent intent = new Intent(getActivity(), CreateOwnerWorkspaceActivity.class);
        startActivity(intent);
    }

    public void onClickListWorkspaceFiles(String workspaceName, View v) {
        Intent intent = new Intent(getActivity(), WorkspaceFileListActivity.class);
        intent.putExtra(EXTRA_WORKSPACE_NAME, workspaceName);
        startActivity(intent);
    }

    public void onClickDeleteWorkspace(String workspaceName, final View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete the workspace '" + workspaceName + "'?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "TO DO: delete workspace", Toast.LENGTH_SHORT).show();
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

    public void onClickWorkspaceSettings(String workspaceName, View v) {
        Intent intent = new Intent(getActivity(), WorkspaceSettingsActivity.class);
        intent.putExtra(EXTRA_WORKSPACE_NAME, workspaceName);
        startActivity(intent);
    }
}
