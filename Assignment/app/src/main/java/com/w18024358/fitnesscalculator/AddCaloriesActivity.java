package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddCaloriesActivity extends AppCompatActivity
{
    //Don't like this - Could come up with a better way
    String selectedList;
    boolean addedFromFullFoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_calorie_list);

        getBackButton().setOnClickListener(view -> returnToCalorieActivity());
        getAddButton().setOnClickListener(view -> addToList());

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            selectedList = extras.getString("Current List");
            addedFromFullFoodList = extras.getBoolean("FullFoodList");
        }
        getHeader().setText(String.format("Adding item to %s:", selectedList));
    }

    //User wants to return to the CalorieActivity (back button)
    private void returnToCalorieActivity()
    {
        if(!addedFromFullFoodList) {
            setResult(Activity.RESULT_CANCELED, getIntent());
        }
        finish();
    }

    private void addToList()
    {
        if(getNameOfItemField().getText().toString().length() == 0 ||
                getQuantityOfItemField().getText().toString().length() == 0 ||
                getTotalCaloriesOfItemField().getText().toString().length() == 0)
        {
            Toast.makeText(this, "Please ensure you have entered valid values", Toast.LENGTH_SHORT).show();
            return;
        }

        String itemName = getNameOfItemField().getText().toString();
        String itemQuantity = getQuantityOfItemField().getText().toString();
        String itemCalories = getTotalCaloriesOfItemField().getText().toString();

        Intent intent;

        //If true then the user needs to be returned to the FullList Activity otherwise CalorieActivity
        if(!addedFromFullFoodList)
        {
            intent = new Intent(this, CalorieActivity.class);
        }
        else
        {
            intent = new Intent(this, FullFoodList.class);
        }

        intent.putExtra("Item Quantity", itemQuantity);
        intent.putExtra("Item Name", itemName);
        intent.putExtra("Item Calories", itemCalories);
        intent.putExtra("Current List", selectedList);
        intent.putExtra("Update List", true);
        finishActivity(Activity.RESULT_OK);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    //Helper methods
    private EditText getNameOfItemField() { return findViewById(R.id.addToListViewItemName); }
    private EditText getQuantityOfItemField() { return findViewById(R.id.addTOListViewQuantityOfItem); }
    private EditText getTotalCaloriesOfItemField() { return findViewById(R.id.addToListViewTotalCalories); }
    private Button getBackButton() { return findViewById(R.id.addToListViewBackButton); }
    private Button getAddButton() { return findViewById(R.id.addToListViewAddButton); }
    private TextView getHeader() { return findViewById(R.id.addToListViewHeaderLabel); }
}