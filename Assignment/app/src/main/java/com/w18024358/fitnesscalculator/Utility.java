package com.w18024358.fitnesscalculator;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//Class that will hold some common functionality to stop repeating code
public class Utility
{
    ArrayList<String> allDates = new ArrayList<>();

    //Transforming a ArrayList<FoodItem> to ArrayList<String>
    ArrayList<String> itemListToStringList(ArrayList<FoodItem> items)
    {
        Log.i("Items", String.valueOf(items));
        ArrayList<String> convertedArray = new ArrayList<>();

        for (int i = 0; i < items.size(); i++)
        {
            convertedArray.add(items.get(i).getItemQuantity());
            convertedArray.add(items.get(i).getItemName());
            convertedArray.add(items.get(i).getItemCalories());
        }
        Log.i("Utility - FoodItem -> String", convertedArray.toString());
        return convertedArray;
    }

    //Transforming a ArrayList<String> to ArrayList<FoodItem>
    ArrayList<FoodItem> stringListToItemList(ArrayList<String> strings, ArrayList<FoodItem> items, int sizeOfTheList)
    {
        Log.i("Strings: ", String.valueOf(strings));
        int count = 0;

        for (int i = 0; i < sizeOfTheList; i++) {
            items.add(new FoodItem(strings.get(count),
                    strings.get(count + 1),
                    strings.get(count + 2)));
            count += 3;
        }
        return items;
    }

    //Calculate calories for all items
    int totalCaloriesEaten(ArrayList<FoodItem> items)
    {
        int sum = 0;

        for(int i = 0; i < items.size(); i++)
        {
            sum += Integer.parseInt(items.get(i).getItemCalories());
        }
        return sum;
    }

    //Get the current date
    String getCurrentDate()
    {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        Date date = calendar.getTime();
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

        return today;
    }

    String getCurrentDateNumerical()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
        java.util.Calendar date = Calendar.getInstance();

        return dateFormat.format(date.getTime());
    }

    ArrayList<String> getAllDates()
    {
        Log.i("Utility: getAllDates()", allDates.toString());
        return allDates;
    }

    void addDateToList(String date)
    {
        Log.i("Utility: addDateToList()", date);
        allDates.add(date);
    }

    //Hiding input fields
    void hideMetricFields(EditText userCM, EditText userKG)
    {
        userCM.setVisibility(View.INVISIBLE);
        userKG.setVisibility(View.INVISIBLE);
    }

    void hideImperialFields(EditText userFT, EditText userInch, EditText weightST, EditText weightLBS)
    {
        userFT.setVisibility(View.INVISIBLE);
        userInch.setVisibility(View.INVISIBLE);
        weightST.setVisibility(View.INVISIBLE);
        weightLBS.setVisibility(View.INVISIBLE);
    }

    //Showing input fields
    void showMetricFields(EditText userCM, EditText userKG)
    {
        userCM.setVisibility(View.VISIBLE);
        userKG.setVisibility(View.VISIBLE);
    }

    void showImperialFields(EditText userFT, EditText userInch, EditText weightST, EditText weightLBS)
    {
        userFT.setVisibility(View.VISIBLE);
        userInch.setVisibility(View.VISIBLE);
        weightST.setVisibility(View.VISIBLE);
        weightLBS.setVisibility(View.VISIBLE);
    }
}
