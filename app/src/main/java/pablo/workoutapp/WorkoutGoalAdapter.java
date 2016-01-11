package pablo.workoutapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pablo on 1/5/2016.
 */
public class WorkoutGoalAdapter extends ArrayAdapter<WorkoutGoal> {
    Context context;
    int layoutResourceId;
    WorkoutGoal[] data = null;

    public WorkoutGoalAdapter(Context context, int layoutResourceId, WorkoutGoal[] data){
        super(context, layoutResourceId, data);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WorkoutGoalHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new WorkoutGoalHolder();
            holder.type =       (TextView)row.findViewById(R.id.profile_goal_type_value);
            holder.startVal =   (TextView)row.findViewById(R.id.profile_goal_start_value);
            holder.currentVal = (TextView)row.findViewById(R.id.profile_goal_current_value);
            holder.targetVal =  (TextView)row.findViewById(R.id.profile_goal_target_value);
            row.setTag(holder);
        } else {
            holder = (WorkoutGoalHolder)row.getTag();
        }

        WorkoutGoal workoutGoal = data[position];
        holder.type.setText(workoutGoal.toString());
        holder.startVal.setText(workoutGoal.getStart().toString());
        holder.currentVal.setText(workoutGoal.getCurrent().toString());
        holder.targetVal.setText(workoutGoal.getTarget().toString());

        return row;
    }

    static class WorkoutGoalHolder {
        TextView type;
        TextView startVal;
        TextView currentVal;
        TextView targetVal;
    }
}
