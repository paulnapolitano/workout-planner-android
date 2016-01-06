package pablo.workoutapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NewGoalActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert(getSupportActionBar()!=null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final WorkoutGoal.Factory newGoalFactory = new WorkoutGoal.Factory();

        // ==================== GoalType selector ====================
        String[] goalTypeChoices = new String[GoalType.values().length];
        for(int i = 0; i<GoalType.values().length; i++){
            goalTypeChoices[i] = GoalType.values()[i].printable();
        }

        ArrayAdapter<CharSequence> goalTypeAdapter =
                new ArrayAdapter<CharSequence>(context,
                        android.R.layout.simple_spinner_item,
                        goalTypeChoices);

        Spinner goalTypeSpinner = (Spinner) findViewById(R.id.new_goal_goal_type_spinner);
        goalTypeSpinner.setAdapter(goalTypeAdapter);
        goalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newGoalFactory.setGoalType(GoalType.fromPrintable((String) parent.getItemAtPosition(position)));
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // ==================== LiftType selector ====================
        String[] liftTypeChoices = new String[LiftType.values().length];
        for(int i = 0; i<LiftType.values().length; i++){
            liftTypeChoices[i] = LiftType.values()[i].printable();
        }

        ArrayAdapter<CharSequence> liftTypeAdapter =
                new ArrayAdapter<CharSequence>(context,
                        android.R.layout.simple_spinner_item,
                        liftTypeChoices);

        Spinner liftTypeSpinner = (Spinner) findViewById(R.id.new_goal_lift_type_spinner);
        liftTypeSpinner.setAdapter(liftTypeAdapter);
        liftTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newGoalFactory.setLiftType(LiftType.fromPrintable((String) parent.getItemAtPosition(position)));
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // ==================== LiftReps input ====================
        View liftRepsBlock = findViewById(R.id.new_goal_reps_block);
        EditText liftRepsInput = (EditText) findViewById(R.id.new_goal_reps_val);
        liftRepsInput.setText("10");

        // =================== CurrentVal input ===================
        EditText startValInput = (EditText) findViewById(R.id.new_goal_current_val);
        startValInput.setText("200");

        // =================== TargetVal input ====================
        EditText targetValInput = (EditText) findViewById(R.id.new_goal_target_val);
        targetValInput.setText("200");
    }
}
