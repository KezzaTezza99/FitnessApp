package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity
{
    //Messing with SharedPreferences
    public static final String SHARED_PREFERENCES = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignIn().setOnClickListener(view -> checkUserDetails());
        LogIn().setOnClickListener(view -> openLoginActivity());
    }

    private void checkUserDetails()
    {
        //This is temp code
        //Should only take them to the BMI Activity if the username and password are both valid
        if(MainEmail().getText().toString().length() == 0 && Password().getText().toString().length() == 0)
        {
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_LONG).show();
        }
        else if(MainEmail().getText().toString().length() < 4 && Password().getText().toString().length() < 4)
        {
            Toast.makeText(this, "Please use a stronger Username and Password", Toast.LENGTH_LONG).show();
        }
        else if(MainEmail().getText().toString().length() < 4 && Password().getText().toString().length() >= 4)
        {
            Toast.makeText(this, "Please use a stronger username", Toast.LENGTH_LONG).show();
        }
        else if(MainEmail().getText().toString().length() >= 4 && Password().getText().toString().length() < 4)
        {
            Toast.makeText(this, "Please use a stronger password", Toast.LENGTH_LONG).show();
        }
        else
        {
            openBMIActivity();
        }
    }

    //User has signed in for the first time
    private void openBMIActivity()
    {
        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String msg = MainEmail().getText().toString();
        editor.putString("Username", msg).apply();

        Intent intent = new Intent(this, FirstUseLoginActivity.class);
        intent.putExtra("Username", MainEmail().getText().toString());
        startActivity(intent);
    }

    //User has an account and wants to log in
    private void openLoginActivity()
    {
        //Creating an intent
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private EditText MainEmail() { return findViewById(R.id.mainEmailField); }
    private EditText Password() { return findViewById(R.id.mainPasswordField); }
    private Button SignIn() { return findViewById(R.id.mainSignInButton); }
    private Button LogIn() { return findViewById(R.id.mainLoginButton); }
}