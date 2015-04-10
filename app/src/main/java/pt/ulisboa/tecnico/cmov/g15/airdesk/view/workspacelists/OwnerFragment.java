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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.OwnerWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.CreateEditOwnerWorkspaceActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditAccessListActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;

public class OwnerFragment extends Fragment {

    public static String SHARED_PREFS_WORKSPACES = "sharedPrefsWorkspace";
    public static String OWNER_WORKSPACE_LIST = "ownerWorkspaceList";
    private AirDesk mAirDesk;
    private ListAdapter<OwnerWorkspace> mListAdapter;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveListOwnerWSSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.owner_layout, container, false);

        restoreListOwnerWSSharedPreferences();

        mAirDesk = (AirDesk) getActivity().getApplication();

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

        Button button = (Button) rootView.findViewById(R.id.create_workspace_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateWorkspace(v);
            }
        });

        return rootView;
    }

    public void onClickCreateWorkspace(View v) {
        Intent intent = new Intent(getActivity(), CreateEditOwnerWorkspaceActivity.class);
        startActivity(intent);
    }

    public void onClickListWorkspaceFiles(OwnerWorkspace workspace, View v) {
        Intent intent = new Intent(getActivity(), FileListActivity.class);
        intent.putExtra(FileListActivity.EXTRA_OWNER_EMAIL, mAirDesk.getUser().getEmail());
        intent.putExtra(FileListActivity.EXTRA_WORKSPACE_NAME, workspace.getName());
        intent.putExtra(FileListActivity.EXTRA_TYPE_OF_WORKSPACE, WorkspaceType.OWNER);
        startActivity(intent);
    }

    public void onClickDeleteWorkspace(final OwnerWorkspace workspace, final View v, final int position) {
        final String workspaceName = workspace.getName();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete the workspace '" + workspaceName + "'?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mAirDesk.deleteOwnerWorkspace(workspaceName)) {
                            mListAdapter.setItems(mAirDesk.getOwnerWorkspaces());
                            mListAdapter.notifyDataSetChanged();

                            Toast.makeText(getActivity(), "delete workspace " + workspaceName, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "could not delete workspace " + workspaceName, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
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

    public void onClickWorkspaceSettings(OwnerWorkspace workspace, View v) {
        Intent intent = new Intent(getActivity(), CreateEditOwnerWorkspaceActivity.class);
        intent.putExtra(CreateEditOwnerWorkspaceActivity.EXTRA_WORKSPACE_NAME, workspace.getName());
        startActivity(intent);
    }

    public void onClickEditAccessList(OwnerWorkspace workspace, View v) {
        Intent intent = new Intent(getActivity(), EditAccessListActivity.class);
        intent.putExtra(EditAccessListActivity.EXTRA_WORKSPACE_NAME, workspace.getName());
        startActivity(intent);
    }

    public void saveListOwnerWSSharedPreferences() {
        /*mAirDesk = (AirDesk) getActivity().getApplication();
        List<OwnerWorkspace> ownerWorkspaceList = mAirDesk.getOwnerWorkspaces();
        if (ownerWorkspaceList == null || ownerWorkspaceList.size() == 0) return;

        //Creating a shared preference
        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFS_WORKSPACES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(ownerWorkspaceList);
        editor.putString(OWNER_WORKSPACE_LIST, json);
        editor.commit();*/
    }

    public void restoreListOwnerWSSharedPreferences() {
        /*SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFS_WORKSPACES, Context.MODE_PRIVATE);

        String ownerWSListJSONString = prefs.getString(OWNER_WORKSPACE_LIST,null);

        if(ownerWSListJSONString != null){
            Type type = new TypeToken<List<OwnerWorkspace>>(){}.getType();
            List<OwnerWorkspace> ownerWorkspaceList = new Gson().fromJson(ownerWSListJSONString, type);
            mAirDesk = (AirDesk) getActivity().getApplication();
            mAirDesk.setOwnerWorkspaces(ownerWorkspaceList);
        }

        */
    }

}
