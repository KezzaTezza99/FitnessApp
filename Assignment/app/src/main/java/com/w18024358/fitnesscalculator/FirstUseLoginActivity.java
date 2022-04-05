package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//Activity's purpose is to capture some important user details once they have logged in
//will save the details to the SharedPreferences and will be used for the Calorie Total and Fitness Page
//User will be given the option to continue without completing the necessary fields and can then
//add these from the profile page when the user deems more appropriate
public class FirstUseLoginActivity extends AppCompatActivity
{
    //TODO the user should be able to submit data but if they don't want to thats also fine
    TextView welcomeMsg;

    EditText heightCm;
    EditText weightKg;
    EditText heightFt;
    EditText heightInch;
    EditText weightStone;
    EditText weightLbs;

    Switch metricToggle;
    Button continueButton;

    //TODO still shows both fields
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use_login);

        welcomeMsg = findViewById(R.id.firstLoginWelcomeMessage);

        //Metric
        heightCm = findViewById(R.id.firstLoginHeightCm);
        weightKg = findViewById(R.id.firstLoginWeightKg);

        //Imperial
        heightFt = findViewById(R.id.firstLoginHeightFeet);
        heightInch = findViewById(R.id.firstLoginHeightInches);
        weightStone = findViewById(R.id.firstLoginWeightStone);
        weightLbs = findViewById(R.id.firstLoginWeightLbs);

        heightFt.setVisibility(View.GONE);
        heightInch.setVisibility(View.GONE);
        weightStone.setVisibility(View.GONE);
        weightLbs.setVisibility(View.GONE);

        metricToggle = findViewById(R.id.firstLoginMetricSwitch);
        metricToggle.setOnClickListener(view -> changeMetrics());

        //Getting the username through saving intent
//        String username = getIntent().getStringExtra("Username");
//        welcomeMsg.setText(String.format("Welcome %s", username));

        //Getting the username through savedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        welcomeMsg.setText(sharedPreferences.getString("EmailTest", ""));

        //TO DELETE SHARED PREFERENCES NEED TO USE yourSharedPreferences.edit().clear().apply()

        continueButton = findViewById(R.id.firstLoginContinueButton);
        continueButton.setOnClickListener(view -> openBMIActivity());
    }

    private void changeMetrics()
    {
        if(heightCm.getVisibility() == View.VISIBLE)
        {
            heightCm.setVisibility(View.INVISIBLE);
            weightKg.setVisibility(View.INVISIBLE);

            heightFt.setVisibility(View.VISIBLE);
            heightInch.setVisibility(View.VISIBLE);
            weightStone.setVisibility(View.VISIBLE);
            weightLbs.setVisibility(View.VISIBLE);

            metricToggle.setText("Imperial");
        }
        else
        {
            heightCm.setVisibility(View.VISIBLE);
            weightKg.setVisibility(View.VISIBLE);

            heightFt.setVisibility(View.INVISIBLE);
            heightInch.setVisibility(View.INVISIBLE);
            weightStone.setVisibility(View.INVISIBLE);
            weightLbs.setVisibility(View.INVISIBLE);

            metricToggle.setText("Metric");
        }
    }

    private void openBMIActivity()
    {
        //Need to check both metric / imperial but separately otherwise will not submit details correctly
        if(metricToggle.getText().equals("Metric") && !heightCm.getText().toString().isEmpty()
                                                    || !weightKg.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Subbed Metric", Toast.LENGTH_SHORT).show();
        }
        else if(metricToggle.getText().equals("Imperial") && !heightFt.getText().toString().isEmpty()
                || !weightStone.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Subbed Imperial", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Not subbed", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String msg = "If you wish to gain the best experience from this application " +
                    "please fill in the necessary details. " + " You can also add details from profile page";

            builder.setMessage(msg)
                    .setPositiveButton("Yes", null)
                    .setNegativeButton("No", null);
            builder.show();
        }
    }
}