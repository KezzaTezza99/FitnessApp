package com.w18024358.fitnesscalculator;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

//Save data when the user closes app?
//Load when user opens app?
//Save data when user leaves the page?
//Load when user opens page
//Save data as soon as item is added?
public class CalorieActivity extends AppCompatActivity implements TargetCalorieDialog.CalorieDialogListener
{
    static final int RETURNED_VALUES = 1;
    static final int EDITED_VALUE = 2;
    static final int DELETED_VALUE = 3;
    static final int ADDED_VALUE = 4;

    //Breakfast
    ArrayList<FoodItem> breakfastFoodItems;
    ListView breakfastListView;
    FoodItemListAdapter breakfastAdapter;
    ImageView breakfastAddButton;

    //Lunch
    ArrayList<FoodItem> lunchFoodItems;
    ListView lunchListView;
    FoodItemListAdapter lunchAdapter;
    ImageView lunchAddButton;

    //Dinner
    ArrayList<FoodItem> dinnerFoodItems;
    ListView dinnerListView;
    FoodItemListAdapter dinnerAdapter;
    ImageView dinnerAddButton;

    //Snacks
    ArrayList<FoodItem> snacksFoodItems;
    ListView snacksListView;
    FoodItemListAdapter snacksAdapter;
    ImageView snacksAddButton;

    //Values from the other activity
    String itemQuantity;
    String itemName;
    String itemCalories;

    //Calorie(s) Label(s)
    TextView totalCalories;
    int totalCaloriesOverall = 0;

    int breakfastSum, lunchSum, dinnerSum, snackSum = 0;

    //--- TEMP SOLUTION?
    TextView caloriesLeft;
    int targetCalories;

    ArrayList<String> updatedList = new ArrayList<>();
    int theListSizeNew;

    //Date
    TextView currentDate;

    ActionBarDrawerToggle sideNavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        //Hardcoding a value for testing purposes
        //Breakfast
        breakfastFoodItems = new ArrayList<>();
        breakfastListView = findViewById(R.id.calorieBreakfastListView);
        breakfastAdapter = new FoodItemListAdapter(this, breakfastFoodItems);
        breakfastListView.setAdapter(breakfastAdapter);

        breakfastAddButton = findViewById(R.id.calorieBreakfastAddIcon);
        breakfastAddButton.setOnClickListener(view -> openAddCaloriesActivity("Breakfast"));

        //Lunch
        lunchFoodItems = new ArrayList<>();
        lunchListView = findViewById(R.id.calorieLunchListView);
        lunchAdapter = new FoodItemListAdapter(this, lunchFoodItems);
        lunchListView.setAdapter(lunchAdapter);

        lunchAddButton = findViewById(R.id.calorieLunchListAddIcon);
        lunchAddButton.setOnClickListener(view -> openAddCaloriesActivity("Lunch"));

        //Dinner
        dinnerFoodItems = new ArrayList<>();
        dinnerListView = findViewById(R.id.calorieDinnerListView);
        dinnerAdapter = new FoodItemListAdapter(this, dinnerFoodItems);
        dinnerListView.setAdapter(dinnerAdapter);

        dinnerAddButton = findViewById(R.id.calorieDinnerListAddIcon);
        dinnerAddButton.setOnClickListener(view -> openAddCaloriesActivity("Dinner"));

        //Snacks
        snacksFoodItems = new ArrayList<>();
        snacksListView = findViewById(R.id.calorieSnackListView);
        snacksAdapter = new FoodItemListAdapter(this, snacksFoodItems);
        snacksListView.setAdapter(snacksAdapter);

        snacksAddButton = findViewById(R.id.calorieSnacksListAddIcon);
        snacksAddButton.setOnClickListener(view -> openAddCaloriesActivity("Snacks"));

        totalCalories = findViewById(R.id.calorieTotalCalories);
        caloriesLeft = findViewById(R.id.calorieRemainingCalories);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Hold down on calories to set your intended target")
                .setPositiveButton("Okay", (dialogInterface, i) -> {});
        builder.show();

        caloriesLeft.setOnLongClickListener(view ->
        {
            setTargetCalories();
            return true;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        boolean loaded = sharedPreferences.getBoolean("DataSaved", Boolean.FALSE);
        if(loaded) loadData();

        currentDate = findViewById(R.id.calorieCurrentDate);
        //Setting the date to equal today's date
        currentDate.setText(getUtility().getCurrentDate());

        currentDate.setOnLongClickListener(view -> {
            openCalendar();
            return true;
        });

        //TODO make this a util? and then pass in the item id as an argument?
        //Main Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainBottomNavigationMenu);
        bottomNavigationView.setSelectedItemId(R.id.calorieActivityMenu);

        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            Intent intent;
            switch(item.getItemId())
            {
                case R.id.bmiActivityMenu:
                    intent = new Intent(this, BMIActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.calorieActivityMenu:
                    return true;
                case R.id.fitnessActivityMenu:
                    intent = new Intent(this, FitnessActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
        });

        //Drawer Navigation
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        //The strings that I didn't pass to constructor provide accessibility for blind people should implement (reads the string values out)
        sideNavigationMenu = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);

        //Can now use the menu
        sideNavigationMenu.syncState();

        //Responding to the navigation buttons
        NavigationView sideNavView = findViewById(R.id.sideNavMenu);
        sideNavView.setNavigationItemSelectedListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.profilePage:
                    startActivity(new Intent(this, UserProfileActivity.class));
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Returning from adding calories but deciding to go back / or back from FullListView
        if (requestCode == RESULT_CANCELED || resultCode == RESULT_CANCELED) {
            Log.i("CalorieActivity:", "Just returning");
        }
        //Returning from FullListView and have edited an item
        else if (requestCode == EDITED_VALUE || resultCode == EDITED_VALUE) {
            Log.i("CalorieActivity:", "Item in list Edited");
            assert data != null;
            updatedList = data.getStringArrayListExtra("The New Item List");
            theListSizeNew = data.getIntExtra("SIZE", 0);
            String currentLst = data.getStringExtra("Current List");
            updateList(currentLst);
        }
        //Returning from FullListView and have deleted an item
        else if (requestCode == DELETED_VALUE || resultCode == DELETED_VALUE) {
            Log.i("CalorieActivity:", "Deleted from List");
            assert data != null;
            updatedList = data.getStringArrayListExtra("The New Item List");
            theListSizeNew = data.getIntExtra("SIZE", 0);
            String currentLst = data.getStringExtra("Current List");
            updateList(currentLst);
        } else if (requestCode == ADDED_VALUE || resultCode == ADDED_VALUE) {
            Log.i("CalorieActivity:", "Added to list from FullListView");

            assert data != null;
            updatedList = data.getStringArrayListExtra("The New Item List");
            theListSizeNew = data.getIntExtra("SIZE", 0);
            String currentLst = data.getStringExtra("Current List");
            updateList(currentLst);
        }
        //Returning from adding an item to list from AddToCalories and FullListView
        else if (requestCode == RETURNED_VALUES || requestCode == RESULT_OK) {
            Log.i("CalorieActivity:", "Added to list from Calorie Activity");
            assert data != null;
            String listToAddTo = data.getStringExtra("Current List");
            System.out.println("List from CalorieActivity: " + listToAddTo);

            itemQuantity = data.getStringExtra("Item Quantity");
            itemName = data.getStringExtra("Item Name");
            itemCalories = data.getStringExtra("Item Calories");

            switch (listToAddTo) {
                case "Breakfast":
                    breakfastFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                    breakfastAdapter.notifyDataSetChanged();

                    breakfastListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                        openFullItemList(listToAddTo, breakfastFoodItems);
                        return false;
                    });
                    addToCalorieTotal(breakfastFoodItems, "breakfast");
                    saveLists(breakfastFoodItems, "Breakfast");
                    break;
                case "Lunch":
                    lunchFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                    lunchAdapter.notifyDataSetChanged();

                    lunchListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                        openFullItemList(listToAddTo, lunchFoodItems);
                        return false;
                    });
                    addToCalorieTotal(lunchFoodItems, "lunch");
                    saveLists(lunchFoodItems, "Lunch");
                    break;
                case "Dinner":
                    dinnerFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                    dinnerAdapter.notifyDataSetChanged();

                    dinnerListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                        openFullItemList(listToAddTo, dinnerFoodItems);
                        return false;
                    });
                    addToCalorieTotal(dinnerFoodItems, "dinner");
                    saveLists(dinnerFoodItems, "Dinner");
                    break;
                case "Snacks":
                    snacksFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                    snacksAdapter.notifyDataSetChanged();

                    snacksListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                        openFullItemList(listToAddTo, snacksFoodItems);
                        return false;
                    });
                    addToCalorieTotal(snacksFoodItems, "snacks");
                    saveLists(snacksFoodItems, "Snacks");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + listToAddTo);
            }
            saveData();
            setCalorieTotal();
            recalculateRemainingCalories();
        }
    }

    @Override
    public void applyUsersCalorieTarget(String target) {
        caloriesLeft.setText(target + "kcal");
        targetCalories = Integer.parseInt(target);

        saveCalorieTotal();
    }

    //User can set what their daily calorie goals are
    private void setTargetCalories() {
        //Need to open something that allows the user to enter a value
        TargetCalorieDialog calorieDialog = new TargetCalorieDialog();
        calorieDialog.show(getSupportFragmentManager(), "Calorie Dialog");
    }

    private void openAddCaloriesActivity(String currentlySelectedListView) {
        //User hasn't entered a target yet so don't allow them to enter anything into the list
        if (caloriesLeft.getText().toString().equals("target kcal")) {
            Toast.makeText(this, "Please enter a calorie target", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("Calorie Activity _variable: " + currentlySelectedListView);
        Intent intent = new Intent(this, AddCaloriesActivity.class);
        //Need to tell the intent which list to the user is adding too
        intent.putExtra("Current List", currentlySelectedListView);
        startActivityForResult(intent, RETURNED_VALUES);
    }

    private void openFullItemList(String selectedListName, ArrayList<FoodItem> theList) {
        ArrayList<String> convertedArray = new ArrayList<String>();
        int size = theList.size();
        System.out.println("Size: " + size);

        for (int i = 0; i < theList.size(); i++)
        {
            convertedArray.add(theList.get(i).getItemQuantity());
            convertedArray.add(theList.get(i).getItemName());
            convertedArray.add(theList.get(i).getItemCalories());
        }

        Intent intent = new Intent(this, FullFoodList.class);
        //Need to tell the intent which list to the user is adding too
        intent.putExtra("Current List Name", selectedListName);
        intent.putStringArrayListExtra("Item List", convertedArray);
        intent.putExtra("Item List Size", size);
        startActivityForResult(intent, RETURNED_VALUES);
    }

    private void addToCalorieTotal(ArrayList<FoodItem> items, String type) {
        switch (type) {
            case "breakfast":
                breakfastSum = addingAllItemCalories(items);
                break;
            case "lunch":
                lunchSum = addingAllItemCalories(items);
                break;
            case "dinner":
                dinnerSum = addingAllItemCalories(items);
                break;
            case "snacks":
                snackSum = addingAllItemCalories(items);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        setCalorieTotal();
    }

    private void setCalorieTotal() {
        totalCaloriesOverall = breakfastSum + lunchSum + dinnerSum + snackSum;
        totalCalories.setText(totalCaloriesOverall + "kcal");
    }

    private void recalculateRemainingCalories() {
        //As the string will include after the number it causes crashes. Need to only get the number
        String[] caloriesEaten = totalCalories.getText().toString().split("k");
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        int calorieTarget = sharedPreferences.getInt("TargetCalories", 0);
        int caloriesRemaining = calorieTarget - Integer.parseInt(caloriesEaten[0]);

        caloriesLeft.setText(caloriesRemaining + "kcal");
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String currentDate = getUtility().getCurrentDateNumerical();
        editor.putBoolean("DataSaved", Boolean.TRUE);

        //Saving today's data separately (as it'll be quicker to save / load just today's - all other dates only needed if user wants to go back through the calendar)
        editor.putString("CurrentDate", currentDate);

        //Saving the date again but will append to this value to hold a "list" of all dates that a user as inputted data
        //This allows the calendar to quickly check to see if the selected data is contained in the "list" of dates stored
        editor.putString("AllDatesSaved", currentDate);
        boolean check = sharedPreferences.getBoolean("DataSaved", Boolean.FALSE);

        if(check)
        {
            //Append to the list
            String oldDates = sharedPreferences.getString("AllDatesSaved", "");
            if(!oldDates.equals(currentDate)) {
                editor.putString("AllDatesSaved", oldDates + " - " + currentDate);
            }
        }
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        //Only load data if its been saved today!
        String dayDataSaved = sharedPreferences.getString("CurrentDate", "");

        int ogCal = sharedPreferences.getInt("TargetCalories", 0);
        caloriesLeft.setText(ogCal + "kcal");

        if(!dayDataSaved.equals(getUtility().getCurrentDateNumerical())) return;

        //LIST STILL ONLY SHOWS ONE ITEM, ITS A STRING IT GETS OVER-RIDDEN (TRY HAVING MULTIPLE BEFORE RELOADING?)
        //NEED TO PUT IT BACK INTO LIST?
        //Loading just BREAKFAST FOR NOW!!
        boolean breakfast = sharedPreferences.getBoolean("Breakfast List Needs Loading", Boolean.FALSE);
        boolean lunch = sharedPreferences.getBoolean("Lunch List Needs Loading", Boolean.FALSE);
        boolean dinner = sharedPreferences.getBoolean("Dinner List Needs Loading", Boolean.FALSE);
        boolean snacks = sharedPreferences.getBoolean("Snacks List Needs Loading", Boolean.FALSE);

        Log.i("Load Data:", String.valueOf(breakfast));

        Gson gson = new Gson();

        if (breakfast)
        {
            //MAKE THIS UTIL?
            //Retrieving the JSON lists
            String json = sharedPreferences.getString("Breakfast List Saved Items", "");
            int breakfastSize = sharedPreferences.getInt("Breakfast List Size On Save", 0);
            Log.i("JSON:", json);
            Utility utility = new Utility();
            ArrayList<String> strings = gson.fromJson(json, ArrayList.class);
            Log.i("JSON ArrayList", strings.toString());

            breakfastFoodItems.clear();
            breakfastFoodItems = utility.stringListToItemList(strings, breakfastFoodItems, breakfastSize);

            //Need to reapply the onLongClickListener
            breakfastListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                openFullItemList("Breakfast", breakfastFoodItems);
                return false;
            });
        }

        if(lunch)
        {
            //Retrieving the JSON lists
            String json1 = sharedPreferences.getString("Lunch List Saved Items", "");
            int lunchSize = sharedPreferences.getInt("Lunch List Size On Save", 0);
            Log.i("JSON:", json1);
            Utility utility = new Utility();
            ArrayList<String> strings = gson.fromJson(json1, ArrayList.class);
            Log.i("JSON ArrayList", strings.toString());

            lunchFoodItems.clear();
            lunchFoodItems = utility.stringListToItemList(strings, lunchFoodItems, lunchSize);

            //Need to reapply the onLongClickListener
            lunchListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                openFullItemList("Lunch", lunchFoodItems);
                return false;
            });
        }

        if(dinner)
        {
            //Retrieving the JSON lists
            String json = sharedPreferences.getString("Dinner List Saved Items", "");
            int dinnerSize = sharedPreferences.getInt("Dinner List Size On Save", 0);
            Log.i("JSON:", json);
            Utility utility = new Utility();
            ArrayList<String> strings = gson.fromJson(json, ArrayList.class);
            Log.i("JSON ArrayList", strings.toString());

            dinnerFoodItems.clear();
            dinnerFoodItems = utility.stringListToItemList(strings, dinnerFoodItems, dinnerSize);

            //Need to reapply the onLongClickListener
            dinnerListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                openFullItemList("Dinner", dinnerFoodItems);
                return false;
            });
        }

        if(snacks)
        {
            //Retrieving the JSON lists
            String json = sharedPreferences.getString("Snacks List Saved Items", "");
            int snacksSize = sharedPreferences.getInt("Snacks List Size On Save", 0);
            Log.i("JSON:", json);
            Utility utility = new Utility();
            ArrayList<String> strings = gson.fromJson(json, ArrayList.class);
            Log.i("JSON ArrayList", strings.toString());

            snacksFoodItems.clear();
            snacksFoodItems = utility.stringListToItemList(strings, snacksFoodItems, snacksSize);

            //Need to reapply the onLongClickListener
            snacksListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                openFullItemList("Snacks", snacksFoodItems);
                return false;
            });
        }
        setCalorieTotal();
        totalCaloriesEatenNeedsRecalculating();
        Log.i("Load Data", "Finished loading");
    }

    private void updateList(String whichList) {
        Log.i("Updating", "-------------------------------------------UPDATING-------------------------------------------");
        Log.i("Updating:", whichList);

        switch (whichList) {
            case "Breakfast":
                breakfastFoodItems.clear();
                updatingList(breakfastFoodItems);
                breakfastAdapter.notifyDataSetChanged();
                addToCalorieTotal(breakfastFoodItems, "breakfast");
                saveLists(breakfastFoodItems, "Breakfast");
                break;
            case "Lunch":
                lunchFoodItems.clear();
                updatingList(lunchFoodItems);
                lunchAdapter.notifyDataSetChanged();
                addToCalorieTotal(lunchFoodItems, "lunch");
                saveLists(lunchFoodItems, "Lunch");
                break;
            case "Dinner":
                dinnerFoodItems.clear();
                updatingList(dinnerFoodItems);
                dinnerAdapter.notifyDataSetChanged();
                addToCalorieTotal(dinnerFoodItems, "dinner");
                saveLists(dinnerFoodItems, "Dinner");
                break;
            case "Snacks":
                snacksFoodItems.clear();
                updatingList(snacksFoodItems);
                snacksAdapter.notifyDataSetChanged();
                addToCalorieTotal(snacksFoodItems, "snacks");
                saveLists(snacksFoodItems, "Snacks");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + whichList);
        }
        setCalorieTotal();
        recalculateRemainingCalories();
    }

    private void updatingList(ArrayList<FoodItem> items) {
        int count = 0;

        for (int i = 0; i < theListSizeNew; i++) {
            items.add(new FoodItem(updatedList.get(count),
                    updatedList.get(count + 1),
                    updatedList.get(count + 2)));
            count += 3;
        }
    }

    private int addingAllItemCalories(ArrayList<FoodItem> items) {
        int tempSum = 0;

        for (int i = 0; i < items.size(); i++) {
            tempSum += Integer.parseInt(items.get(i).getItemCalories());
        }
        return tempSum;
    }

    private void saveLists(ArrayList<FoodItem> items, String theList)
    {
        //TEMP
        Utility utility = new Utility();
        //

        Log.i("Calorie Activity: ", "------Saving Data------");
        //TODO make SharedPreferences a method and call that?
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Turning the FoodItem ArrayList to a String ArrayList (Easier to get back from the JSON object)
        ArrayList<String> stringArrayList;
        stringArrayList = utility.itemListToStringList(items);

        Gson gson = new Gson();
        String json = gson.toJson(stringArrayList);
        Log.i("Ths JSON being saved:", json);

        switch (theList)
        {
            case "Breakfast":
                //Saving the current data, useful for when user comes back to the app that day
                editor.putBoolean("Breakfast List Needs Loading", Boolean.TRUE);
                editor.putString("Breakfast List Saved Items", json);
                editor.putInt("Breakfast List Size On Save", breakfastFoodItems.size());

                //Saving the current data but appending it to a list of stored previous data. Used in calendar to check previous days
                //Storing a HashMap that will hold the date and the list
                HashMap<String, String> allBreakfastData = new HashMap<>();
                //Storing the date and the data
                allBreakfastData.put(getUtility().getCurrentDateNumerical(), json);

                //Now creating another HashMap that again stores the date as a key but the value stored will be the size of the list, needed for when I recreate the lists in Calendar
                HashMap<String, Integer> allBreakfastDataInformation = new HashMap<>();
                //Storing the date then the size
                allBreakfastDataInformation.put(getUtility().getCurrentDateNumerical(), items.size());

                //Turning the HashMap(s) into a String(s) that can be stored in the SharedPreferences
                String data = gson.toJson(allBreakfastData);
                String info = gson.toJson(allBreakfastDataInformation);

                editor.putString("Breakfast List All Saved Data", data);
                editor.putString("Breakfast List All Info", info);
                editor.putBoolean("Breakfast Data Saved", Boolean.TRUE);

                boolean breakfastCheck = sharedPreferences.getBoolean("Breakfast Data Saved", Boolean.FALSE);

                //If the is true then the user will be saving data for a second time, this can from adding to the list for the current day or adding info for a different day
                //need to track all the information if I want to be able to display data through the calendar, database would probably work better but this quicker and easier
                if(breakfastCheck)
                {
                    //TODO can modify this to used the SP Utility
                    //Getting the string of data to append data too
                    String oldData = sharedPreferences.getString("Breakfast List All Saved Data", "");

                    //Transforming back into a Map
                    Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                    HashMap<String, String> fullBreakfastDataList = gson.fromJson(oldData, type);

                    //Appending the new value to the map
                    fullBreakfastDataList.put(getUtility().getCurrentDateNumerical(), json);

                    //Getting the stored map that holds the size of the lists
                    String oldInfo = sharedPreferences.getString("Breakfast List All Info", "");

                    //Transforming back into a Map
                    Type type2 = new TypeToken<HashMap<String, Integer>>(){}.getType();
                    HashMap<String, Integer> fullBreakfastInfoList = gson.fromJson(oldInfo, type2);

                    //Appending the new values to the map
                    fullBreakfastInfoList.put(getUtility().getCurrentDateNumerical(), items.size());

                    //Turning back into a string to store in shared preferences
                    String newData = gson.toJson(fullBreakfastDataList);
                    String newInfo = gson.toJson(fullBreakfastInfoList);
                    editor.putString("Breakfast List All Saved Data", newData);
                    editor.putString("Breakfast List All Info", newInfo);
                }
                break;
            case "Lunch":
                //
                editor.putBoolean("Lunch List Needs Loading", Boolean.TRUE);
                editor.putString("Lunch List Saved Items", json);
                editor.putInt("Lunch List Size On Save", lunchFoodItems.size());

                //This data being saved is used for the Calendar Activity
                //Storing a HashMap that will hold the date and the list on the last save
                HashMap<String, String> allLunchData = new HashMap<>();
                //Storing the date and the data
                allLunchData.put(getUtility().getCurrentDateNumerical(), json);

                //Now creating another HashMap that again stores the date as a key but the value stored will be the size of the list, needed for when I recreate the lists in Calendar
                HashMap<String, Integer> allLunchDataInformation = new HashMap<>();
                //Storing the date then the size
                allLunchDataInformation.put(getUtility().getCurrentDateNumerical(), items.size());

                //Turning the HashMap(s) into a String(s) that can be stored in the SharedPreferences
                String data1 = gson.toJson(allLunchData);
                String info1 = gson.toJson(allLunchDataInformation);

                editor.putString("Lunch List All Saved Data", data1);
                editor.putString("Lunch List All Info", info1);
                editor.putBoolean("Lunch Data Saved", Boolean.TRUE);

                boolean lunchCheck = sharedPreferences.getBoolean("Lunch Data Saved", Boolean.FALSE);

                //If the is true then the user will be saving data for a second time, this can from adding to the list for the current day or adding info for a different day
                //need to track all the information if I want to be able to display data through the calendar, database would probably work better but this quicker and easier
                if(lunchCheck)
                {
                    //Getting the string of data to append data too
                    String oldData = sharedPreferences.getString("Lunch List All Saved Data", "");

                    //Transforming back into a Map
                    Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                    HashMap<String, String> fullLunchDataList = gson.fromJson(oldData, type);

                    //Appending the new value to the map
                    fullLunchDataList.put(getUtility().getCurrentDateNumerical(), json);

                    //Getting the stored map that holds the size of the lists
                    String oldInfo = sharedPreferences.getString("Lunch List All Info", "");

                    //Transforming back into a Map
                    Type type2 = new TypeToken<HashMap<String, Integer>>(){}.getType();
                    HashMap<String, Integer> fullLunchInfoList = gson.fromJson(oldInfo, type2);

                    //Appending the new values to the map
                    fullLunchInfoList.put(getUtility().getCurrentDateNumerical(), items.size());

                    //Turning back into a string to store in shared preferences
                    String newData = gson.toJson(fullLunchDataList);
                    String newInfo = gson.toJson(fullLunchInfoList);
                    editor.putString("Lunch List All Saved Data", newData);
                    editor.putString("Lunch List All Info", newInfo);
                }
                break;
            case "Dinner":
                editor.putBoolean("Dinner List Needs Loading", true);
                editor.putString("Dinner List Saved Items", json);
                editor.putInt("Dinner List Size On Save", dinnerFoodItems.size());

                //Saving the data for the calendar to use
                //Storing a HashMap that will hold the date and the list
                HashMap<String, String> allDinnerData = new HashMap<>();
                //Storing the date and the data
                allDinnerData.put(getUtility().getCurrentDateNumerical(), json);

                //Now creating another HashMap that again stores the date as a key but the value stored will be the size of the list, needed for when I recreate the lists in Calendar
                HashMap<String, Integer> allDinnerDataInformation = new HashMap<>();
                //Storing the date then the size
                allDinnerDataInformation.put(getUtility().getCurrentDateNumerical(), items.size());

                //Turning the HashMap(s) into a String(s) that can be stored in the SharedPreferences
                String data2 = gson.toJson(allDinnerData);
                String info2 = gson.toJson(allDinnerDataInformation);

                editor.putString("Dinner List All Saved Data", data2);
                editor.putString("Dinner List All Info", info2);
                editor.putBoolean("Dinner Data Saved", Boolean.TRUE);

                boolean dinnerCheck = sharedPreferences.getBoolean("Dinner Data Saved", Boolean.FALSE);

                //If the is true then the user will be saving data for a second time, this can from adding to the list for the current day or adding info for a different day
                //need to track all the information if I want to be able to display data through the calendar, database would probably work better but this quicker and easier
                if(dinnerCheck)
                {
                    //Getting the string of data to append data too
                    String oldData = sharedPreferences.getString("Dinner List All Saved Data", "");

                    //Transforming back into a Map
                    Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                    HashMap<String, String> fullDinnerDataList = gson.fromJson(oldData, type);

                    //Appending the new value to the map
                    fullDinnerDataList.put(getUtility().getCurrentDateNumerical(), json);

                    //Getting the stored map that holds the size of the lists
                    String oldInfo = sharedPreferences.getString("Dinner List All Info", "");

                    //Transforming back into a Map
                    Type type2 = new TypeToken<HashMap<String, Integer>>(){}.getType();
                    HashMap<String, Integer> fullDinnerInfoList = gson.fromJson(oldInfo, type2);

                    //Appending the new values to the map
                    fullDinnerInfoList.put(getUtility().getCurrentDateNumerical(), items.size());

                    //Turning back into a string to store in shared preferences
                    String newData = gson.toJson(fullDinnerDataList);
                    String newInfo = gson.toJson(fullDinnerInfoList);
                    editor.putString("Dinner List All Saved Data", newData);
                    editor.putString("Dinner List All Info", newInfo);
                }
                break;
            case "Snacks":
                editor.putBoolean("Snacks List Needs Loading", true);
                editor.putString("Snacks List Saved Items", json);
                editor.putInt("Snacks List Size On Save", items.size());

                //Adding to the calendar saved data
                //Storing a HashMap that will hold the date and the list
                HashMap<String, String> allSnacksData = new HashMap<>();
                //Storing the date and the data
                allSnacksData.put(getUtility().getCurrentDateNumerical(), json);

                //Now creating another HashMap that again stores the date as a key but the value stored will be the size of the list, needed for when I recreate the lists in Calendar
                HashMap<String, Integer> allSnacksDataInformation = new HashMap<>();
                //Storing the date then the size
                allSnacksDataInformation.put(getUtility().getCurrentDateNumerical(), items.size());

                //Turning the HashMap(s) into a String(s) that can be stored in the SharedPreferences
                String data3 = gson.toJson(allSnacksData);
                String info3 = gson.toJson(allSnacksDataInformation);

                editor.putString("Snacks List All Saved Data", data3);
                editor.putString("Snacks List All Info", info3);
                editor.putBoolean("Snacks Data Saved", Boolean.TRUE);

                boolean snackCheck = sharedPreferences.getBoolean("Snacks Data Saved", Boolean.FALSE);

                //If the is true then the user will be saving data for a second time, this can from adding to the list for the current day or adding info for a different day
                //need to track all the information if I want to be able to display data through the calendar, database would probably work better but this quicker and easier
                if(snackCheck)
                {
                    //Getting the string of data to append data too
                    String oldData = sharedPreferences.getString("Snacks List All Saved Data", "");

                    //Transforming back into a Map
                    Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                    HashMap<String, String> fullSnackDataList = gson.fromJson(oldData, type);

                    //Appending the new value to the map
                    fullSnackDataList.put(getUtility().getCurrentDateNumerical(), json);

                    //Getting the stored map that holds the size of the lists
                    String oldInfo = sharedPreferences.getString("Snacks List All Info", "");

                    //Transforming back into a Map
                    Type type2 = new TypeToken<HashMap<String, Integer>>(){}.getType();
                    HashMap<String, Integer> fullSnacksInfoList = gson.fromJson(oldInfo, type2);

                    //Appending the new values to the map
                    fullSnacksInfoList.put(getUtility().getCurrentDateNumerical(), items.size());

                    //Turning back into a string to store in shared preferences
                    String newData = gson.toJson(fullSnackDataList);
                    String newInfo = gson.toJson(fullSnacksInfoList);
                    editor.putString("Snacks List All Saved Data", newData);
                    editor.putString("Snacks List All Info", newInfo);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + theList);
        }
        editor.apply();
        recalculateRemainingCalories();
    }

    //When the app loads data the calories wont be be accounted for (recalculating here but need to know which lists had items in)
    private void totalCaloriesEatenNeedsRecalculating()
    {
        Utility utility = new Utility();
        int runningTotal = 0;

        if(!breakfastFoodItems.isEmpty())
        {
            breakfastSum += utility.totalCaloriesEaten(breakfastFoodItems);
            runningTotal += breakfastSum;
        }

        if(!lunchFoodItems.isEmpty())
        {
            lunchSum += utility.totalCaloriesEaten(lunchFoodItems);
            runningTotal += lunchSum;
        }

        if(!dinnerFoodItems.isEmpty())
        {
            dinnerSum += utility.totalCaloriesEaten(dinnerFoodItems);
            runningTotal += dinnerSum;
        }

        if(!snacksFoodItems.isEmpty())
        {
            snackSum += utility.totalCaloriesEaten(snacksFoodItems);
            runningTotal += snackSum;
        }
        setCalorieTotal();
        caloriesLeftNeedRecalculating(runningTotal);
    }

    //Saving the calorie total entered by the user (saving it at saveData was causing a bug where if you go back to the page
    // and add after saving data the calories would go minus)
    private void saveCalorieTotal()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("TargetCalories", targetCalories).apply();
    }

    private void caloriesLeftNeedRecalculating(int caloriesEaten)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        int calorieTarget = sharedPreferences.getInt("TargetCalories", 0);

        int calories = calorieTarget - caloriesEaten;

        caloriesLeft.setText(calories + "kcal");
    }

    private void openCalendar()
    {
        Intent intent = new Intent(this, Calendar.class);
        intent.putExtra("ActivityID", "Calorie");
        startActivityForResult(intent, RETURNED_VALUES);
    }

    private  Utility getUtility()
    {
        return new Utility();
    }
}