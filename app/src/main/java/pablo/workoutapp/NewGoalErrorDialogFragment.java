package pablo.workoutapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Pablo on 1/12/2016.
 */
public class NewGoalErrorDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle args = getArguments();
        WorkoutGoal failedGoal = WorkoutGoal.fromBundle(args);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(!failedGoal.validValues() && !failedGoal.validDates()) {
            builder.setMessage("Error: values and dates invalid");
        } else if(!failedGoal.validValues()) {
            builder.setMessage("Error: values invalid");
        } else if(!failedGoal.validDates()) {
            builder.setMessage("Error: dates invalid");
        } else {
            builder.setMessage("Error: unknown");
        }

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               });
        return builder.create();
    }

    public static NewGoalErrorDialogFragment newInstance(WorkoutGoal.Builder workoutGoalBuilder){
        NewGoalErrorDialogFragment f = new NewGoalErrorDialogFragment();

        Bundle args = workoutGoalBuilder.setProfile(new WorkoutProfile("test")).create().toBundle();
        f.setArguments(args);

        return f;
    }
}
