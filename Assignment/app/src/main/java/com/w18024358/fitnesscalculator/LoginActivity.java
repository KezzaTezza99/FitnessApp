package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Temp Code - Username kyle password pass
        //Eventually will use a database to check these details
        EditText loginUsername = findViewById(R.id.loginEmailField);
        EditText loginPassword = findViewById(R.id.loginPasswordField);

        //Login button
        Button loginLoginButton = findViewById(R.id.loginLoginButton);
        //Sign-in button
        Button loginSignUpButton = findViewById(R.id.loginSignUpButton);

        //Onclick events
        loginLoginButton.setOnClickListener(view -> checkLoginDetails(loginUsername, loginPassword));
        loginSignUpButton.setOnClickListener(view -> openMainActivity());
    }

    //Checking to see if user details inputted are correct
    private void checkLoginDetails(EditText username, EditText password)
    {
        if(username.getText().toString().contentEquals("kyle") && password.getText().toString().contentEquals("pass"))
        {
            //Detail's are correct to open Activity
            openHomeActivity();
        }
    }

    private void openHomeActivity()
    {
        Intent intent = new Intent(this, BMIActivity.class);
        startActivity(intent);
    }

    private void openMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}