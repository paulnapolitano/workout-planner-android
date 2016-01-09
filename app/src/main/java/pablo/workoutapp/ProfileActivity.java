package pablo.workoutapp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity
                             implements DeleteProfileDialogFragment.DeleteProfileDialogListener {
    Context context = this;
    WorkoutProfile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get current profile from DB
        currentProfile = WorkoutProfileDbHelper.getCurrentProfile(context);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Update Profile details based on db query
        // -------- Name --------
        TextView profileName = (TextView) findViewById(R.id.profile_name);
        profileName.setText(currentProfile.getName());
        // ----- Start Date -----
        TextView profileDate = (TextView) findViewById(R.id.profile_start_date);
        profileDate.setText(currentProfile.getLastEdited());

        // Level Wheel
        ProgressWheel progressWheel = (ProgressWheel) findViewById(R.id.level_wheel);
        progressWheel.incrementProgress(100);

        // Edit Profile Button
        ImageButton editButton = (ImageButton) findViewById(R.id.edit_profile_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // =================== Current Goals ===================
        if(WorkoutGoalDbHelper.profileHasGoals(context, currentProfile)){
            // Get List of Goals associated with current profile
            final WorkoutGoal[] workoutGoals = WorkoutGoalDbHelper.getProfileGoals(context,
                                                                                   currentProfile);

            // Inflate list of Goals using adapter
            ListView workoutGoalList = (ListView) findViewById(R.id.workout_goal_list);
            final WorkoutGoalAdapter goalAdapter =
                  new WorkoutGoalAdapter(context, R.layout.list_element_workout_goal, workoutGoals);
            workoutGoalList.setAdapter(goalAdapter);
        }

        // ---------------------- New Goal ----------------------
        Button newGoalButton = (Button) findViewById(R.id.workout_goal_new_button);
        newGoalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, NewGoalActivity.class);
                startActivity(intent);
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
    public void onDialogPositiveClick(DialogFragment dialog) {
        int profileId = currentProfile.getId();
        WorkoutProfileDbHelper.deleteProfile(context, profileId);
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
