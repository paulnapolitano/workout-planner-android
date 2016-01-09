package pablo.workoutapp;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Pablo on 1/9/2016.
 */
public class WorkoutApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
