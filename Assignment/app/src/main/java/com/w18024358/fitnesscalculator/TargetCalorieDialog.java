package com.w18024358.fitnesscalculator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TargetCalorieDialog extends AppCompatDialogFragment
{
    private EditText usersCalorieTarget;
    private CalorieDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.target_calorie_dialog, null);

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        //User hasn't entered a valid string
                        if(usersCalorieTarget.getText().toString().length() == 0)
                        {
                            Toast.makeText(getContext(), "You need to enter a valid number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String calorieTarget = usersCalorieTarget.getText().toString();
                        listener.applyUsersCalorieTarget(calorieTarget);
                    }
                });
        usersCalorieTarget = view.findViewById(R.id.targetCalorieValue);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try
        {
            listener = (CalorieDialogListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " does not implement CalorieDialogListener");
        }
    }

    public interface CalorieDialogListener
    {
        void applyUsersCalorieTarget(String target);
    }
}
