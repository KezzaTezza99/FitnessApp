package com.w18024358.fitnesscalculator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<PairOfInfo> {
    ArrayList<PairOfInfo> info;
    Context myContext;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PairOfInfo> objects)
    {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
        info = objects;
        myContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_row, parent, false);

        TextView quantity = view.findViewById(R.id.listViewQuantityOfItem);
        TextView theItem = view.findViewById(R.id.listViewItem);
        TextView amountOfCalories = view.findViewById(R.id.listViewItemTotalCalories);

        quantity.setText(info.get(position).getFname());
        theItem.setText(info.get(position).getSname());
        amountOfCalories.setText(info.get(position).getTest());

        return view;
    }
}
