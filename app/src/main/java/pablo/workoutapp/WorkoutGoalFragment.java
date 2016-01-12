package pablo.workoutapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WorkoutGoalFragment extends Fragment {
    protected WorkoutGoal workoutGoal;

    public WorkoutGoalFragment() {
        // Required empty public constructor
    }

    public static WorkoutGoalFragment newInstance(WorkoutGoal workoutGoal){
        WorkoutGoalFragment workoutGoalFragment = new WorkoutGoalFragment();
        Bundle args = workoutGoal.toBundle();
        workoutGoalFragment.setArguments(args);
        return workoutGoalFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_workout_goal, container, false);

        // Get arguments from parent fragment
        Bundle args = getArguments();
        if(args == null){
            this.workoutGoal = new WorkoutGoal.Factory().create();
        } else {
            this.workoutGoal = WorkoutGoal.fromBundle(args);
        }

        WorkoutGoalHolder holder;
        holder = new WorkoutGoalHolder();
        holder.type =       (TextView)layoutView.findViewById(R.id.profile_goal_type_value);
        holder.startVal =   (TextView)layoutView.findViewById(R.id.profile_goal_start_value);
        holder.currentVal = (TextView)layoutView.findViewById(R.id.profile_goal_current_value);
        holder.targetVal =  (TextView)layoutView.findViewById(R.id.profile_goal_target_value);
        layoutView.setTag(holder);

        holder.type.setText(workoutGoal.toString());
        holder.startVal.setText(workoutGoal.getStart().toString());
        holder.currentVal.setText(workoutGoal.getCurrent().toString());
        holder.targetVal.setText(workoutGoal.getTarget().toString());

        return layoutView;
    }

    static class WorkoutGoalHolder {
        TextView type;
        TextView startVal;
        TextView currentVal;
        TextView targetVal;
    }
}
