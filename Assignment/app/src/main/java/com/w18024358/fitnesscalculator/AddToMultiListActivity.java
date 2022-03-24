package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddToMultiListActivity extends AppCompatActivity {

    EditText nameOfItemField;
    EditText quantityOfItemField;
    EditText totalCaloriesOfItemField;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_multi_list);

        nameOfItemField = findViewById(R.id.addToListViewItemName);
        quantityOfItemField = findViewById(R.id.addTOListViewQuantityOfItem);
        totalCaloriesOfItemField = findViewById(R.id.addToListViewTotalCalories);

        add = findViewById(R.id.addButtonToListMulti);
        add.setOnClickListener(view -> addToList());
    }

    private void addToList()
    {
        //Could rewrite this if statement
        //Getting the text inside the field
        if(nameOfItemField.getText().toString().length() > 0 &&
                quantityOfItemField.getText().toString().length() > 0 &&
                totalCaloriesOfItemField.getText().toString().length() > 0)
        {
            String itemName = nameOfItemField.getText().toString();
            String itemQuantity = quantityOfItemField.getText().toString();
            String itemCalories = totalCaloriesOfItemField.getText().toString();

            //Need to sort this
            Intent intent = new Intent(this, CalorieActivity.class);
            intent.putExtra("Item Quantity", itemQuantity);
            intent.putExtra("Item Name", itemName);
            intent.putExtra("Item Calories", itemCalories);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "Please ensure you have entered a name", Toast.LENGTH_LONG).show();
        }
    }
}