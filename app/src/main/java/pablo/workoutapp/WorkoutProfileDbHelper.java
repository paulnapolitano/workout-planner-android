package pablo.workoutapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;

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
                DbContract.ProfileEntry.COLUMN_NAME_ID,
                DbContract.ProfileEntry.COLUMN_NAME_NAME,
                DbContract.ProfileEntry.COLUMN_NAME_HISTORY,
                DbContract.ProfileEntry.COLUMN_NAME_IMAGE,
                DbContract.ProfileEntry.COLUMN_NAME_LEVEL,
                DbContract.ProfileEntry.COLUMN_NAME_EXPERIENCE,
                DbContract.ProfileEntry.COLUMN_NAME_LAST_EDITED,
                DbContract.ProfileEntry.COLUMN_NAME_CURRENT
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

        // Create CursorHelper to neaten cursor calls
        CursorHelper cursorHelper = new CursorHelper(cursor);

        // Move cursor to first (only) Profile result
        cursor.moveToFirst();

        // Instantiate profile from database information
        String profileName = cursorHelper.getString(DbContract.ProfileEntry.COLUMN_NAME_NAME);
        WorkoutProfile currentProfile = new WorkoutProfile(profileName);
        currentProfile.setId(cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_ID));
        currentProfile.setImage(cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_IMAGE));
        currentProfile.setLevel(cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_LEVEL));
        currentProfile.setExperience(cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_EXPERIENCE));
        currentProfile.setLastEdited(cursorHelper.getDateTime(DbContract.ProfileEntry.COLUMN_NAME_LAST_EDITED));

        // ======================= Get History ========================
        Integer historyID = cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_HISTORY);

        // Columns to get
        projection = new String[]{DbContract.HistoryEntry.COLUMN_NAME_ID};

        // Column in WHERE clause
        selection = DbContract.HistoryEntry.COLUMN_NAME_ID + "=?";

        // Value in WHERE clause
        selectionArgs = new String[] { historyID.toString() };

        // Recycle old cursor and create new one
        cursor.close();
        cursor = db.query(
                DbContract.HistoryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursorHelper = new CursorHelper(cursor);

        // Move cursor to first (only) History result
        cursor.moveToFirst();

        // Instantiate new WorkoutHistory from db query
        WorkoutHistory currentHistory = new WorkoutHistory();
        currentHistory.setHistoryID(cursorHelper.getInt(DbContract.HistoryEntry.COLUMN_NAME_ID));

        // Set history of profile
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
        // Open database as readable
        final WorkoutDbHelper dbHelper = new WorkoutDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Update last current profile to not current
        ContentValues values = new ContentValues();
        values.put(DbContract.ProfileEntry.COLUMN_NAME_CURRENT, 0);
        String selection = DbContract.ProfileEntry.COLUMN_NAME_CURRENT + " LIKE ?";
        String[] selectionArgs = { "1" };
        db.update(DbContract.ProfileEntry.TABLE_NAME, values, selection, selectionArgs);

        // New value for current
        values.clear();
        values.put(DbContract.ProfileEntry.COLUMN_NAME_CURRENT, 1);
        selection = DbContract.ProfileEntry.COLUMN_NAME_NAME + " LIKE ?";
        selectionArgs = new String[]{ newCurrentProfileName };
        db.update(DbContract.ProfileEntry.TABLE_NAME, values, selection, selectionArgs);

        // Close database to prevent leaks
        db.close();
    }

    public static void addNewProfile(Context context, String newProfileName) {
        // Instantiate new Profile and History
        WorkoutProfile newProfile = new WorkoutProfile(newProfileName);
        WorkoutHistory newHistory = newProfile.getHistory();

        // Open database as writable
        final WorkoutDbHelper dbHelper = new WorkoutDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Supply values for new History row, excluding ID (autoincrement)
        ContentValues values = new ContentValues();

        // Create new History row in DB, return its ID value
        int historyId = insertAndId(context,
                                    DbContract.HistoryEntry.TABLE_NAME,
                                    DbContract.HistoryEntry.COLUMN_NAME_ID,
                                    values);

        // Supply values for new Profile row, excluding ID (autoincrement)
        values.clear();
        values.put(DbContract.ProfileEntry.COLUMN_NAME_NAME, newProfile.getName());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_HISTORY, historyId);
        values.put(DbContract.ProfileEntry.COLUMN_NAME_EXPERIENCE, newProfile.getExperience());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_LEVEL, newProfile.getLevel());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_IMAGE, newProfile.getImage());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_LAST_EDITED, newProfile.getLastEdited());
        values.put(DbContract.ProfileEntry.COLUMN_NAME_CURRENT, 1);

        // Create new Profile row in DB
        db.insert(DbContract.ProfileEntry.TABLE_NAME, DbContract.ProfileEntry.COLUMN_NAME_HISTORY, values);

        // Close database to prevent leaks
        db.close();
    }

    public static int insertAndId(Context context, String tableName, String idColumn, ContentValues values){
        // Inserts new row into database and returns its ID
        // NOTE: Assumes that ID is AUTOINCREMENT NOT NULL
        final WorkoutDbHelper dbHelper = new WorkoutDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Insert new row into table
        db.insert(tableName, idColumn, values);

        // =========== Get ID of new History row in DB ===========
        // Columns to get
        String[] projection = new String[]{idColumn};

        // Sorting criteria
        String sortOrder = idColumn + " DESC";

        // Recycle old cursor and create new one
        Cursor cursor = db.query(
            tableName,
            projection,
            null,
            null,
            null,
            null,
            null
        );
        CursorHelper cursorHelper = new CursorHelper(cursor);

        // Move cursor to first (largest) ID
        cursor.moveToFirst();

        return cursorHelper.getInt(idColumn);
    }
}
