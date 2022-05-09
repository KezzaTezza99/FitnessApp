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

//TODO - BUG FOUND! If the user opens a date with saved data and then loads another day with saved data both are displayed
//- should just need to clear the lists before loading them?

//The purpose of the calendar activity is to allow the user to go back through previous dates to see information previously inputted
//as of now calendar only works with the CalorieActivity (only if data is saved)
//The code is structured to allow the calendar to expand with the FitnessActivity just not implemented yet
public class Calendar extends AppCompatActivity
{
    //User selected Date stores the date the user chooses in the calendar
    String userSelectedDate = null;
    //Last selected date is used to stop the user being able to repeatedly choose one day which would cause the list of items to duplicate N times
    String lastSelectedDate = "";

    //Need to recreate the lists so this is the same as CalorieActivity
    //The list of Food Items
    ArrayList<FoodItem> breakfastFoodItems, lunchFoodItems, dinnerFoodItems, snacksFoodItems;
    //The ListViews
    ListView breakfastListView, lunchListView, dinnerListView, snacksListView;
    //The FoodList Array Adapters
    FoodItemListAdapter breakfastAdapter, lunchAdapter, dinnerAdapter, snacksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        //Initialising all the necessary information (Breakfast)
        breakfastFoodItems = new ArrayList<>();
        breakfastListView = findViewById(R.id.calendarBreakfastListView);
        breakfastAdapter = new FoodItemListAdapter(this, breakfastFoodItems);
        breakfastListView.setAdapter(breakfastAdapter);

        //Lunch
        lunchFoodItems = new ArrayList<>();
        lunchListView = findViewById(R.id.calendarLunchListView);
        lunchAdapter = new FoodItemListAdapter(this, lunchFoodItems);
        lunchListView.setAdapter(lunchAdapter);

        //Dinner
        dinnerFoodItems = new ArrayList<>();
        dinnerListView = findViewById(R.id.calendarDinnerListView);
        dinnerAdapter = new FoodItemListAdapter(this, dinnerFoodItems);
        dinnerListView.setAdapter(dinnerAdapter);

        //Snacks
        snacksFoodItems = new ArrayList<>();
        snacksListView = findViewById(R.id.calendarSnacksListView);
        snacksAdapter = new FoodItemListAdapter(this, snacksFoodItems);
        snacksListView.setAdapter(snacksAdapter);

        //Setting an onclick listener to listen for what date the user selects
        getCalendarView().setOnDateChangeListener((calendarView, i, i1, i2) ->
        {
            String date = i2 + "/" + (i1 + 1) + "/" + i;
            Log.i("onSelectedDayChange: ", "Date: " + date);
            userSelectedDate = date;
            openSelectedDate();
        });
        getCancelButton().setOnClickListener(view -> goBack());
    }

    //User wants to go back to the previous activity
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

    //Open the selected date chosen by the user
    private void openSelectedDate()
    {
        //On the first click this will always be true so the statement is entered, which I want, when the user changes the date then the statement will then still be true
        //only false if the user selects one date i.e., 6/5/22 and then chooses the 6/5/22 again. Stops the list being shown for N amount of times the date is selected
        //which was a bug found during testing
        if(!lastSelectedDate.equals(userSelectedDate))
        {
            //Setting the last selected date to be the selected date
            lastSelectedDate = userSelectedDate;

            SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

            //User has selected a date now check if the user is from calorie or fitness page
            if (calorieActivity())
            {
                String dateSaved = sharedPreferences.getString("AllDatesSaved", "");
                //No data saved yet should return now before I crash the application!
                if (dateSaved == null) return;

                //SharedPreferences contains saved data now need to check if the date chosen has data stored for it
                if (dateSaved.contains(userSelectedDate))
                {
                    //The date has data!
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

                    //User could have saved data i.e., set amount of calories but no lists can be stored so will crash or only breakfast items or only snacks etc,.
                    //Only continue if the list has data
                    if(breakfastData != null && !breakfastData.isEmpty())
                    {
                        //Found a bug where if you go from one selected date with data and select another valid date then both sets of items will be displayed
                        //This is a fix to the problem as I am ensuring the lists are empty
                        if(!breakfastFoodItems.isEmpty())
                        {
                            breakfastFoodItems.clear();
                            breakfastAdapter.notifyDataSetChanged();
                            getBreakfastListView().setVisibility(View.INVISIBLE);
                            getBreakfastListViewHeader().setVisibility(View.INVISIBLE);
                        }
                        //The HashMap has data but not necessarily on this date (need to check again) i.e., today may only store snacks etc
                        if (breakfast.containsKey(userSelectedDate))
                        {
                            Log.i("The map", String.valueOf(breakfast));

                            //Making sure the message is not displayed (i.e., the user selected a non valid date first then selected a valid date)
                            getMessage().setVisibility(View.INVISIBLE);

                            //Replacing all of the punctuation in the map to easily get the correct items to store back into FoodItems Array
                            String item = Objects.requireNonNull(breakfast.get(userSelectedDate), "breakfast must not be null")
                                    .replaceAll("\"", "")
                                    .replaceAll("\\[", "")
                                    .replaceAll("]", "");
                            //The Data that is now cleaned from the map
                            Log.i("Data", item);

                            //This would split up all the data into single words i.e, quantity, item name, item calories
                            String[] foodItem = item.split(",");

                            int count = 0;
                            int size = Objects.requireNonNull(breakfastInfoMap.get(userSelectedDate), "breakfast size must not be null");

                            //Showing the lists
                            getBreakfastListViewHeader().setVisibility(View.VISIBLE);
                            getBreakfastListView().setVisibility(View.VISIBLE);

                            if (breakfastFoodItems != null)
                                breakfastFoodItems.clear();

                            //Looping through the size of the list
                            for (int j = 0; j < size; j++) {
                                //Adding the items back into the Breakfast<FoodItems> ArrayList
                                breakfastFoodItems.add(new FoodItem(foodItem[count], foodItem[count + 1], foodItem[count + 2]));
                                count += 3;
                            }
                            //Notifying the adapter to update the list view
                            breakfastAdapter.notifyDataSetChanged();
                        }// end breakfast.contains()
                    }//end breakfastData != null
                    //Checking that the LunchData String has data
                    if(lunchData != null && !lunchData.isEmpty())
                    {
                        if(!lunchFoodItems.isEmpty())
                        {
                            lunchFoodItems.clear();
                            lunchAdapter.notifyDataSetChanged();
                            getLunchListView().setVisibility(View.INVISIBLE);
                            getLunchListViewHeader().setVisibility(View.INVISIBLE);
                        }

                        //It has data but now need to check it has data containing to the selected date
                        if (lunch.containsKey(userSelectedDate))
                        {
                            //Todo - Make this whole thing a method or something repeating a lot of code here and I don't like
                            Log.i("Lunch map", String.valueOf(lunch));

                            //Making sure the message is not displayed (i.e., the user selected a non valid date first then selected a valid date)
                            getMessage().setVisibility(View.INVISIBLE);

                            //Again cleaning the data stored in the map to only include commas, which can then be used to index the correct words that need to be
                            //split to put back into the arraylist
                            String item = Objects.requireNonNull(lunch.get(userSelectedDate), "lunch must not be null")
                                    .replaceAll("\"", "")
                                    .replaceAll("\\[", "")
                                    .replaceAll("]", "");
                            Log.i("Lunch data", item);

                            //This would split up all the data into three words (item quantity, item name, item calories)
                            String[] foodItem = item.split(",");

                            int count = 0;
                            int size = Objects.requireNonNull(lunchInfoMap.get(userSelectedDate), "lunch size must not be null");

                            //Showing the lists
                            getLunchListViewHeader().setVisibility(View.VISIBLE);
                            getLunchListView().setVisibility(View.VISIBLE);

                            //Adding the items into the LunchFoodItems Array
                            for (int j = 0; j < size; j++) {
                                lunchFoodItems.add(new FoodItem(foodItem[count], foodItem[count + 1], foodItem[count + 2]));
                                count += 3;
                            }
                            lunchAdapter.notifyDataSetChanged();
                        }//end lunch.contains()
                    }//end lunch != null
                    //Checking to make sure that the dinner data string is not null or empty
                    if(dinnerData != null && !dinnerData.isEmpty())
                    {
                        if(!dinnerFoodItems.isEmpty())
                        {
                            dinnerFoodItems.clear();
                            dinnerAdapter.notifyDataSetChanged();
                            getDinnerListView().setVisibility(View.INVISIBLE);
                            getDinnerListViewHeader().setVisibility(View.INVISIBLE);
                        }
                        //It contains data but does it contain data for the selected date?
                        if (dinner.containsKey(userSelectedDate))
                        {
                            Log.i("Dinner map", String.valueOf(dinner));

                            //Making sure the message is not displayed (i.e., the user selected a non valid date first then selected a valid date)
                            getMessage().setVisibility(View.INVISIBLE);

                            String item = Objects.requireNonNull(dinner.get(userSelectedDate), "dinner must not be null")
                                    .replaceAll("\"", "")
                                    .replaceAll("\\[", "")
                                    .replaceAll("]", "");
                            Log.i("Dinner Data", item);

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
                    if(snacksData != null && !snacksData.isEmpty())
                    {
                        if(!snacksFoodItems.isEmpty())
                        {
                            snacksFoodItems.clear();
                            snacksAdapter.notifyDataSetChanged();
                            getSnacksListView().setVisibility(View.INVISIBLE);
                            getSnacksListViewHeader().setVisibility(View.INVISIBLE);
                        }
                        if (snacks.containsKey(userSelectedDate))
                        {
                            Log.i("Snacks map", String.valueOf(snacks));

                            //Making sure the message is not displayed (i.e., the user selected a non valid date first then selected a valid date)
                            getMessage().setVisibility(View.INVISIBLE);

                            String item = Objects.requireNonNull(snacks.get(userSelectedDate), "snacks must not be null")
                                    .replaceAll("\"", "")
                                    .replaceAll("\\[", "")
                                    .replaceAll("]", "");
                            Log.i("Snacks Data", item);

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
                else
                {
                    noData();
                }//end else
            }//end calorieActivity()
        }//end !lastSelectedDate
        if(fitnessActivity())
        {
            //If I was to publish this app or keep working on it then I would add functionallity to utilize the calendar in FitnessActivity. I.e., show completed workouts etc?
            Log.i("Calendar", "Came from fitness page");
        }//end fitnessActivity()
    }//end openSelectedDate()

    //Checking if the user came from the Calorie Activity
    private boolean calorieActivity()
    {
        String intent = getIntent().getStringExtra("ActivityID");
        return intent.equals("Calorie");
    }

    //Checking if the user came from the Fitness Activity
    private boolean fitnessActivity()
    {
        String intent = getIntent().getStringExtra("ActivityID");
        return intent.equals("Fitness");
    }

    //The was no data for selected date
    private void noData()
    {
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

        //Removing the data if there is any (just making sure if user came from a valid date to invalid that the lists are emptied)
        if (breakfastFoodItems != null)
            breakfastFoodItems.clear();
        if (lunchFoodItems != null)
            lunchFoodItems.clear();
        if (dinnerFoodItems != null)
            dinnerFoodItems.clear();
        if (snacksFoodItems != null)
            snacksFoodItems.clear();
    }

    //Helper Methods
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