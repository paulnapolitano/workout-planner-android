package pablo.workoutapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Pablo on 1/5/2016.
 */
public class WorkoutProfileDbHelper {
    public static WorkoutProfile getCurrentProfile(Context context) {
        // Get database as readable
        final WorkoutDbHelper dbHelper = new WorkoutDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // ======================= Get Profile ========================
        // Columns to get
        String[] projection = {
                DbContract.ProfileEntry.COLUMN_NAME_NAME,
                DbContract.ProfileEntry.COLUMN_NAME_CURRENT,
                DbContract.ProfileEntry.COLUMN_NAME_EXPERIENCE,
                DbContract.ProfileEntry.COLUMN_NAME_HISTORY,
                DbContract.ProfileEntry.COLUMN_NAME_IMAGE,
                DbContract.ProfileEntry.COLUMN_NAME_LAST_EDITED,
                DbContract.ProfileEntry.COLUMN_NAME_LEVEL
        };

        // Column in WHERE clause
        String selection = DbContract.ProfileEntry.COLUMN_NAME_CURRENT + "=?";

        // Value in WHERE clause
        String[] selectionArgs = {"1"};

        // Sorting criteria
        String sortOrder = DbContract.ProfileEntry.COLUMN_NAME_CURRENT + " DESC";

        // Create Cursor from database query
        Cursor cursor = db.query(
                DbContract.ProfileEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        // Move cursor to first (only) Profile result
        cursor.moveToFirst();

        // Instantiate profile from database information
        String profileName = cursor.getString(cursor.getColumnIndexOrThrow(
                DbContract.ProfileEntry.COLUMN_NAME_NAME));
        WorkoutProfile currentProfile = new WorkoutProfile(profileName);
        currentProfile.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(
                DbContract.ProfileEntry.COLUMN_NAME_LEVEL)));
        currentProfile.setExperience(cursor.getInt(cursor.getColumnIndexOrThrow(
                DbContract.ProfileEntry.COLUMN_NAME_EXPERIENCE)));
        currentProfile.setImage(cursor.getInt(cursor.getColumnIndexOrThrow(
                DbContract.ProfileEntry.COLUMN_NAME_IMAGE)));
        currentProfile.setLastEdited(cursor.getString(cursor.getColumnIndexOrThrow(
                DbContract.ProfileEntry.COLUMN_NAME_LAST_EDITED)));
        currentProfile.setId(cursor.getInt(cursor.getColumnIndexOrThrow(
                DbContract.ProfileEntry.COLUMN_NAME_ID)));

        // ======================= Get History ========================
        int historyID = cursor.getInt(cursor.getColumnIndexOrThrow(
                DbContract.ProfileEntry.COLUMN_NAME_HISTORY));

        // Columns to get
        projection = new String[]{DbContract.HistoryEntry.COLUMN_NAME_ID};

        // Recycle old cursor and create new one
        cursor.close();
        cursor = db.query(
                DbContract.HistoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Move cursor to first (only) History result
        cursor.moveToFirst();

        WorkoutHistory currentHistory = new WorkoutHistory();
        currentHistory.setHistoryID(cursor.getInt(cursor.getColumnIndexOrThrow(
                DbContract.HistoryEntry.COLUMN_NAME_ID)));
        currentProfile.setHistory(currentHistory);
        cursor.close();

        return currentProfile;
    }

    public static ArrayList<String> getAllProfileNames(Context context) {
        // Get database as readable
        final WorkoutDbHelper dbHelper = new WorkoutDbHelper(context);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        // ======================= Get Profile ========================
        // Columns to get
        String[] projection = {
                DbContract.ProfileEntry.COLUMN_NAME_NAME,
        };

        // Sorting criteria
        String sortOrder = DbContract.ProfileEntry.COLUMN_NAME_CURRENT + " DESC";

        // Create Cursor from database query
        Cursor cursor = db.query(
                DbContract.ProfileEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        // Move cursor to first (only) Profile result
        cursor.moveToFirst();

        // Instantiate profile list from database information
        String profileName;
        final ArrayList<String> list = new ArrayList<>();
        while(!cursor.isAfterLast()) {
            profileName = cursor.getString(cursor.getColumnIndexOrThrow(
                    DbContract.ProfileEntry.COLUMN_NAME_NAME));
            list.add(profileName);

            cursor.moveToNext();
        }

        db.close();
        return list;
    }

    public static void updateCurrentProfile(Context context, String newCurrentProfileName) {
        final WorkoutDbHelper dbHelper = new WorkoutDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Update last current profile to not current
        ContentValues values = new ContentValues();
        values.put(DbContract.ProfileEntry.COLUMN_NAME_CURRENT, 0);

        String selection = DbContract.ProfileEntry.COLUMN_NAME_CURRENT + " LIKE ?";
        String[] selectionArgs = { "1" };
        db.update(
                DbContract.ProfileEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        // New value for current
        values.clear();
        values.put(DbContract.ProfileEntry.COLUMN_NAME_CURRENT, 1);

        selection = DbContract.ProfileEntry.COLUMN_NAME_NAME + " LIKE ?";
        selectionArgs = new String[]{ newCurrentProfileName };

        db.update(
                DbContract.ProfileEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
    }

    public static void addNewProfile(Context context, String newProfileName) {
        final WorkoutDbHelper dbHelper = new WorkoutDbHelper(context);
        WorkoutProfile newProfile = new WorkoutProfile(newProfileName);
        WorkoutHistory newHistory = newProfile.getHistory();

        // Create new History row in DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.insert(DbContract.HistoryEntry.TABLE_NAME,
                DbContract.HistoryEntry.COLUMN_NAME_ID,
                values);

        // Create new Profile row in DB
        values.clear();
        values.put(DbContract.ProfileEntry.COLUMN_NAME_NAME,
                newProfile.getName());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_HISTORY,
                newProfile.getHistory().getHistoryID());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_EXPERIENCE,
                newProfile.getExperience());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_LEVEL,
                newProfile.getLevel());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_IMAGE,
                newProfile.getImage());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_LAST_EDITED,
                newProfile.getLastEdited());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_CURRENT, 1);
        db.insert(DbContract.ProfileEntry.TABLE_NAME,
                DbContract.ProfileEntry.COLUMN_NAME_HISTORY,
                values);
        db.close();
    }
}
