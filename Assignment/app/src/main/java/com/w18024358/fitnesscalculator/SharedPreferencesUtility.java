package com.w18024358.fitnesscalculator;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;

//Utility class to help with shared preferences (mainly modifying saved data lists)
public class SharedPreferencesUtility extends AppCompatActivity
{
    //Used to transform the JSON saved data in SharedPreferences back into HashMaps (Used so I can modify data  if necessary i.e., Calendar -> FullList -> Item Deleted)
    HashMap<String, String> transformJSONBackToMapData(String data) {return getGson().fromJson(data, getDataType()); }
    //Same as above but this method stores the Date - Size of list instead of Date - List Data
    HashMap<String, Integer> transformJSONBackToMapSize(String data) {return getGson().fromJson(data, getSizeType()); }

    //Helper methods
    SharedPreferences getSP() { return getSharedPreferences("sharedPrefs", MODE_PRIVATE); }
    SharedPreferences.Editor getEditor() { return getSP().edit(); }
    Type getDataType() {return new TypeToken<HashMap<String, String>>() {}.getType();}
    Type getSizeType() {return new TypeToken<HashMap<String, Integer>>() {}.getType();}
    Gson getGson() { return new Gson(); }
}
