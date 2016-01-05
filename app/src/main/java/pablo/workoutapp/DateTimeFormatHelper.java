package pablo.workoutapp;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Pablo on 1/5/2016.
 */
public class DateTimeFormatHelper {
    static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");

    public static DateTime stringToDateTime(String dateStr) {
        return formatter.parseDateTime(dateStr);
    }
    public static String dateTimeToString(DateTime dateTime) {
        return formatter.print(dateTime);
    }
}
