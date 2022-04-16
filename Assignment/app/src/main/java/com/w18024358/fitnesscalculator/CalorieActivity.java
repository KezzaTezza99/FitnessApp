package com.w18024358.fitnesscalculator;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    ArrayList<Integer> itemsToDelete = new ArrayList<Integer>();
    ArrayList<String> updatedList = new ArrayList<>();
    int theListSizeNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

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
        //loadData();
    }

    //TODO make it actually make changes to the list when going back to the CalorieActivity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO Create static codes for return(s)
        //Returning from adding calories but deciding to go back / or back from FullListView
        if(requestCode == RESULT_CANCELED || resultCode == RESULT_CANCELED)
        {
            Log.i("CalorieActivity:", "Just returning");
        }
        //Returning from FullListView and have edited an item
        else if(requestCode == 2 || resultCode == 2)
        {
            Log.i("CalorieActivity:", "Item in list Edited");
            assert data != null;
            updatedList = data.getStringArrayListExtra("The New Item List");
            theListSizeNew = data.getIntExtra("SIZE", 0);
            String currentLst = data.getStringExtra("Current List");
            updateList(currentLst);
        }
        //Returning from FullListView and have deleted an item
        else if(requestCode == 3 || resultCode == 3)
        {
            Log.i("CalorieActivity:", "Deleted from List");
            assert data != null;
            updatedList = data.getStringArrayListExtra("The New Item List");
            theListSizeNew = data.getIntExtra("SIZE", 0);
            String currentLst = data.getStringExtra("Current List");
            updateList(currentLst);
        }
        else if(requestCode == 10 || resultCode == 10)
        {
            Log.i("CalorieActivity:", "Added to list from FullListView");

            assert data != null;
            updatedList = data.getStringArrayListExtra("The New Item List");
            theListSizeNew = data.getIntExtra("SIZE", 0);
            String currentLst = data.getStringExtra("Current List");
            updateList(currentLst);
        }
        //Returning from adding an item to list from AddToCalories and FullListView
        else if(requestCode == RETURNED_VALUES || requestCode == RESULT_OK)
        {
            Log.i("CalorieActivity:", "Added to list from Calorie Activity");
            assert data != null;
            String listToAddTo = data.getStringExtra("Current List");
            System.out.println("List from CalorieActivity: " + listToAddTo);

            itemQuantity = data.getStringExtra("Item Quantity");
            itemName = data.getStringExtra("Item Name");
            itemCalories = data.getStringExtra("Item Calories");

            switch (listToAddTo)
             {
                 case "Breakfast":
                     breakfastFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                     breakfastAdapter.notifyDataSetChanged();

                     breakfastListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                         openFullItemList(listToAddTo, breakfastFoodItems);
                         return false;
                     });
                     addToCalorieTotal(breakfastFoodItems, "breakfast");
                     break;
                 case "Lunch":
                     lunchFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                     lunchAdapter.notifyDataSetChanged();

                     lunchListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                         openFullItemList(listToAddTo, lunchFoodItems);
                         return false;
                     });
                     addToCalorieTotal(lunchFoodItems, "lunch");
                     break;
                 case "Dinner":
                     dinnerFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                     dinnerAdapter.notifyDataSetChanged();

                     dinnerListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                         openFullItemList(listToAddTo, dinnerFoodItems);
                         return false;
                     });
                     addToCalorieTotal(dinnerFoodItems, "dinner");
                     break;
                 case "Snacks":
                     snacksFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                     snacksAdapter.notifyDataSetChanged();

                     snacksListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                         openFullItemList(listToAddTo, snacksFoodItems);
                         return false;
                     });
                     addToCalorieTotal(snacksFoodItems, "snacks");
                     break;
                 default:
                     throw new IllegalStateException("Unexpected value: " + listToAddTo);
             }
            setCalorieTotal();
            recalculateRemainingCalories();
        }
    }

    @Override
    public void applyUsersCalorieTarget(String target) {
        caloriesLeft.setText(target + "kcal");
        targetCalories = Integer.parseInt(target);

        //Saving the data to sharedPreferences
        saveData();
    }

    //User can set what their daily calorie goals are
    private void setTargetCalories()
    {
        //Need to open something that allows the user to enter a value
        TargetCalorieDialog calorieDialog = new TargetCalorieDialog();
        calorieDialog.show(getSupportFragmentManager(), "Calorie Dialog");
    }

    private void openAddCaloriesActivity(String currentlySelectedListView)
    {
        //User hasn't entered a target yet so don't allow them to enter anything into the list
        if(caloriesLeft.getText().toString().equals("target kcal"))
        {
            Toast.makeText(this, "Please enter a calorie target", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("Calorie Activity _variable: " + currentlySelectedListView);
        Intent intent = new Intent(this, AddCaloriesActivity.class);
        //Need to tell the intent which list to the user is adding too
        intent.putExtra("Current List", currentlySelectedListView);
        startActivityForResult(intent, RETURNED_VALUES);
    }

    private void openFullItemList(String selectedListName, ArrayList<FoodItem> theList)
    {
        ArrayList<String> convertedArray = new ArrayList<String>();
        int size = theList.size();
        System.out.println("Size: " + size);

        for(int i = 0; i < theList.size(); i++)
        {
            convertedArray.add(theList.get(i).getItemQuantity());
            convertedArray.add(theList.get(i).getItemName());
            convertedArray.add(theList.get(i).getItemCalories());

            System.out.println("The list: " + theList.get(i).getItemQuantity());
            System.out.println("The list: " + theList.get(i).getItemName());
            System.out.println("The list: " + theList.get(i).getItemCalories());
        }

        for(int j = 0; j < convertedArray.size(); j++)
        {
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

    private void addToCalorieTotal(ArrayList<FoodItem> items, String type)
    {
        switch(type)
        {
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

    private void setCalorieTotal()
    {
        totalCaloriesOverall = breakfastSum + lunchSum + dinnerSum + snackSum;
        totalCalories.setText(totalCaloriesOverall + "kcal");
    }

    //TODO make this work
    private void recalculateRemainingCalories()
    {
        //As the string will include after the number it causes crashes. Need to only get the number
        String[] caloriesEaten = totalCalories.getText().toString().split("k");

        int caloriesRemaining = targetCalories - Integer.parseInt(caloriesEaten[0]);
        caloriesLeft.setText(caloriesRemaining + "kcal");
    }

    private void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("TargetCalories", targetCalories);
        editor.putBoolean("DataSaved", Boolean.TRUE);
        editor.apply();
    }

    //TODO make sharedPrefs a method and return it like in BMI keep good DRY
    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("DataSaved", Boolean.TRUE))
        {
            int ogCal = sharedPreferences.getInt("TargetCalories", 0);
            Log.i("Load Data:", String.valueOf(ogCal));
            caloriesLeft.setText(ogCal + "kcal");
        }
    }

    private void updateList(String whichList)
    {
        Log.i("Updating", "-------------------------------------------UPDATING-------------------------------------------");
        Log.i("Updating:", whichList);
        int count = 0;

        switch(whichList)
        {
            case "Breakfast":
                breakfastFoodItems.clear();
                updatingList(breakfastFoodItems);
                breakfastAdapter.notifyDataSetChanged();
                addToCalorieTotal(breakfastFoodItems, "breakfast");
                break;
            case "Lunch":
                lunchFoodItems.clear();
                updatingList(lunchFoodItems);
                lunchAdapter.notifyDataSetChanged();
                addToCalorieTotal(lunchFoodItems, "lunch");
                break;
            case "Dinner":
                dinnerFoodItems.clear();
                updatingList(dinnerFoodItems);
                dinnerAdapter.notifyDataSetChanged();
                addToCalorieTotal(dinnerFoodItems, "dinner");
                break;
            case "Snacks":
                snacksFoodItems.clear();
                updatingList(snacksFoodItems);
                snacksAdapter.notifyDataSetChanged();
                addToCalorieTotal(snacksFoodItems, "snacks");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + whichList);
        }
        setCalorieTotal();
        recalculateRemainingCalories();
    }

    private void updatingList(ArrayList<FoodItem> items)
    {
        int count = 0;

        for (int i = 0; i < theListSizeNew; i++) {
            items.add(new FoodItem(updatedList.get(count),
                    updatedList.get(count + 1),
                    updatedList.get(count + 2)));
            count += 3;
        }
    }

    private int addingAllItemCalories(ArrayList<FoodItem> items)
    {
        int tempSum = 0;

        for (int i = 0; i < items.size(); i++) {
            tempSum += Integer.parseInt(items.get(i).getItemCalories());
        }
        return tempSum;
    }
}