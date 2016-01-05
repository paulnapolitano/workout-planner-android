package pablo.workoutapp;

import android.provider.BaseColumns;

import java.util.Date;

import pablo.workoutapp.WorkoutHistory;

/**
 * Created by Pablo on 12/27/2015.
 */
public class DbContract {
    public DbContract(){}

    public static abstract class ProfileEntry implements BaseColumns {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_NAME_ID = "profileId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_HISTORY = "history";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_LEVEL = "level";
        public static final String COLUMN_NAME_EXPERIENCE = "experience";
        public static final String COLUMN_NAME_LAST_EDITED = "lastEdited";
        public static final String COLUMN_NAME_CURRENT = "current";
    }

    public static abstract class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_ID = "historyId";
    }

    public static abstract class GoalEntry implements BaseColumns {
        public static final String TABLE_NAME = "goal";
        public static final String COLUMN_NAME_ID = "goalId";
        public static final String COLUMN_NAME_PROFILE = "profile";
        public static final String COLUMN_NAME_GOAL_TYPE = "goalType";
        public static final String COLUMN_NAME_LIFT_TYPE = "liftType";
        public static final String COLUMN_NAME_LIFT_REPS = "liftReps";
        public static final String COLUMN_NAME_START_DATE = "startDate";
        public static final String COLUMN_NAME_END_DATE = "endDate";
        public static final String COLUMN_NAME_START_VALUE = "startValue";
        public static final String COLUMN_NAME_CURRENT_VALUE = "currentValue";
        public static final String COLUMN_NAME_TARGET_VALUE = "targetValue";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTOINCREMENT = " AUTOINCREMENT NOT NULL";
    public static final String SQL_CREATE_PROFILE_TABLE =
            "CREATE TABLE " + ProfileEntry.TABLE_NAME + " (" +
            ProfileEntry.COLUMN_NAME_ID + INT_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            ProfileEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            ProfileEntry.COLUMN_NAME_HISTORY + INT_TYPE + COMMA_SEP +
            ProfileEntry.COLUMN_NAME_EXPERIENCE + INT_TYPE + COMMA_SEP +
            ProfileEntry.COLUMN_NAME_LEVEL + INT_TYPE + COMMA_SEP +
            ProfileEntry.COLUMN_NAME_IMAGE + INT_TYPE + COMMA_SEP +
            ProfileEntry.COLUMN_NAME_LAST_EDITED + TEXT_TYPE + COMMA_SEP +
            ProfileEntry.COLUMN_NAME_CURRENT + INT_TYPE + COMMA_SEP +
            "FOREIGN KEY(" + ProfileEntry.COLUMN_NAME_HISTORY + ") REFERENCES " +
            HistoryEntry.TABLE_NAME + "(historyId)" +
            " )";

    public static final String SQL_CREATE_HISTORY_TABLE =
            "CREATE TABLE " + HistoryEntry.TABLE_NAME + " (" +
            HistoryEntry.COLUMN_NAME_ID + INT_TYPE + PRIMARY_KEY + AUTOINCREMENT +
            " )";

    public static final String SQL_CREATE_GOAL_TABLE =
            "CREATE TABLE " + GoalEntry.TABLE_NAME + " (" +
            GoalEntry.COLUMN_NAME_ID + INT_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
            GoalEntry.COLUMN_NAME_PROFILE + INT_TYPE + COMMA_SEP +
            GoalEntry.COLUMN_NAME_GOAL_TYPE + INT_TYPE + COMMA_SEP +
            GoalEntry.COLUMN_NAME_LIFT_TYPE + INT_TYPE + COMMA_SEP +
            GoalEntry.COLUMN_NAME_LIFT_REPS + INT_TYPE + COMMA_SEP +
            GoalEntry.COLUMN_NAME_START_DATE + TEXT_TYPE + COMMA_SEP +
            GoalEntry.COLUMN_NAME_END_DATE + TEXT_TYPE + COMMA_SEP +
            GoalEntry.COLUMN_NAME_START_VALUE + INT_TYPE + COMMA_SEP +
            GoalEntry.COLUMN_NAME_CURRENT_VALUE + INT_TYPE + COMMA_SEP +
            GoalEntry.COLUMN_NAME_TARGET_VALUE + INT_TYPE + COMMA_SEP +
            "FOREIGN KEY(" + GoalEntry.COLUMN_NAME_PROFILE + ") REFERENCES " +
            ProfileEntry.TABLE_NAME + "(profileId)" +
            " )";

    public static final String SQL_DELETE_PROFILE_TABLE = "DROP TABLE IF EXISTS " +
                                                          ProfileEntry.TABLE_NAME;
    public static final String SQL_DELETE_HISTORY_TABLE = "DROP TABLE IF EXISTS " +
                                                          HistoryEntry.TABLE_NAME;
    public static final String SQL_DELETE_GOAL_TABLE = "DROP TABLE IF EXISTS " +
                                                       GoalEntry.TABLE_NAME;

}
