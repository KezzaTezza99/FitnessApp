package com.w18024358.fitnesscalculator;
import android.content.Context;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;

//TODO Needs completing and refactoring already!

public class JsonUtility extends AppCompatActivity
{
    Context myContext;
    String json;                                        //Entire JSON File
    String selectedData;                                //Holds the workout data for selected day only
    String currentDay;                                  //Holds the current day
    boolean workout;

    public JsonUtility(Context myContext)
    {
        this.myContext = myContext;
        workout = true;
        ParseJSONFile();
    }

    //Parse a JSON File
    String ParseJSONFile()
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
            return null;
        }
        Log.i("JSON", json);
        return json;
    }

    //Read the JSON String to get the info for each day
    String SplitWorkoutBasedOnDays(String data, String day)
    {
        int index;
        int end;
        if(day.equals("Wednesday") || day.equals("Sunday"))
        {
            workout = false;
            index = data.indexOf("NA");
            end = data.indexOf("}", index);
            selectedData = data.substring(index, end);
            Log.i("Selected Day with No Workout", selectedData);
        }
        else
        {
            index = data.indexOf(day);
            end = data.indexOf(GetTheNextDay(day));
            selectedData = data.substring(index, end);
            Log.i("Selected Day", selectedData);
        }
        return selectedData;
    }

    String GetTheNextDay(String currentDate)
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
                throw new IllegalStateException("Unexpected value: " + currentDate);
        }
    }

    //Break the data into 5 sections (Exercise name, warmup set etc.,)
    String[] SplitTheData(String data)
    {
        //String[] usefulData = new String[data.length()];
        String[] usefulData;
        int compoundIndex;
        if(!workout)
        {
            usefulData = new String[1];
            //Compound Workout name (Rest Day)
            compoundIndex = data.indexOf("No Workout");
            int compoundEnd = data.indexOf("]", compoundIndex);
            String workoutName = data.substring(compoundIndex + 15, compoundEnd - 1);
            usefulData[0] = workoutName;
            Log.i("Workout name no workout", workoutName);

            return usefulData;
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

            return usefulData;
        }
    }

    boolean getWorkout()
    {
        return workout;
    }
}
