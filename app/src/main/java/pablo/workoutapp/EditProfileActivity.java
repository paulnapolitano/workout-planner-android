package pablo.workoutapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class EditProfileActivity extends AppCompatActivity {
    public final static String PROFILE_NAME = "pablo.workoutapp.PROFILE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;

        WorkoutProfile currentProfile = WorkoutProfileDbHelper.getCurrentProfile(context);
        System.out.println(currentProfile.toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
