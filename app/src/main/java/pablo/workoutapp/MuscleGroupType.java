package pablo.workoutapp;

/**
 * Created by Pablo on 1/4/2016.
 */
public enum MuscleGroupType {
    PECTORALS  (0, "Pectorals"),
    DELTOIDS   (1, "Deltoids"),
    BICEPS     (2, "Biceps"),
    FOREARMS   (3, "Forearms"),
    ABDOMINALS (4, "Abdominals"),
    QUADRICEPS (5, "Quadriceps"),
    TRICEPS    (6, "Triceps"),
    TRAPS      (7, "Traps"),
    LATS       (8, "Lats"),
    HAMSTRINGS (9, "Hamstrings"),
    CALVES     (10,"Calves");


    private int number;
    private String printable;

    MuscleGroupType(int number, String printable){
        this.number=number;
        this.printable=printable;
    }
}
