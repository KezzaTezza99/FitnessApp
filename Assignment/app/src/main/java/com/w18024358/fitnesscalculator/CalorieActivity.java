package com.w18024358.fitnesscalculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class CalorieActivity extends AppCompatActivity {
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
    int sum = 0;

    int breakfastSum, lunchSum, dinnerSum, snackSum = 0;

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RETURNED_VALUES || requestCode == RESULT_OK)
        {
            String listToAddTo = data.getStringExtra("Current List");
            System.out.println("List: " + listToAddTo);

            if(listToAddTo.equals("Breakfast"))
            {
                //TODO Make this a method and call it? Keeps code DRY
                itemQuantity = data.getStringExtra("Item Quantity");
                itemName = data.getStringExtra("Item Name");
                itemCalories = data.getStringExtra("Item Calories");
                //end _todo

                breakfastFoodItems.add(new FoodItem(itemQuantity, itemName, itemCalories));
                breakfastAdapter.notifyDataSetChanged();

                breakfastListView.setOnItemLongClickListener((adapterView, view, i, l) ->  {
                    openFullItemList(listToAddTo, breakfastFoodItems);
                    return false;
                });

                System.out.println("Current Selected List: " + listToAddTo);

                addToCalorieTotal(breakfastFoodItems, "breakfast");
            }
            else if(listToAddTo.equals("Lunch"))
            {
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

                addToCalorieTotal(lunchFoodItems, "lunch");
            }
            else if(listToAddTo.equals("Dinner"))
            {
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

                addToCalorieTotal(dinnerFoodItems, "dinner");
            }
            else if(listToAddTo.equals("Snacks"))
            {
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

                addToCalorieTotal(snacksFoodItems, "snacks");
            }
            else
            {
                //TODO Add error handling?
                //Something went wrong
                System.out.println("Something went wrong when getting the list to add too - List value:  " + listToAddTo);
            }
            setCalorieTotal();
        }
    }

    private void openAddCaloriesActivity(String currentlySelectedListView)
    {
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

    private int addToCalorieTotal(ArrayList<FoodItem> items, String type)
    {
        int tempSum = 0;

        switch(type)
        {
            case "breakfast":
            for (int i = 0; i < items.size(); i++) {
                tempSum += Integer.parseInt(items.get(i).getItemCalories());
            }
            return breakfastSum = tempSum;

            case "lunch":
            for (int i = 0; i < items.size(); i++) {
                tempSum += Integer.parseInt(items.get(i).getItemCalories());
            }
            return lunchSum = tempSum;

            case "dinner":
            for (int i = 0; i < items.size(); i++) {
                tempSum += Integer.parseInt(items.get(i).getItemCalories());
            }
            return dinnerSum = tempSum;

            case "snacks":
                for (int i = 0; i < items.size(); i++) {
                    tempSum += Integer.parseInt(items.get(i).getItemCalories());
                }
                return snackSum = tempSum;

            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private void setCalorieTotal()
    {
        totalCaloriesOverall = breakfastSum + lunchSum + dinnerSum + snackSum;
        totalCalories.setText(totalCaloriesOverall + "kcal");
    }
}


















//        //Onclick event for clicking on element(s) inside the ListView
//        breakfastListView.setOnItemClickListener((adapterView, view, i, l) -> {
//            Toast.makeText(CalorieActivity.this, "Clicked Item: " + i + " " + arrayList.get(i), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
//        });
//
////        //Onclick for entering
////        enter.setOnClickListener(view -> {
////            String text = inputText.getText().toString();
////            if(text == null || text.length() == 0)
////            {
////                Toast.makeText(this, "Enter an item", Toast.LENGTH_LONG).show();
////            }
////            else
////            {
////                addItem(text);
////                inputText.setText("");
////                Toast.makeText(this, "Added: " + text, Toast.LENGTH_SHORT).show();
////            }
////        });