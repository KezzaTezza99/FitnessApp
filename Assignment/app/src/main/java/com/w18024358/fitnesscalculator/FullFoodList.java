package com.w18024358.fitnesscalculator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.channels.CancelledKeyException;
import java.util.ArrayList;

public class FullFoodList extends AppCompatActivity implements EditItemCalorieDialog.EditCalorieDialogListener
{
    static final int RETURNED_VALUES = 1;

    //TODO Refactor

    String selectedList;
    ArrayList<String> stringItemList;
    ArrayList<FoodItem> theList;
    int listSize;
    int newListSize;

    ListView list;
    FoodItemListAdapter adapter;

    int position;

    //Values from the other activity
    String itemQuantity;
    String itemName;
    String itemCalories;

    ArrayList<Integer> positionsOfDeletedItem;
    int arrayPosition = 0;

    boolean itemEdited, itemDeleted, itemAdded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_food_item_list);

        getAddButton().setOnClickListener(view -> openAddToCalorieList());
        getDoneButton().setOnClickListener(view -> goBackToCaloriePage());

        stringItemList = new ArrayList<String>();
        theList = new ArrayList<FoodItem>();

        positionsOfDeletedItem = new ArrayList<Integer>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedList = extras.getString("Current List Name");
            stringItemList = extras.getStringArrayList("Item List");
            listSize = extras.getInt("Item List Size");
            newListSize = listSize;
        }

        getItemListHeader().setText(selectedList + ":");

        int count = 0;

        //TODO MAKE THIS UTIL ?
        //Taking the string array of all the items and transforming it into a FoodItem Array
        for (int i = 0; i < listSize; i++) {
            theList.add(new FoodItem(stringItemList.get(count),
                    stringItemList.get(count + 1),
                    stringItemList.get(count + 2)));
            count += 3;
        }

        //Getting the ListView
        list = findViewById(R.id.fullItemList);
        adapter = new FoodItemListAdapter(this, theList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Adding on item click which will allow the User to Edit an item in the list
        list.setOnItemClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            String msg = "Would you like to edit item: " + theList.get(i).getItemName();

            DialogInterface.OnClickListener dcl = (dialogInterface, j) -> {
                if (j == DialogInterface.BUTTON_POSITIVE) {
                    itemEdited = true;
                    position = i;
                    openDialog();
                }
            };
            builder.setMessage(msg)
                    .setPositiveButton("Yes", dcl)
                    .setNegativeButton("No", dcl);
            builder.show();
        });

        //Adding an on long item click which will allow the User to Delete an item in the list
        list.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            String message = "Do you want to delete " + theList.get(i).getItemName();

            DialogInterface.OnClickListener dcl = ((dialogInterface, j) ->
            {
                //TODO Item deletes from FullList but crashes when returning to the CalorieActivity
                if (j == DialogInterface.BUTTON_POSITIVE) {
                    //Telling Calorie Activity that there has been an item deleted
                    itemDeleted = true;

                    //Deleted the item from the new list that was passed to FullFoodList
                    //Also need to delete the item from the main list in CalorieActivity!!!!!!!
                    theList.remove(i);
                    positionsOfDeletedItem.add(arrayPosition, i);
                    arrayPosition++;
                    adapter.notifyDataSetChanged();

                }
            });

            builder.setMessage(message)
                    .setPositiveButton("Yes", dcl)
                    .setNegativeButton("No", dcl);
            builder.show();
            return true;
        });
    }

    //The user has edited an item
    @Override
    public void applyNewCalorieItem(String itemName, String itemQuantity, String totalCalories) {
        //TODO Make the changes carry over to the list view (Calorie Activity)
        //Getting the item that needs to be edited and then applying the new values
        theList.get(position).setItemQuantity(itemQuantity);
        theList.get(position).setItemName(itemName);
        theList.get(position).setItemCalories(totalCalories);

        adapter.notifyDataSetChanged();
        newListSize++;
    }

    //When the user returns to this activity from AddCaloriesActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RETURNED_VALUES || requestCode == RESULT_OK || requestCode == 10 || resultCode == 10)
        {
            if(data == null)
            {
                return;
            }
            //TODO Prevent any information being added when user goes back
            //This is when the user returns from AddCaloriesActivity from FullListView
            Toast.makeText(this, "Testing", Toast.LENGTH_SHORT).show();
            itemAdded = true;

            //The user has added an item using the Add button inside the FullListView
            itemQuantity = data.getStringExtra("Item Quantity");
            itemName = data.getStringExtra("Item Name");
            itemCalories = data.getStringExtra("Item Calories");

            //Applied the new item to the local list, still need to tell the CalorieActivity list that there has been an item added
            theList.add(new FoodItem(itemQuantity, itemName, itemCalories));
            adapter.notifyDataSetChanged();
        }
    }

    //The user wants to add to the list
    private void openAddToCalorieList()
    {
        Intent intent = new Intent(this, AddCaloriesActivity.class);
        //As the user is adding from the FullList and not CalorieActivity the activity needs to know
        intent.putExtra("FullFoodList", Boolean.TRUE);
        startActivityForResult(intent, RETURNED_VALUES);
    }

    //Method to go back to CalorieActivity once the user has clicked done
    private void goBackToCaloriePage()
    {
        if(itemEdited)
        {
            getIntent().putExtra("Item Edited", true);
            finishActivity(2);
            setResult(2, getIntent());

        }
        else if(itemDeleted)
        {
            getIntent().putExtra("Item Deleted", true);
            finishActivity(3);
            setResult(3, getIntent());
        }
        else if(itemAdded)
        {
            getIntent().putExtra("Item Added", true);
            finishActivity(10);
            setResult(10, getIntent());
        }
        else
        {
            finishActivity(RESULT_CANCELED);
            setResult(RESULT_CANCELED, getIntent());
        }

        //Storing the current list to transfer the contents to the CalorieActivity
        getIntent().putExtra("Current List", selectedList);
        getIntent().putExtra("Previous List Size", listSize);
        getIntent().putExtra("New List Size", newListSize);

        //Putting the entire list into a ArrayList<String> that can be given to the intent
        //TODO UTIL
        ArrayList<String> listForStorage = new ArrayList<>();
        for(int i = 0; i < theList.size(); i++)
        {
            listForStorage.add(theList.get(i).getItemQuantity());
            listForStorage.add(theList.get(i).getItemName());
            listForStorage.add(theList.get(i).getItemCalories());
        }

        Log.i("THE LIST IN FULLFOODLIST:", String.valueOf(listForStorage));

        getIntent().putExtra("SIZE", theList.size());
        getIntent().putStringArrayListExtra("The New Item List", listForStorage);
        finish();
    }

    //The user wants to edit an item so need to open the dialog box that will allow them to
    private void openDialog()
    {
        //Custom Dialog to edit the currently selected item
        EditItemCalorieDialog editItemCalorieDialog = new EditItemCalorieDialog();
        editItemCalorieDialog.show(getSupportFragmentManager(), "Edit Calorie Dialog");
    }

    //Helper Methods
    private TextView getItemListHeader()
    {
        return findViewById(R.id.fullItemListHeaderLabel);
    }

    private ImageView getAddButton()
    {
        return findViewById(R.id.fullItemListAddIcon);
    }

    private Button getDoneButton()
    {
        return findViewById(R.id.fullItemListDoneButton);
    }

    private Button getEditButton()
    {
        return findViewById(R.id.fullItemListEditButton);
    }

    private Button getRemoveButton()
    {
        return findViewById(R.id.fullItemListRemoveButton);
    }
}