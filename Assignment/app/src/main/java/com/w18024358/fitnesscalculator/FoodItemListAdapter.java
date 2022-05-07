package com.w18024358.fitnesscalculator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.ArrayList;

//Used to take the ArrayList<FoodItem> and adapt it to be displayed in a custom view
public class FoodItemListAdapter extends ArrayAdapter<FoodItem>
{
    ArrayList<FoodItem> foodItems;
    Context myContext;

    public FoodItemListAdapter(@NonNull Context context, @NonNull ArrayList<FoodItem> objects)
    {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
        foodItems = objects;
        myContext = context;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_calorie_list_view, parent, false);

        TextView quantity = view.findViewById(R.id.listViewQuantityOfItem);
        TextView theItem = view.findViewById(R.id.listViewItem);
        TextView amountOfCalories = view.findViewById(R.id.listViewItemTotalCalories);

        quantity.setText(foodItems.get(position).getItemQuantity());
        theItem.setText(foodItems.get(position).getItemName());
        amountOfCalories.setText(foodItems.get(position).getItemCalories());

        return view;
    }
}