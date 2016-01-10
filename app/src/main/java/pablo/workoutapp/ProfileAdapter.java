package pablo.workoutapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Pablo on 1/9/2016.
 */
public class ProfileAdapter extends ArrayAdapter<WorkoutProfile> {
    Context context;
    int layoutResourceId;
    WorkoutProfile[] data = null;

    public ProfileAdapter(Context context, int layoutResourceId, WorkoutProfile[] data){
        super(context, layoutResourceId, data);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WorkoutProfileHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new WorkoutProfileHolder();
            holder.levelWheel  = (ProgressWheel)row.findViewById(R.id.level_wheel);
            holder.levelNumber = (TextView)row.findViewById(R.id.level_number);
            holder.name        = (TextView)row.findViewById(R.id.profile_name);
            holder.lastEdited  = (TextView)row.findViewById(R.id.profile_last_edited);
            row.setTag(holder);
        } else {
            holder = (WorkoutProfileHolder)row.getTag();
        }

        WorkoutProfile workoutProfile = data[position];
        Integer level = workoutProfile.getLevel();
        holder.levelWheel.incrementProgress(workoutProfile.getExperience());
        holder.levelNumber.setText(level.toString());
        holder.name.setText(workoutProfile.getName());
        holder.lastEdited.setText(
                DateTimeFormatHelper.stringToDisplayString(workoutProfile.getLastEdited()));

        return row;
    }

    static class WorkoutProfileHolder {
        ProgressWheel levelWheel;
        TextView levelNumber;
        TextView name;
        TextView lastEdited;
    }
}
