package com.w18024358.fitnesscalculator;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Workouts
{
    private String workoutName;
    private String warmupSet;
    private String workingSet1, workingSet2, workingSet3;

    //Workout Name, Warmup, Working Set 1, Working Set 2, Working Set 3
    public Workouts(String workoutName, String warmupSet, String workingSet1, String workingSet2, String workingSet3) {
        this.workoutName = workoutName;
        this.warmupSet = warmupSet;
        this.workingSet1 = workingSet1;
        this.workingSet2 = workingSet2;
        this.workingSet3 = workingSet3;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public String getWarmupSet() {
        return warmupSet;
    }

    public String getWorkingSet1() {
        return workingSet1;
    }

    public String getWorkingSet2() {
        return workingSet2;
    }

    public String getWorkingSet3() {
        return workingSet3;
    }
}
