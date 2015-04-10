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

        ListView fileList = (ListView) findViewById(R.id.file_list);

        List<AirDeskFile> files = mAirDesk.getWorkspaceFiles(mUserEmail, mWorkspaceName);


        final ListAdapter<AirDeskFile> listAdapter = new ListAdapter<AirDeskFile>(this, R.layout.file_item, files) {
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

        fileList.setAdapter(listAdapter);
        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickShowFile(listAdapter.getItem(position), view);
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

    public void onClickDeleteFile(AirDeskFile file, View v) {
        Toast.makeText(this, "deleting file " + file.getName(), Toast.LENGTH_SHORT).show();
        //mAirDesk.deleteFile(file.getId());
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
        final EditText input = new EditText(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New File")
                .setMessage("File name: ")
                .setView(input)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int which) {
                        String fileName = input.getText().toString();
                        createAndEditFile(mWorkspaceType, fileName, v);
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

    // returns false if it fails to create the file
    public void createAndEditFile(WorkspaceType mWorkspaceType, String fileName, View v) {
        if (fileName == null || fileName.isEmpty()) {
            Toast.makeText(this, "invalid file name", Toast.LENGTH_SHORT).show();
            onClickCreateFile(mWorkspaceType, v);
            // TO DO: check if there is already a file with that name
        } else {
            mAirDesk.createFile(mUserEmail, mWorkspaceName, fileName, mWorkspaceType);
           // Toast.makeText(this, "TO DO: check if file exists", Toast.LENGTH_SHORT).show();
           // Toast.makeText(this, "TO DO: create file", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, EditFileActivity.class);
            intent.putExtra(EditFileActivity.EXTRA_WORKSPACE_NAME, mWorkspaceName);
            intent.putExtra(EditFileActivity.EXTRA_WORKSPACE_OWNER, mUserEmail);
            intent.putExtra(EditFileActivity.EXTRA_FILE_NAME, fileName);
            intent.putExtra(EditFileActivity.EXTRA_TYPE_OF_WORKSPACE, mWorkspaceType);
            Log.e("Assinatura", "WSname: "+ mWorkspaceName +"WSowner: "+ mUserEmail + "Filename: " + fileName + "WStype: " +mWorkspaceType.toString());
            startActivity(intent);

        }
    }
}
