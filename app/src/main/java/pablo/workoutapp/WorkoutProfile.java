package pablo.workoutapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pablo on 12/26/2015.
 */
public class WorkoutProfile implements Parcelable{
    private static final String TAG = "WorkoutProfile";
    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

    private int id;
    private String name;
    private WorkoutHistory history;
    private int image;
    private int level;
    private int experience;
    private DateTime lastEdited;

    public static final Creator CREATOR = new Creator(){
        public WorkoutProfile createFromParcel(Parcel source) {
            return new WorkoutProfile(source);
        }
        public WorkoutProfile[] newArray(int size) {
            return new WorkoutProfile[size];
        }
    };

    public WorkoutProfile(String name){
        this.name = name;
        this.image = R.drawable.profile_default_48dp;
        this.history = new WorkoutHistory();
        this.level = 1;
        this.experience = 0;
        this.lastEdited = new DateTime();
    }

    public WorkoutProfile(Parcel source){
        Log.v(TAG, "ParcelData(Parcel source): time to put back parcel data");
        history = (WorkoutHistory) source.readParcelable(WorkoutHistory.class.getClassLoader());
        this.name = source.readString();
        this.image = source.readInt();
        this.level = source.readInt();
        this.experience = source.readInt();
        this.lastEdited = DateTimeFormatHelper.stringToDateTime(source.readString());
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v(TAG, "writeToParcel..." + flags);
        dest.writeParcelable(history, flags);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(image);
        dest.writeInt(level);
        dest.writeInt(experience);
        dest.writeString(formatter.print(lastEdited));
    }

    public Integer getId()             { return id; }
    public String getName()            { return name; }
    public int getImage()              { return image; }
    public int getLevel()              { return level; }
    public int getExperience()         { return experience; }
    public String getLastEdited()      { return formatter.print(lastEdited); }
    public WorkoutHistory getHistory() { return history; }

    public void setId(Integer id)                 { this.id = id; }
    public void setName(String name)              { this.name = name; }
    public void setImage(int image)               { this.image = image; }
    public void setLevel(int level)               { this.level = level; }
    public void setExperience(int experience)     { this.experience = experience; }
    public void setLastEdited(DateTime lastEdited){ this.lastEdited = lastEdited; }
    public void setLastEdited(String lastEdited) {
        this.lastEdited = DateTimeFormatHelper.stringToDateTime(lastEdited);
    }
    public void setHistory(WorkoutHistory history){this.history = history; }

    @Override
    public String toString(){
        return "Name: " + name;
    }
}
