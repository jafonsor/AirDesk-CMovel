package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.WorkspacesListActivity;


public class LoginActivity extends ActionBarActivity {

    private AirDesk airDesk;
    public final static String EXTRA_LOGIN_EMAIL
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.MainActivity.LOGIN_EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        airDesk = (AirDesk)getApplication();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Call back for Login button.
     * Reads email from text box and opens the workspace list activity.
     */
    public void login(View view) {
        Intent intent = new Intent(this, WorkspacesListActivity.class);
        EditText editText = (EditText) findViewById(R.id.login_email_box);
        String login_email = editText.getText().toString().trim();
        editText = (EditText) findViewById(R.id.login_nickname_box);
        String login_nickname = editText.getText().toString().trim();

        if(login_email != null && !login_email.equals("")) {
            //TODO verificar que email é unico
            User user = new User(login_nickname, login_email);
            airDesk.setUser(user);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"E-mail is required", Toast.LENGTH_SHORT).show();
        }
    }
}
