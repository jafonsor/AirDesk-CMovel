package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
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
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.OwnerFragment;

public class FileListActivity extends ActionBarActivity {

    public final static String EXTRA_WORKSPACE_ID
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity.WORKSPACE_ID";

    private AirDesk mAirDesk;
    private Integer mWorkspaceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        mAirDesk = (AirDesk) getApplication();
        Intent intent = getIntent();
        mWorkspaceId = intent.getIntExtra(EXTRA_WORKSPACE_ID, -1);

        TextView workspaceNameView = (TextView)findViewById(R.id.workspace_name);

        workspaceNameView.setText(mWorkspaceId);

        ListView fileList = (ListView) findViewById(R.id.file_list);

        List<AirDeskFile> files = mAirDesk.getWorkspaceFiles(mWorkspaceId);

        final ListAdapter<AirDeskFile> listAdapter = new ListAdapter<AirDeskFile>(this, R.layout.file_item, files) {
            @Override
            public void initItemView(final AirDeskFile file, View view, final int poisition) {
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
                onClickCreateFile(v);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workspace_file_list, menu);
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


    public void onClickDeleteFile(AirDeskFile file, View v) {
        Toast.makeText(this, "deleting file " + file.getName(), Toast.LENGTH_SHORT).show();
        mAirDesk.deleteFile(file.getId());
    }

    public void onClickShowFile(AirDeskFile file, View v) {
        Intent intent = new Intent(this, ShowFileActivity.class);
        /*
        intent.putExtra(ShowFileActivity.EXTRA_)
        intent.putExtra(ShowFileActivity.EXTRA_FILE_NAME, fileName);
        intent.putExtra(ShowFileActivity.EXTRA_WORKSPACE_NAME, mWorkspaceName);
        intent.putExtra(ShowFileActivity.EXTRA_IS_OWNER, mIsOwner);*/
        startActivity(intent);
    }

    public void onClickCreateFile(final View v) {
        final EditText input = new EditText(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("New File")
            .setMessage("File name: ")
            .setView(input)
            .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface di, int which) {
                    String fileName = input.getText().toString();
                    createAndEditFile(fileName, v);
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
    public void createAndEditFile(String fileName, View v) {
        if(fileName == null || fileName.isEmpty()) {
            Toast.makeText(this, "invalid file name", Toast.LENGTH_SHORT).show();
            onClickCreateFile(v);
            // TO DO: check if there is already a file with that name
        } else {
            Toast.makeText(this, "TO DO: check if file exists", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "TO DO: create file", Toast.LENGTH_SHORT).show();

            Integer fileId = mAirDesk.createFile(fileName);

            Intent intent = new Intent(this, EditFileActivity.class);
            intent.putExtra(EditFileActivity.EXTRA_FILE_ID, fileId);
            startActivity(intent);
        }
    }
}
