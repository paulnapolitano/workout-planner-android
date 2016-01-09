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
        // Use current startDate as default picker date
        Bundle dateBundle = getArguments();
        int year = dateBundle.getInt("year");
        int month = dateBundle.getInt("month");
        int day = dateBundle.getInt("day");

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        listener.onStartDateSet(view, year, month, day);
    }
}
