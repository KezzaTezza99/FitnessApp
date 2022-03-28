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
import java.util.Locale;

public class BMIActivity extends AppCompatActivity {
    //TODO change the Imperial fields to actually use one value and then split that value to two
    //Every value before the decimal place (to the left) will be the first edittext field and everything to the right will be the second
    //TODO ensure that once changing metrics actually clears the field.
    //Not a necessary task but just tidies the application up, if the user changes the metric  and then goes back to the prev metric
    //and then clears the fields and changes back then the other metric actually still appears when it should now be blank
    //TODO the app crashes if you change metric and have no foot inputted and only have an inch
    //TODO If I clear the fields I can no longer calculate both weight and height if switching metrics only one at a time
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
    //Or simply do everything here and not have to use different methods
    private void convertCurrentMeasurements(int metric)
    {
        if(metric == 0) {
            //Height
            if (!bmiUserHeight().getText().toString().isEmpty()) {
                //Converting cm to ft
                convertHeight(0);
            }
            //Weight
            if (!bmiUserWeight().getText().toString().isEmpty()) {
                convertWeight(0);
            }
        }
        if(metric == 1) {
            //Height
            if (!bmiHeightFoot().getText().toString().isEmpty() || !bmiHeightInches().getText().toString().isEmpty()) {
                convertHeight(1);
            }
            //Weight
            if (!bmiWeightStone().getText().toString().isEmpty() || !bmiWeightLbs().getText().toString().isEmpty()) {
                convertWeight(1);
            }
        }
    }

    //These methods so far stop the crashes
    private void convertHeight(int metric)
    {
        if(metric == 0)
        {
            //Getting the currently inputted cm and transferring that into inches
            double inches = Util().CmToInch(Double.parseDouble(bmiUserHeight().getText().toString()));
            //Now changing the inches into feet
            double foot = Util().InchToFoot(inches);

            clearMetricHeightFields();

            //Storing the result in a string that will be split into feet and inches
            String results[] = Util().BeforeAfterDecimalPoint(foot);
            //Displaying the relative information in the correct EditText(s)
            bmiHeightFoot().setText("" + results[0]);
            bmiHeightInches().setText("" + results[1]);
        }

        if(metric == 1)
        {
            double ft;
            //Setting this value to 0 so if the user doesn't add a measurement for the inch EditText field and then changes metrics the application would crash
            //with out ensuring this value is set if bmiHeightInches() == null -> this way also means no necessary check
            double inches = 0;

            //Need to check the foot and the inch fields before calculating the CM
            if(bmiHeightInches().getText().toString().length() != 0)
            {
                //As inches here will not be accurate, i.e lets say this returns 10 inches it should be treat as 0.10 inches and not 10
                //as the conversion back to cm will lead to a value to high so need to multiply this value by 0.01
                inches = Double.parseDouble(bmiHeightInches().getText().toString()) * 0.01;
                System.out.println("Inside inches != 0, Inches: " + inches);
            }

            double foot;
            if(!bmiHeightFoot().getText().toString().isEmpty())
            {
                foot = Double.parseDouble(bmiHeightFoot().getText().toString());
            } else { foot = 0; }

            clearImperialHeightFields();

            DecimalFormat decimalFormat = new DecimalFormat("##.##");
            ft = Double.parseDouble(decimalFormat.format(foot + inches));

            bmiUserHeight().setText("" + Util().FootToCm(ft));
        }
    }

    //Defiantly needs a rework and refactoring
    private void convertWeight(int metric)
    {
        if(metric == 0)
        {
            //TODO Need to write a method here that accurately splits up KG to Stone if the stone is one decimal value before point
            double lbs = Util().KgToLbs(Double.parseDouble(bmiUserWeight().getText().toString()));
            double stone = Util().LbsToStone(lbs);

            clearMetricWeightFields();

            String results[] = Util().BeforeAfterDecimalPoint(stone);
            bmiWeightStone().setText("" + results[0]);
            bmiWeightLbs().setText("" + results[1]);
        }

        if(metric == 1)
        {
            double st;
            double lbs = 0;

            //Need to check the foot and the inch fields before calculating the CM
            if(bmiWeightLbs().getText().toString().length() != 0)
            {
                lbs = Double.parseDouble(bmiWeightLbs().getText().toString()) * 0.01;
            }

            double stone;
            if(!bmiWeightStone().getText().toString().isEmpty())
            {
                stone = Double.parseDouble(bmiWeightStone().getText().toString());
            } else { stone = 0; }

            clearImperialWeightFields();

            DecimalFormat decimalFormat = new DecimalFormat("##.##");
            st = Double.parseDouble(decimalFormat.format(stone + lbs));

            bmiUserWeight().setText("" + Util().StoneToKg(st));
        }
    }

    //TODO Add the code to Math Utility????
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

    //Looking to builder pattern so I can use one method to clear fields based on input??
    //Add a param here and maybe have 0 clear all fields, 1 metric fields and then 2 imperial fields?
    //Clearing the fields of user input
    private void clearFields()
    {
        //Clear all fields
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

    private void clearMetricHeightFields()
    {
        bmiUserHeight().setText("");
    }

    private void clearMetricWeightFields()
    {
        bmiUserWeight().setText("");

    }

    private void clearImperialHeightFields()
    {
        bmiHeightFoot().setText("");
        bmiHeightInches().setText("");
    }

    private void clearImperialWeightFields()
    {
        bmiWeightStone().setText("");
        bmiWeightLbs().setText("");
    }
}