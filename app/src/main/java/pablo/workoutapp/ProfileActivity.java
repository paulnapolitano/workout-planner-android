package pablo.workoutapp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity
                             implements DeleteProfileDialogFragment.DeleteProfileDialogListener {
    // TODO: Make activity update after goal creation
    // TODO: Make goal fragment GONE if no goals for profile
    Context context = this;
    WorkoutDatabaseUser dbUser = new WorkoutDatabaseUser(context);

    WorkoutProfile currentProfile;

    static final int GOAL_REQUEST = 0;

    private boolean returningWithResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout and Toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable Up button on toolbar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get current profile from DB
        currentProfile = dbUser.profiles.getCurrent();

        // Add goal swipe fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        WorkoutGoalPagerFragment goalFragment = new WorkoutGoalPagerFragment();
        fragmentManager.beginTransaction()
                       .replace(R.id.workout_goal_fragment_container, goalFragment, "GOAL_FRAG")
                       .commit();



        // Update Profile details based on db query
        // -------- Name --------
        TextView profileName = (TextView) findViewById(R.id.profile_name);
        profileName.setText(currentProfile.getName());
        // ----- Start Date -----
        TextView profileDate = (TextView) findViewById(R.id.profile_start_date);
        profileDate.setText(DateTimeFormatHelper.stringToDisplayString(currentProfile.getLastEdited()));

        // ----- Level Wheel ----
        ProgressWheel progressWheel = (ProgressWheel) findViewById(R.id.level_wheel);
        progressWheel.incrementProgress(currentProfile.getExperience());

        TextView profileLevel = (TextView) findViewById(R.id.level_number);
        profileLevel.setText(currentProfile.getLevelString());

        // Edit Profile Button
        ImageButton editButton = (ImageButton) findViewById(R.id.edit_profile_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // ---------------------- New Goal ----------------------
        Button newGoalButton = (Button) findViewById(R.id.workout_goal_new_button);
        newGoalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, NewGoalActivity.class);
                startActivityForResult(intent, GOAL_REQUEST);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Intent intent;
        switch(item.getItemId()) {
            case R.id.action_body:
                intent = new Intent(context, BodyActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_calendar:
                intent = new Intent(context, CalendarActivity.class);
                startActivity(intent);
                return true;
            case R.id.delete_profile:
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOAL_REQUEST) {
            if (resultCode == RESULT_OK) {
                returningWithResult = true;
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(returningWithResult){
            System.out.println("RETURNING WITH RESULT!");
            // Refresh goal fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment goalFrag = fragmentManager.findFragmentByTag("GOAL_FRAG");
            final FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.detach(goalFrag);
            ft.attach(goalFrag);
            ft.commit();
        }

        returningWithResult = false;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        int profileId = currentProfile.getId();
        dbUser.profiles.delete(profileId);

        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    public void showDeleteDialog(){
        DialogFragment dialog = new DeleteProfileDialogFragment();
        dialog.show(getFragmentManager(), "DeleteProfileDialogFragment");
    }


}
