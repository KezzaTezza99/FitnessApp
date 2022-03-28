package com.w18024358.fitnesscalculator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class FullFoodList extends AppCompatActivity {

    //TODO Refactor
    TextView itemListHeader;
    ImageView addButton;
    Button doneButton;
    Button editButton;
    Button removeButton;

    String selectedList;
    ArrayList<String> stringItemList;
    ArrayList<FoodItem> theList;
    int listSize;

    ListView list;
    FoodItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_food_item_list);

        addButton = findViewById(R.id.fullItemListAddIcon);
        doneButton = findViewById(R.id.fullItemListDoneButton);
        editButton = findViewById(R.id.fullItemListEditButton);
        removeButton = findViewById(R.id.fullItemListRemoveButton);

        addButton.setOnClickListener(view -> openAddToCalorieList());
        doneButton.setOnClickListener(view -> goBackToCaloriePage());

        stringItemList = new ArrayList<String>();
        theList = new ArrayList<FoodItem>();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            selectedList = extras.getString("Current List Name");
            stringItemList = extras.getStringArrayList("Item List");
            listSize = extras.getInt("Item List Size");
        }

        itemListHeader = findViewById(R.id.fullItemListHeaderLabel);
        itemListHeader.setText(selectedList + ":");

        int count = 0;

        for(int i = 0; i < listSize; i++)
        {
            theList.add(new FoodItem(stringItemList.get(count),
                    stringItemList.get(count + 1),
                    stringItemList.get(count + 2)));
            count += 3;
        }

        list = findViewById(R.id.fullItemList);
        adapter = new FoodItemListAdapter(this, theList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        list.setOnItemClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            String msg = "Would you like to edit item: " + theList.get(i).getItemName();

            DialogInterface.OnClickListener dcl = (dialogInterface, j) -> {
                if (j == DialogInterface.BUTTON_POSITIVE) {
                    Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
                }
            };
            builder.setMessage(msg)
                    .setPositiveButton("Yes", dcl)
                    .setNegativeButton("No", dcl);
            builder.show();
        });
    }

    private void openAddToCalorieList()
    {
        Intent intent = new Intent(this, AddCaloriesActivity.class);
        startActivity(intent);
    }

    private void goBackToCaloriePage()
    {
        finishActivity(Activity.RESULT_CANCELED);
        setResult(RESULT_CANCELED, getIntent());
        finish();
    }
}