package com.w18024358.fitnesscalculator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

//TODO finish this
public class FitnessListAdapter extends ArrayAdapter<Workouts>
{
    ArrayList<Workouts> workouts;
    Context myContext;
    TextView workoutName;
    TextView numberOfReps;
    TextView numberOfSets;
    TextView weight;

    public FitnessListAdapter(@NonNull Context context, @NonNull ArrayList<Workouts> objects)
    {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
        workouts = objects;
        myContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_compound_workout_list_view, parent, false);

//        TextView workoutName = view.findViewById(R.id.listViewQuantityOfItem);
//        TextView numberOfReps = view.findViewById(R.id.listViewItem);
//        TextView numberOfSets = view.findViewById(R.id.listViewItemTotalCalories);
//        TextView weight;

        //workoutName.setText(workouts.get(position).getWorkoutName());
        return view;
    }
}
