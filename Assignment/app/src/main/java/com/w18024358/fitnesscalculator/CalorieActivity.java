package com.w18024358.fitnesscalculator;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;

public class CalorieActivity extends AppCompatActivity implements TargetCalorieDialog.CalorieDialogListener {
    //TODO REFACTOR THIS CLASS
    //TODO Add a class that does toasts for me?? Maybe another UTIL class? Like Advanced Programming
    //TODO make sure all of the boxes and headers are the same size and in the same positions
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

        caloriesLeft.setOnLongClickListener(view -> {
            setTargetCalories();
            return false;
        });

        //Maybe add a bundles bit here like other activity to stop null data being passed?

        //Load all data previously entered (so far this is only the total calories the user would like to eat a day)
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Returning from adding calories but deciding to go back
        if(requestCode == RESULT_CANCELED || resultCode == RESULT_CANCELED)
        {
            Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
            return;
        }

         else if(requestCode == RETURNED_VALUES || requestCode == RESULT_OK)
         {
            String listToAddTo = data.getStringExtra("Current List");
            System.out.println("List from CalorieActivity: " + listToAddTo);

             itemQuantity = data.getStringExtra("Item Quantity");
             itemName = data.getStringExtra("Item Name");
             itemCalories = data.getStringExtra("Item Calories");

             int originalListSize = data.getIntExtra("Previous List Size", 0);
             int newListSize = data.getIntExtra("New List Size", 0);

             switch (listToAddTo)
             {
                 case "Breakfast":
                     if(originalListSize != newListSize) {
                         itemsToDelete = data.getIntegerArrayListExtra("Positions To Delete");

                         Toast.makeText(this, "Items to delete: " + itemsToDelete.get(0).toString(), Toast.LENGTH_SHORT).show();

                         //Delete the item
                         for (int i = 0; i < breakfastFoodItems.size(); i++) {
                             if (itemsToDelete.get(i) != null || itemsToDelete.get(i) == 0) {
                                 breakfastFoodItems.remove(i);
                             }
                         }
                         breakfastAdapter.notifyDataSetChanged();
                     }

                     breakfastFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                     breakfastAdapter.notifyDataSetChanged();

                     breakfastListView.setOnItemLongClickListener((adapterView, view, i, l) ->
                     {
                         openFullItemList(listToAddTo, breakfastFoodItems);
                         return false;
                     });
                     //addToCalorieTotal(breakfastFoodItems, "breakfast");
                     break;
                 case "Lunch":
                     itemQuantity = data.getStringExtra("Item Quantity");
                     itemName = data.getStringExtra("Item Name");
                     itemCalories = data.getStringExtra("Item Calories");

                     lunchFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                     lunchAdapter.notifyDataSetChanged();

                     lunchListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                         openFullItemList(listToAddTo, lunchFoodItems);
                         return false;
                     });

                     System.out.println("Current Selected List: " + listToAddTo);

                     //addToCalorieTotal(lunchFoodItems, "lunch");
                     break;
                 case "Dinner":
                     itemQuantity = data.getStringExtra("Item Quantity");
                     itemName = data.getStringExtra("Item Name");
                     itemCalories = data.getStringExtra("Item Calories");

                     dinnerFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                     dinnerAdapter.notifyDataSetChanged();

                     dinnerListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                         openFullItemList(listToAddTo, dinnerFoodItems);
                         return false;
                     });

                     System.out.println("Current Selected List: " + listToAddTo);

                     //addToCalorieTotal(dinnerFoodItems, "dinner");
                     break;
                 case "Snacks":
                     itemQuantity = data.getStringExtra("Item Quantity");
                     itemName = data.getStringExtra("Item Name");
                     itemCalories = data.getStringExtra("Item Calories");

                     snacksFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                     snacksAdapter.notifyDataSetChanged();

                     snacksListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                         openFullItemList(listToAddTo, snacksFoodItems);
                         return false;
                     });

                     System.out.println("Current Selected List: " + listToAddTo);

                     //addToCalorieTotal(snacksFoodItems, "snacks");
                     break;
                 default:
                     //TODO Add error handling?
                     //Something went wrong
                     System.out.println("Something went wrong when getting the list to add too - List value:  " + listToAddTo);
                     break;
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

// TODO Fix this as rn it actually just breaks the entire program
    //Im thinking it has something todo with trying to calculate the calories of deleted items

//    private int addToCalorieTotal(ArrayList<FoodItem> items, String type)
//    {
//        int tempSum = 0;
//
//        switch(type)
//        {
//            case "breakfast":
//                for (int i = 0; i < items.size(); i++) {
//                    tempSum += Integer.parseInt(items.get(i).getItemCalories());
//                }
//                return breakfastSum = tempSum;
//
//            case "lunch":
//                for (int i = 0; i < items.size(); i++) {
//                    tempSum += Integer.parseInt(items.get(i).getItemCalories());
//                }
//                return lunchSum = tempSum;
//
//            case "dinner":
//                for (int i = 0; i < items.size(); i++) {
//                    tempSum += Integer.parseInt(items.get(i).getItemCalories());
//                }
//                return dinnerSum = tempSum;
//
//            case "snacks":
//                for (int i = 0; i < items.size(); i++) {
//                    tempSum += Integer.parseInt(items.get(i).getItemCalories());
//                }
//                return snackSum = tempSum;
//
//            default:
//                throw new IllegalStateException("Unexpected value: " + type);
//        }
//    }

    private void setCalorieTotal()
    {
        totalCaloriesOverall = breakfastSum + lunchSum + dinnerSum + snackSum;
        totalCalories.setText(totalCaloriesOverall + "kcal");
    }

    private void recalculateRemainingCalories()
    {
        //As the string will include after the number it causes crashes. Need to only get the number
        String caloriesEaten[] = String.valueOf(totalCalories.getText().toString()).split("k");

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
        editor.commit();

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
}








//OLD CODE
//        //User has decided to go back from the Full-List
//        if(requestCode == RESULT_CANCELED || resultCode == RESULT_CANCELED)
//        {
//            //Don't have to store this value could just use getStringExtra directly
//            String listToUpdate = data.getStringExtra("Current List");
//
//            itemQuantity = data.getStringExtra("Item Quantity");
//            itemName = data.getStringExtra("Item Name");
//            itemCalories = data.getStringExtra("Item Calories");
//
//            switch(listToUpdate)
//            {
//                case "Breakfast":
//                    breakfastFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
//                    breakfastAdapter.notifyDataSetChanged();
//                    break;
//                case "Lunch":
//                    lunchFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
//                    lunchAdapter.notifyDataSetChanged();
//                    break;
//                case "Dinner":
//                    dinnerFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
//                    dinnerAdapter.notifyDataSetChanged();
//                    break;
//                case "Snacks":
//                    snacksFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
//                    snacksAdapter.notifyDataSetChanged();
//                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + listToUpdate);
//            }
//            recalculateRemainingCalories();
//        }