package pablo.workoutapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 12/27/2015.
 */
public class WorkoutDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "WorkoutApp.db";

    public WorkoutDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.SQL_CREATE_HISTORY_TABLE);
        db.execSQL(DbContract.SQL_CREATE_PROFILE_TABLE);
        db.execSQL(DbContract.SQL_CREATE_GOAL_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.SQL_DELETE_PROFILE_TABLE);
        db.execSQL(DbContract.SQL_DELETE_HISTORY_TABLE);
        db.execSQL(DbContract.SQL_DELETE_GOAL_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
