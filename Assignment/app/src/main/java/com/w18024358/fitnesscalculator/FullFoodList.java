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

//When user LongHolds on a ListView in the CalorieActivity it opens this activity which is responsible for displaying the list of items "fullscreen"
public class FullFoodList extends AppCompatActivity implements EditItemCalorieDialog.EditCalorieDialogListener
{
    //Need to respond differently when going back to the CalorieActivity or FullListView depending on the actions the user had taken
    static final int RETURNED_VALUES = 1;
    static final int EDITED_VALUE = 2;
    static final int DELETED_VALUE = 3;
    static final int ADDED_VALUE = 4;

    //Need to know what list the user wants to be fully displayed (what ListView did they long hold on)
    String selectedList;
    //Can't pass ArrayList<FoodItems> but can pass ArrayList<String> through Intents so need to store the ArrayList<String> and transform it back into correct ArrayList
    ArrayList<String> stringItemList;
    //Will use the String ArrayList to transform the items back into FoodItems
    ArrayList<FoodItem> theList;
    //Need to know if the list has been added to or deleted from
    int listSize;

    ListView list;
    FoodItemListAdapter adapter;

    //Need to know the position in the array of item to edit, get this by storing the value when the user clicks on an item in the FullFoodList
    int position;

    //Values from the other activity
    String itemQuantity;
    String itemName;
    String itemCalories;

    //Need to know what return code to return to previous intent with
    boolean itemEdited, itemDeleted, itemAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_food_item_list);

        //Setting on click listeners for the buttons
        getAddButton().setOnClickListener(view -> openAddToCalorieList());
        getDoneButton().setOnClickListener(view -> goBackToCaloriePage());

        //Initialising the arrays
        stringItemList = new ArrayList<String>();
        theList = new ArrayList<FoodItem>();

        //Ensuring that the data passed to the intents is not null before accessing them
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            selectedList = extras.getString("Current List Name");
            stringItemList = extras.getStringArrayList("Item List");
            listSize = extras.getInt("Item List Size");
        }

        //Displaying to the user above the ListView which list they selected
        getItemListHeader().setText(selectedList + ":");

        //Converting the ArrayList<String> into ArrayList<FoodItem>
        theList = getUtility().stringListToItemList(stringItemList, theList, listSize);

        //Getting the ListView
        list = findViewById(R.id.fullItemList);
        adapter = new FoodItemListAdapter(this, theList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Adding on item click which will allow the User to Edit an item in the list
        list.setOnItemClickListener((adapterView, view, i, l) ->
        {
            //User has clicked on an item to edit
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            String msg = "Would you like to edit item: " + theList.get(i).getItemName();

            DialogInterface.OnClickListener dcl = (dialogInterface, j) -> {
                if (j == DialogInterface.BUTTON_POSITIVE)
                {
                    //They want to edit an item so set itemEdited to true and get the item index
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
        list.setOnItemLongClickListener((adapterView, view, i, l) ->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            String message = "Do you want to delete " + theList.get(i).getItemName();

            DialogInterface.OnClickListener dcl = ((dialogInterface, j) ->
            {
                if (j == DialogInterface.BUTTON_POSITIVE) {
                    //Telling Calorie Activity that there has been an item deleted
                    itemDeleted = true;

                    //Removing the item from the list and updating the ListView
                    theList.remove(i);
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
    public void applyNewCalorieItem(String itemName, String itemQuantity, String totalCalories)
    {
        //Getting the item that needs to be edited and then applying the new values
        theList.get(position).setItemQuantity(itemQuantity);
        theList.get(position).setItemName(itemName);
        theList.get(position).setItemCalories(totalCalories);

        adapter.notifyDataSetChanged();
    }

    //When the user returns to this activity from AddCaloriesActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RETURNED_VALUES || requestCode == RESULT_OK)
        {
            if(data == null) { return; }

            //The user has added a value from AddToCalorieActivity and is now back to FullListView
            itemAdded = true;

            //The user has added an item using the Add button inside the FullListView
            itemQuantity = data.getStringExtra("Item Quantity");
            itemName = data.getStringExtra("Item Name");
            itemCalories = data.getStringExtra("Item Calories");

            //Adding the new item to the list
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
            finishActivity(EDITED_VALUE);
            setResult(EDITED_VALUE, getIntent());
        }
        else if(itemDeleted)
        {
            getIntent().putExtra("Item Deleted", true);
            finishActivity(DELETED_VALUE);
            setResult(DELETED_VALUE, getIntent());
        }
        else if(itemAdded)
        {
            getIntent().putExtra("Item Added", true);
            finishActivity(ADDED_VALUE);
            setResult(ADDED_VALUE, getIntent());
        }
        else
        {
            finishActivity(RESULT_CANCELED);
            setResult(RESULT_CANCELED, getIntent());
        }

        //Putting the entire list into a ArrayList<String> that can be given to the intent
        ArrayList<String> listForStorage = getUtility().itemListToStringList(theList);

        Log.i("The list in FullFoodList:", String.valueOf(listForStorage));

        //Storing the current list to transfer the contents to the CalorieActivity
        getIntent().putExtra("Current List", selectedList);
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
    private Utility getUtility() { return new Utility(); }
}