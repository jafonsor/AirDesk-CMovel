package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;

/**
 * Created by MSC on 06/04/2015.
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;

public class ForeignFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.foreign_layout, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.foreign_workspaces_list);

        // TO DO: fetch real foreign workspaces
        List<String> elements = new ArrayList<String>() {{
            add("foreign workspace 1");
            add("foreign workspace 2");
        }};

        final ListAdapter<String> listAdapter = new ListAdapter<String>(getActivity(), R.layout.foreign_workspace_item, elements) {
            @Override
            public void initItemView(final String workspaceName, View view) {
                TextView textView = (TextView) view.findViewById(R.id.workspace_name);
                textView.setText(workspaceName);

                Button removeForeignWorkspaceButton = (Button) view.findViewById(R.id.remove_workspace_button);
                removeForeignWorkspaceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickRemoveForeignWorkspace(workspaceName, v);
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

        Button editTagsButton = (Button) rootView.findViewById(R.id.edit_tags_button);
        editTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEditTags(v);
            }
        });

        return rootView;
    }

    public void onClickRemoveForeignWorkspace(String workspaceName, View v) {
        Toast.makeText(getActivity(), "TO DO: remove foreign workspace", Toast.LENGTH_SHORT).show();
    }

    public void onClickListWorkspaceFiles(String workspaceName, View v) {
        Intent intent = new Intent(getActivity(), FileListActivity.class);
        intent.putExtra(FileListActivity.EXTRA_WORKSPACE_NAME, workspaceName);
        startActivity(intent);
    }

    public void onClickEditTags(View v) {
        Toast.makeText(getActivity(), "TO DO: edit tags popup", Toast.LENGTH_SHORT).show();
    }
}
