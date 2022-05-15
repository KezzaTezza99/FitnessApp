package com.w18024358.fitnesscalculator;
import android.content.Context;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

//A class that provides me with Utility Functions for reading and parsing JSON files
public class JsonUtility extends AppCompatActivity
{
    Context myContext;
    String json;                                        //Entire JSON File
    String selectedData;                                //Holds the workout data for selected day only
    boolean workout;

    public JsonUtility(Context myContext)
    {
        this.myContext = myContext;
        parseJSONFile();
    }

    //Parse a JSON File
    void parseJSONFile()
    {
        json = null;

        try
        {
            //Getting the file to parse
            InputStream inputStream = myContext.getAssets().open("workouts.json");
            //Getting the size
            int size = inputStream.available();
            //Allocating a buffer
            byte[] buffer = new byte[size];
            //Reading and closing the file
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        Log.i("JSON", json);
        setJSON(json);
    }

    void setJSON(String JSON)
    {
        this.json = JSON;
    }
    String getJSON()
    {
        return json;
    }

    //Read the JSON String to get the info for each day
    String splitWorkoutBasedOnDays(String data, String day)
    {
        //TODO could refactor this
        int index;
        int end;
        if(day.equals("Wednesday") || day.equals("Sunday"))
        {
            setWorkout(false);
            index = data.indexOf("NA");
            end = data.indexOf("}", index);
            selectedData = data.substring(index, end);
            Log.i("Selected Day with No Workout", selectedData);
        }
        else if(day.equals("Monday") || day.equals("Tuesday") || day.equals("Thursday") || day.equals("Friday"))
        {
            setWorkout(true);
            index = data.indexOf(day);
            end = data.indexOf(getTheNextDay(day));
            selectedData = data.substring(index, end);
            Log.i("Selected Day", selectedData);
        }
        else
        {
            //Incorrect shouldn't reach here, error handling
            setWorkout(false);
            index = data.indexOf("NA");
            end = data.indexOf("}", index);
            selectedData = data.substring(index, end);
        }
        return selectedData;
    }

    //Gets the next day which basically just stops reading the file once it reaches the return value
    String getTheNextDay(String currentDate)
    {
        switch(currentDate)
        {
            case "Monday":
                return "Tuesday";
            case "Tuesday":
                return "Thursday";
            case "Thursday":
                return  "Friday";
            case "Friday":
                return "Saturday";
            case "Saturday":
                return "NA";
            default:
                //There is a mistake in the code somewhere as this shouldn't be reached (i.e., calling this method without using the getCurrentCate() method)
                //Don't want to crash the application instead just return no workout
                return "NA";
        }
    }

    //Break the data into 5 sections (Exercise name, warmup set etc.,)
    String[] splitTheData(String data)
    {
        Log.i("Split the Data", "Inside the method");

        String[] usefulData;
        int compoundIndex;

        if(!getWorkout())
        {
            usefulData = new String[1];
            //Compound Workout name (Rest Day)
            compoundIndex = data.indexOf("No Workout");
            int compoundEnd = data.indexOf("]", compoundIndex);
            String workoutName = data.substring(compoundIndex + 15, compoundEnd - 1);
            usefulData[0] = workoutName;
            Log.i("Workout name no workout", workoutName);
        }
        else
        {

            usefulData = new String[6];

            //Compound Workout name
            compoundIndex = data.indexOf("Compound");
            int compoundEnd = data.indexOf("]", compoundIndex);
            String workoutName = data.substring(compoundIndex + 12, compoundEnd);
            usefulData[0] = workoutName;
            Log.i("Workout name", workoutName);

            //Warmup Information
            int warmupIndex = data.indexOf("Warmup");
            int warmupEnd = data.indexOf(",", warmupIndex);
            String warmupInformation = data.substring(warmupIndex + 9, warmupEnd);
            usefulData[1] = warmupInformation;
            Log.i("Warmup info", warmupInformation);

            //Working Set Primary
            int primaryIndex = data.indexOf("Primary");
            int primaryEnd = data.indexOf("]", primaryIndex);
            String primaryInfo = data.substring(primaryIndex + 11, primaryEnd);             //(11 gets rid of some useless information)
            usefulData[2] = primaryInfo;
            Log.i("Primary Working Set", primaryInfo);

            //Working Set Secondary
            int secondaryIndex = data.indexOf("Secondary");
            int secondaryEnd = data.indexOf("]", secondaryIndex);
            String secondaryInfo = data.substring(secondaryIndex + 13, secondaryEnd);       //+13 removes the words "Secondary:" from the string
            usefulData[3] = secondaryInfo;
            Log.i("Secondary Working Set", secondaryInfo);

            //Isolation Workout Info
            int isolationIndex = data.indexOf("Isolation");
            int isolationEnd = data.indexOf("]", isolationIndex);
            String isolationInfo = data.substring(isolationIndex + 13, isolationEnd);       //+13 removes the word Key in the JSON as its not needed
            usefulData[4] = isolationInfo;
            Log.i("Isolation Info", isolationInfo);

            //Isolation Set Info
            int isolationSetIndex = data.indexOf("Working", isolationEnd);
            int isolationSetEnd = data.indexOf("]", isolationSetIndex);
            String isolationSetInfo = data.substring(isolationSetIndex + 15, isolationSetEnd);
            usefulData[5] = isolationSetInfo;
            Log.i("Isolation Set", isolationSetInfo);

        }
        return usefulData;
    }

    //Helper methods
    void setWorkout(boolean value) { workout = value; }
    boolean getWorkout()
    {
        return workout;
    }
}
