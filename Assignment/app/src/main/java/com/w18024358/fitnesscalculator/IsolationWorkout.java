package com.w18024358.fitnesscalculator;

public class IsolationWorkout
{
    private final String workoutName1;
    private final String workoutName2;
    private final String workoutName3;
    private final String set1;
    private final String set2;
    private final String set3;
    private final String set4;
    private final String set5;

    public IsolationWorkout(String workoutName1, String workoutName2, String workoutName3, String set1, String set2, String set3, String set4, String set5) {
        this.workoutName1 = workoutName1;
        this.workoutName2 = workoutName2;
        this.workoutName3 = workoutName3;
        this.set1 = set1;
        this.set2 = set2;
        this.set3 = set3;
        this.set4 = set4;
        this.set5 = set5;
    }

    public String getWorkoutName1() {
        return workoutName1;
    }

    public String getWorkoutName2() {
        return workoutName2;
    }

    public String getWorkoutName3() {
        return workoutName3;
    }

    public String getSet1() {
        return set1;
    }

    public String getSet2() {
        return set2;
    }

    public String getSet3() {
        return set3;
    }

    public String getSet4() {
        return set4;
    }

    public String getSet5() {
        return set5;
    }
}
