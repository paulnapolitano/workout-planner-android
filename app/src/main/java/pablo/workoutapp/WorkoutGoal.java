package pablo.workoutapp;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by Pablo on 1/4/2016.
 */
public class WorkoutGoal {
    private final String TAG = "WorkoutGoal";

    private int id;
    private GoalType goal = null;
    private LiftType lift = null;
    private DateTime startDate;
    private DateTime endDate;
    private WorkoutProfile profile;
    private int liftReps = 0;
    private int start;
    private int current;
    private int target;

    public WorkoutGoal(Factory factory){
        this.id        = factory.id;
        this.goal      = factory.goal;
        this.lift      = factory.lift;
        this.liftReps  = factory.liftReps;
        this.startDate = factory.startDate;
        this.endDate   = factory.endDate;
        this.start     = factory.start;
        this.current   = factory.current;
        this.target    = factory.target;
        this.profile   = factory.profile;
    }

    @Override
    public String toString(){
        String returnString;
        switch(goal){
            case LOSE_WEIGHT:
                returnString = String.format("Lose %d lb in %d days",
                        (start - target), Days.daysBetween(startDate, endDate).getDays());
            case GAIN_WEIGHT:
                returnString = String.format("Gain %d lb in %d days",
                        (target - start), Days.daysBetween(startDate, endDate).getDays());
            case GAIN_STRENGTH:
                returnString = String.format("Add %d lb to %s %dRM",
                        (target - start), lift.printable(), liftReps);
            default:
                returnString = "None";
        }

        return returnString;
    }

    public int getId()                 { return id; }
    public GoalType getGoalType()      { return goal; }
    public LiftType getLiftType()      { return lift; }
    public int getLiftReps()           { return liftReps; }
    public DateTime getStartDate()     { return startDate; }
    public DateTime getEndDate()       { return endDate; }
    public WorkoutProfile getProfile() { return profile; }
    public int getStart()              { return start; }
    public int getCurrent()            { return current; }
    public int getTarget()             { return target; }


    public static class Factory {
        private int id;
        private GoalType goal;
        private LiftType lift;
        private DateTime startDate;
        private DateTime endDate;
        private WorkoutProfile profile;
        private int liftReps;
        private int start;
        private int current;
        private int target;

        public Factory(){
            this.goal = null;
            this.lift = null;
        }

        public Factory setGoalType(GoalType goal){
            this.goal = goal;
            return this;
        }
        public Factory setLiftType(LiftType lift){
            this.lift = lift;
            return this;
        }
        public Factory setLiftReps(int liftReps){
            this.liftReps = liftReps;
            return this;
        }
        public Factory setStartDate(DateTime startDate){
            this.startDate = startDate;
            return this;
        }
        public Factory setEndDate(DateTime endDate){
            this.endDate = endDate;
            return this;
        }
        public Factory setStart(int start){
            this.start = start;
            return this;
        }
        public Factory setCurrent(int current){
            this.current = current;
            return this;
        }
        public Factory setTarget(int target){
            this.target = target;
            return this;
        }
        public Factory setProfile(WorkoutProfile profile){
            this.profile = profile;
            return this;
        }
        public Factory setId(int id){
            this.id = id;
            return this;
        }

        public GoalType       getGoalType(){ return this.goal; }
        public LiftType       getLiftType(){ return this.lift; }
        public int            getLiftReps(){ return this.liftReps; }
        public DateTime       getStartDate(){ return this.startDate; }
        public DateTime       getEndDate(){ return this.endDate; }
        public int            getStart(){ return this.start; }
        public int            getCurrent(){ return this.current; }
        public int            getTarget(){ return this.target; }
        public WorkoutProfile getProfile(){ return this.profile; }
        public int            getId(){ return this.id; }
    }
}
