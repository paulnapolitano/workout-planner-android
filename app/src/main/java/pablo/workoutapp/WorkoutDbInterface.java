package pablo.workoutapp;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Pablo on 1/9/2016.
 */
public interface WorkoutDbInterface {
    int insert(String tableColumn, String idColumn, ContentValues values);
    void open();
    void close();
}
