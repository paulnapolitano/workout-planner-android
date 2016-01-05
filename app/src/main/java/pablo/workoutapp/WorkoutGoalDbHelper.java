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
public class WorkoutGoalDbHelper {
    public void addToDb(Context context, WorkoutGoal workoutGoal, WorkoutProfile workoutProfile){
        final WorkoutDbHelper dbHelper = new WorkoutDbHelper(context);

        // Create new Goal row in DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.GoalEntry.COLUMN_NAME_GOAL_TYPE, workoutGoal.getGoalType().number());
        values.put(DbContract.GoalEntry.COLUMN_NAME_LIFT_TYPE, workoutGoal.getLiftType().number());
        values.put(DbContract.GoalEntry.COLUMN_NAME_START_DATE,
                   DateTimeFormatHelper.dateTimeToString(workoutGoal.getStartDate()));
        values.put(DbContract.GoalEntry.COLUMN_NAME_END_DATE,
                   DateTimeFormatHelper.dateTimeToString(workoutGoal.getEndDate()));
        values.put(DbContract.GoalEntry.COLUMN_NAME_START_VALUE, workoutGoal.getStart());
        values.put(DbContract.GoalEntry.COLUMN_NAME_LIFT_REPS, workoutGoal.getLiftReps());
        values.put(DbContract.GoalEntry.COLUMN_NAME_TARGET_VALUE, workoutGoal.getTarget());
        values.put(DbContract.GoalEntry.COLUMN_NAME_CURRENT_VALUE, workoutGoal.getCurrent());
        values.put(DbContract.GoalEntry.COLUMN_NAME_PROFILE, workoutGoal.getProfile().getId());

        db.insert(DbContract.ProfileEntry.TABLE_NAME,
                DbContract.ProfileEntry.COLUMN_NAME_HISTORY,
                values);
        db.close();
    }

    public ArrayList<WorkoutGoal> getProfileGoals(Context context, WorkoutProfile workoutProfile){
        // Instantiate list of workoutGoals
        ArrayList<WorkoutGoal> workoutGoals = new ArrayList<>();
        WorkoutGoal workoutGoal;

        // Get database as readable
        final WorkoutDbHelper dbHelper = new WorkoutDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // ======================= Get Goals ========================
        // Columns to get
        String[] projection = {
                DbContract.GoalEntry.COLUMN_NAME_ID,
                DbContract.GoalEntry.COLUMN_NAME_PROFILE,
                DbContract.GoalEntry.COLUMN_NAME_GOAL_TYPE,
                DbContract.GoalEntry.COLUMN_NAME_LIFT_TYPE,
                DbContract.GoalEntry.COLUMN_NAME_LIFT_REPS,
                DbContract.GoalEntry.COLUMN_NAME_START_DATE,
                DbContract.GoalEntry.COLUMN_NAME_END_DATE,
                DbContract.GoalEntry.COLUMN_NAME_START_VALUE,
                DbContract.GoalEntry.COLUMN_NAME_CURRENT_VALUE,
                DbContract.GoalEntry.COLUMN_NAME_TARGET_VALUE
        };

        // Column in WHERE clause
        String selection = DbContract.GoalEntry.COLUMN_NAME_PROFILE + "=?";

        // Value in WHERE clause
        String[] selectionArgs = { workoutProfile.getId().toString() };

        // Sorting criteria
        String sortOrder = DbContract.GoalEntry.COLUMN_NAME_START_DATE + " DESC";

        // Create Cursor from database query
        Cursor cursor = db.query(
                DbContract.GoalEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        // Move cursor to first (only) Goal result
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            // Instantiate goal from database information and add to list
            int goalTypeInt = cursor.getInt(cursor.getColumnIndexOrThrow(
                    DbContract.GoalEntry.COLUMN_NAME_GOAL_TYPE));
            GoalType goalType = GoalType.values()[goalTypeInt];

            int liftTypeInt = cursor.getInt(cursor.getColumnIndexOrThrow(
                    DbContract.GoalEntry.COLUMN_NAME_LIFT_TYPE));
            LiftType liftType = LiftType.values()[liftTypeInt];

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(
                    DbContract.GoalEntry.COLUMN_NAME_ID));
            int liftReps = cursor.getInt(cursor.getColumnIndexOrThrow(
                    DbContract.GoalEntry.COLUMN_NAME_LIFT_REPS));
            int startVal = cursor.getInt(cursor.getColumnIndexOrThrow(
                    DbContract.GoalEntry.COLUMN_NAME_START_VALUE));
            int currentVal = cursor.getInt(cursor.getColumnIndexOrThrow(
                    DbContract.GoalEntry.COLUMN_NAME_CURRENT_VALUE));
            int targetVal = cursor.getInt(cursor.getColumnIndexOrThrow(
                    DbContract.GoalEntry.COLUMN_NAME_TARGET_VALUE));
            DateTime startDate = DateTimeFormatHelper.stringToDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(
                            DbContract.GoalEntry.COLUMN_NAME_START_DATE)));
            DateTime endDate = DateTimeFormatHelper.stringToDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(
                            DbContract.GoalEntry.COLUMN_NAME_END_DATE)));

            WorkoutGoal.Factory workoutGoalFactory = new WorkoutGoal.Factory();
            workoutGoalFactory.setId(id)
                              .setCurrent(currentVal)
                              .setTarget(targetVal)
                              .setStart(startVal)
                              .setEndDate(endDate)
                              .setStartDate(startDate)
                              .setGoalType(goalType)
                              .setLiftType(liftType)
                              .setLiftReps(liftReps)
                              .setProfile(workoutProfile);
            workoutGoal = new WorkoutGoal(workoutGoalFactory);
            workoutGoals.add(workoutGoal);
        }

        return workoutGoals;
    }
}
