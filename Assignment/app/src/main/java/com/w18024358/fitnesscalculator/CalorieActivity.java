package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class CalorieActivity extends AppCompatActivity {
    //TODO REFACTOR THIS CLASS
    //TODO Add a class that does toasts for me?? Maybe another UTIL class? Like Advanced Programming

//    ListView breakfastListView;
//    EditText inputText;
//    ImageView enter;
//
//    ArrayList<String> arrayList;
//    ArrayAdapter arrayAdapter;
//
//
//    //Dialog Stuff
//    private TextView txt1;
//    private TextView txt2;
//    private TextView txt3;
//    private TextView txt4;
//
//
//    //New List View Stuff
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

//        breakfastListView = findViewById(R.id.calorieBreakfastListView);
//        inputText = findViewById(R.id.calorieAddToListText);
//        enter = findViewById(R.id.calorieAddIcon);
//
//        enter.setOnClickListener(view -> openDialog());
//
//        txt1 = findViewById(R.id.textView1);
//        txt2 = findViewById(R.id.textView2);
//        txt3 = findViewById(R.id.textView3);
//        txt4 = findViewById(R.id.textView4);
//
//        arrayList = new ArrayList<>();
//        arrayList.add("Add items to test");
//
//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
//        breakfastListView.setAdapter(arrayAdapter);
//
//
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







        //New List View Stuff
        //HardCoding these values first
        list = (ListView) findViewById(R.id.newListView);

        //FoodItem item01 = new FoodItem(2, "Eggs", 50);
        //FoodItem item02 = new FoodItem(1, "Pepperoni Pizza Slice", 150);


        //Adding the FoodItem(s) objects to an ArrayList
        ArrayList<FoodItem> foodItemArrayList = new ArrayList<>();
        //foodItemArrayList.add(item01);
        //foodItemArrayList.add(item02);

        FoodItemListAdapter adapter = new FoodItemListAdapter(this, R.layout.calorie_view_layout, foodItemArrayList);
        list.setAdapter(adapter);

        //updateInfo();
        updateList();
    }

    private void updateList()
    {
        int itemQuantity = Integer.parseInt(getIntent().getStringExtra("Item Quantity"));
        String itemType = getIntent().getStringExtra("Item Type");
        int itemCalorie = Integer.parseInt(getIntent().getStringExtra("Total Calorie"));
    }

//
//    private void addItem(String item)
//    {
//        arrayList.add(item);
//        breakfastListView.setAdapter(arrayAdapter);
//    }
//
//    private void openDialog()
//    {
//        Intent intent = new Intent(this, AddCaloriesActivity.class);
//        startActivity(intent);
//    }
//
//    public void updateInfo()
//    {
//        txt1.setText(getIntent().getStringExtra("Item Type"));
//        txt2.setText(getIntent().getStringExtra("Item Name"));
//        txt3.setText(getIntent().getStringExtra("Item Quantity"));
//        txt4.setText(getIntent().getStringExtra("Total Calorie"));
//    }
}