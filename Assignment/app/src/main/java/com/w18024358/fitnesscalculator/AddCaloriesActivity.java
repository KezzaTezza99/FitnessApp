package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCaloriesActivity extends AppCompatActivity {
    //TODO Refactor
    EditText nameOfItemField;
    EditText quantityOfItemField;
    EditText totalCaloriesOfItemField;

    Button backButton;
    Button addButton;

    String selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_calorie_list);


        nameOfItemField = findViewById(R.id.addToListViewItemName);
        quantityOfItemField = findViewById(R.id.addTOListViewQuantityOfItem);
        totalCaloriesOfItemField = findViewById(R.id.addToListViewTotalCalories);

        backButton = findViewById(R.id.addToListViewBackButton);
        addButton = findViewById(R.id.addToListViewAddButton);

        backButton.setOnClickListener(view -> returnToCalorieActivity());
        addButton.setOnClickListener(view -> addToList());

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            selectedList = extras.getString("Current List");
        }
    }

    private void returnToCalorieActivity()
    {
        //TODO Fix this as if I go back and have items in the List it actually wipes them
        //finish() and finishActivity() don't work
        Intent intent = new Intent(this, CalorieActivity.class);
        startActivity(intent);
    }

    private void addToList()
    {
        if(nameOfItemField.getText().toString().length() == 0 ||
                quantityOfItemField.getText().toString().length() == 0 ||
                totalCaloriesOfItemField.getText().toString().length() == 0)
        {
            Toast.makeText(this, "Please ensure you have entered valid values", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String itemName = nameOfItemField.getText().toString();
            String itemQuantity = quantityOfItemField.getText().toString();
            String itemCalories = totalCaloriesOfItemField.getText().toString();

            Intent intent = new Intent(this, CalorieActivity.class);
            intent.putExtra("Item Quantity", itemQuantity);
            intent.putExtra("Item Name", itemName);
            intent.putExtra("Item Calories", itemCalories);
            intent.putExtra("Current List", selectedList);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}