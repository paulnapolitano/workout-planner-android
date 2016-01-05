package pablo.workoutapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Pablo on 1/1/2016.
 */
public class MiniMuscleGroupAdapter extends ArrayAdapter<MuscleGroup>{
    Context context;
    int layoutResourceId;
    MuscleGroup data[] = null;

    public MiniMuscleGroupAdapter(Context context, int layoutResourceId, MuscleGroup[] data){
        super(context, layoutResourceId, data);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MuscleGroupHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new MuscleGroupHolder();
            holder.name = (TextView)row.findViewById(R.id.muscle_group_name);

            row.setTag(holder);
        } else {
            holder = (MuscleGroupHolder)row.getTag();
        }

        MuscleGroup muscleGroup = data[position];
        holder.name.setText(muscleGroup.getName());

        return row;
    }

    static class MuscleGroupHolder {
        TextView name;
    }
}
