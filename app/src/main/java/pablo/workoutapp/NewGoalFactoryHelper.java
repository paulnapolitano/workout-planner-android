package pablo.workoutapp;

import android.widget.EditText;

/**
 * Created by Pablo on 1/10/2016.
 */
public class NewGoalFactoryHelper {
    public final static int LIFT_REPS = 0;
    public final static int TARGET = 1;
    public final static int CURRENT = 2;

    private WorkoutGoal.Factory factory;

    public NewGoalFactoryHelper(WorkoutGoal.Factory factory){
        this.factory = factory;
    }

    public void setField(int field, Integer value, EditText input){
        switch (field){
            case LIFT_REPS:
                factory.setLiftReps(value);
                input.setText(String.format("%d", value));
                break;
            case CURRENT:
                factory.setCurrent(value);
                input.setText(String.format("%d lb", value));
                break;
            case TARGET:
                factory.setTarget(value);
                input.setText(String.format("%d lb", value));
                break;
        }
    }
    
    public void changeField(int field, int delta, EditText input){
        int oldValue;

        switch(field) {
            case LIFT_REPS:
                oldValue = factory.getLiftReps();
                break;
            case CURRENT:
                oldValue = factory.getCurrent();
                break;
            case TARGET:
                oldValue = factory.getTarget();
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
                factory.setLiftReps(newValue);
                input.setText(String.format("%d", newValue));
                break;
            case CURRENT:
                factory.setCurrent(newValue);
                input.setText(String.format("%d lb", newValue));
                break;
            case TARGET:
                factory.setTarget(newValue);
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
