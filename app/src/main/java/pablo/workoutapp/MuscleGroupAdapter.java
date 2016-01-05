package pablo.workoutapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pablo on 1/1/2016.
 */
public class MuscleGroupAdapter extends ArrayAdapter<MuscleGroup> {
    Context context;
    int layoutResourceId;
    int expandedLayoutResourceId;
    MuscleGroup data = null;

    public MuscleGroupAdapter(Context context, int layoutResourceId, MuscleGroup data){
        super(context, layoutResourceId, new MuscleGroup[]{data});
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MuscleGroupHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new MuscleGroupHolder();
            holder.name = (TextView)row.findViewById(R.id.muscle_group_name);

            row.setTag(holder);
        } else {
            holder = (MuscleGroupHolder)row.getTag();
        }

        MuscleGroup muscleGroup = data;
        holder.name.setText(muscleGroup.getName());

        return row;
    }

    static class MuscleGroupHolder {
        ImageView image;
        TextView name;
    }
}
