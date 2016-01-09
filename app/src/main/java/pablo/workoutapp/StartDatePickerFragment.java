package pablo.workoutapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import org.joda.time.DateTime;

public class StartDatePickerFragment extends DialogFragment
                                     implements DatePickerDialog.OnDateSetListener {
    private final String TAG = "fragment_edit_start_date";

    private DatePickerFragmentListener listener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            listener = (DatePickerFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement OnDateSetListener");
        }
    }

    public StartDatePickerFragment(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use current date as default picker date
        final DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        listener.onStartDateSet(view, year, month, day);
    }
}
