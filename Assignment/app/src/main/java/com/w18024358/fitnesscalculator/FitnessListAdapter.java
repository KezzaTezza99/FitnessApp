package com.w18024358.fitnesscalculator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

//TODO finish this
public class FitnessListAdapter extends ArrayAdapter<Workouts>
{
    //Stops the lists having both workouts in them
    ArrayList<Workouts> primaryWorkout;
    ArrayList<Workouts> secondaryWorkout;

    Context myContext;

    TextView workoutName;
    TextView warmupSet;
    TextView workingSet1;
    TextView workingSet2;
    TextView workingSet3;

    boolean primary;

    public FitnessListAdapter(@NonNull Context context, @NonNull ArrayList<Workouts> objects, boolean primary)
    {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
        myContext = context;
        this.primary = primary;

        if(primary)
        {
            primaryWorkout = objects;
        }
        else
        {
            secondaryWorkout = objects;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_compound_workout_list_view, parent, false);

        workoutName = view.findViewById(R.id.customWorkoutExerciseName);
        warmupSet = view.findViewById(R.id.customWorkoutWarmupAmount);
        workingSet1 = view.findViewById(R.id.customWorkoutWorkingSet1Amount);
        workingSet2 = view.findViewById(R.id.customWorkoutWorkingSet2Amount);
        workingSet3 = view.findViewById(R.id.customWorkoutWorkingSet3Amount);

        if(primary) {
            workoutName.setText(primaryWorkout.get(position).getWorkoutName());
            warmupSet.setText(primaryWorkout.get(position).getWarmupSet());
            workingSet1.setText(primaryWorkout.get(position).getWorkingSet1());
            workingSet2.setText(primaryWorkout.get(position).getWorkingSet2());
            workingSet3.setText(primaryWorkout.get(position).getWorkingSet3());
        }
        else
        {
            workoutName.setText(secondaryWorkout.get(position).getWorkoutName());
            warmupSet.setText(secondaryWorkout.get(position).getWarmupSet());
            workingSet1.setText(secondaryWorkout.get(position).getWorkingSet1());
            workingSet2.setText(secondaryWorkout.get(position).getWorkingSet2());
            workingSet3.setText(secondaryWorkout.get(position).getWorkingSet3());
        }
        return view;
    }
}
