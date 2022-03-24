package com.w18024358.fitnesscalculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class CalorieActivity extends AppCompatActivity {
    //TODO REFACTOR THIS CLASS
    //TODO Add a class that does toasts for me?? Maybe another UTIL class? Like Advanced Programming

    static final int RETURNED_VALUES = 1;
    ArrayList<PairOfInfo> foodItems;
    ListView breakfastListView;
    CustomAdapter adapter;
    ImageView addButton;

    //Values from the other activity
    String fname;
    String sname;
    String txtTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        //Hardcoding a value for testing purposes
        foodItems= new ArrayList<PairOfInfo>();

        breakfastListView = findViewById(R.id.calorieBreakfastListView);

        adapter = new CustomAdapter(this, android.R.layout.simple_list_item_2, foodItems);

        breakfastListView.setAdapter(adapter);

        addButton = findViewById(R.id.calorieAddIcon);
        addButton.setOnClickListener(view -> openAddCaloriesActivity());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RETURNED_VALUES || requestCode == RESULT_OK)
        {
            fname = data.getStringExtra("Name");
            sname = data.getStringExtra("Surname");
            txtTest = data.getStringExtra("TestStr");

            foodItems.add(new PairOfInfo(fname, sname, txtTest));
            adapter.notifyDataSetChanged();
        }
    }

    private void openAddCaloriesActivity()
    {
        Intent intent = new Intent(this, AddToMultiListActivity.class);
        startActivityForResult(intent, RETURNED_VALUES);
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