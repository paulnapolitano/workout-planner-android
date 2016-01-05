package pablo.workoutapp;

import android.graphics.drawable.Drawable;

/**
 * Created by Pablo on 1/1/2016.
 */
public class MuscleGroup {
    private String name;
    private int image;
    private boolean isFront;

    public MuscleGroup(String name, int image){
        this.name = name;
        this.image = image;
        this.isFront = true;
    }

    public MuscleGroup(String name, int image, boolean isFront){
        this.name = name;
        this.image = image;
        this.isFront = isFront;
    }

    public boolean isFront(){ return isFront; }
    public String getName(){ return name; }
    public int getImage(){ return image; }

    public void setName(String name){ this.name = name; }
    public void setImage(int image){ this.image = image; }
    public void setFront(boolean isFront){
        this.isFront = isFront;
    }
}
