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

//TODO From Nick ----> Make code bulletproof, make the app as user-friendly as possible (i.e., already provide keyboards to type etc), add extra functionality that is unique to course
public class MainActivity extends AppCompatActivity {

    //Temp Code
    private Button CalorieButton;
    private Button FitnessButton;

    //Messing with SharedPreferences
    public static final String SHARED_PREFERENCES = "sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalorieButton = findViewById(R.id.Calorie);
        FitnessButton = findViewById(R.id.Fitness);

        SignIn().setOnClickListener(view -> checkUserDetails());
        LogIn().setOnClickListener(view -> openLoginActivity());

        CalorieButton.setOnClickListener(view -> openCalorieActivity());
        FitnessButton.setOnClickListener(view -> openFitnessActivity());

        SharedPreferences sp = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        sp.edit().clear().apply();
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
            openBMIActivity(MainEmail());
        }
    }

    //User has made an account
    private void openBMIActivity(EditText emailAddress)
    {
//        //Creating an intent
//        Intent intent = new Intent(this, BMIActivity.class);
//        intent.putExtra("EmailAddress", emailAddress.getText().toString());
//        startActivity(intent);

        //TODO Refactor
        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String msg = MainEmail().getText().toString();
        editor.putString("EmailTest", msg);
        editor.commit();
        Log.i("KYLE COMMITTING TO SHAREDPREFERENCES: ", msg);

        //TODO COME BACK TO THIS
        //Temp shit but will become final
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

    //Helper methods - means I don't need to pass around objects through parameters and can just call this method from anywhere in this activity
    //Returns Username
    private EditText MainEmail() { return findViewById(R.id.mainEmailField); }
    //Returns Password
    private EditText Password()
    {
        return findViewById(R.id.mainPasswordField);
    }
    //Returns Sign-in Button
    private Button SignIn()
    {
        return findViewById(R.id.mainSignInButton);
    }
    //Returns Login Button
    private Button LogIn()
    {
        return findViewById(R.id.mainLoginButton);
    }

    //TEMP
    private void openCalorieActivity()
    {
        Intent intent = new Intent(this, CalorieActivity.class);
        startActivity(intent);
    }

    private void openFitnessActivity()
    {
        Intent intent = new Intent(this, FitnessActivity.class);
        startActivity(intent);
    }
}