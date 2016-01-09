package pablo.workoutapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;

public class NewGoalActivity extends AppCompatActivity
                             implements DatePickerFragmentListener {
    Context context = this;
    DateTime startDateTime;
    DateTime endDateTime;
    TextView startDateDisplay;
    TextView endDateDisplay;

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

        // ====================== Start Date ======================
        startDateDisplay = (TextView) findViewById(R.id.new_goal_start_date);
        final Button startDateButton = (Button) findViewById(R.id.new_goal_start_date_button);
        startDateTime = new DateTime();
        startDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(startDateTime));
        startDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartDatePickerFragment startDatePickerFragment = new StartDatePickerFragment();
                startDatePickerFragment.show(getFragmentManager(), startDatePickerFragment.getTag());
            }
        });

        // ======================= End Date =======================
        endDateDisplay = (TextView) findViewById(R.id.new_goal_end_date);
        final Button endDateButton = (Button) findViewById(R.id.new_goal_end_date_button);
        endDateTime = new DateTime();
        endDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(endDateTime));
        endDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EndDatePickerFragment endDatePickerFragment = new EndDatePickerFragment();
                endDatePickerFragment.show(getFragmentManager(), endDatePickerFragment.getTag());
            }
        });
    }

    @Override
    public void onStartDateSet(DatePicker view, int year, int month, int day) {
        startDateTime = new DateTime(year, month, day, 0, 0, 0, 0);
        if(endDateTime.isBefore(startDateTime)){
            endDateTime = startDateTime;
            endDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(endDateTime));
        }
        startDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(startDateTime));
    }

    @Override
    public void onEndDateSet(DatePicker view, int year, int month, int day) {
        endDateTime = new DateTime(year, month, day, 0, 0, 0, 0);
        if(endDateTime.isBefore(startDateTime)){
            startDateTime = endDateTime;
            startDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(startDateTime));
        }
        endDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(endDateTime));
    }
}
