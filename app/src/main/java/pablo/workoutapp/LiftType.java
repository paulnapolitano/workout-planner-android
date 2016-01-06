package pablo.workoutapp;

/**
 * Created by Pablo on 1/4/2016.
 */
public enum LiftType {
    BENCH_PRESS_BARBELL (0, "Barbell Bench Press", MuscleGroupType.PECTORALS,
                                                   MuscleGroupType.DELTOIDS),
    BENCH_PRESS_DUMBBELL (1, "Dumbbell Bench Press", MuscleGroupType.PECTORALS,
                                                     MuscleGroupType.DELTOIDS),
    CURLS_DUMBBELL (2, "Dumbbell Curls", MuscleGroupType.BICEPS);

    private int number;
    private String printable;
    private MuscleGroupType[] muscleGroups;

    LiftType(int number, String printable, MuscleGroupType... muscleGroups){
        this.number = number;
        this.printable = printable;
        this.muscleGroups = muscleGroups;
    }

    public int number(){return number;}
    public String printable(){return printable;}
    public MuscleGroupType[] muscleGroups(){return muscleGroups;}

    public static LiftType fromPrintable(String printable){
        switch(printable){
            case "Barbell Bench Press":
                return BENCH_PRESS_BARBELL;
            case "Dumbbell Bench Press":
                return BENCH_PRESS_DUMBBELL;
            case "Dumbbell Curls":
                return CURLS_DUMBBELL;
            default:
                return null;
        }
    }
}
