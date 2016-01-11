package pablo.workoutapp;

/**
 * Created by Pablo on 1/4/2016.
 */
public enum GoalType {
    LOSE_WEIGHT   (0, "Lose Weight"),
    GAIN_WEIGHT   (1, "Gain Weight"),
    GAIN_STRENGTH (2, "Gain Strength"),
    GAIN_STAMINA  (3, "Gain Stamina");
//    GAIN_SPEED    (4, "Gain Speed");

    private String printable;
    private int number;

    GoalType(int number, String printable){
        this.number = number;
        this.printable = printable;
    }

    public int number(){return number;}
    public String printable(){return printable;}

    public static GoalType fromPrintable(String printable){
        switch(printable){
            case "Lose Weight":
                return LOSE_WEIGHT;
            case "Gain Weight":
                return GAIN_WEIGHT;
            case "Gain Strength":
                return GAIN_STRENGTH;
            case "Gain Stamina":
                return GAIN_STAMINA;
//            case "Gain Speed":
//                return GAIN_SPEED;
            default:
                return null;
        }
    }
}
