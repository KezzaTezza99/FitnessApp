//package com.w18024358.fitnesscalculator;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import java.util.ArrayList;
//
//public class FoodItemListAdapter extends ArrayAdapter<FoodItem>
//{
//    ArrayList<PairOfInfo> info;
//    Context myContext;
//
//    public FoodItemListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FoodItem> objects)
//    {
//        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
//        info = objects;
//        myContext = context;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position,View convertView, ViewGroup parent)
//    {
//        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.custom_row, parent, false);
//
//        TextView fname = view.findViewById(R.id.listViewFname);
//        TextView sname = view.findViewById(R.id.listViewSname);
//
//        fname.setText(info.get(position).getFname());
//        sname.setText(info.get(position).getSname());
//
//        return view;
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////        //TODO Get these values dynamically
////        //Getting the Food Item Information
////        int quantityOfItem = getItem(position).getQuantity();
////        String nameOfItem = getItem(position).getItem();
////        int calorieOfItem = getItem(position).getCalories();
////
////        //Creating a FoodItem Object with this information
////        FoodItem foodItem = new FoodItem(quantityOfItem, nameOfItem, calorieOfItem);
////
////        LayoutInflater inflater = LayoutInflater.from(context);
////        convertView = inflater.inflate(resource, parent, false);
////
////        //Declaring the fields inside the ListView
////        EditText quantity = convertView.findViewById(R.id.calorieLayoutQuantityOfItem);
////        EditText name = convertView.findViewById(R.id.calorieLayoutItem);
////        EditText calorie = convertView.findViewById(R.id.calorieLayoutCalorie);
////
////        //Setting the values
////        quantity.setText(quantityOfItem);
////        name.setText(nameOfItem);
////        calorie.setText(calorieOfItem);
////
////        return convertView;