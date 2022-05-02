package com.w18024358.fitnesscalculator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

//Was used as a Dialog but changed to be an Activity to get setOnDateChangeListener to work correctly
public class Calendar extends AppCompatActivity
{
    //Don't like doing this but necessary
    String userSelectedDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        getCalendarView().setOnDateChangeListener((calendarView, i, i1, i2) -> {
            String date = i2 + "/" + (i1 + 1) + "/" + i;
            Log.i("onSelectedDayChange: ", "Date: " + date);
            userSelectedDate = date;


            //Test -- TODO this is actually semi working
            SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
            String dateSaved = sharedPreferences.getString("Breakfast Date Saved", "");
            Log.i("Date from SP -> ", dateSaved);
            Log.i("Date from cal ->", date);

            //TODO So it gets the data, just need to do something with it
            //Perhaps show it on this screen underneath the calendar
            //Could then make okay move calendar and only show lists?
            if(dateSaved.contains(date)) {
                //Setting and displaying the message that will inform the user that the selected data does in-fact have data
                getMessage().setText("Selected date has calorie data stored");
                getMessage().setVisibility(View.VISIBLE);

                Log.i("Inside Calendar Loading", "-------------------------------------------------------------------------");
                //Retrieving the JSON lists
                String json = sharedPreferences.getString("Breakfast List Saved Items", "");
                int breakfastSize = sharedPreferences.getInt("Breakfast List Size On Save", 0);
                Log.i("JSON:", json);
                Utility utility = new Utility();
                ArrayList<String> strings = new Gson().fromJson(json, ArrayList.class);
                Log.i("JSON ArrayList", strings.toString());

//                breakfastFoodItems.clear();
                ArrayList<FoodItem> breakfastFoodItems = new ArrayList<FoodItem>();
                breakfastFoodItems = utility.stringListToItemList(strings, breakfastFoodItems, breakfastSize);

                for (int j = 0; j < breakfastSize; j++) {
                    Log.i("Calendar", breakfastFoodItems.get(j).getItemQuantity() + " " + breakfastFoodItems.get(j).getItemName() + " " + breakfastFoodItems.get(j).getItemCalories());
                    Toast.makeText(this, "Data: " + breakfastFoodItems.get(j).getItemQuantity() + " " + breakfastFoodItems.get(j).getItemName() + " " + breakfastFoodItems.get(j).getItemCalories(), Toast.LENGTH_SHORT).show();
                }
                Log.i("Finished Calendar Loading", "-------------------------------------------------------------------------");
            }
            else
            {
                //Informing the user that the selected date has no stored data
                getMessage().setText("Selected date has no calorie data stored");
                getMessage().setVisibility(View.VISIBLE);
                Log.i("Calendar Info", "Unfortunately no data saved");
            }
        });

        Intent inboundIntent = getIntent();

        if(inboundIntent != null)
        {
            if(calorieActivity())
            {
                Toast.makeText(this, "Calories", Toast.LENGTH_SHORT).show();
            }
            if(fitnessActivity())
            {
                Toast.makeText(this, "Fitness", Toast.LENGTH_SHORT).show();
            }
        }

        getCancelButton().setOnClickListener(view -> goBack());
        getOkayButton().setOnClickListener(view -> openSelectedDate());
    }

    private void goBack()
    {
        Intent intent;

        if(calorieActivity())
        {
            intent = new Intent(this, CalorieActivity.class);
        }
        else
        {
            intent = new Intent(this, FitnessActivity.class);
        }
        finishActivity(Activity.RESULT_CANCELED);
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    //TODO redo this to work with new functionality
    private void openSelectedDate()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        //User has selected a date now check if the user is from calorie or fitness page
        if(calorieActivity())
        {
            //Check if that date has an entry
            String listOfDates = sharedPreferences.getString("AllDates", "");
            //As the user could check when only used the app for a date need to check for this edge case
            String currentDateEntry = sharedPreferences.getString("CurrentDate", "");

            if(!listOfDates.isEmpty() && listOfDates.contains(userSelectedDate))
            {
                Log.i("OpenSelectedDate()", "Selected date: " + userSelectedDate);
            }
            if(currentDateEntry.contains(getUtility().getCurrentDateNumerical()))
            {
                Log.i("OpenSelectedDate()", "Selected today idiot");
                goBack();
            }
        }
    }

    private boolean calorieActivity()
    {
        String intent = getIntent().getStringExtra("ActivityID");
        return intent.equals("Calorie");
    }

    private boolean fitnessActivity()
    {
        String intent = getIntent().getStringExtra("ActivityID");
        return intent.equals("Fitness");
    }

    private Utility getUtility()
    {
        return new Utility();
    }

    private CalendarView getCalendarView()
    {
        return findViewById(R.id.calendarCalendarView);
    }
    private TextView getMessage() { return findViewById(R.id.calendarDataMessage); }
    private Button getCancelButton()
    {
        return findViewById(R.id.calendarCancelButton);
    }
    private Button getOkayButton() { return findViewById(R.id.calendarOkayButton); }
}
