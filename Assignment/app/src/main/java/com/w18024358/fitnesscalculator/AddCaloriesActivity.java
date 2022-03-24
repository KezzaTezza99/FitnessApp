package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddCaloriesActivity extends AppCompatActivity {
    //TODO Refactor
    private EditText typeOfItem;
    private EditText nameOfItem;
    private EditText quantityOfItem;
    private EditText totalCalories;
    private Button backButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calories);

        typeOfItem = findViewById(R.id.addCalorieTypeOfItem);
        nameOfItem = findViewById(R.id.addCalorieNameOfItem);
        quantityOfItem = findViewById(R.id.addCalorieQuantityOfItem);
        totalCalories = findViewById(R.id.addCalorieTotalCalories);
        backButton = findViewById(R.id.addCalorieBackButton);
        addButton = findViewById(R.id.addCalorieAddButton);

        backButton.setOnClickListener(view -> goBackToCalorieActivity());
        addButton.setOnClickListener(view -> addItemToCalorieActivity());
    }

    private void goBackToCalorieActivity()
    {
        finish();
    }

    private void addItemToCalorieActivity()
    {
//        if(typeOfItem.getText().toString().length() <= 0 || nameOfItem.getText().toString().length() <= 0
//                || quantityOfItem.getText().toString().length() <= 0 || totalCalories.getText().toString().length() <= 0)
//        {
//            Toast.makeText(this, "Please ensure all fields are filled out", Toast.LENGTH_LONG).show();
//        }
//        else {
        String itemType = typeOfItem.getText().toString();
        String itemName = nameOfItem.getText().toString();
        int itemQuantity = Integer.parseInt(quantityOfItem.getText().toString());
        int calories = Integer.parseInt(totalCalories.getText().toString());

        Intent intent = new Intent(this, CalorieActivity.class);
        intent.putExtra("Item Type", itemType);
        intent.putExtra("Item Name", itemName);
        intent.putExtra("Item Quantity", itemQuantity);
        intent.putExtra("Total Calorie", calories);
        setResult(Activity.RESULT_OK, intent);
        finish();
        //}
    }
}