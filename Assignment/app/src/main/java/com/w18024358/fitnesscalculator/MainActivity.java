package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Username
        EditText mainEmailAddress = findViewById(R.id.mainEmailField);
        //Password
        EditText mainPassword = findViewById(R.id.mainPasswordField);
        //Sign in Button
        Button mainSignInButton = findViewById(R.id.mainSignInButton);
        //Login Button
        Button mainLoginButton = findViewById(R.id.mainLoginButton);

        //Onclick event for sign-in button
        mainSignInButton.setOnClickListener(view -> checkUserDetails(mainEmailAddress, mainPassword));
        //Onclick event for login button
        mainLoginButton.setOnClickListener(view -> openLoginActivity());
    }

    //Used to check if the details entered are valid
    private void checkUserDetails(EditText emailAddress, EditText password)
    {
//        char specialCharacters[] = new char[6];
//        specialCharacters[0] = '!';
//        specialCharacters[1] = '?';
//        specialCharacters[2] = '@';
//        specialCharacters[3] = '#';
//        specialCharacters[4] = '$';

        //Should only take them to the BMI Activity if the username and password are both valid
        if(emailAddress.getText().toString().length() == 0 && password.getText().toString().length() == 0)
        {
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_LONG).show();
        }
        else if(emailAddress.getText().toString().length() < 4 && password.getText().toString().length() < 4)
        {
            Toast.makeText(this, "Please use a stronger Username and Password", Toast.LENGTH_LONG).show();
        }
        else if(emailAddress.getText().toString().length() < 4 && password.getText().toString().length() >= 4)
        {
            Toast.makeText(this, "Please use a stronger username", Toast.LENGTH_LONG).show();
        }
        else if(emailAddress.getText().toString().length() >= 4 && password.getText().toString().length() < 4)
        {
            Toast.makeText(this, "Please use a stronger password", Toast.LENGTH_LONG).show();
        }
//        else
//        {
//            for(int i = 0; i < password.getText().toString().length(); i++) {
//                for(int j = 0; j < specialCharacters.toString().length(); j++) {
//                    if (password.getText().toString().indexOf(i) != specialCharacters[j]) {
//                        Toast.makeText(this, "Password needs to contain a special character", Toast.LENGTH_LONG).show();
//                    }
//                    else{
//                        openBMIActivity(emailAddress);
//                    }
//                }
//            }
//        }
        else
        {
            openBMIActivity(emailAddress);
        }
    }

    //User has made an account
    private void openBMIActivity(EditText emailAddress)
    {
        //Creating an intent
        Intent intent = new Intent(this, BMIActivity.class);
        intent.putExtra("EmailAddress", emailAddress.getText().toString());
        startActivity(intent);
    }

    //User has an account and wants to log in
    private void openLoginActivity()
    {
        //Creating an intent
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}