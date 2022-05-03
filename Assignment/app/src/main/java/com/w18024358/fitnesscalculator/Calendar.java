package com.w18024358.fitnesscalculator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
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
    String lastSelectedDate = "";

    //Breakfast
    ArrayList<FoodItem> breakfastFoodItems;
    ListView breakfastListView;
    FoodItemListAdapter breakfastAdapter;

    //Lunch
    ArrayList<FoodItem> lunchFoodItems;
    ListView lunchListView;
    FoodItemListAdapter lunchAdapter;

    //Dinner
    ArrayList<FoodItem> dinnerFoodItems;
    ListView dinnerListView;
    FoodItemListAdapter dinnerAdapter;

    //Snacks
    ArrayList<FoodItem> snacksFoodItems;
    ListView snacksListView;
    FoodItemListAdapter snacksAdapter;

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

        lunchFoodItems = new ArrayList<>();
        lunchListView = findViewById(R.id.calendarLunchListView);
        lunchAdapter = new FoodItemListAdapter(this, lunchFoodItems);
        lunchListView.setAdapter(lunchAdapter);

        dinnerFoodItems = new ArrayList<>();
        dinnerListView = findViewById(R.id.calendarDinnerListView);
        dinnerAdapter = new FoodItemListAdapter(this, dinnerFoodItems);
        dinnerListView.setAdapter(dinnerAdapter);

        snacksFoodItems = new ArrayList<>();
        snacksListView = findViewById(R.id.calendarSnacksListView);
        snacksAdapter = new FoodItemListAdapter(this, snacksFoodItems);
        snacksListView.setAdapter(snacksAdapter);

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
        if(!lastSelectedDate.equals(userSelectedDate))
        {
            lastSelectedDate = userSelectedDate;

            SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

            //User has selected a date now check if the user is from calorie or fitness page
            if (calorieActivity())
            {
                String dateSaved = sharedPreferences.getString("AllDatesSaved", "");
                //No data saved yet but doesn't catch all errors
                if (dateSaved == null) return;

                if (dateSaved.contains(userSelectedDate)) {
                    Gson gson = new Gson();
                    //Getting the HashMap that contains all dates with data saved then getting HashMap that stored the size of lists
                    //Breakfast - could make this all be one method, would make it cleaner - TODO Refactor
                    String breakfastData = sharedPreferences.getString("Breakfast List All Saved Data", "");
                    String breakfastInfo = sharedPreferences.getString("Breakfast List All Info", "");

                    //Lunch
                    String lunchData = sharedPreferences.getString("Lunch List All Saved Data", "");
                    String lunchInfo = sharedPreferences.getString("Lunch List All Info", "");

                    //Dinner
                    String dinnerData = sharedPreferences.getString("Dinner List All Saved Data", "");
                    String dinnerInfo = sharedPreferences.getString("Dinner List All Info", "");

                    //Snacks
                    String snacksData = sharedPreferences.getString("Snacks List All Saved Data", "");
                    String snacksInfo = sharedPreferences.getString("Snacks List All Info", "");

                    //Used to transform the two strings into the two different HashMaps needed
                    Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    Type type2 = new TypeToken<HashMap<String, Integer>>() {
                    }.getType();

                    //Turning back into HashMap to check the keys contain the selected date (Breakfast)
                    HashMap<String, String> breakfast = gson.fromJson(breakfastData, type);
                    HashMap<String, Integer> breakfastInfoMap = gson.fromJson(breakfastInfo, type2);

                    //Turning back into HashMap to check the keys contain the selected date (Lunch)
                    HashMap<String, String> lunch = gson.fromJson(lunchData, type);
                    HashMap<String, Integer> lunchInfoMap = gson.fromJson(lunchInfo, type2);

                    //Turning back into HashMap to check the keys contain the selected date (Dinner)
                    HashMap<String, String> dinner = gson.fromJson(dinnerData, type);
                    HashMap<String, Integer> dinnerInfoMap = gson.fromJson(dinnerInfo, type2);

                    //Turning back into HashMap to check the keys contain the selected date (Snacks)
                    HashMap<String, String> snacks = gson.fromJson(snacksData, type);
                    HashMap<String, Integer> snacksInfoMap = gson.fromJson(snacksInfo, type2);

                    Log.i("Breakfast Data", breakfastData);
                    //User could have saved data i.e., set amount of calories but no lists can be stored so will crash
                    if(breakfastData != null && !breakfastData.isEmpty()) {
                        Log.i("Fuck", "Why are you fucking here");
                        if (breakfast.containsKey(userSelectedDate))
                        {
                            //Making sure the message is not displayed (i.e., the user selected a non valid date first then selected a valid date)
                            getMessage().setVisibility(View.INVISIBLE);

                            Log.i("The map", String.valueOf(breakfast));

                            String item = Objects.requireNonNull(breakfast.get(userSelectedDate), "breakfast must not be null")
                                    .replaceAll("\"", "")
                                    .replaceAll("\\[", "")
                                    .replaceAll("]", "");
                            Log.i("Data", item);

                            //This would split up all the data
                            String[] foodItem = item.split(",");

                            int count = 0;
                            int size = Objects.requireNonNull(breakfastInfoMap.get(userSelectedDate), "breakfast size must not be null");

                            //Showing the lists
                            getBreakfastListViewHeader().setVisibility(View.VISIBLE);
                            getBreakfastListView().setVisibility(View.VISIBLE);

                            for (int j = 0; j < size; j++) {
                                breakfastFoodItems.add(new FoodItem(foodItem[count], foodItem[count + 1], foodItem[count + 2]));
                                count += 3;
                            }
                            breakfastAdapter.notifyDataSetChanged();
                        }// end breakfast.contains()
                    }//end breakfastData != null
                    if(lunchData != null && !lunchData.isEmpty()) {
                        if (lunch.containsKey(userSelectedDate) && lunchData != null) {
                            //This should be a method  - Keep code DRY TODO (Refactor)
                            //Making sure the message is not displayed (i.e., the user selected a non valid date first then selected a valid date)
                            getMessage().setVisibility(View.INVISIBLE);

                            Log.i("The map", String.valueOf(lunch));

                            String item = Objects.requireNonNull(lunch.get(userSelectedDate), "lunch must not be null")
                                    .replaceAll("\"", "")
                                    .replaceAll("\\[", "")
                                    .replaceAll("]", "");
                            Log.i("Data", item);

                            //This would split up all the data
                            String[] foodItem = item.split(",");

                            int count = 0;
                            int size = Objects.requireNonNull(lunchInfoMap.get(userSelectedDate), "lunch size must not be null");

                            //Showing the lists
                            getLunchListViewHeader().setVisibility(View.VISIBLE);
                            getLunchListView().setVisibility(View.VISIBLE);

                            //This could be a method too
                            for (int j = 0; j < size; j++) {
                                lunchFoodItems.add(new FoodItem(foodItem[count], foodItem[count + 1], foodItem[count + 2]));
                                count += 3;
                            }
                            lunchAdapter.notifyDataSetChanged();
                        }//end lunch.contains()
                    }//end lunch != null
                    if(dinnerData != null && !dinnerData.isEmpty()) {
                        if (dinner.containsKey(userSelectedDate) && dinnerData != null) {
                            //Making sure the message is not displayed (i.e., the user selected a non valid date first then selected a valid date)
                            getMessage().setVisibility(View.INVISIBLE);

                            Log.i("The map", String.valueOf(lunch));

                            String item = Objects.requireNonNull(dinner.get(userSelectedDate), "dinner must not be null")
                                    .replaceAll("\"", "")
                                    .replaceAll("\\[", "")
                                    .replaceAll("]", "");
                            Log.i("Data", item);

                            //This would split up all the data
                            String[] foodItem = item.split(",");

                            int count = 0;
                            int size = Objects.requireNonNull(dinnerInfoMap.get(userSelectedDate), "dinner size must not be null");

                            //Showing the lists
                            getDinnerListViewHeader().setVisibility(View.VISIBLE);
                            getDinnerListView().setVisibility(View.VISIBLE);

                            //This could be a method too
                            for (int j = 0; j < size; j++) {
                                dinnerFoodItems.add(new FoodItem(foodItem[count], foodItem[count + 1], foodItem[count + 2]));
                                count += 3;
                            }
                            dinnerAdapter.notifyDataSetChanged();
                        }//end dinner.contains()
                    }//end dinner != null
                    if(snacksData != null && !snacksData.isEmpty()) {
                        if (snacks.containsKey(userSelectedDate) && snacksData != null) {
                            //Making sure the message is not displayed (i.e., the user selected a non valid date first then selected a valid date)
                            getMessage().setVisibility(View.INVISIBLE);

                            Log.i("The map", String.valueOf(lunch));

                            String item = Objects.requireNonNull(snacks.get(userSelectedDate), "snacks must not be null")
                                    .replaceAll("\"", "")
                                    .replaceAll("\\[", "")
                                    .replaceAll("]", "");
                            Log.i("Data", item);

                            //This would split up all the data
                            String[] foodItem = item.split(",");

                            int count = 0;
                            int size = Objects.requireNonNull(snacksInfoMap.get(userSelectedDate), "snacks size must not be null");

                            //Showing the lists
                            getSnacksListViewHeader().setVisibility(View.VISIBLE);
                            getSnacksListView().setVisibility(View.VISIBLE);

                            //This could be a method too
                            for (int j = 0; j < size; j++) {
                                snacksFoodItems.add(new FoodItem(foodItem[count], foodItem[count + 1], foodItem[count + 2]));
                                count += 3;
                            }
                            snacksAdapter.notifyDataSetChanged();
                        }//end snacks.contains()
                    }//end snacksData != null
                }//end dataSaved.contains()
                else {
                    getMessage().setText("No available data on this selected date");
                    getMessage().setVisibility(View.VISIBLE);

                    //Hiding the lists
                    getBreakfastListViewHeader().setVisibility(View.INVISIBLE);
                    getBreakfastListView().setVisibility(View.INVISIBLE);
                    getLunchListViewHeader().setVisibility(View.INVISIBLE);
                    getLunchListView().setVisibility(View.INVISIBLE);
                    getDinnerListViewHeader().setVisibility(View.INVISIBLE);
                    getDinnerListView().setVisibility(View.INVISIBLE);
                    getSnacksListViewHeader().setVisibility(View.INVISIBLE);
                    getSnacksListView().setVisibility(View.INVISIBLE);

                    //Removing the data if there is any
                    if (breakfastFoodItems != null)
                        breakfastFoodItems.clear();
                    if (lunchFoodItems != null)
                        lunchFoodItems.clear();
                    if (dinnerFoodItems != null)
                        dinnerFoodItems.clear();
                    if (snacksFoodItems != null)
                        snacksFoodItems.clear();
                }//end else
            }//end calorieActivity()
        }//end !lastSelectedDate
        else if(fitnessActivity())
        {

        }//end fitnessActivity()
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
    private TextView getDinnerListViewHeader() { return findViewById(R.id.calendarDinnerHeader); }
    private ListView getDinnerListView() { return findViewById(R.id.calendarDinnerListView); }
    private TextView getSnacksListViewHeader() { return findViewById(R.id.calendarSnacksHeader); }
    private ListView getSnacksListView() { return findViewById(R.id.calendarSnacksListView); }
    private Button getCancelButton() { return findViewById(R.id.calendarCancelButton); }
}