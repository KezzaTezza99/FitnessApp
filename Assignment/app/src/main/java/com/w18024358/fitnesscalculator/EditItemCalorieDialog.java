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
import java.util.Objects;

//Used when the user wants to edit an item in the list
public class EditItemCalorieDialog extends AppCompatDialogFragment
{
    //Need to get the new information that the user wants to edit the FoodItem too
    private EditText newItemName;
    private EditText newItemQuantity;
    private EditText newItemCalories;

    private EditCalorieDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity(), "EditItem Calorie Dialog needs to be not null"));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_calorie_item_dialog, null);

        builder.setView(view)
                .setNegativeButton("cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Okay", (dialogInterface, i) -> {
                    //Not valid data
                    if(newItemName.getText().toString().length() == 0 ||
                            newItemQuantity.getText().toString().length() == 0 ||
                            newItemCalories.getText().toString().length() == 0)
                    {
                        Toast.makeText(getContext(), "You need to enter valid data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Assigning new variables
                    String itemName = newItemName.getText().toString();
                    int itemQuantity = Integer.parseInt(newItemQuantity.getText().toString());
                    int itemCalories = Integer.parseInt(newItemCalories.getText().toString());

                    listener.applyNewCalorieItem(itemName, String.valueOf(itemQuantity), String.valueOf(itemCalories));
                });
        newItemName = view.findViewById(R.id.editCaloriesItemName);
        newItemQuantity = view.findViewById(R.id.editCaloriesItemQuantity);
        newItemCalories = view.findViewById(R.id.editCaloriesItemCalories);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try
        {
            listener = (EditItemCalorieDialog.EditCalorieDialogListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context + " does not implement EditCalorieDialogListener");
        }
    }

    public interface EditCalorieDialogListener
    {
        void applyNewCalorieItem(String itemName, String itemQuantity, String totalCalories);
    }
}