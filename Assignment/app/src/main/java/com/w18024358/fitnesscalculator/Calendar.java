package com.w18024358.fitnesscalculator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

//Was used as a Dialog but changed to be an Activity to get setOnDateChangeListener to work correctly
public class Calendar extends AppCompatActivity
{
    //TODO Crashes if just change date with no data in any list
    //Don't like doing this but necessary
    String userSelectedDate = null;
    String previouslySelectedDate = "no date";

    //Breakfast
    ArrayList<FoodItem> breakfastFoodItems;
    ListView breakfastListView;
    FoodItemListAdapter breakfastAdapter;

    //Works but I need to think about how does it know which list data to load
    //How does it know what date was also stores
    //I think instead of saving a string off all the dates, I actually need to save a HashMap (maybe one for each list) that will hold the date saved and the list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        breakfastFoodItems = new ArrayList<>();
        breakfastListView = findViewById(R.id.calendarBreakfastListView);
        breakfastAdapter = new FoodItemListAdapter(this, breakfastFoodItems);
        breakfastListView.setAdapter(breakfastAdapter);

        getCalendarView().setOnDateChangeListener((calendarView, i, i1, i2) -> {
            String date = i2 + "/" + (i1 + 1) + "/" + i;
            Log.i("onSelectedDayChange: ", "Date: " + date);
            userSelectedDate = date;
            openSelectedDate();
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
            if(!previouslySelectedDate.equals(userSelectedDate))
            {
                previouslySelectedDate = userSelectedDate;

                Gson gson = new Gson();
                String dateSaved = sharedPreferences.getString("AllDatesSaved", "");

                //No data saved yet but doesn't catch all errors
                if(dateSaved == null) return;

                //It contains the dates but now need to find out which list has saved data
                if (dateSaved.contains(userSelectedDate))
                {
                    //Getting the HashMap that contains all dates with data saved then getting HashMap that stored the size of lists
                    String breakfastData = sharedPreferences.getString("Breakfast List All Saved Data", "");
                    String breakfastInfo = sharedPreferences.getString("Breakfast List All Info", "");

                    //Turning back into HashMap to check the keys contain the selected date
                    Type type = new TypeToken<HashMap<String, String>>() {}.getType();
                    HashMap<String, String> breakfast = gson.fromJson(breakfastData, type);

                    Type type2 = new TypeToken<HashMap<String, Integer>>() {}.getType();
                    HashMap<String, Integer> breakfastInfoMap = gson.fromJson(breakfastInfo, type2);

                    //User could have saved data i.e., set amount of calories but no lists can be stored so will crash
                    if(breakfastData == null || breakfastInfo == null || breakfast == null) return;

                    if(breakfast.containsKey(userSelectedDate))
                    {
                        //Making sure the message is not displayed (i.e., the user selected a non valid date first then selected a valid date)
                        getMessage().setVisibility(View.INVISIBLE);

                        Log.i("The map", String.valueOf(breakfast));

                        String item = breakfast.get(userSelectedDate).replaceAll(",", " ").replaceAll("\\p{Punct}", "");
                        Log.i("Data", item);

                        //This would split up all the data
                        String[] foodItem = item.split(" ");

                        int count = 0;
                        int size = Objects.requireNonNull(breakfastInfoMap.get(userSelectedDate));

                        //Showing the lists
                        getBreakfastListViewHeader().setVisibility(View.VISIBLE);
                        getBreakfastListView().setVisibility(View.VISIBLE);

                        for (int j = 0; j < size; j++)
                        {
                            breakfastFoodItems.add(new FoodItem(foodItem[count], foodItem[count + 1], foodItem[count + 2]));
                            count += 3;
                        }
                        breakfastAdapter.notifyDataSetChanged();
                    }// end breakfast.contains()
                }//end dataSaved.contains()
                else
                {
                    getMessage().setText("No available data on this selected date");
                    getMessage().setVisibility(View.VISIBLE);

                    //Hiding the list
                    getBreakfastListViewHeader().setVisibility(View.INVISIBLE);
                    getBreakfastListView().setVisibility(View.INVISIBLE);

                    //Removing the data if there is any
                    if(breakfastFoodItems.size() != 0)
                        breakfastFoodItems.clear();
                }//end else
            }//end previouslySelectedDate
        }//end calorieActivity()
        else if(fitnessActivity())
        {

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

    private Utility getUtility() { return new Utility(); }
    private CalendarView getCalendarView() { return findViewById(R.id.calendarCalendarView); }
    private TextView getMessage() { return findViewById(R.id.calendarDataMessage); }
    private TextView getBreakfastListViewHeader() { return findViewById(R.id.calendarBreakfastHeader); }
    private ListView getBreakfastListView() { return findViewById(R.id.calendarBreakfastListView); }
    private TextView getLunchListViewHeader() { return findViewById(R.id.calendarLunchHeader); }
    private ListView getLunchListView() { return findViewById(R.id.calendarLunchListView); }
    private TextView getDinnerListViewHeader() { return findViewById(R.id.calendarDinnerListView); }
    private ListView getDinnerListView() { return findViewById(R.id.calendarDinnerListView); }
    private TextView getSnacksListViewHeader() { return findViewById(R.id.calendarSnacksHeader); }
    private ListView getSnacksListView() { return findViewById(R.id.calendarSnacksListView); }
    private Button getCancelButton() { return findViewById(R.id.calendarCancelButton); }
}