package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;

/**
 * Created by MSC on 06/04/2015.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions.AirDeskException;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.LoginActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.Utils;

public class ForeignFragment extends Fragment {

    private AirDesk mAirDesk;
    private ListAdapter<ForeignWorkspace> mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.foreign_layout, container, false);
        mAirDesk = (AirDesk) getActivity().getApplication();
        ListView listView = (ListView) rootView.findViewById(R.id.foreign_workspaces_list);

        List<ForeignWorkspace> elements = mAirDesk.searchWorkspaces();

        mListAdapter = new ListAdapter<ForeignWorkspace>(getActivity(), R.layout.foreign_workspace_item, elements) {
            @Override
            public void initItemView(final ForeignWorkspace workspace, View view, final int position) {
                final String workspaceName = workspace.getName();
                TextView textView = (TextView) view.findViewById(R.id.workspace_name);
                textView.setText(workspaceName);

                Button removeForeignWorkspaceButton = (Button) view.findViewById(R.id.remove_workspace_button);
                removeForeignWorkspaceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickRemoveForeignWorkspace(workspace, v, position);
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

        Button editTagsButton = (Button) rootView.findViewById(R.id.edit_tags_button);
        editTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEditTags(v);
            }
        });

        return rootView;
    }

    public void onClickRemoveForeignWorkspace(ForeignWorkspace workspace, View v, int position) {
        final String workspaceName = workspace.getName();
        final String userEmail = workspace.getOwner().getEmail();
        //final Integer workspaceId = workspace.getId();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete the workspace '" + workspaceName + "'?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            mAirDesk.blockForeignWorkspace(userEmail, workspaceName);
                            mAirDesk.deleteForeignWorkspace(userEmail, workspaceName);
                            Toast.makeText(getActivity(), "Foreign workspace Deleted", Toast.LENGTH_SHORT).show();
                        } catch (AirDeskException e) {
                            Log.e("error", e.getMessage());
                            Toast.makeText(getActivity(), "Error removing workspace", Toast.LENGTH_SHORT).show();
                        }
                        mListAdapter.setItems(mAirDesk.searchWorkspaces());
                        mListAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog deleteFileDialog = alertDialogBuilder.create();
        deleteFileDialog.show();
    }

    public void onClickListWorkspaceFiles(ForeignWorkspace workspace, View v) {
        Intent intent = new Intent(getActivity(), FileListActivity.class);
        intent.putExtra(FileListActivity.EXTRA_OWNER_EMAIL, mAirDesk.getUser().getEmail());
        intent.putExtra(FileListActivity.EXTRA_WORKSPACE_NAME, workspace.getName());
        intent.putExtra(FileListActivity.EXTRA_TYPE_OF_WORKSPACE, WorkspaceType.FOREIGN);
        startActivity(intent);
    }

    public void onClickEditTags(View v) {
        User user = mAirDesk.getUser();
        final EditText input = new EditText(v.getContext());
        input.setHint("Tags separated by ;");
        input.setText(Utils.generateStringTagsFromList(user.getUserTags()));
        AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                .setTitle("Edit Tags")
                .setMessage("Tags: ")
                .setView(input)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int which) {
                        String new_tags = input.getText().toString();
                        List<String> tagList = Utils.retrieveTagsFromInputText(new_tags);
                        if (tagList != null) {
                            mAirDesk.changeUserTags(tagList);
                            mListAdapter.notifyDataSetChanged();

                            SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();

                            Set<String> set = new HashSet<String>();
                            set.addAll(mAirDesk.getUser().getUserTags());
                            editor.putStringSet(LoginActivity.STATE_TAGS, set);

                            editor.commit();

                            Toast.makeText(getActivity().getApplicationContext(), "Tags have been updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Invalid tags", Toast.LENGTH_SHORT).show();
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
