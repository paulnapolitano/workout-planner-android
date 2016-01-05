package pablo.workoutapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Pablo on 12/26/2015.
 */
public class WorkoutHistory implements Parcelable {
    private static final String TAG = "WorkoutHistory";

    private int id;

    public static Creator CREATOR = new Creator() {
        public WorkoutHistory createFromParcel(Parcel source) {
            return new WorkoutHistory(source);
        }

        public WorkoutHistory[] newArray(int size) {
            return new WorkoutHistory[size];
        }
    };

    public WorkoutHistory() {}

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v(TAG, "writeToParcel..." + flags);
    }

    public WorkoutHistory(Parcel source) {

    }

    public int getHistoryID() {
        return id;
    }

    public void setHistoryID(int id) {
        this.id = id;
    }
}

