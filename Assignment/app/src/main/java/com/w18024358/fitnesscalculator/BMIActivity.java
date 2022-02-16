package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BMIActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        //Hiding BMI result fields until the user presses the calculate button
        bmiText().setVisibility(View.GONE);
        bmiField().setVisibility(View.GONE);

        //Hiding the Imperial Measurements by default
        bmiHeightFoot().setVisibility(View.GONE);
        bmiHeightInches().setVisibility(View.GONE);
        bmiWeightStone().setVisibility(View.GONE);
        bmiWeightLbs().setVisibility(View.GONE);

        //Temp test
        Switch toggle = findViewById(R.id.switch1);
        toggle.setOnClickListener(view -> changeMetrics());


        //Getting the Button
        bmiCalculateButton().setOnClickListener(view -> checkFieldsNotEmpty());
    }

    //Checking the fields are not empty before calling calculateBMI() ensures that the app doesn't crash if a field is empty
    private void checkFieldsNotEmpty()
    {
        //We need to know what measurement system the user is using
        //Metric
        if(currentMetricSelected() == 0) {
            //Only letting the user be able to calculate BMI if all fields filled in
            if (!bmiUserHeight().getText().toString().isEmpty() && !bmiUserWeight().getText().toString().isEmpty() && !bmiUserAge().getText().toString().isEmpty()) {
                calculateBMIPressed();
            } else {
                Toast.makeText(this, "Please ensure all fields are filled in", Toast.LENGTH_SHORT).show();
            }
        }
        //Imperial
        if(currentMetricSelected() == 1) {
            if(!bmiHeightFoot().getText().toString().isEmpty() && !bmiHeightInches().getText().toString().isEmpty() && !bmiWeightStone().getText().toString().isEmpty()
            && !bmiWeightLbs().getText().toString().isEmpty()) {
                calculateBMIPressed();
            } else {
                Toast.makeText(this, "Please ensure all fields are filled in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Called if the BMI Button is pressed
    private void calculateBMIPressed()
    {
        //Setting the visibility again
        bmiText().setVisibility(View.VISIBLE);
        bmiField().setVisibility(View.VISIBLE);

        //Creating a variable that will hold the answer
        double answer;

        if(currentMetricSelected() == 0)
        {
            //Use Metric - 0
            answer = calculateBMI(0);
        }
        else
        {
            answer = calculateBMI(1);
        }

        //Displaying the answer briefly as a popup
        Toast.makeText(this, "BMI: " + answer, Toast.LENGTH_LONG).show();
        System.out.println("BMI: " + answer);

        //Adding the answer to the BMI Field
        bmiField().setText("" + answer);
        bmiField().setEnabled(false);

        //Clearing the fields
        clearFields();

        //Changing the button to say re-calculate BMI
        bmiCalculateButton().setText("Recalculate BMI");

    }

    //Swap between using Metric and Imperial measuring systems
    private void changeMetrics()
    {
        //Change to Imperial Measurement(s)
        if(bmiUserHeight().getVisibility() == View.VISIBLE)
        {
            //Hiding the Metric Field
            bmiUserHeight().setVisibility(View.GONE);
            bmiUserWeight().setVisibility(View.GONE);
            //Showing the Imperial Field
            bmiHeightFoot().setVisibility(View.VISIBLE);
            bmiHeightInches().setVisibility(View.VISIBLE);
            bmiWeightStone().setVisibility(View.VISIBLE);
            bmiWeightLbs().setVisibility(View.VISIBLE);
            //Changing the Switch Text
            bmiMeasurementToggle().setText("Imperial");
        }
        //Change to Metric Measurement(s)
        else
        {
            //Hiding the Imperial Field
            bmiHeightFoot().setVisibility(View.GONE);
            bmiHeightInches().setVisibility(View.GONE);
            bmiWeightStone().setVisibility(View.GONE);
            bmiWeightLbs().setVisibility(View.GONE);
            //Showing the Metric Field
            bmiUserHeight().setVisibility(View.VISIBLE);
            bmiUserWeight().setVisibility(View.VISIBLE);
            //Changing the Switch Text
            bmiMeasurementToggle().setText("Metric");
        }
    }

    private double calculateBMI(int unit)
    {
        //0 metric / 1 imperial / -1 error
        if(unit == 0)
        {
            //Getting the user input - HEIGHT
            double heightCM = Double.parseDouble(bmiUserHeight().getText().toString());
            //Converting the CMs to Ms
            double height = heightCM / 100.0;
            //Getting the user input - WEIGHT
            double weight = Double.parseDouble(bmiUserWeight().getText().toString());
            //BMI using Metric -> kg / m^2
            double BMI = weight / (height * height);
            //Rounding the final answer to 1 decimal place
            return (double) Math.round(BMI * 10.0) / 10.0;
        }
        else if(unit == 1)
        {
            //Getting user input - HEIGHT
            double heightFeet = Double.parseDouble(bmiHeightFoot().getText().toString());
            double heightInches = Double.parseDouble(bmiHeightInches().getText().toString());
            //Need to convert the feet into inches
            double inches = heightFeet * 12.0;
            //Adding converted feet with the original inches inputted by the user
            double height = inches + heightInches;
            Toast.makeText(this, "Inches: " + height, Toast.LENGTH_LONG).show();

            //Getting user input - WEIGHT
            double weightStone = Double.parseDouble(bmiWeightStone().getText().toString());
            double weightLbs = Double.parseDouble(bmiWeightLbs().getText().toString());
            //Converting the Stone to Lbs
            double lbs = weightStone * 14.0;
            //Adding converted lbs with the original lbs inputted by the user
            double weight = lbs + weightLbs;

            //BMI using Imperial -> (lbs x 703) / inches^2
            double BMI = (weight * 703) / (height * height);
            //Rounding the final answer
            return (double)Math.round(BMI * 10.0) / 10.0;
        }
        else
        {
            //Adding this guard just in case
            Toast.makeText(this, "An error occurred while calculating BMI. Please try again", Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    //Helper Methods
    //Having methods to get the Text View to stop DRY - Kept repeating code
    private TextView bmiText()
    {
        return findViewById(R.id.bmiBMILabel);
    }
    private EditText bmiField()
    {
        return findViewById(R.id.bmiBMIResult);
    }
    //These three methods allow me to call them in checkFieldsNotEmpty without taking any parameters
    //such as checkFieldsNotEmpty(EditText height...) makes the method more reusable as can be called from anywhere (no local EditTexts needed)
    //also means I don't need three variables in onCreate() to call the method as the method takes in no parameters using this
    private EditText bmiUserHeight()
    {
        return findViewById(R.id.bmiHeightTextField);
    }
    private EditText bmiUserWeight()
    {
        return findViewById(R.id.bmiWeightTextField);
    }
    private EditText bmiUserAge()
    {
        return findViewById(R.id.bmiAgeTextField);
    }
    private Button bmiCalculateButton()
    {
        return findViewById(R.id.bmiCalculateBMIButton);
    }
    private EditText bmiHeightFoot() { return findViewById(R.id.bmiFootTextField); }
    private EditText bmiHeightInches() {return findViewById(R.id.bmiInchesTextField); }
    private EditText bmiWeightStone() { return findViewById(R.id.bmiStoneTextField); }
    private EditText bmiWeightLbs() { return findViewById(R.id.bmiLbsTextField); }
    private Switch bmiMeasurementToggle() { return findViewById(R.id.switch1); }

    //Temp Solution
    private int currentMetricSelected()
    {
        //Returns 0 for Metric and 1 for Imperial
        if(bmiUserHeight().getVisibility() == View.VISIBLE)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    //Clearing the fields of user input
    private void clearFields()
    {
        //Metric
        bmiUserHeight().setText("");
        bmiUserWeight().setText("");
        //Imperial
        bmiHeightFoot().setText("");
        bmiHeightInches().setText("");
        bmiWeightStone().setText("");
        bmiWeightLbs().setText("");
        //Common
        bmiUserAge().setText("");
    }
}