package pablo.workoutapp;

import android.widget.EditText;

/**
 * Created by Pablo on 1/10/2016.
 */
public class NewGoalFactoryHelper {
    public final static int LIFT_REPS = 0;
    public final static int TARGET = 1;
    public final static int CURRENT = 2;

    private WorkoutGoal.Builder builder;

    public NewGoalFactoryHelper(WorkoutGoal.Builder builder){
        this.builder = builder;
    }

    public void setField(int field, Integer value, EditText input){
        switch (field){
            case LIFT_REPS:
                builder.setLiftReps(value);
                input.setText(String.format("%d", value));
                break;
            case CURRENT:
                builder.setCurrent(value);
                input.setText(String.format("%d lb", value));
                break;
            case TARGET:
                builder.setTarget(value);
                input.setText(String.format("%d lb", value));
                break;
        }
    }
    
    public void changeField(int field, int delta, EditText input){
        int oldValue;

        switch(field) {
            case LIFT_REPS:
                oldValue = builder.getLiftReps();
                break;
            case CURRENT:
                oldValue = builder.getCurrent();
                break;
            case TARGET:
                oldValue = builder.getTarget();
                break;
            default:
                oldValue = 0;
        }

        int newValue = oldValue + delta;
        if(newValue<0){
            newValue = 0;
        } else if(newValue>999){
            newValue = 999;
        }

        switch(field) {
            case LIFT_REPS:
                builder.setLiftReps(newValue);
                input.setText(String.format("%d", newValue));
                break;
            case CURRENT:
                builder.setCurrent(newValue);
                input.setText(String.format("%d lb", newValue));
                break;
            case TARGET:
                builder.setTarget(newValue);
                input.setText(String.format("%d lb", newValue));
                break;
            default:
        }
    }
    public void incrementField(int field, EditText input){
        int delta;
        switch(field) {
            case LIFT_REPS:
                delta = 1;
                break;
            case CURRENT:
                delta = 5;
                break;
            case TARGET:
                delta = 5;
                break;
            default:
                delta = 1;
        }
        changeField(field, delta, input);
    }
    public void decrementField(int field, EditText input){
        int delta;
        switch(field) {
            case LIFT_REPS:
                delta = -1;
                break;
            case CURRENT:
                delta = -5;
                break;
            case TARGET:
                delta = -5;
                break;
            default:
                delta = -1;
        }
        changeField(field, delta, input);
    }
}
