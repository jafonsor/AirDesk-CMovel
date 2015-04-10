package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.WorkspaceType;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;

public class FileListActivity extends ActionBarActivity {

    public final static String EXTRA_OWNER_EMAIL
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity.EXTRA_WORKSPACE_OWNER_EMAIL";

    public final static String EXTRA_WORKSPACE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity.EXTRA_WORKSPACE_NAME";

    public final static String EXTRA_TYPE_OF_WORKSPACE
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity.EXTRA_TYPE_OF_WORKSPACE";


    private AirDesk mAirDesk;
    private String mUserEmail;
    private String mWorkspaceName;
    private WorkspaceType mWorkspaceType;

    ListAdapter<AirDeskFile> mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        mAirDesk = (AirDesk) getApplication();
        Intent intent = getIntent();
        mUserEmail = intent.getStringExtra(EXTRA_OWNER_EMAIL);
        mWorkspaceName = intent.getStringExtra(EXTRA_WORKSPACE_NAME);
        mWorkspaceType = (WorkspaceType) intent.getSerializableExtra(EXTRA_TYPE_OF_WORKSPACE);


        TextView workspaceNameView = (TextView) findViewById(R.id.workspace_name);
        workspaceNameView.setText(mWorkspaceName);

        ListView fileList = (ListView) findViewById(R.id.file_list);

        List<AirDeskFile> files = mAirDesk.getWorkspaceFiles(mUserEmail, mWorkspaceName);


        mListAdapter = new ListAdapter<AirDeskFile>(this, R.layout.file_item, files) {
            @Override
            public void initItemView(final AirDeskFile file, View view, final int position) {
                TextView fileNameView = (TextView) view.findViewById(R.id.file_name);
                fileNameView.setText(file.getName());

                Button deleteButton = (Button) view.findViewById(R.id.delete_file_button);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickDeleteFile(file, v);
                    }
                });
            }
        };

        fileList.setAdapter(mListAdapter);
        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickShowFile(mListAdapter.getItem(position), view);
            }
        });

        Button createFileButton = (Button) findViewById(R.id.create_file_button);
        createFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateFile(mWorkspaceType,v);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        EditText fileNameText = (EditText) findViewById(R.id.new_file_name_input);
        fileNameText.getText().clear();
    }

    public void onClickDeleteFile(AirDeskFile file, View v) {
        if(!mAirDesk.deleteFile(mUserEmail, mWorkspaceName, file.getName(), mWorkspaceType)) {
            Toast.makeText(this, "file not deleted", Toast.LENGTH_SHORT).show();
        } else {
            mListAdapter.setItems(mAirDesk.getWorkspaceFiles(mUserEmail, mWorkspaceName));
            mListAdapter.notifyDataSetChanged();
            Toast.makeText(this, "file deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickShowFile(AirDeskFile file, View v) {
        Intent intent = new Intent(this, ShowFileActivity.class);
        String mFileName = file.getName();

        intent.putExtra(ShowFileActivity.EXTRA_WORKSPACE_NAME, mWorkspaceName);
        intent.putExtra(ShowFileActivity.EXTRA_WORKSPACE_OWNER, mUserEmail);
        intent.putExtra(ShowFileActivity.EXTRA_FILE_NAME, mFileName);
        intent.putExtra(ShowFileActivity.EXTRA_TYPE_OF_WORKSPACE, mWorkspaceType);
        startActivity(intent);
    }

    public void onClickCreateFile(final WorkspaceType mWorkspaceType, final View v) {
        EditText fileNameText = (EditText) findViewById(R.id.new_file_name_input);
        String fileName = fileNameText.getText().toString();

        if (fileName == null || fileName.isEmpty()) {
            Toast.makeText(this, "Invalid file name.", Toast.LENGTH_SHORT).show();
            // TO DO: check if there is already a file with that name
        } else {
            if(mAirDesk.fileExists(mUserEmail, mWorkspaceName, fileName, mWorkspaceType)) {
                Toast.makeText(this, "Already exists a file with that name.", Toast.LENGTH_SHORT).show();
            }
            else {
                if(!mAirDesk.createFile(mUserEmail, mWorkspaceName, fileName, mWorkspaceType)) {
                    Toast.makeText(this, "could not create file: Workspace full", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, EditFileActivity.class);
                    intent.putExtra(EditFileActivity.EXTRA_WORKSPACE_NAME, mWorkspaceName);
                    intent.putExtra(EditFileActivity.EXTRA_WORKSPACE_OWNER, mUserEmail);
                    intent.putExtra(EditFileActivity.EXTRA_FILE_NAME, fileName);
                    intent.putExtra(EditFileActivity.EXTRA_TYPE_OF_WORKSPACE, mWorkspaceType);
                    // Toast.makeText(this, "File successfully created.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }

        }
    }
}
