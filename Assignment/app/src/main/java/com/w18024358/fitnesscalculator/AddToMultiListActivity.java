package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddToMultiListActivity extends AppCompatActivity {

    EditText nameField;
    EditText surnameField;
    EditText testField;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_multi_list);

        nameField = findViewById(R.id.addToListTextMulti);
        surnameField = findViewById(R.id.addToListText2Multi);
        testField = findViewById(R.id.addTestField);
        add = findViewById(R.id.addButtonToListMulti);

        add.setOnClickListener(view -> addToList());
    }

    private void addToList()
    {
        //Could rewrite this if statement
        //Getting the text inside the field
        if(nameField.getText().toString().length() > 0 && surnameField.getText().toString().length() > 0) {
            String name = nameField.getText().toString();
            String surname = surnameField.getText().toString();
            String test = testField.getText().toString();
            Intent intent = new Intent(this, CalorieActivity.class);
            intent.putExtra("Name", name);
            intent.putExtra("Surname", surname);
            intent.putExtra("TestStr", test);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "Please ensure you have entered a name", Toast.LENGTH_LONG).show();
        }
    }
}