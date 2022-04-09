package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Locale;

public class BMIActivity extends AppCompatActivity
{
    //TODO change the Imperial fields to actually use one value and then split that value to two
    //Every value before the decimal place (to the left) will be the first edittext field and everything to the right will be the second
    //TODO ensure that once changing metrics actually clears the field.
    //Not a necessary task but just tidies the application up, if the user changes the metric  and then goes back to the prev metric
    //and then clears the fields and changes back then the other metric actually still appears when it should now be blank
    //TODO the app crashes if you change metric and have no foot inputted and only have an inch
    //TODO If I clear the fields I can no longer calculate both weight and height if switching metrics only one at a time
    //TODO the values still need restricting i.e., inches should not go over 12 before going to a Ft
    //TODO If the SharedPreferences had data in it then should automatically display it

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        //Checking to see if the user entered any data previously if so displaying the information
        if(getIntent().getBooleanExtra("DataSaved", Boolean.TRUE))
        {
            Log.i("Data Loaded: ", "True");
            checkUserData();
        }
        else
        {
            Log.i("Data Loaded: ", "False");

            //Make this a method as repeating code
            //Hiding the Imperial Measurements by default
            bmiHeightFoot().setVisibility(View.GONE);
            bmiHeightInches().setVisibility(View.GONE);
            bmiWeightStone().setVisibility(View.GONE);
            bmiWeightLbs().setVisibility(View.GONE);
        }

        //Hiding BMI result fields until the user presses the calculate button
        bmiText().setVisibility(View.GONE);
        bmiField().setVisibility(View.GONE);

        //Getting the Toggle and setting an onclick listener
        bmiMeasurementToggle().setOnClickListener(view -> changeMetrics());
        //Getting the Button and setting an onclick listener
        bmiCalculateButton().setOnClickListener(view -> checkFieldsNotEmpty());
    }

    private void checkUserData()
    {
        boolean metric = getIntent().getBooleanExtra("Metric", Boolean.FALSE);
        boolean imperial = getIntent().getBooleanExtra("Imperial", Boolean.FALSE);

        //The user has entered metric measurements
        if(getIntent().getBooleanExtra("Metric", Boolean.TRUE) == metric)
        {
            //Hiding imperial fields first
            bmiHeightFoot().setVisibility(View.GONE);
            bmiHeightInches().setVisibility(View.GONE);
            bmiWeightStone().setVisibility(View.GONE);
            bmiWeightLbs().setVisibility(View.GONE);

            //Now putting the data entered into the fields
            bmiUserHeight().setText(getIntent().getStringExtra("HeightCM"));
            bmiUserWeight().setText((getIntent().getStringExtra("WeightKG")));
        }
        //User has entered imperial measurements
        else if(getIntent().getBooleanExtra("Imperial", Boolean.TRUE) == imperial)
        {
            //Hiding metric fields and showing the imperial --- TODO Make these methods as well
            bmiUserHeight().setVisibility(View.GONE);
            bmiUserWeight().setVisibility(View.GONE);

            bmiHeightFoot().setVisibility(View.VISIBLE);
            bmiHeightInches().setVisibility(View.VISIBLE);
            bmiWeightStone().setVisibility(View.VISIBLE);
            bmiWeightLbs().setVisibility(View.VISIBLE);

            //Setting the fields
            bmiHeightFoot().setText(getIntent().getStringExtra("HeightFT"));
            bmiHeightInches().setText(getIntent().getStringExtra("HeightInch"));
            bmiWeightStone().setText(getIntent().getStringExtra("WeightST"));
            bmiWeightLbs().setText(getIntent().getStringExtra("WeightLBS"));

            bmiMeasurementToggle().setText("Imperial");
            bmiMeasurementToggle().setChecked(true);
        }
    }

    //Is this bad?
    private MathUtility Util() { return new MathUtility(); }

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
        if(metric == 0)
        {
            //Height
            if (!bmiUserHeight().getText().toString().isEmpty())
            {
                //Converting cm to ft
                String answer[] = Util().convertHeight(0, Integer.parseInt(bmiUserHeight().getText().toString()), 0, 0);
                bmiHeightFoot().setText(answer[0]);
                bmiHeightInches().setText(answer[1]);

                Log.i("Answer[0] CM -> Ft: ", answer[0]);
                Log.i("Answer[1]: Cm -> Ft: ", answer[1]);

                clearMetricHeightFields();
            }
            //Weight
            if (!bmiUserWeight().getText().toString().isEmpty())
            {
                //KG to Stone
                String answer[] = Util().convertWeight(0, Integer.parseInt(bmiUserWeight().getText().toString()), 0 , 0);
                bmiWeightStone().setText(answer[0]);
                bmiWeightLbs().setText(answer[1]);

                Log.i("Answer[0] KG -> St: ", answer[0]);
                Log.i("Answer[1]: KG -> St: ", answer[1]);

                clearMetricWeightFields();
            }
        }
        if(metric == 1)
        {
            //Height
            if (!bmiHeightFoot().getText().toString().isEmpty() || !bmiHeightInches().getText().toString().isEmpty())
            {
                //Converting FT to CM
                String answer[] = Util().convertHeight(1, 0, Integer.parseInt(bmiHeightFoot().getText().toString()), Integer.parseInt(bmiHeightInches().getText().toString()));
                bmiUserHeight().setText(answer[0]);

                Log.i("Answer[0] Ft -> Cm: ", answer[0]);
                Log.i("Answer[1]: Ft -> Cm: ", answer[1]);

                clearImperialHeightFields();
            }
            //Weight
            if (!bmiWeightStone().getText().toString().isEmpty() || !bmiWeightLbs().getText().toString().isEmpty())
            {
                //Stone to KG
                String answer[] = Util().convertWeight(1, 0, Integer.parseInt(bmiWeightStone().getText().toString()), Integer.parseInt(bmiWeightLbs().getText().toString()));
                bmiUserWeight().setText(answer[0]);

                Log.i("Answer[0] St -> KG: ", answer[0]);
                Log.i("Answer[1]: St -> KG: ", answer[1]);

                clearImperialWeightFields();
            }
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
        if(bmiUserHeight().getVisibility() == View.VISIBLE)
        {
            return 0;
        }
        else
        {
            return 1;
        }
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

    private void clearMetricHeightFields() { bmiUserHeight().setText(""); }
    private void clearMetricWeightFields() { bmiUserWeight().setText(""); }
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

    private void loadData()
    {
        //TEMP
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("MetricToggle", Boolean.FALSE)) {
            bmiMeasurementToggle().setChecked(Boolean.FALSE);
            String height = sharedPreferences.getString("HeightCm", "");
            bmiUserHeight().setText(height);
            String weight = sharedPreferences.getString("WeightKg", "");
            bmiUserWeight().setText(weight);

            //Need to ensure metric fields visible and imperial invisible
            bmiUserHeight().setVisibility(View.VISIBLE);
            bmiUserWeight().setVisibility(View.VISIBLE);

            bmiHeightFoot().setVisibility(View.INVISIBLE);
            bmiHeightInches().setVisibility(View.INVISIBLE);
            bmiWeightStone().setVisibility(View.INVISIBLE);
            bmiWeightLbs().setVisibility(View.INVISIBLE);

            Log.i("Metric Measurements: ", height + weight);
        } else if (sharedPreferences.getBoolean("MetricToggle", Boolean.TRUE)) {
            //Doesn't reach here yet???? WHY
            //Changing the text to Imperial and turning the toggle on
            bmiMeasurementToggle().setText("Imperial");
            bmiMeasurementToggle().setChecked(Boolean.TRUE);
            bmiHeightFoot().setText(sharedPreferences.getString("HeightFt", ""));
            bmiHeightInches().setText(sharedPreferences.getString("HeightInch", ""));
            bmiWeightStone().setText(sharedPreferences.getString("WeightSt", ""));
            bmiWeightLbs().setText(sharedPreferences.getString("WeightLbs", ""));

            Log.i("Imperial Measurements: ", sharedPreferences.getString("HeightFt", "") + sharedPreferences.getString("HeightInch", ""));
            Log.i("Imperial Measurements: ", sharedPreferences.getString("WeightSt", "") + sharedPreferences.getString("WeightLbs", ""));

            //Need to ensure metric fields invisible and imperial visible
            bmiUserHeight().setVisibility(View.INVISIBLE);
            bmiUserWeight().setVisibility(View.INVISIBLE);

            bmiHeightFoot().setVisibility(View.VISIBLE);
            bmiHeightInches().setVisibility(View.VISIBLE);
            bmiWeightStone().setVisibility(View.VISIBLE);
            bmiWeightLbs().setVisibility(View.VISIBLE);
        }
    }
}