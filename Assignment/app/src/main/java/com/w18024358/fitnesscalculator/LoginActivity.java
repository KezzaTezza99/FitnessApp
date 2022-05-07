package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Onclick events
        getLoginButton().setOnClickListener(view -> checkLoginDetails(getUsername().getText().toString(), getPassword().getText().toString()));
        getSignUpButton().setOnClickListener(view -> openMainActivity());
    }

    //Checking to see if user details inputted are correct
    private void checkLoginDetails(String username, String password)
    {
        //Temp Code - Username kyle password pass -- Eventually will use a database to check these details
        if(username.equals("kyle") && password.equals("pass"))
        {
            //Detail's are correct to open Activity
            openHomeActivity();
        }
    }

    //Opening the BMI Activity (Not the main activity but is the main calculator I suppose)
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

    //Helper methods
    private EditText getUsername() { return findViewById(R.id.loginEmailField); }
    private EditText getPassword() { return findViewById(R.id.loginPasswordField); }
    private Button getLoginButton() { return findViewById(R.id.loginLoginButton); }
    private Button getSignUpButton() { return findViewById(R.id.loginSignUpButton); }
}