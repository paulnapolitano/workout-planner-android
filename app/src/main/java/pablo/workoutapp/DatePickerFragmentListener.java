package pablo.workoutapp;

import android.widget.DatePicker;

/**
 * Created by Pablo on 1/9/2016.
 */
public interface DatePickerFragmentListener {
    void onStartDateSet(DatePicker view, int year, int month, int day);
    void onEndDateSet(DatePicker view, int year, int month, int day);
}
