package pablo.workoutapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Pablo on 1/9/2016.
 */
public class WorkoutDatabaseUser {
    private final WorkoutDbHelper dbHelper;
    private SQLiteDatabase db;

    public GoalAccess goals = new GoalAccess();
    public ProfileAccess profiles = new ProfileAccess();
    public HistoryAccess histories = new HistoryAccess();

    public WorkoutDatabaseUser(Context context){
        dbHelper = new WorkoutDbHelper(context);
    }

    public class GoalAccess {
        private String TABLE_NAME = DbContract.GoalEntry.TABLE_NAME;

        public GoalAccess(){}

        public void objectToRow(WorkoutGoal workoutGoal){
            // Create new Goal row in DB
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

            open();
            db.insert(DbContract.GoalEntry.TABLE_NAME,
                    DbContract.GoalEntry.COLUMN_NAME_ID,
                    values);
            close();
        }
        public WorkoutGoal[] forProfile(WorkoutProfile workoutProfile){
            // Instantiate list of workoutGoals
            ArrayList<WorkoutGoal> workoutGoals = new ArrayList<>();
            WorkoutGoal workoutGoal;

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
            String sortOrder = DbContract.GoalEntry.COLUMN_NAME_START_DATE + " ASC";

            // Create Cursor from database query
            open();
            Cursor cursor = db.query(
                    DbContract.GoalEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            // Move cursor to first Goal result
            // Avoid endless loop if no results, return empty list
            if(!cursor.moveToFirst()){
                WorkoutGoal[] goals = new WorkoutGoal[workoutGoals.size()];
                workoutGoals.toArray(goals);
                return goals;
            }

            // Move cursor before first Goal result
            cursor.moveToPrevious();

            while(cursor.moveToNext()){
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

                WorkoutGoal.Builder workoutGoalBuilder = new WorkoutGoal.Builder();
                workoutGoal = workoutGoalBuilder.setId(id)
                                                .setCurrent(currentVal)
                                                .setTarget(targetVal)
                                                .setStart(startVal)
                                                .setEndDate(endDate)
                                                .setStartDate(startDate)
                                                .setGoalType(goalType)
                                                .setLiftType(liftType)
                                                .setLiftReps(liftReps)
                                                .setProfile(workoutProfile)
                                                .create();
                workoutGoals.add(workoutGoal);
            }

            cursor.close();
            close();

            WorkoutGoal[] goals = new WorkoutGoal[workoutGoals.size()];
            workoutGoals.toArray(goals);
            return goals;
        }
        public boolean profileHasGoals(WorkoutProfile workoutProfile) {
            // ======================= Get Goals ========================
            // Columns to get
            String[] projection = {
                    DbContract.GoalEntry.COLUMN_NAME_PROFILE,
            };

            // Column in WHERE clause
            String selection = DbContract.GoalEntry.COLUMN_NAME_PROFILE + "=?";

            // Value in WHERE clause
            String[] selectionArgs = {workoutProfile.getId().toString()};

            open();
            // Create Cursor from database query
            Cursor cursor = db.query(
                    DbContract.GoalEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            // Move cursor to first (only) Goal result
            cursor.moveToFirst();

            // Return boolean representing whether cursor has >0 objects
            boolean returnBool = !cursor.isAfterLast();
            cursor.close();
            close();

            return returnBool;
        }
        public int insert(String idColumn, ContentValues values){
            // Inserts new row into database and returns its ID
            // NOTE: Assumes that ID is AUTOINCREMENT NOT NULL
            // ============ Insert new row into table =============
            open();
            db.insert(TABLE_NAME, idColumn, values);

            // =========== Get ID of new Goal row in DB ===========
            // Columns to get
            String[] projection = new String[]{idColumn};

            // Sorting criteria
            String sortOrder = idColumn + " DESC";

            // Recycle old cursor and create new one
            Cursor cursor = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );
            CursorHelper cursorHelper = new CursorHelper(cursor);

            // Move cursor to first (largest) ID
            cursor.moveToFirst();

            int id = cursorHelper.getInt(idColumn);
            cursor.close();
            close();

            return id;
        }
    }
    public class ProfileAccess {
        private String TABLE_NAME = DbContract.ProfileEntry.TABLE_NAME;

        public ProfileAccess(){}

        public WorkoutProfile getCurrent() {
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
            open();
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

            // Instantiate WorkoutProfile from row
            WorkoutProfile workoutProfile = rowToObject(cursor);

            cursor.close();
            close();

            return workoutProfile;
        }
        public ArrayList<String> getAllNames() {
            // ======================= Get Profile ========================
            // Columns to get
            String[] projection = {
                    DbContract.ProfileEntry.COLUMN_NAME_NAME,
            };

            // Sorting criteria
            String sortOrder = DbContract.ProfileEntry.COLUMN_NAME_CURRENT + " DESC";

            // Create Cursor from database query
            open();
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

            cursor.close();
            close();

            return list;
        }
        public WorkoutProfile[] getAll() {
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

            // Sorting criteria
            String sortOrder = DbContract.ProfileEntry.COLUMN_NAME_CURRENT + " DESC" + ", "
                             + DbContract.ProfileEntry.COLUMN_NAME_LAST_EDITED + " DESC";

            // Create Cursor from database query
            open();
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
            final ArrayList<WorkoutProfile> list = new ArrayList<>();
            while(!cursor.isAfterLast()) {
                // Instantiate WorkoutProfile from each row
                list.add(rowToObject(cursor));
                cursor.moveToNext();
            }

            cursor.close();
            close();

            WorkoutProfile[] allProfiles = new WorkoutProfile[list.size()];
            list.toArray(allProfiles);
            return allProfiles;
        }
        public void setCurrent(String newCurrentProfileName) {
            // Update last current profile to not current
            ContentValues values = new ContentValues();
            values.put(DbContract.ProfileEntry.COLUMN_NAME_CURRENT, 0);
            String selection = DbContract.ProfileEntry.COLUMN_NAME_CURRENT + " LIKE ?";
            String[] selectionArgs = { "1" };

            open();
            db.update(DbContract.ProfileEntry.TABLE_NAME, values, selection, selectionArgs);

            // New value for current
            values.clear();
            values.put(DbContract.ProfileEntry.COLUMN_NAME_CURRENT, 1);
            selection = DbContract.ProfileEntry.COLUMN_NAME_NAME + " LIKE ?";
            selectionArgs = new String[]{ newCurrentProfileName };

            db.update(DbContract.ProfileEntry.TABLE_NAME, values, selection, selectionArgs);
            close();
        }
        public void addNew(String newProfileName) {
            // Instantiate new Profile and History
            WorkoutProfile newProfile = new WorkoutProfile(newProfileName);
            WorkoutHistory newHistory = newProfile.getHistory();

            // Supply values for new History row, excluding ID (autoincrement)
            ContentValues values = new ContentValues();

            // Create new History row in DB, return its ID value
            int historyId = histories.insert(DbContract.HistoryEntry.COLUMN_NAME_ID, values);

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
            open();
            db.insert(DbContract.ProfileEntry.TABLE_NAME, DbContract.ProfileEntry.COLUMN_NAME_HISTORY, values);
            close();
        }
        public boolean delete(int deleteProfileId) {
            open();
            boolean returnVal = db.delete(DbContract.ProfileEntry.TABLE_NAME,
                    DbContract.ProfileEntry.COLUMN_NAME_ID + "=" + deleteProfileId, null) > 0;
            close();

            return returnVal;
        }
        private WorkoutProfile rowToObject(Cursor cursor){
            CursorHelper cursorHelper = new CursorHelper(cursor);

            // Instantiate profile from database information
            String profileName = cursorHelper.getString(DbContract.ProfileEntry.COLUMN_NAME_NAME);
            WorkoutProfile workoutProfile = new WorkoutProfile(profileName);
            workoutProfile.setId(cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_ID));
            workoutProfile.setImage(cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_IMAGE));
            workoutProfile.setLevel(cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_LEVEL));
            workoutProfile.setExperience(cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_EXPERIENCE));
            workoutProfile.setLastEdited(cursorHelper.getDateTime(DbContract.ProfileEntry.COLUMN_NAME_LAST_EDITED));

            // ======================= Get History ========================
            Integer historyID = cursorHelper.getInt(DbContract.ProfileEntry.COLUMN_NAME_HISTORY);

            // Columns to get
            String[] projection = new String[]{DbContract.HistoryEntry.COLUMN_NAME_ID};

            // Column in WHERE clause
            String selection = DbContract.HistoryEntry.COLUMN_NAME_ID + "=?";

            // Value in WHERE clause
            String[] selectionArgs = new String[] { historyID.toString() };

            // Create new cursor

            open();
            Cursor historyCursor = db.query(
                    DbContract.HistoryEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            cursorHelper = new CursorHelper(historyCursor);

            // Move cursor to first (only) History result
            historyCursor.moveToFirst();

            // Instantiate new WorkoutHistory from db query
            WorkoutHistory currentHistory = new WorkoutHistory();
            currentHistory.setHistoryID(cursorHelper.getInt(DbContract.HistoryEntry.COLUMN_NAME_ID));

            // Set history of profile
            workoutProfile.setHistory(currentHistory);

            historyCursor.close();
            close();

            return workoutProfile;
        }
        public int insert(String idColumn, ContentValues values){
            // Inserts new row into database and returns its ID
            // NOTE: Assumes that ID is AUTOINCREMENT NOT NULL

            // Insert new row into table
            open();
            db.insert(TABLE_NAME, idColumn, values);

            // =========== Get ID of new History row in DB ===========
            // Columns to get
            String[] projection = new String[]{idColumn};

            // Sorting criteria
            String sortOrder = idColumn + " DESC";

            // Recycle old cursor and create new one
            Cursor cursor = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );
            CursorHelper cursorHelper = new CursorHelper(cursor);

            // Move cursor to first (largest) ID
            cursor.moveToFirst();

            int id = cursorHelper.getInt(idColumn);
            cursor.close();
            close();

            return id;
        }
    }
    public class HistoryAccess {
        private String TABLE_NAME = DbContract.HistoryEntry.TABLE_NAME;

        public HistoryAccess(){}

        public int insert(String idColumn, ContentValues values){
            // Inserts new row into database and returns its ID
            // NOTE: Assumes that ID is AUTOINCREMENT NOT NULL

            // Insert new row into table
            open();
            db.insert(TABLE_NAME, idColumn, values);

            // =========== Get ID of new History row in DB ===========
            // Columns to get
            String[] projection = new String[]{idColumn};

            // Sorting criteria
            String sortOrder = idColumn + " DESC";

            // Recycle old cursor and create new one
            Cursor cursor = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );
            CursorHelper cursorHelper = new CursorHelper(cursor);

            // Move cursor to first (largest) ID
            cursor.moveToFirst();

            int id = cursorHelper.getInt(idColumn);
            cursor.close();
            close();

            return id;
        }
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }
    public void close(){
        db.close();
    }
}
