package com.w18024358.fitnesscalculator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IsolationWorkoutListAdapter extends ArrayAdapter<IsolationWorkout>
{
    ArrayList<IsolationWorkout> isolationWorkout;
    Context myContext;
    TextView workoutName1, workoutName2, workoutName3;
    TextView workout1Set1, workout1Set2, workout1Set3, workout1Set4, workout1Set5;
    TextView workout2Set1, workout2Set2, workout2Set3, workout2Set4, workout2Set5;
    TextView workout3Set1, workout3Set2, workout3Set3, workout3Set4, workout3Set5;

    public IsolationWorkoutListAdapter(@NonNull Context context, @NonNull ArrayList<IsolationWorkout> objects)
    {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
        myContext = context;
        isolationWorkout = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_isolation_workout_list_view, parent, false);

        workoutName1 = view.findViewById(R.id.customIsolationWorkoutName);
        workoutName2 = view.findViewById(R.id.customIsolationWorkoutName2);
        workoutName3 = view.findViewById(R.id.customIsolationWorkoutName3);

        workout1Set1 = view.findViewById(R.id.customIsolationSet1Value);
        workout1Set2 = view.findViewById(R.id.customIsolationSet2Value);
        workout1Set3 = view.findViewById(R.id.customIsolationSet3Value);
        workout1Set4 = view.findViewById(R.id.customIsolationSet4Value);
        workout1Set5 = view.findViewById(R.id.customIsolationSet5Value);

        workout2Set1 = view.findViewById(R.id.customIsolationSet1ValueExercise2);
        workout2Set2 = view.findViewById(R.id.customIsolationSet2ValueExercise2);
        workout2Set3 = view.findViewById(R.id.customIsolationSet3ValueExercise2);
        workout2Set4 = view.findViewById(R.id.customIsolationSet4ValueExercise2);
        workout2Set5 = view.findViewById(R.id.customIsolationSet5ValueExercise2);

        workout3Set1 = view.findViewById(R.id.customIsolationSet1ValueExercise3);
        workout3Set2 = view.findViewById(R.id.customIsolationSet2ValueExercise3);
        workout3Set3 = view.findViewById(R.id.customIsolationSet3ValueExercise3);
        workout3Set4 = view.findViewById(R.id.customIsolationSet4ValueExercise3);
        workout3Set5 = view.findViewById(R.id.customIsolationSet5ValueExercise3);

        //Setting the Values
        workoutName1.setText(isolationWorkout.get(position).getWorkoutName1());
        workoutName2.setText(isolationWorkout.get(position).getWorkoutName2());
        workoutName3.setText(isolationWorkout.get(position).getWorkoutName3());

        workout1Set1.setText(isolationWorkout.get(position).getSet1());
        workout1Set2.setText(isolationWorkout.get(position).getSet2());
        workout1Set3.setText(isolationWorkout.get(position).getSet3());
        workout1Set4.setText(isolationWorkout.get(position).getSet4());
        workout1Set5.setText(isolationWorkout.get(position).getSet5());

        workout2Set1.setText(isolationWorkout.get(position).getSet1());
        workout2Set2.setText(isolationWorkout.get(position).getSet2());
        workout2Set3.setText(isolationWorkout.get(position).getSet3());
        workout2Set4.setText(isolationWorkout.get(position).getSet4());
        workout2Set5.setText(isolationWorkout.get(position).getSet5());

        workout3Set1.setText(isolationWorkout.get(position).getSet1());
        workout3Set2.setText(isolationWorkout.get(position).getSet2());
        workout3Set3.setText(isolationWorkout.get(position).getSet3());
        workout3Set4.setText(isolationWorkout.get(position).getSet4());
        workout3Set5.setText(isolationWorkout.get(position).getSet5());

        return view;
    }
}

