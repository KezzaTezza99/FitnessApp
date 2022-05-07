package com.w18024358.fitnesscalculator;
import android.util.Log;

//Class will take the workout information provided by the JSON Util and then
//break the information up to work with the FitnessActivity ListViews
public class WorkoutUtil
{
    String primaryCompoundExerciseName, secondaryCompoundExerciseName;
    String warmup;
    String primaryWorkingSet1, primaryWorkingSet2, primaryWorkingSet3;
    String secondaryWorkingSet1, secondaryWorkingSet2, secondaryWorkingSet3;

    String isolationExercise1, isolationExercise2, isolationExercise3;
    String isolationWorkingSet1, isolationWorkingSet2, isolationWorkingSet3, isolationWorkingSet4, isolationWorkingSet5;

    public WorkoutUtil(String[] workoutInfo, boolean isWorkout)
    {
        //Basically cleaning the data, removing all the unnecessary information such as punctuation
        if(isWorkout)
        {
            //String should include information about a workout so getting only the relevant information
            int compoundWorkoutNameIndex = workoutInfo[0].indexOf(",");
            primaryCompoundExerciseName = workoutInfo[0].substring(1, compoundWorkoutNameIndex - 1);
            secondaryCompoundExerciseName = workoutInfo[0].substring(compoundWorkoutNameIndex + 3, workoutInfo[0].length() - 1);
            Log.i("Exercise Names", primaryCompoundExerciseName + " -- " + secondaryCompoundExerciseName);

            //Warmup set information - removing the " " from string
            warmup = workoutInfo[1].replaceAll("\\p{Punct}", "");
            Log.i("Warmup", warmup);

            //Getting the primary working set this is stored as 5, 5, 5 e.g., but this will need to be three separate values for ListView
            //Removing all punctuation to get just the values and will also remove whitespace (should always be three values)
            String primaryWorkingSet = workoutInfo[2].replaceAll("\\p{Punct}", "").replaceAll("\\p{javaWhitespace}", "");
            primaryWorkingSet1 = primaryWorkingSet.substring(0, 1);
            primaryWorkingSet2 = primaryWorkingSet.substring(1, 2);
            primaryWorkingSet3 = primaryWorkingSet.substring(2, 3);
            Log.i("Primary", " \n" + primaryWorkingSet1 + "\n" + primaryWorkingSet2 + "\n" + primaryWorkingSet3);

            //Doing the same as above but for the secondary working set which is stored in the same format just different values
            //Primary working set also stores single digit values i.e., 5 but secondary stores two double digit values i.e., 12 and 10 then one single digit i.e., 5
            String secondaryWorkingSet = workoutInfo[3].replaceAll("\\p{Punct}", "").replaceAll("\\p{javaWhitespace}", "");
            secondaryWorkingSet1 = secondaryWorkingSet.substring(0, 2);
            secondaryWorkingSet2 = secondaryWorkingSet.substring(2, 4);
            secondaryWorkingSet3 = secondaryWorkingSet.substring(4, 5);
            Log.i("Secondary", " \n" + secondaryWorkingSet1 + "\n" + secondaryWorkingSet2 + "\n" + secondaryWorkingSet3);

            //Getting the Names of the isolation movements, every workout session includes 3 isolation movements
            //First removing all the quotation marks from the string to make it easier to get just the necessary information
            String isolationMovements = workoutInfo[4].replaceAll("\"", "");
            //A comma separates all of the names of the isolation movements, getting that index to substring each movement to its own string
            int commaIndex = isolationMovements.indexOf(",");
            int lastCommaIndex = isolationMovements.lastIndexOf(",");
            isolationExercise1 = isolationMovements.substring(0, commaIndex);
            isolationExercise2 = isolationMovements.substring(commaIndex + 2, lastCommaIndex);
            isolationExercise3 = isolationMovements.substring(lastCommaIndex + 2, isolationMovements.length() - 1);
            Log.i("Isolation", " \n" + isolationExercise1 + "\n" + isolationExercise2 + "\n" + isolationExercise3);

            //Getting the isolation movement sets stored similarly as before but stores 5 total values, 3 two digit values 2 single digit values
            String isolationWorkingSet = workoutInfo[5].replaceAll("\\p{Punct}", "").replaceAll("\\p{javaWhitespace}", "");
            isolationWorkingSet1 = isolationWorkingSet.substring(0, 2);
            isolationWorkingSet2 = isolationWorkingSet.substring(2, 4);
            isolationWorkingSet3 = isolationWorkingSet.substring(4, 6);
            isolationWorkingSet4 = isolationWorkingSet.substring(6, 7);
            isolationWorkingSet5 = isolationWorkingSet.substring(7, 8);
            Log.i("Isolation Working Set", " \n" + isolationWorkingSet1 + "\n" + isolationWorkingSet2  + "\n" + isolationWorkingSet3 + "\n"
                    + isolationWorkingSet4 + "\n" +  isolationWorkingSet5);
        }
        else
        {
            //There is no workout today
            primaryCompoundExerciseName = workoutInfo[0];
            Log.i("Exercise Name", primaryCompoundExerciseName);
        }
    }

    //Getter methods
    public String getPrimaryCompoundExerciseName() {
        return primaryCompoundExerciseName;
    }
    public String getSecondaryCompoundExerciseName() {
        return secondaryCompoundExerciseName;
    }
    //TODO do this differently based on if the user has entered weights or not?????
    public String getWarmup() {
        return warmup + " at ";
    }
    public String getPrimaryWorkingSet1() {
        return primaryWorkingSet1 + " reps at ";
    }
    public String getPrimaryWorkingSet2() {
        return primaryWorkingSet2 + " reps at ";
    }
    public String getPrimaryWorkingSet3() {
        return primaryWorkingSet3 + " reps at ";
    }
    public String getSecondaryWorkingSet1() {
        return secondaryWorkingSet1 + " reps at ";
    }
    public String getSecondaryWorkingSet2() {
        return secondaryWorkingSet2 + " reps at ";
    }
    public String getSecondaryWorkingSet3() {
        return secondaryWorkingSet3 + " reps at ";
    }
    public String getIsolationExercise1() {
        return isolationExercise1;
    }
    public String getIsolationExercise2() {
        return isolationExercise2;
    }
    public String getIsolationExercise3() {
        return isolationExercise3;
    }
    public String getIsolationWorkingSet1() {
        return isolationWorkingSet1;
    }
    public String getIsolationWorkingSet2() {
        return isolationWorkingSet2;
    }
    public String getIsolationWorkingSet3() {
        return isolationWorkingSet3;
    }
    public String getIsolationWorkingSet4() {
        return isolationWorkingSet4;
    }
    public String getIsolationWorkingSet5() {
        return isolationWorkingSet5;
    }
}