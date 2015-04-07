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

import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.OwnerFragment;

public class FileListActivity extends ActionBarActivity {

    public final static String EXTRA_FILE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity.FILE_NAME";

    public final static String EXTRA_WORKSPACE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity.WORKSPACE_NAME";

    public final static String EXTRA_IS_OWNER
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity.EXTRA_IS_OWNER";

    private String mWorkspaceName;
    private boolean mIsOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        Intent intent = getIntent();
        mWorkspaceName = intent.getStringExtra(EXTRA_WORKSPACE_NAME);
        mIsOwner = intent.getBooleanExtra(EXTRA_IS_OWNER, false);
        Log.d("on filelist create:", "workspace name = " + mWorkspaceName);
        Log.d("on filelist create:", "is owner = " + mWorkspaceName);
        TextView workspaceNameView = (TextView)findViewById(R.id.workspace_name);

        String workspaceBelonging = (mIsOwner)? "Owner" : "Foreign";
        workspaceNameView.setText(workspaceBelonging + ": " + mWorkspaceName);

        ListView fileList = (ListView) findViewById(R.id.file_list);
        List<String> files = new ArrayList<String>() {{
            add("file1");
            add("file2");
        }};

        final ListAdapter<String> listAdapter = new ListAdapter<String>(this, R.layout.file_item, files) {
            @Override
            public void initItemView(final String fileName, View view) {
                TextView fileNameView = (TextView) view.findViewById(R.id.file_name);
                fileNameView.setText(fileName);

                Button deleteButton = (Button) view.findViewById(R.id.delete_file_button);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickDeleteFile(fileName, v);
                    }
                });
            }
        };

        fileList.setAdapter(listAdapter);
        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = listAdapter.getItem(position);
                onClickShowFile(fileName, view);
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


    public void onClickDeleteFile(String fileName, View v) {
        Toast.makeText(this, "TO DO: delete file " + fileName, Toast.LENGTH_SHORT).show();
    }

    public void onClickShowFile(String fileName, View v) {
        Intent intent = new Intent(this, ShowFileActivity.class);
        intent.putExtra(ShowFileActivity.EXTRA_FILE_NAME, fileName);
        intent.putExtra(ShowFileActivity.EXTRA_WORKSPACE_NAME, mWorkspaceName);
        intent.putExtra(ShowFileActivity.EXTRA_IS_OWNER, mIsOwner);
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
            Intent intent = new Intent(this, EditFileActivity.class);
            intent.putExtra(EditFileActivity.EXTRA_FILE_NAME, fileName);
            intent.putExtra(EditFileActivity.EXTRA_WORKSPACE_NAME, mWorkspaceName);
            intent.putExtra(EditFileActivity.EXTRA_IS_OWNER, mIsOwner);
            startActivity(intent);
        }
    }
}
