package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class BMIActivity extends AppCompatActivity {
    //TODO Abstract away a lot of the helper functionality? Make a util class?
    //Create a math class that can do the calculations needed for me? Make the Activities less cluttered
    //TODO Fix the issue with swapping metrics decreases the user's inputted information each time
    //I think its caused by the second measurement field
    //TODO change the Imperial fields to actually use one value and then split that value to two
    //Every value before the decimal place (to the left) will be the first edittext field and everything to the right will be the second

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

    //Is this bad?
    private MathUtility Util()
    {
        return new MathUtility();
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
    //TODO make this actually use the Math Utility
    private void convertCurrentMeasurements(int metric)
    {
        if(metric == 0) {
            //Height
            if (!bmiUserHeight().getText().toString().isEmpty()) {
                //Converting cm to ft
                convertHeight(0);

                //double inches = Util().CmToInch(Double.parseDouble(bmiUserHeight().getText().toString()));
                //System.out.println("CM to Inches: " + inches);
                //System.out.println("Inches to Feet: " + Util().InchToFoot(inches));

            }
            //Weight
            if (!bmiUserWeight().getText().toString().isEmpty()) {
                convertWeight(0);

                //double lbs = Util().KgToLbs(Double.parseDouble(bmiUserWeight().getText().toString()));
                //System.out.println("KG to Lbs: " + lbs);
                //System.out.println("Lbs to Stone: " + Util().LbsToStone(lbs));
                //double stone = Util().LbsToStone(lbs);
                //bmiWeightStone().setText("" + stone);
            }
        }
        if(metric == 1) {
            //Height
            if (!bmiHeightFoot().getText().toString().isEmpty() || !bmiHeightInches().getText().toString().isEmpty()) {
                convertHeight(1);

                //bmiUserHeight().setText("" + Util().FootToCm(Double.parseDouble(bmiHeightFoot().getText().toString())));
            }
            //Weight
            if (!bmiWeightStone().getText().toString().isEmpty() || !bmiWeightLbs().getText().toString().isEmpty()) {
                convertWeight(1);

                //bmiUserWeight().setText("" + Util().StoneToKg(Double.parseDouble(bmiWeightStone().getText().toString())));
            }
        }
    }

    //These methods so far stop the crashes
    private void convertHeight(int metric)
    {
        if(metric == 0)
        {
//            //Metric to Imperial
//            //Getting the currently inputted info
//            double temp = Double.parseDouble(bmiUserHeight().getText().toString());
//            //Clearing the fields clear so the metric doesn't stay if user goes back
//            bmiUserHeight().setText("");
//            //Metric (cm) to Imperial(ft / inch) ->  cm / 30.48
//            double height = temp / 30.48;
//            System.out.println("Height: " + height);
//            String ft = String.valueOf(height);
//            //Needs to be a char if using double it converts the double from string incorrectly resulting in the incorrect data
//            char foot = ft.charAt(0);
//            char inch = ft.charAt(2);
//            bmiHeightFoot().setText("" + foot);
//            bmiHeightInches().setText("" + inch);

            //Setting the field
            //System.out.println("Testing the decimal split method: " + Util().BeforeAfterDecimalPoint(bmiHeightFoot().getText().toString()));
            double inches = Util().CmToInch(Double.parseDouble(bmiUserHeight().getText().toString()));
            //bmiHeightFoot().setText("" + Util().InchToFoot(inches));
            double foot = Util().InchToFoot(inches);
            //bmiHeightFoot().setText("" + Util().BeforeAfterDecimalPoint(String.valueOf(foot)));

            String results[] = Util().BeforeAfterDecimalPoint(foot);
            bmiHeightFoot().setText("" + results[0]);
            bmiHeightInches().setText("" + results[1]);
        }

        //DO I REALLY NEED TO CLEAR THE FIELDS??
        if(metric == 1)
        {
//            //Imperial to Metric
//            //Getting the current inputted info
//            double footTemp = Double.parseDouble(bmiHeightFoot().getText().toString());
//            //As this value can potentially be empty i.e 5ft then setting to 0 else getting the inputted value i.e 5ft 2
//            double inchesTemp;
//            //Needed if inches is valid
//            double heightInch;
//            if(!bmiHeightInches().getText().toString().isEmpty())
//            {
//                inchesTemp = Double.parseDouble(bmiHeightInches().getText().toString());
//                //I know that the field is used so clearing both here
//                bmiHeightFoot().setText("");
//                bmiHeightInches().setText("");
//                //Imperial inches to cm -> inch * 2.54
//                heightInch = inchesTemp * 2.54;
//            }
//            else
//            {
//                inchesTemp = 0;
//                //Else just need to clear foot field
//                bmiHeightFoot().setText("");
//                //Inches not needed
//                heightInch = 0;
//            }
//            //Imperial (ft / inches) to Metric (cm) -> ft * 30.48
//            double height = (footTemp * 30.48) + heightInch;
//            bmiUserHeight().setText("" + height);

            double inches;
            double ft;
            //Need to check the foot and the inch fields before calculating the CM
            if(bmiHeightInches().getText().toString().length() != 0)
            {
                //As inches here will not be accurate, i.e lets say this returns 10 inches it should be treat as 0.10 inches and not 10
                //as the conversion back to cm will lead to a value to high so need to multiply this value by 0.01
                inches = Double.parseDouble(bmiHeightInches().getText().toString()) * 0.01;
                System.out.println("Inside inches != 0, Inches: " + inches);
            }
            else
            {
                inches = 0;
                System.out.println("Inside else inches != 0, Inches: " + inches);
            }
            System.out.println("Outside inches != 0, Inches: " + inches);

            double foot = Double.parseDouble(bmiHeightFoot().getText().toString());

            //Need to add them differently
            if(inches != 0) {
                //Divide by mass 12
                //double inchToFeet = inches / 12.0;
                double inchToFeet = inches;
                System.out.println("Inside inches != 0, InchToFeet: " + inchToFeet);
                System.out.print("Test addition 1: " + (foot + inchToFeet));

                DecimalFormat decimalFormat = new DecimalFormat("##.##");
                ft = Double.parseDouble(decimalFormat.format(foot + inchToFeet));
                System.out.println("Test 2: " + ft);

                System.out.println("Inside inches != 0, ft: " + ft);
            } else {
                ft = foot;
                System.out.println("Inside else inches != 0, ft: " + ft);
            }

            System.out.println("Foot: " + foot + " Inches: " + inches + "Adding the strings: " + ft);
            System.out.println("Foot: " + ft + "Should be: " + Util().FootToCm(ft));
            double answer = Util().FootToCm(ft);
            System.out.println("ANSWER: " + answer);

            bmiUserHeight().setText("" + answer);
            //bmiUserHeight().setText("" + Util().FootToCm(Double.parseDouble(bmiHeightFoot().getText().toString())));
        }
    }

    //Defiantly needs a rework and refactoring
    private void convertWeight(int metric)
    {
        if(metric == 0)
        {
            //TODO Need to write a method here that accurately splits up KG to Stone if the stone is one decimal value before point
//            double temp = Double.parseDouble(bmiUserWeight().getText().toString());
//            //Clearing the fields
//            bmiUserWeight().setText("");
//            //Metric(kg) to Imperial(st / lbs) -> kg / 6.35 approx
//            double weight = temp / 6.35;
//            System.out.println("Weight: " + weight);
//            String st = String.valueOf(weight);
//            char stone1 = st.charAt(0);
//            char stone2 = st.charAt(1);
//
//            //NOW THESE BREAKS MORE?
//            //Maybe round this va2lue??
////            char lbs1, lbs2;
////            //This causes crash
////            if(st.length() == 4) {
////                lbs1 = st.charAt(3);
////            } else { lbs1 = 0; }
////            if(st.length() == 5) {
////                 lbs2 = st.charAt(4);
////            } else {lbs2 = 0; }
//            char lbs1 = st.charAt(3);
//            char lbs2 = st.charAt(4);
//
//            String stone = String.valueOf(stone1) + String.valueOf(stone2);
//            String lbs = String.valueOf(lbs1) + String.valueOf(lbs2);
//
//            bmiWeightStone().setText("" + stone);
//            bmiWeightLbs().setText("" + lbs);
//
            double lbs = Util().KgToLbs(Double.parseDouble(bmiUserWeight().getText().toString()));
            bmiWeightStone().setText("" + Util().LbsToStone(lbs));
        }

        if(metric == 1)
        {
//            double temp = Double.parseDouble(bmiWeightStone().getText().toString());
//            //Clearing the fields
//            bmiWeightStone().setText("");
//            //Imperial (stone / lbs) to Metric (kg) -> Stone and lbs * 6.35
//            double weight = temp * 6.35;
//            System.out.println("Stone -> KG Weight: " + weight);
//            bmiUserWeight().setText("" + weight);

            bmiUserWeight().setText("" + Util().StoneToKg(Double.parseDouble(bmiWeightStone().getText().toString())));
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