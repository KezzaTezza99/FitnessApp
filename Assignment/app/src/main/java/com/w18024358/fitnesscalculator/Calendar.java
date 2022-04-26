package com.w18024358.fitnesscalculator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private Button getCancelButton()
    {
        return findViewById(R.id.calendarCancelButton);
    }
    private Button getOkayButton() { return findViewById(R.id.calendarOkayButton); }
}
