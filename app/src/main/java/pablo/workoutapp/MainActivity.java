package pablo.workoutapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final Context context = this;

    private final static String PROFILE_NAME = "pablo.workoutapp.PROFILE_NAME";

    WorkoutDatabaseUser dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout and Toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbUser = new WorkoutDatabaseUser(context);

        // Get all profiles from database
        WorkoutProfile[] allProfiles = dbUser.profiles.getAll();

        // Format profile ListView with custom layout
        ProfileAdapter adapter = new ProfileAdapter(this, R.layout.list_element_profile, allProfiles);
        final ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        // On selection of a profile, set it to current and move to profile view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // Get name of clicked profile
                final TextView nameField = (TextView) view.findViewById(R.id.profile_name);
                final String name = (String) nameField.getText();

                // Update profile to current
                dbUser.profiles.setCurrent(name);

                // Go to profile view
                Intent intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // On creation of a new profile, set it to current and move to profile view
        final Button newProfileButton = (Button) findViewById(R.id.button_new_profile);
        newProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt_new_profile, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.nameInput);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Get input from user
                                        String newName = userInput.getText().toString();

                                        // Create and add new profile to database, make it current
                                        dbUser.profiles.addNew(newName);
                                        dbUser.profiles.setCurrent(newName);

                                        // Move to profile activity
                                        Intent intent = new Intent(context, ProfileActivity.class);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

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
}
