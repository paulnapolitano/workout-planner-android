package pablo.workoutapp;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.joda.time.DateTime;

import java.lang.reflect.Field;

/**
 * Created by Pablo on 1/5/2016.
 */
public class CursorHelper {
    private final Cursor cursor;

    public CursorHelper(Cursor cursor){
        this.cursor = cursor;
    }

    public int getInt(String columnName){
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }
    public String getString(String columnName){
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }
    public DateTime getDateTime(String columnName){
        return DateTimeFormatHelper.stringToDateTime(getString(columnName));
    }
}
