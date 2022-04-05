package com.w18024358.fitnesscalculator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CalendarDialog extends AppCompatDialogFragment
{
    //Probably need a database to be set up here if I want to be able utilize the calendar function
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater  = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.calendar, null);

        builder.setView(view)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO The user would be able to do some stuff here but not to sure what yet
                        Toast.makeText(getContext(), "To be honest not sure what to add here", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }
}
