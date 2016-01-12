package pablo.workoutapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Pablo on 1/11/2016.
 */
public class WorkoutGoalPagerFragment extends Fragment {
    // TODO: Limit number of queries made here

    /**
     * The number of pages (wizard steps) to show.
     */
    private int numPages = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager pager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    WorkoutGoal[] workoutGoals;
    Button backButton;
    Button forwardButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_workout_goal_pager, container, false);
        Context context = layoutView.getContext();

        WorkoutDatabaseUser dbUser = new WorkoutDatabaseUser(context);
        WorkoutProfile currentProfile = dbUser.profiles.getCurrent();
        workoutGoals = dbUser.goals.forProfile(currentProfile);
        numPages = workoutGoals.length;

        // Instantiate a ViewPager and a PagerAdapter.
        pager = (ViewPager) layoutView.findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new WorkoutGoalPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);

        // Buttons
        backButton = (Button) layoutView.findViewById(R.id.back_button);
        forwardButton = (Button) layoutView.findViewById(R.id.forward_button);

        // Back button starts invisible
        backButton.setVisibility(View.INVISIBLE);
        if (numPages <= 1){ forwardButton.setVisibility(View.INVISIBLE); }

        backButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                pager.setCurrentItem(getItem(-1), true);
                updateButtonVisibility();
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                pager.setCurrentItem(getItem(+1), true);
                updateButtonVisibility();
            }
        });

        return layoutView;
    }

    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }

    private void updateButtonVisibility() {
        int currentItem = pager.getCurrentItem();
        if(currentItem == 0) {
            backButton.setVisibility(View.INVISIBLE);
            forwardButton.setVisibility(View.VISIBLE);
        }
        if (currentItem == numPages - 1) {
            backButton.setVisibility(View.VISIBLE);
            forwardButton.setVisibility(View.INVISIBLE);
        } else if(currentItem != 0){
            backButton.setVisibility(View.VISIBLE);
            forwardButton.setVisibility(View.VISIBLE);
        }
    }

    private class WorkoutGoalPagerAdapter extends FragmentStatePagerAdapter {
        public WorkoutGoalPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WorkoutGoalFragment.init(workoutGoals[position]);
        }

        @Override
        public int getCount() {
            return numPages;
        }
    }
}
