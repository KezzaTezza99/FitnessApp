package com.w18024358.fitnesscalculator;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CalorieActivity extends AppCompatActivity implements TargetCalorieDialog.CalorieDialogListener {
    //TODO REFACTOR THIS CLASS
    //TODO Add a class that does toasts for me?? Maybe another UTIL class? Like Advanced Programming
    //TODO make sure all of the boxes and headers are the same size and in the same positions
    //TODO --- The lists all need to be saved :/ keep data persistence
    static final int RETURNED_VALUES = 1;

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

    //NAV Stuff
    ImageView bmiButton;
    ImageView calorieButton;
    ImageView fitnessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        //Temp
        bmiButton = findViewById(R.id.calorieBMIButton);
        calorieButton = findViewById(R.id.calorieCalorieButton);
        fitnessButton = findViewById(R.id.calorieFitnessButton);

        bmiButton.setOnClickListener(view -> openBMI());
        calorieButton.setOnClickListener(view -> openCalorie());
        fitnessButton.setOnClickListener(view -> openFitness());
        //end of nav

        //Hardcoding a value for testing purposes
        //Breakfast
        breakfastFoodItems = new ArrayList<FoodItem>();
        breakfastListView = findViewById(R.id.calorieBreakfastListView);
        breakfastAdapter = new FoodItemListAdapter(this, breakfastFoodItems);
        breakfastListView.setAdapter(breakfastAdapter);

        breakfastAddButton = findViewById(R.id.calorieBreakfastAddIcon);
        breakfastAddButton.setOnClickListener(view -> openAddCaloriesActivity("Breakfast"));

        //Lunch
        lunchFoodItems = new ArrayList<FoodItem>();
        lunchListView = findViewById(R.id.calorieLunchListView);
        lunchAdapter = new FoodItemListAdapter(this, lunchFoodItems);
        lunchListView.setAdapter(lunchAdapter);

        lunchAddButton = findViewById(R.id.calorieLunchListAddIcon);
        lunchAddButton.setOnClickListener(view -> openAddCaloriesActivity("Lunch"));

        //Dinner
        dinnerFoodItems = new ArrayList<FoodItem>();
        dinnerListView = findViewById(R.id.calorieDinnerListView);
        dinnerAdapter = new FoodItemListAdapter(this, dinnerFoodItems);
        dinnerListView.setAdapter(dinnerAdapter);

        dinnerAddButton = findViewById(R.id.calorieDinnerListAddIcon);
        dinnerAddButton.setOnClickListener(view -> openAddCaloriesActivity("Dinner"));

        //Snacks
        snacksFoodItems = new ArrayList<FoodItem>();
        snacksListView = findViewById(R.id.calorieSnackListView);
        snacksAdapter = new FoodItemListAdapter(this, snacksFoodItems);
        snacksListView.setAdapter(snacksAdapter);

        snacksAddButton = findViewById(R.id.calorieSnacksListAddIcon);
        snacksAddButton.setOnClickListener(view -> openAddCaloriesActivity("Snacks"));

        totalCalories = findViewById(R.id.calorieTotalCalories);
        caloriesLeft = findViewById(R.id.calorieRemainingCalories);

        //TODO Add this back in for final build
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Hold down on calories to set your intended target")
//                .setPositiveButton("Okay", (dialogInterface, i) -> {});
//        builder.show();

        caloriesLeft.setOnLongClickListener(view ->
        {
            setTargetCalories();
            return true;
        });

        //Maybe add a bundles bit here like other activity to stop null data being passed?
        //Load all data previously entered (so far this is only the total calories the user would like to eat a day)
        //Add a flag
        //TODO calories doesnt work as intended if loaded from intent
        //It should be modified to only work on new days???
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        boolean loaded = sharedPreferences.getBoolean("DataSaved", Boolean.FALSE);
        if(loaded) loadData();
    }

    //TODO make it actually make changes to the list when going back to the CalorieActivity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO Create static codes for return(s)
        //Returning from adding calories but deciding to go back / or back from FullListView
        if (requestCode == RESULT_CANCELED || resultCode == RESULT_CANCELED) {
            Log.i("CalorieActivity:", "Just returning");
        }
        //Returning from FullListView and have edited an item
        else if (requestCode == 2 || resultCode == 2) {
            Log.i("CalorieActivity:", "Item in list Edited");
            assert data != null;
            updatedList = data.getStringArrayListExtra("The New Item List");
            theListSizeNew = data.getIntExtra("SIZE", 0);
            String currentLst = data.getStringExtra("Current List");
            updateList(currentLst);
        }
        //Returning from FullListView and have deleted an item
        else if (requestCode == 3 || resultCode == 3) {
            Log.i("CalorieActivity:", "Deleted from List");
            assert data != null;
            updatedList = data.getStringArrayListExtra("The New Item List");
            theListSizeNew = data.getIntExtra("SIZE", 0);
            String currentLst = data.getStringExtra("Current List");
            updateList(currentLst);
        } else if (requestCode == 10 || resultCode == 10) {
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
            setCalorieTotal();
            recalculateRemainingCalories();
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.i("OnPause:", "Pausing and Saving Data");
//        if(!breakfastFoodItems.isEmpty())
//            saveLists(breakfastFoodItems, "Breakfast");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.i("OnResume:", "Resuming and Loading Data");
//        boolean load = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getBoolean("DataSaved", Boolean.FALSE);
//        if(load)
//            loadData();
//    }

    @Override
    public void applyUsersCalorieTarget(String target) {
        caloriesLeft.setText(target + "kcal");
        targetCalories = Integer.parseInt(target);

        //Saving the data to sharedPreferences
        saveData();
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

        for (int i = 0; i < theList.size(); i++) {
            convertedArray.add(theList.get(i).getItemQuantity());
            convertedArray.add(theList.get(i).getItemName());
            convertedArray.add(theList.get(i).getItemCalories());

            System.out.println("The list: " + theList.get(i).getItemQuantity());
            System.out.println("The list: " + theList.get(i).getItemName());
            System.out.println("The list: " + theList.get(i).getItemCalories());
        }

        for (int j = 0; j < convertedArray.size(); j++) {
            System.out.println("Converted Array " + j + ": " + convertedArray.get(j));
        }

        System.out.println("Checking array: " + convertedArray);


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
    }

    private void setCalorieTotal() {
        totalCaloriesOverall = breakfastSum + lunchSum + dinnerSum + snackSum;
        totalCalories.setText(totalCaloriesOverall + "kcal");
    }

    //TODO make this work
    private void recalculateRemainingCalories() {
        //As the string will include after the number it causes crashes. Need to only get the number
        String[] caloriesEaten = totalCalories.getText().toString().split("k");

        int caloriesRemaining = targetCalories - Integer.parseInt(caloriesEaten[0]);
        caloriesLeft.setText(caloriesRemaining + "kcal");
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("TargetCalories", targetCalories);
        editor.putBoolean("DataSaved", Boolean.TRUE);
        editor.apply();
    }

    //TODO make sharedPrefs a method and return it like in BMI keep good DRY
    //Need to save individual lists, maybe pass the list name and then save that to preferences and save potentially 4 lists?
    //Load all 4 if not null?
    //Redisplay the values??
    //Save the date it was so can use a calendar
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        Log.i("Load Data", "Just Loaded");
        int ogCal = sharedPreferences.getInt("TargetCalories", 0);
        Log.i("Load Data:", String.valueOf(ogCal));
        caloriesLeft.setText(ogCal + "kcal");

        //LIST STILL ONLY SHOWS ONE ITEM, ITS A STRING IT GETS OVER-RIDDEN (TRY HAVING MULTIPLE BEFORE RELOADING?)
        //NEED TO PUT IT BACK INTO LIST?
        //Loading just BREAKFAST FOR NOW!!
        boolean breakfast = sharedPreferences.getBoolean("Breakfast List Needs Loading", Boolean.FALSE);
        boolean lunch = sharedPreferences.getBoolean("Lunch List Needs Loading", Boolean.FALSE);
        boolean dinner = sharedPreferences.getBoolean("Dinner List Needs Loading", Boolean.FALSE);
        boolean snacks = sharedPreferences.getBoolean("Snacks List Needs Loading", Boolean.FALSE);

        Log.i("Load Data:", String.valueOf(breakfast));

        if (breakfast)
        {
            //MAKE THIS UTIL?
            //Retrieving the JSON lists
            String json = sharedPreferences.getString("Breakfast List Saved Items", "");
            int breakfastSize = sharedPreferences.getInt("Breakfast List Size On Save", 0);
            Log.i("JSON:", json);
            Utility utility = new Utility();
            ArrayList<String> strings = new Gson().fromJson(json, ArrayList.class);
            Log.i("JSON ArrayList", strings.toString());

            breakfastFoodItems.clear();
            breakfastFoodItems = utility.stringListToItemList(strings, breakfastFoodItems, breakfastSize);
        }

        if(lunch)
        {
            //Retrieving the JSON lists
            String json = sharedPreferences.getString("Lunch List Saved Items", "");
            int lunchSize = sharedPreferences.getInt("Lunch List Size On Save", 0);
            Log.i("JSON:", json);
            Utility utility = new Utility();
            ArrayList<String> strings = new Gson().fromJson(json, ArrayList.class);
            Log.i("JSON ArrayList", strings.toString());

            lunchFoodItems.clear();
            lunchFoodItems = utility.stringListToItemList(strings, lunchFoodItems, lunchSize);
        }

        if(dinner)
        {
            //Retrieving the JSON lists
            String json = sharedPreferences.getString("Dinner List Saved Items", "");
            int dinnerSize = sharedPreferences.getInt("Dinner List Size On Save", 0);
            Log.i("JSON:", json);
            Utility utility = new Utility();
            ArrayList<String> strings = new Gson().fromJson(json, ArrayList.class);
            Log.i("JSON ArrayList", strings.toString());

            dinnerFoodItems.clear();
            dinnerFoodItems = utility.stringListToItemList(strings, dinnerFoodItems, dinnerSize);
        }

        if(snacks)
        {
            //Retrieving the JSON lists
            String json = sharedPreferences.getString("Snacks List Saved Items", "");
            int snacksSize = sharedPreferences.getInt("Snacks List Size On Save", 0);
            Log.i("JSON:", json);
            Utility utility = new Utility();
            ArrayList<String> strings = new Gson().fromJson(json, ArrayList.class);
            Log.i("JSON ArrayList", strings.toString());

            snacksFoodItems.clear();
            snacksFoodItems = utility.stringListToItemList(strings, snacksFoodItems, snacksSize);
        }
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

    private void openBMI()
    {
        Intent intent = new Intent(this, BMIActivity.class);
        startActivity(intent);
    }

    private void openCalorie()
    {
        Intent intent = new Intent(this, CalorieActivity.class);
        startActivity(intent);
    }

    private void openFitness()
    {
        Intent intent = new Intent(this, FitnessActivity.class);
        startActivity(intent);
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
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList = utility.itemListToStringList(items);

        Gson gson = new Gson();
        String json = gson.toJson(stringArrayList);
        Log.i("Ths JSON being saved:", json);

        switch (theList)
        {
            case "Breakfast":
                Log.i("Inside Switch:", "Made it");
                editor.putBoolean("Breakfast List Needs Loading", Boolean.TRUE);
                editor.putString("Breakfast List Saved Items", json);
                editor.putInt("Breakfast List Size On Save", breakfastFoodItems.size());
                break;
            case "Lunch":
                editor.putBoolean("Lunch List Needs Loading", Boolean.TRUE);
                editor.putString("Lunch List Saved Items", json);
                editor.putInt("Lunch List Size On Save", lunchFoodItems.size());
                break;
            case "Dinner":
                editor.putBoolean("Dinner List Needs Loading", true);
                editor.putString("Dinner List Saved Items", json);
                editor.putInt("Dinner List Size On Save", dinnerFoodItems.size());
                break;
            case "Snacks":
                editor.putBoolean("Snacks List Needs Loading", true);
                editor.putString("Snacks List Saved Items", json);
                editor.putInt("Snacks List Size On Save", snacksFoodItems.size());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + theList);
        }
        editor.apply();
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

    private void caloriesLeftNeedRecalculating(int caloriesEaten)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        int calorieTarget = sharedPreferences.getInt("TargetCalories", 0);

        int calories = calorieTarget - caloriesEaten;

        caloriesLeft.setText(calories + "kcal");
    }
}