package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.Utils;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists.SwipeActivity;


public class LoginActivity extends ActionBarActivity {

    private AirDesk airDesk;
    public final static String EXTRA_LOGIN_EMAIL
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.MainActivity.LOGIN_EMAIL";
    public final static String STATE_EMAIL = "emailState";
    public final static String STATE_NICKNAME = "nicknameState";
    public final static String STATE_TAGS = "tagsState";
    public final static String SHARED_PREFS_FILE = "sharedPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        airDesk = (AirDesk) getApplication();

        //load user from preference
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        if (prefs != null) {
            String email = prefs.getString(STATE_EMAIL, null);
            String nickname = prefs.getString(STATE_NICKNAME, null);
            Set<String> set = prefs.getStringSet(STATE_TAGS, null);

            if(email!=null) {
                User user = new User(nickname, email);
                if(set!=null) user.setUserTags(new ArrayList<String>(set));
                Toast.makeText(getApplicationContext(), "Welcome: " + email, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SwipeActivity.class));
                finish();
            }
        }

    }

    /**
     * Call back for Login button.
     * Reads email from text box and opens the workspace list activity.
     */
    public void login(View view) {
        Intent intent = new Intent(this, SwipeActivity.class);
        EditText editText = (EditText) findViewById(R.id.login_email_box);
        String login_email = editText.getText().toString().trim();
        editText = (EditText) findViewById(R.id.login_nickname_box);
        String login_nickname = editText.getText().toString().trim();
        editText = (EditText) findViewById(R.id.login_tags_box);
        String tags = editText.getText().toString().trim();


        if (login_email != null && !login_email.equals("")) {
            //TODO verificar que email é unico
            User user = new User(login_nickname, login_email);
            if(Utils.retrieveTagsFromInputText(tags)!=null) user.setUserTags(Utils.retrieveTagsFromInputText(tags));
            airDesk.setUser(user);


            //Save user state
            //save the task list to preference
            SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(STATE_EMAIL, airDesk.getUser().getEmail());
            editor.putString(STATE_NICKNAME, airDesk.getUser().getUserName());

            Set<String> set = new HashSet<String>();
            set.addAll(airDesk.getUser().getUserTags());
            editor.putStringSet(STATE_TAGS, set);

            editor.commit();

            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "E-mail is required", Toast.LENGTH_SHORT).show();
        }
    }


}
