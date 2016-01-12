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

import java.util.ArrayList;

/**
 * Created by Pablo on 1/11/2016.
 */
public class WorkoutGoalPagerFragment extends Fragment {
    // TODO: Limit number of queries made here
    // FIXME: 1/12/2016 PagerAdapter shows duplicates unless new fragments added to END of its list

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

    Button backButton;
    Button forwardButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("WorkoutGoalPagerFragment.onCreateView()");

        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_workout_goal_pager, container, false);
        Context context = layoutView.getContext();

        WorkoutDatabaseUser dbUser = new WorkoutDatabaseUser(context);
        WorkoutProfile currentProfile = dbUser.profiles.getCurrent();

        WorkoutGoal[] workoutGoals = dbUser.goals.forProfile(currentProfile);
        numPages = workoutGoals.length;

        // Instantiate a ViewPager and a PagerAdapter.
        pager = (ViewPager) layoutView.findViewById(R.id.pager);
        pager.setCurrentItem(0);

        pagerAdapter = new WorkoutGoalPagerAdapter(getChildFragmentManager(), workoutGoals);
        pager.setAdapter(pagerAdapter);
        updateGoals();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateButtonVisibility();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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

    private void updateGoals(){
        pagerAdapter.notifyDataSetChanged();
    }

    private class WorkoutGoalPagerAdapter extends FragmentStatePagerAdapter {
        private WorkoutGoal[] workoutGoals;

        public WorkoutGoalPagerAdapter(FragmentManager fm, WorkoutGoal[] workoutGoals) {
            super(fm);
            this.workoutGoals = workoutGoals;
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println("GETTING ITEM");
            return WorkoutGoalFragment.newInstance(workoutGoals[position]);
        }

        public int getItemPosition(Object item) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return numPages;
        }
    }
}
