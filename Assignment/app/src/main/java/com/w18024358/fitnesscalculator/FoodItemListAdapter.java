package com.w18024358.fitnesscalculator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FoodItemListAdapter extends ArrayAdapter<FoodItem>
{
    private Context context;
    private int resource;

    public FoodItemListAdapter(Context context, int resource, ArrayList<FoodItem> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent)
    {
        //TODO Get these values dynamically
        //Getting the Food Item Information
        int quantityOfItem = getItem(position).getQuantity();
        String nameOfItem = getItem(position).getItem();
        int calorieOfItem = getItem(position).getCalories();

        //Creating a FoodItem Object with this information
        FoodItem foodItem = new FoodItem(quantityOfItem, nameOfItem, calorieOfItem);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        //Declaring the fields inside the ListView
        EditText quantity = convertView.findViewById(R.id.calorieLayoutQuantityOfItem);
        EditText name = convertView.findViewById(R.id.calorieLayoutItem);
        EditText calorie = convertView.findViewById(R.id.calorieLayoutCalorie);

        //Setting the values
        quantity.setText(quantityOfItem);
        name.setText(nameOfItem);
        calorie.setText(calorieOfItem);

        return convertView;
    }
}
