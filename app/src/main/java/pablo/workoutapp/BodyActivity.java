package pablo.workoutapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class BodyActivity extends AppCompatActivity {
    Context context = this;
    Boolean viewIsFront;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);
        final ListView listView = (ListView) findViewById(R.id.listview);
        final View detailedView = (View) findViewById(R.id.detailed_view);
        detailedView.setVisibility(View.GONE);
        final TextView name = (TextView) detailedView.findViewById(R.id.muscle_group_name);
        final ImageView image = (ImageView) detailedView.findViewById(R.id.muscle_group_image);
        final ImageView bodyImage = (ImageView) detailedView.findViewById(R.id.body_image);

        final MuscleGroup[] list = {
            new MuscleGroup("Deltoids",   R.drawable.f_deltoids_select,  true),
            new MuscleGroup("Biceps",     R.drawable.f_biceps_select,    true),
            new MuscleGroup("Forearms",   R.drawable.f_forearms_select,  true),
            new MuscleGroup("Pectorals",  R.drawable.f_pectorals_select, true),
            new MuscleGroup("Abdominals", R.drawable.f_abs_select,       true),
            new MuscleGroup("Quadriceps", R.drawable.f_quads_select,     true),
            new MuscleGroup("Triceps",    R.drawable.r_triceps_0,        false),
            new MuscleGroup("Traps",      R.drawable.r_traps_0,          false),
            new MuscleGroup("Lats",       R.drawable.r_lats_0,           false),
            new MuscleGroup("Hamstrings", R.drawable.r_hamstrings_0,     false),
            new MuscleGroup("Calves",     R.drawable.r_calves_0,         false)
        };

        // Inflate list of MuscleGroups (shown as list of MuscleGroup.names)
        // Use adapter
        final MiniMuscleGroupAdapter miniAdapter =
                new MiniMuscleGroupAdapter(context, R.layout.list_element_mini_muscle_grp, list);
        listView.setAdapter(miniAdapter);

        // On item click, update detailed view to reflect clicked view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            MuscleGroup muscleGroup;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!detailedView.isShown()){
                    detailedView.setVisibility(view.VISIBLE);
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) listView.getLayoutParams();
                    p.setMargins(0,0,0,0);
                    listView.requestLayout();
                }

                muscleGroup = list[position];
                name.setText(muscleGroup.getName());
                image.setImageResource(muscleGroup.getImage());
                if(muscleGroup.isFront()){
                    bodyImage.setImageResource(R.drawable.f_body);
                } else {
                    bodyImage.setImageResource(R.drawable.r_body);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_body, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.reset:
                reset();
                return true;
            case R.id.action_body_switch:
                imageClear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayFront(){

        imageView.setImageResource(0);
        imageView.setImageResource(R.drawable.r_traps_0_200_480dp);
        viewIsFront=true;
    }

    public void displayRear(){
        imageView.setImageResource(0);
        viewIsFront=false;
    }

    public void switchViews(){
        if(viewIsFront) {
            displayRear();
        } else {
            displayFront();
        }
    }


    public void imageClear(){

    }

    public void reset(){

    }
}

