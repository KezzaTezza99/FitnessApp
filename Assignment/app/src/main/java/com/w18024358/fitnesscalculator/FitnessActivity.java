package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FitnessActivity extends AppCompatActivity {
    static final int RETURNED_VALUES = 1;

    TextView currentDay;

    ImageView bmiButton;
    ImageView calorieButton;
    ImageView fitnessButton;

    ListView compoundMovementPrimaryListView;
    CompoundWorkoutListAdapter compoundMovementPrimaryAdapter;

    ListView compoundMovementSecondaryListView;
    CompoundWorkoutListAdapter compoundMovementSecondaryAdapter;

    ListView isolationMovementListView;
    IsolationWorkoutListAdapter isolationWorkoutListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        currentDay = findViewById(R.id.fitnessCurrentDayLabel);

        //
        bmiButton = findViewById(R.id.fitnessBMIButton);
        calorieButton = findViewById(R.id.fitnessCalorieButton);
        fitnessButton = findViewById(R.id.fitnessFitnessButton);

        bmiButton.setOnClickListener(view -> openBMI());
        calorieButton.setOnClickListener(view -> openCalorie());
        fitnessButton.setOnClickListener(view -> openFitness());

        //Getting the current day -- TODO Utility
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        Date date = calendar.getTime();
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        currentDay.setText(today);

        currentDay.setOnLongClickListener(view -> {
            openCalendar();
            return true;
        });

        //THIS ACTUALLY WORKS BUT I NEED TO GET THE ACTUAL JSON INFORMATION
        //AND THEN FORMAT IT NICELY
        //FINALLY GET WEIGHT FROM THE USER PROFILE PAGE IF INCLUDED OTHERWISE NOT SPECIFY?
//        ArrayList<Workouts> tst = new ArrayList();
//        tst.add(new Workouts("Bench", "5x5 ", "5x5 ", "5x5", "Triceps ", "5x5 "));
//        compoundMovementPrimaryListView = findViewById(R.id.fitnessCompoundMovementListView1);
//        compoundMovementPrimaryAdapter = new FitnessListAdapter(this, tst);
//        compoundMovementPrimaryListView.setAdapter(compoundMovementPrimaryAdapter);

        //Doing JSON stuff etc
        //Make this a method?
        Utility utility = new Utility();
        JsonUtility jsonUtility = new JsonUtility(this);
        String data = jsonUtility.SplitWorkoutBasedOnDays(jsonUtility.json,  utility.getCurrentDate());
        String[] str = jsonUtility.SplitTheData(data);
        WorkoutUtil workoutUtil = new WorkoutUtil(str, jsonUtility.getWorkout());
        //_end

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

    private void openBMI()
    {
        Intent intent = new Intent(this, BMIActivity.class);
        startActivity(intent);
    }

    private void openCalorie()
    {
        Intent intent = new Intent(this, CalorieActivity.class);
        startActivity(intent);
    }

    private void openFitness()
    {
        Intent intent = new Intent(this, FitnessActivity.class);
        startActivity(intent);
    }

    private void openCalendar()
    {
        Intent intent = new Intent(this, Calendar.class);
        intent.putExtra("ActivityID", "Fitness");
        startActivityForResult(intent, RETURNED_VALUES);
    }

    private Utility getUtility()
    {
        return new Utility();
    }

    private JsonUtility getJSONUtility()
    {
        return new JsonUtility(this);
    }
}