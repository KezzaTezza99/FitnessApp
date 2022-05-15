package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class FitnessActivity extends AppCompatActivity {
    //Todo sort calories out on here - predict how much each activity would burn?
    //add the ability to open the lists - then mark complete? (this could then update calories burnt?
    //maybe start the workout by having a timer that takes up the space at the bottom???
    //TODO Change the weights used for exercises if the user has inputted values from UserProfile, if not just show the workout instead?

    //TODO -- New bug always says there is no workout info even if there is
    //Used to tell the intent that
    static final int RETURNED_VALUES = 1;

    //The three list views (primary / secondary) compound movement exercise and isolation exercises
    ListView compoundMovementPrimaryListView, compoundMovementSecondaryListView, isolationMovementListView;
    //The three ArrayAdapters for the corresponding ArrayList<CompoundWorkout>
    CompoundWorkoutListAdapter compoundMovementPrimaryAdapter, compoundMovementSecondaryAdapter;
    //Array Adapter for ArrayList<IsolationWorkout>
    IsolationWorkoutListAdapter isolationWorkoutListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        //Creating this here otherwise doesn't work as intended
        JsonUtility jsonUtility = new JsonUtility(this);

        //Setting the current day
        getCurrentDay().setText(getUtility().getCurrentDate());

        //Setting a LongClickListener - user holds on the day and it'll open the calendar activity
        getCurrentDay().setOnLongClickListener(view -> {
            openCalendar();
            return true;
        });


        //Doing JSON stuff - Basically reading the file into data then splitting the data on selected date (current day) i.e., get Thursday's workout
        String data = jsonUtility.splitWorkoutBasedOnDays(jsonUtility.getJSON(), getUtility().getCurrentDate());
        String[] str = jsonUtility.splitTheData(data);
        //Sending the data of today's workout to WorkoutUtil which is responsible for reading the JSON and transforming it into ArrayList<T> (T being Compound or Isolation Workout)
        WorkoutUtil workoutUtil = new WorkoutUtil(str, jsonUtility.getWorkout());

        //Seeing if theres is a workout
        boolean isWorkout = jsonUtility.getWorkout();

        //There wasn't a workout for today so need to hide the ListView(s)
        if (!isWorkout) {
            //Show the message but hide the Lists
            getRestDayMessage().setVisibility(View.VISIBLE);
            getCompoundMovementLabel().setVisibility(View.INVISIBLE);
            getIsolationMovementLabel().setVisibility(View.INVISIBLE);
            getCompoundMovementPrimaryListView().setVisibility(View.INVISIBLE);
            getCompoundMovementSecondaryListView().setVisibility(View.INVISIBLE);
            getIsolationMovementListView().setVisibility(View.INVISIBLE);
        } else
        {
            //Primary Exercise
            ArrayList<CompoundWorkout> primaryWorkout = new ArrayList<>();
            primaryWorkout.add(new CompoundWorkout(workoutUtil.getPrimaryCompoundExerciseName(), workoutUtil.getWarmup(), workoutUtil.getPrimaryWorkingSet1(),
                    workoutUtil.getPrimaryWorkingSet2(), workoutUtil.getPrimaryWorkingSet3()));
            compoundMovementPrimaryListView = findViewById(R.id.fitnessCompoundMovementListView1);
            compoundMovementPrimaryAdapter = new CompoundWorkoutListAdapter(this, primaryWorkout, true);
            compoundMovementPrimaryListView.setAdapter(compoundMovementPrimaryAdapter);

            //Secondary Exercise
            ArrayList<CompoundWorkout> secondaryWorkout = new ArrayList<>();
            secondaryWorkout.add(new CompoundWorkout(workoutUtil.getSecondaryCompoundExerciseName(), workoutUtil.getWarmup(), workoutUtil.getSecondaryWorkingSet1(),
                    workoutUtil.getSecondaryWorkingSet2(), workoutUtil.getSecondaryWorkingSet3()));
            compoundMovementSecondaryListView = findViewById(R.id.fitnessCompoundMovementListView2);
            compoundMovementSecondaryAdapter = new CompoundWorkoutListAdapter(this, secondaryWorkout, false);
            compoundMovementSecondaryListView.setAdapter(compoundMovementSecondaryAdapter);

            //Isolation Exercises
            ArrayList<IsolationWorkout> isolationWorkout = new ArrayList<>();
            isolationWorkout.add(new IsolationWorkout(workoutUtil.getIsolationExercise1(), workoutUtil.getIsolationExercise2(), workoutUtil.getIsolationExercise3(),
                    workoutUtil.getIsolationWorkingSet1(), workoutUtil.getIsolationWorkingSet2(), workoutUtil.getIsolationWorkingSet3(), workoutUtil.getIsolationWorkingSet4(), workoutUtil.getIsolationWorkingSet5()));
            isolationMovementListView = findViewById(R.id.fitnessIsolationMovementListView1);
            isolationWorkoutListAdapter = new IsolationWorkoutListAdapter(this, isolationWorkout);
            isolationMovementListView.setAdapter(isolationWorkoutListAdapter);
        }

        // Main Navigation (bottom nav bar)
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainBottomNavigationMenu);
        bottomNavigationView.setSelectedItemId(R.id.fitnessActivityMenu);

        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.bmiActivityMenu:
                    intent = new Intent(this, BMIActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.calorieActivityMenu:
                    intent = new Intent(this, CalorieActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.fitnessActivityMenu:
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
        });

        //Side Bar Navigation (Pulls out using right swipe gesture)
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        //The strings that I didn't pass to constructor provide accessibility for blind people should implement (reads the string values out)
        ActionBarDrawerToggle sideNavigationMenu = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);

        //Can now use the menu
        sideNavigationMenu.syncState();

        ///Setting on click listener which will allow me to respond to nav selections
        NavigationView sideNavView = findViewById(R.id.sideNavMenu);
        sideNavView.setNavigationItemSelectedListener(item ->
        {
            //Responding to the navigation buttons
            switch (item.getItemId()) {
                case R.id.profilePage:
                    startActivity(new Intent(this, UserProfileActivity.class));
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
        });
    }

    //The user has long held on today's day, need to open the Calendar
    private void openCalendar() {
        Intent intent = new Intent(this, Calendar.class);
        intent.putExtra("ActivityID", "Fitness");
        startActivityForResult(intent, RETURNED_VALUES);
    }

    //Helper Methods
    private TextView getCurrentDay() { return findViewById(R.id.fitnessCurrentDayLabel); }
    private TextView getCompoundMovementLabel() { return findViewById(R.id.fitnessCompoundMovementLabel); }
    private TextView getIsolationMovementLabel() { return findViewById(R.id.fitnessIsolationMovementLabel); }
    private ListView getCompoundMovementPrimaryListView() { return findViewById(R.id.fitnessCompoundMovementListView1); }
    private ListView getCompoundMovementSecondaryListView() { return findViewById(R.id.fitnessCompoundMovementListView2); }
    private ListView getIsolationMovementListView() { return findViewById(R.id.fitnessIsolationMovementListView1); }
    private TextView getRestDayMessage()
    {
        return findViewById(R.id.fitnessRestDayMessage);
    }
    private Utility getUtility()
    {
        return new Utility();
    }
}