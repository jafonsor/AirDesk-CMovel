package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;

/**
 * Created by MSC on 06/04/2015.
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.WorkspaceSettingsActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.CreateOwnerWorkspaceActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditAccessListActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;

public class OwnerFragment extends Fragment {

    private AirDesk mAirDesk;
    private ListAdapter<OwnerWorkspace> mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.owner_layout, container, false);
        mAirDesk = (AirDesk)getActivity().getApplication();

        ListView listView = (ListView) rootView.findViewById(R.id.owner_workspaces_list);

        List<OwnerWorkspace> elements = mAirDesk.getOwnerWorkspaces();


        mListAdapter = new ListAdapter<OwnerWorkspace>(getActivity(), R.layout.owner_workspace_item, elements) {
            @Override
            public void initItemView(final OwnerWorkspace workspace, View view, final int position) {
                String workspaceName = workspace.getName();
                TextView textView = (TextView) view.findViewById(R.id.workspace_name);
                textView.setText(workspaceName);

                Button deleteWorkspaceButton = (Button) view.findViewById(R.id.delete_workspace_button);
                deleteWorkspaceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickDeleteWorkspace(workspace, v, position);
                    }
                });

                Button workspaceStingsButton = (Button) view.findViewById(R.id.workspace_settings_button);
                workspaceStingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickWorkspaceSettings(workspace, v);
                    }
                });

                Button editAccessListButton = (Button) view.findViewById(R.id.edit_access_list_button);
                editAccessListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickEditAccessList(workspace, v);
                    }
                });
            }
        };

        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickListWorkspaceFiles(mListAdapter.getItem(position), view);
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

    public void onClickListWorkspaceFiles(OwnerWorkspace workspace, View v) {
        Intent intent = new Intent(getActivity(), FileListActivity.class);
        intent.putExtra(FileListActivity.EXTRA_WORKSPACE_ID, workspace.getId());
        startActivity(intent);
    }

    public void onClickDeleteWorkspace(OwnerWorkspace workspace, final View v, final int position) {
        final String workspaceName = workspace.getName();
        final Integer workspaceId  = workspace.getId();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete the workspace '" + workspaceName + "'?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "TO DO: delete workspace", Toast.LENGTH_SHORT).show();
                        mAirDesk.deleteOwnerWorkspace(workspaceId);
                        mListAdapter.notifyDataSetChanged();
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

    public void onClickWorkspaceSettings(OwnerWorkspace workspace, View v) {
        Intent intent = new Intent(getActivity(), WorkspaceSettingsActivity.class);
        intent.putExtra(WorkspaceSettingsActivity.EXTRA_WORKSPACE_ID, workspace.getId());
        startActivity(intent);
    }

    public void onClickEditAccessList(OwnerWorkspace workspace, View v) {
        Intent intent = new Intent(getActivity(), EditAccessListActivity.class);
        intent.putExtra(EditAccessListActivity.EXTRA_WORKSPACE_ID, workspace.getId());
        startActivity(intent);
    }
}
