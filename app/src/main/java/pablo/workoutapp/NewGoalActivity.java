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
    View liftTypeBlock;
    View liftRepsBlock;

    private final int LIFT_REPS = NewGoalFactoryHelper.LIFT_REPS;
    private final int CURRENT = NewGoalFactoryHelper.CURRENT;
    private final int TARGET = NewGoalFactoryHelper.TARGET;

    private final int SHOW_WEIGHT = 0;
    private final int SHOW_LIFTING = 1;
    private final int SHOW_ALL = 2;

    private int visibility = SHOW_WEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert(getSupportActionBar()!=null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final WorkoutGoal.Factory newGoalFactory = new WorkoutGoal.Factory();
        final NewGoalFactoryHelper factoryHelper = new NewGoalFactoryHelper(newGoalFactory);
        final WorkoutDatabaseUser dbUser = new WorkoutDatabaseUser(context);

        // =================================== GoalType selector ===================================
        String[] goalTypeChoices = new String[GoalType.values().length];
        for(int i = 0; i<GoalType.values().length; i++){
            goalTypeChoices[i] = GoalType.values()[i].printable();
        }

        ArrayAdapter<CharSequence> goalTypeAdapter =
                new ArrayAdapter<CharSequence>(context,
                        android.R.layout.simple_spinner_dropdown_item,
                        goalTypeChoices);

        Spinner goalTypeSpinner = (Spinner) findViewById(R.id.new_goal_goal_type_spinner);
        goalTypeSpinner.setAdapter(goalTypeAdapter);

        // Set factory goal type to default value
        newGoalFactory.setGoalType(GoalType.values()[0]);

        goalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GoalType goal = GoalType.fromPrintable((String) parent.getItemAtPosition(position));
                newGoalFactory.setGoalType(goal);
                assert(goal!=null);
                switch(goal){
                    case GAIN_STAMINA:
                        visibility = SHOW_LIFTING;
                        break;
                    case GAIN_STRENGTH:
                        visibility = SHOW_LIFTING;
                        break;
                    default:
                        visibility = SHOW_WEIGHT;
                }
                updateVisibility();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // =================================== LiftType selector ===================================
        String[] liftTypeChoices = new String[LiftType.values().length];
        for(int i = 0; i<LiftType.values().length; i++){
            liftTypeChoices[i] = LiftType.values()[i].printable();
        }

        ArrayAdapter<CharSequence> liftTypeAdapter =
                new ArrayAdapter<CharSequence>(context,
                        android.R.layout.simple_spinner_dropdown_item,
                        liftTypeChoices);

        Spinner liftTypeSpinner = (Spinner) findViewById(R.id.new_goal_lift_type_spinner);
        liftTypeSpinner.setAdapter(liftTypeAdapter);

        // Set factory goal type to default value
        newGoalFactory.setLiftType(LiftType.values()[0]);

        liftTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LiftType lift = LiftType.fromPrintable((String) parent.getItemAtPosition(position));
                newGoalFactory.setLiftType(lift);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // ==================================== LiftReps input ====================================
        // Get relevant views from layout
        final EditText liftRepsInput = (EditText) findViewById(R.id.new_goal_reps_val);
        final Button   liftRepsMinus = (Button)   findViewById(R.id.new_goal_reps_minus);
        final Button   liftRepsPlus =  (Button)   findViewById(R.id.new_goal_reps_plus);

        // Initialize field value
        factoryHelper.setField(LIFT_REPS, 10, liftRepsInput);

        // Configure +/- buttons
        liftRepsMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { factoryHelper.decrementField(LIFT_REPS, liftRepsInput); }
        });
        liftRepsPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { factoryHelper.incrementField(LIFT_REPS, liftRepsInput); }
        });


        // =================================== CurrentVal input ===================================
        // Get relevant views from layout
        final EditText startValInput = (EditText) findViewById(R.id.new_goal_current_val);
        final Button   currentMinus =  (Button)   findViewById(R.id.new_goal_current_minus);
        final Button   currentPlus =   (Button)   findViewById(R.id.new_goal_current_plus);

        // Initialize field value
        factoryHelper.setField(CURRENT, 200, startValInput);

        // Configure +/- buttons
        currentMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { factoryHelper.decrementField(CURRENT, startValInput); }
        });
        currentPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { factoryHelper.incrementField(CURRENT, startValInput); }
        });


        // ==================================== TargetVal input ====================================
        // Get relevant views from layout
        final EditText targetValInput = (EditText) findViewById(R.id.new_goal_target_val);
        final Button   targetMinus =    (Button)   findViewById(R.id.new_goal_target_minus);
        final Button   targetPlus =     (Button)   findViewById(R.id.new_goal_target_plus);

        // Initialize field value
        factoryHelper.setField(TARGET, 200, targetValInput);

        // Configure +/- buttons' onClick methods
        targetMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { factoryHelper.decrementField(TARGET, targetValInput); }
        });
        targetPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { factoryHelper.incrementField(TARGET, targetValInput); }
        });


        // ====================================== Start Date ======================================
        // Get relevant views from layout
        startDateDisplay = (TextView) findViewById(R.id.new_goal_start_date);
        final Button startDateButton = (Button) findViewById(R.id.new_goal_start_date_button);

        // Initialize date display
        startDateTime = new DateTime();
        startDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(startDateTime));

        // Configure date selection button's onClick method
        startDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize fragment
                StartDatePickerFragment startDatePickerFragment = new StartDatePickerFragment();

                // Pass current start date to fragment
                // Month needs to be decremented by 1 on pass to DatePicker, since Joda monthOfYear
                // is indexed starting at 1, and DatePicker monthOfYear is indexed starting at 0.
                Bundle startDateBundle = new Bundle();
                startDateBundle.putInt("year", startDateTime.getYear());
                startDateBundle.putInt("month", startDateTime.getMonthOfYear() - 1);
                startDateBundle.putInt("day", startDateTime.getDayOfMonth());
                startDatePickerFragment.setArguments(startDateBundle);

                // Show fragment
                startDatePickerFragment.show(getFragmentManager(),startDatePickerFragment.getTag());
            }
        });


        // ======================================= End Date =======================================
        // Get relevant views from layout
        endDateDisplay = (TextView) findViewById(R.id.new_goal_end_date);
        final Button endDateButton = (Button) findViewById(R.id.new_goal_end_date_button);

        // Initialize date display
        endDateTime = new DateTime();
        endDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(endDateTime));

        // Configure date selection button's onClick method
        endDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize fragment
                EndDatePickerFragment endDatePickerFragment = new EndDatePickerFragment();

                // Pass current end date to fragment
                // Month needs to be decremented by 1 on pass to DatePicker, since Joda monthOfYear
                // is indexed starting at 1, and DatePicker monthOfYear is indexed starting at 0.
                Bundle endDateBundle = new Bundle();
                endDateBundle.putInt("year", endDateTime.getYear());
                endDateBundle.putInt("month", endDateTime.getMonthOfYear() - 1);
                endDateBundle.putInt("day", endDateTime.getDayOfMonth());
                endDatePickerFragment.setArguments(endDateBundle);

                // Show fragment
                endDatePickerFragment.show(getFragmentManager(), endDatePickerFragment.getTag());
            }
        });


        // ===================================== Create Button =====================================
        Button createButton = (Button) findViewById(R.id.new_goal_create_button);
        createButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Set goal's profile to current
                WorkoutProfile currentProfile = dbUser.profiles.getCurrent();
                newGoalFactory.setProfile(currentProfile);

                // Add goal to database
                WorkoutGoal newGoal = newGoalFactory.create();
                dbUser.goals.objectToRow(newGoal);

                // Move back to profile view
                finish();
            }
        });


        // =================================== Show/Hide Fields ===================================
        liftTypeBlock = findViewById(R.id.new_goal_block_lift_type);
        liftRepsBlock = findViewById(R.id.new_goal_block_lift_reps);

        updateVisibility();
    }


    public void updateVisibility(){
        switch(visibility) {
            case SHOW_LIFTING:
                liftRepsBlock.setVisibility(View.VISIBLE);
                liftTypeBlock.setVisibility(View.VISIBLE);
                break;
            case SHOW_WEIGHT:
                liftRepsBlock.setVisibility(View.GONE);
                liftTypeBlock.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onStartDateSet(DatePicker view, int year, int month, int day) {
        startDateTime = new DateTime(year, month + 1, day, 0, 0, 0, 0);
        if(endDateTime.isBefore(startDateTime)){
            endDateTime = startDateTime;
            endDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(endDateTime));
        }
        startDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(startDateTime));
    }

    @Override
    public void onEndDateSet(DatePicker view, int year, int month, int day) {
        endDateTime = new DateTime(year, month + 1, day, 0, 0, 0, 0);
        if(endDateTime.isBefore(startDateTime)){
            startDateTime = endDateTime;
            startDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(startDateTime));
        }
        endDateDisplay.setText(DateTimeFormatHelper.dateTimeToDisplayString(endDateTime));
    }
}
