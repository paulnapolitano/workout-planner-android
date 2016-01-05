package pablo.workoutapp;

/**
 * Created by Pablo on 1/4/2016.
 */
public enum GoalType {
    LOSE_WEIGHT   (0, "Lose Weight"),
    GAIN_WEIGHT   (1, "Gain Weight"),
    GAIN_STRENGTH (2, "Gain Strength"),
    GAIN_STAMINA  (3, "Gain Stamina"),
    GAIN_SPEED    (4, "Gain Speed");

    private String printable;
    private int number;

    GoalType(int number, String printable){
        this.number = number;
        this.printable = printable;
    }

    public int number(){return number;}
    public String printable(){return printable;}
}
