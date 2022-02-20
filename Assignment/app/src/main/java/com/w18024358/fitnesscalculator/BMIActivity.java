package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.TimeZoneFormat;
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
    //TODO
    //Abstract away a lot of the helper functionality? Make a util class?
    //Create a math class that can do the calculations needed for me? Make the Activities less cluttered
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

        //Getting the Toggle and setting an onclick listener
        bmiMeasurementToggle().setOnClickListener(view -> changeMetrics());
        //Getting the Button and setting an onclick listener
        bmiCalculateButton().setOnClickListener(view -> checkFieldsNotEmpty());
    }

    //Checking the fields are not empty before calling calculateBMI() ensures that the app doesn't crash if a field is empty
    private void checkFieldsNotEmpty()
    {
        //Need to know what measurement system the user is using -> Metric = 0 and Imperial = 1
        if(currentMetricSelected() == 0)
        {
            //Only letting the user be able to calculate BMI if all fields filled in
            if (!bmiUserHeight().getText().toString().isEmpty() && !bmiUserWeight().getText().toString().isEmpty() && !bmiUserAge().getText().toString().isEmpty())
            {
                calculateBMIPressed();
            }
            else
            {
                Toast.makeText(this, "Please ensure all fields are filled in", Toast.LENGTH_SHORT).show();
            }
        }

        if(currentMetricSelected() == 1)
        {
            if(!bmiHeightFoot().getText().toString().isEmpty() && !bmiHeightInches().getText().toString().isEmpty() && !bmiWeightStone().getText().toString().isEmpty()
            && !bmiWeightLbs().getText().toString().isEmpty())
            {
                calculateBMIPressed();
            }
            else
            {
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
            convertCurrentMeasurements(0);
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
            convertCurrentMeasurements(1);
        }
    }

    //Current Task - FINISH
    //So far changing from KGs crashes the app
    //Very messy
    private void convertCurrentMeasurements(int metric)
    {
        System.out.println("Test before null test: " + bmiUserHeight().getText().toString());

        if(bmiUserHeight().getText().toString() == null) {
            System.out.println("String is null");
        }
        if(bmiUserHeight().getText().toString() != null) {
            System.out.println("String is not null!");
            String str = bmiUserHeight().getText().toString();
            System.out.println("String value: " + str);
        }
        if(bmiUserHeight().getText().toString() == "cm") {
            System.out.println("Height is equal to cm");
        }
        if(bmiUserHeight().getText().toString() == "")
        {
            System.out.println("string is actually ewual to empty ''");
        }
        if(bmiUserHeight().getText().toString() != "cm") {
            System.out.println("NOT EQUAL TO CM");
        }

        if(bmiUserHeight().getVisibility() == View.VISIBLE && metric == 0)
        {
            //Height
            if(bmiUserHeight().getText().toString() != "cm" || bmiUserHeight().getText().toString() != null)
            {
                //Converting cm to ft
                convertHeight(0);
            }
            if(bmiUserWeight().getText().toString() != "kg" || bmiUserWeight().getText().toString() != null)
            {
                convertWeight(0);
            }
        }
        if(bmiHeightFoot().getVisibility() == View.VISIBLE && metric == 1)
        {
            //Height
            if(bmiHeightFoot().getText().toString() != "foot" || bmiHeightFoot().getText().toString() != null &&
               bmiHeightInches().getText().toString() != "inches" || bmiHeightInches().getText().toString() != null)
            {
                convertHeight(1);
            }
            //Weight
            if(bmiWeightStone().getText().toString() != "stone" || bmiWeightStone().getText().toString() != null &&
                    bmiWeightLbs().getText().toString() != "lbs" || bmiWeightLbs().getText().toString() != null)
            {
                convertWeight(2);
            }
        }
    }

    //These methods so far stop the crashes
    private void convertHeight(int metric)
    {
        if(metric == 0)
        {
            //Metric to Imperial
            //Getting the currently inputted info
            double temp = Double.parseDouble(bmiUserHeight().getText().toString());
            //Clearing the fields clear so the metric doesn't stay if user goes back
            bmiUserHeight().setText("");
            //Metric (cm) to Imperial(ft / inch) ->  cm / 30.48
            double height = (temp / 30.48);
            System.out.println("Height: " + height);
            String ft = String.valueOf(height);
            //Needs to be a char if using double it converts the double from string incorrectly resulting in the incorrect data
            char foot = ft.charAt(0);
            char inch = ft.charAt(2);
            bmiHeightFoot().setText("" + foot);
            bmiHeightInches().setText("" + inch);
        }
    }

    //Defiantly needs a rework and refactoring
    private void convertWeight(int metric)
    {
        if(metric == 0)
        {
            //TODO Need to write a method here that accurately splits up KG to Stone if the stone is one decimal value before point
            double temp = Double.parseDouble(bmiUserWeight().getText().toString());
            //Clearing the fields
            bmiUserWeight().setText("");
            //Metric(kg) to Imperial(st / lbs) -> kg / 6.35 approx
            double weight = temp / 6.35;
            System.out.println("Weight: " + weight);
            String st = String.valueOf(weight);
            char stone1 = st.charAt(0);
            char stone2 = st.charAt(1);

            //Maybe round this value??
            char lbs1 = st.charAt(3);
            char lbs2 = st.charAt(4);

            String stone = String.valueOf(stone1) + String.valueOf(stone2);
            String lbs = String.valueOf(lbs1) + String.valueOf(lbs2);

            bmiWeightStone().setText("" + stone);
            bmiWeightLbs().setText("" + lbs);
        }
    }

    private double calculateBMI(int unit)
    {
        //0 metric - 1 imperial
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
        else
        {
            //Getting user input - HEIGHT
            double heightFeet = Double.parseDouble(bmiHeightFoot().getText().toString());
            double heightInches = Double.parseDouble(bmiHeightInches().getText().toString());
            //Need to convert the feet into inches
            double inches = heightFeet * 12.0;
            //Adding converted feet with the original inches inputted by the user
            double height = inches + heightInches;
            //Getting user input - WEIGHT
            double weightStone = Double.parseDouble(bmiWeightStone().getText().toString());
            double weightLbs = Double.parseDouble(bmiWeightLbs().getText().toString());
            //Converting the Stone to Lbs
            double lbs = weightStone * 14.0;
            //Adding converted lbs with the original lbs inputted by the user
            double weight = lbs + weightLbs;
            //BMI using Imperial -> (lbs x 703) / inches^2
            double BMI = ((weight * 703) / (height * height));
            //Rounding the final answer
            return (double)Math.round(BMI * 10.0) / 10.0;
        }
    }

    //Helper Methods
    //Returning Switches / EditText etc this way allows me to stop repeating code
    private Switch bmiMeasurementToggle() { return findViewById(R.id.bmiMetricToggle); }
    //These three methods allow me to call them in checkFieldsNotEmpty without taking any parameters
    //such as checkFieldsNotEmpty(EditText height...) makes the method more reusable as can be called from anywhere (no local EditTexts needed)
    //also means I don't need three variables in onCreate() to call the method as the method takes in no parameters using this
    private EditText bmiUserHeight() { return findViewById(R.id.bmiHeightTextField); }
    private EditText bmiUserWeight() { return findViewById(R.id.bmiWeightTextField); }
    private EditText bmiUserAge() { return findViewById(R.id.bmiAgeTextField); }
    private Button bmiCalculateButton() { return findViewById(R.id.bmiCalculateBMIButton); }
    private EditText bmiHeightFoot() { return findViewById(R.id.bmiFootTextField); }
    private EditText bmiHeightInches() {return findViewById(R.id.bmiInchesTextField); }
    private EditText bmiWeightStone() { return findViewById(R.id.bmiStoneTextField); }
    private EditText bmiWeightLbs() { return findViewById(R.id.bmiLbsTextField); }
    private TextView bmiText() { return findViewById(R.id.bmiBMILabel); }
    private EditText bmiField() { return findViewById(R.id.bmiBMIResult); }

    private int currentMetricSelected()
    {
        //Returns 0 for Metric and 1 for Imperial
        if(bmiUserHeight().getVisibility() == View.VISIBLE) { return 0; }
        else { return 1; }
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