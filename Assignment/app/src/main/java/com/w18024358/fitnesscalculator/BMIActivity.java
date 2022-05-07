package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.Locale;

//Activity is responsible for calculating the BMI of the user
public class BMIActivity extends AppCompatActivity
{
    //TODO If the SharedPreferences had data in it then should automatically display it
    //TODO fix the BMI page when you access it by logging in - if no details show nothing if details then show the weight / height
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        hideImperialFields();
        hideBMIFields();

        //Need to hide the information as it can only be displayed once BMI is calculated
        getResultsInfo().setVisibility(View.INVISIBLE);
        //Checking to see if any information is saved, if so should display it
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        boolean data = sharedPreferences.getBoolean("User Info Saved", Boolean.FALSE);

        //Getting the Toggle and setting an onclick listener
        bmiMeasurementToggle().setOnClickListener(view -> changeMetrics());
        //Getting the Button and setting an onclick listener
        bmiCalculateButton().setOnClickListener(view -> checkFieldsNotEmpty());

        //Bottom Navigation menu (main navigation)
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainBottomNavigationMenu);
        bottomNavigationView.setSelectedItemId(R.id.bmiActivityMenu);

        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            Intent intent;
            switch(item.getItemId())
            {
                case R.id.bmiActivityMenu:
                    return true;
                case R.id.calorieActivityMenu:
                    intent = new Intent(this, CalorieActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.fitnessActivityMenu:
                    intent = new Intent(this, FitnessActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
        });

        //Drawer Navigation (secondary navigation) would be used for settings and logging out etc if the app was to be extended
        //Getting the layout that allows the draw to move from "off screen" to on screen
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        //The strings that I didn't pass to constructor provide accessibility for blind people should implement these for more accessibility need TODO
        ActionBarDrawerToggle sideNavigationMenu = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        sideNavigationMenu.syncState();

        ///Setting on click listener which will allow me to respond to nav selections
        //Responding to the navigation buttons
        NavigationView sideNavView = findViewById(R.id.sideNavMenu);
        sideNavView.setNavigationItemSelectedListener(item ->
        {
            //Leaving this as a switch for when more functionality is added
            switch (item.getItemId())
            {
                case R.id.profilePage:
                    startActivity(new Intent(this, UserProfileActivity.class));
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
        });

        //Checking to see if the user entered any data previously if so displaying the information
        if(data) { loadData(); }
    }

    //Got a few utility files that provide me with useful methods
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
        showBMIFields();

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
        //Sending the result to set the info msg
        setResultsInfo(answer);

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
            hideMetricFields();

            //Showing the Imperial Field
            showImperialFields();

            //Changing the Switch Text
            bmiMeasurementToggle().setText("Imperial");

            convertCurrentMeasurements(0);
        }
        //Change to Metric Measurement(s)
        else
        {
            //Hiding the Imperial Field
            hideImperialFields();

            //Showing the Metric Field
            showMetricFields();

            //Changing the Switch Text
            bmiMeasurementToggle().setText("Metric");

            convertCurrentMeasurements(1);
        }
    }

    private void convertCurrentMeasurements(int metric)
    {
        //Metric Measurements
        if(metric == 0)
        {
            //Height
            if (!bmiUserHeight().getText().toString().isEmpty())
            {
                //Converting cm to ft
                String[] answer = Util().convertHeight(0, Integer.parseInt(bmiUserHeight().getText().toString()), 0, 0);

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
                String[] answer = Util().convertWeight(0, Integer.parseInt(bmiUserWeight().getText().toString()), 0 , 0);
                bmiWeightStone().setText(answer[0]);
                bmiWeightLbs().setText(answer[1]);

                Log.i("Answer[0] KG -> St: ", answer[0]);
                Log.i("Answer[1]: KG -> St: ", answer[1]);

                clearMetricWeightFields();
            }
        }
        //Imperial Measurements
        if(metric == 1)
        {
            int ft = -1;
            int inches = -1;

            //Stops the app crashing
            if(bmiHeightFoot().getText().toString().isEmpty())
            {
                ft = 0;
            }
            if(bmiHeightInches().getText().toString().isEmpty())
            {
                inches = 0;
            }

            int st = -1;
            int lbs = -1;

            //Again to stop app crashing if one of the values is blank when the user tries to convert the measurements
            if(bmiWeightStone().getText().toString().isEmpty())
            {
                st = 0;
            }
            if(bmiWeightLbs().getText().toString().isEmpty())
            {
                lbs = 0;
            }

            //Height
            if (!bmiHeightFoot().getText().toString().isEmpty() || !bmiHeightInches().getText().toString().isEmpty())
            {
                if(ft == -1)
                {
                    ft = Integer.parseInt(bmiHeightFoot().getText().toString());
                }
                if(inches == -1)
                {
                    inches = Integer.parseInt(bmiHeightInches().getText().toString());
                }

                //Converting FT to CM
                String[] answer = Util().convertHeight(1, 0, ft, inches);
                bmiUserHeight().setText(answer[0]);

                Log.i("Answer[0] Ft -> Cm: ", answer[0]);
                Log.i("Answer[1]: Ft -> Cm: ", answer[1]);

                clearImperialHeightFields();
            }
            //Weight
            if (!bmiWeightStone().getText().toString().isEmpty() || !bmiWeightLbs().getText().toString().isEmpty())
            {
                if(st == -1)
                {
                    st = Integer.parseInt(bmiWeightStone().getText().toString());
                }
                if(lbs == -1)
                {
                    lbs = Integer.parseInt(bmiWeightLbs().getText().toString());
                }

                //Stone to KG
                String[] answer = Util().convertWeight(1, 0, st, lbs);
                bmiUserWeight().setText(answer[0]);

                Log.i("Answer[0] St -> KG: ", answer[0]);
                Log.i("Answer[1]: St -> KG: ", answer[1]);

                clearImperialWeightFields();
            }
        }
    }

    //Calculating the users BMI
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

    //Setting the results
    private void setResultsInfo(double usersBMI)
    {
        //Underweight (< 18.5)
        if(usersBMI < 18.5)
        {
            getResultsInfo().setText("Underweight");
        }
        //Normal weight (18.5 - 24.9)
        else if(usersBMI >= 18.5 && usersBMI <= 24.9)
        {
            getResultsInfo().setText("Normal Weight");
        }
        //Overweight (25.0 - 29.9)
        else if(usersBMI >= 25.0 && usersBMI <= 29.9)
        {
            getResultsInfo().setText("Overweight");
        }
        //Obesity class 1 (30.0 - 34.9)
        else if(usersBMI >= 30.0 && usersBMI <= 34.9)
        {
            getResultsInfo().setText("Obesity class 1");
        }
        //Obesity class 2 (35.0 - 39.9)
        else if(usersBMI >= 35.0 && usersBMI <= 39.9)
        {
            getResultsInfo().setText("Obesity class 2");
        }
        //Obesity class 3 (> 40)
        else
        {
            getResultsInfo().setText("Obesity class 3");
        }
        getResultsInfo().setVisibility(View.VISIBLE);
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
    private TextView getResultsInfo() { return findViewById(R.id.bmiUserResultsInfo); }

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

    //Hiding all the fields
    private void hideAllFields()
    {
        //Hiding the Metric Fields
        bmiUserHeight().setVisibility(View.INVISIBLE);
        bmiUserWeight().setVisibility(View.INVISIBLE);

        //Hiding the Imperial Fields
        bmiHeightFoot().setVisibility(View.INVISIBLE);
        bmiHeightInches().setVisibility(View.INVISIBLE);
        bmiWeightStone().setVisibility(View.INVISIBLE);
        bmiWeightLbs().setVisibility(View.INVISIBLE);

        //Hiding BMI result fields
        bmiText().setVisibility(View.INVISIBLE);
        bmiField().setVisibility(View.INVISIBLE);
    }

    //Hiding just the metric input fields
    private void hideMetricFields()
    {
        bmiUserHeight().setVisibility(View.INVISIBLE);
        bmiUserWeight().setVisibility(View.INVISIBLE);
    }

    //Hiding the imperial input fields
    private void hideImperialFields()
    {
        bmiHeightFoot().setVisibility(View.INVISIBLE);
        bmiHeightInches().setVisibility(View.INVISIBLE);
        bmiWeightStone().setVisibility(View.INVISIBLE);
        bmiWeightLbs().setVisibility(View.INVISIBLE);
    }

    //Showing the metric input fields
    private void showMetricFields()
    {
        bmiUserHeight().setVisibility(View.VISIBLE);
        bmiUserWeight().setVisibility(View.VISIBLE);
    }

    //Showing the imperial fields
    private void showImperialFields()
    {
        bmiHeightFoot().setVisibility(View.VISIBLE);
        bmiHeightInches().setVisibility(View.VISIBLE);
        bmiWeightStone().setVisibility(View.VISIBLE);
        bmiWeightLbs().setVisibility(View.VISIBLE);
    }

    //Hiding the BMI fields
    private void hideBMIFields()
    {
        bmiText().setVisibility(View.INVISIBLE);
        bmiField().setVisibility(View.INVISIBLE);
    }

    private void showBMIFields()
    {
        bmiText().setVisibility(View.VISIBLE);
        bmiField().setVisibility(View.VISIBLE);
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
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        boolean metricToggle = sharedPreferences.getBoolean("MetricToggle", Boolean.FALSE);

        if (metricToggle)
        {
            bmiMeasurementToggle().setChecked(Boolean.FALSE);
            String height = sharedPreferences.getString("Height CM", "");
            bmiUserHeight().setText(height);
            String weight = sharedPreferences.getString("Weight KG", "");
            bmiUserWeight().setText(weight);

            //Need to ensure metric fields visible and imperial invisible
            bmiUserHeight().setVisibility(View.VISIBLE);
            bmiUserWeight().setVisibility(View.VISIBLE);

            bmiHeightFoot().setVisibility(View.INVISIBLE);
            bmiHeightInches().setVisibility(View.INVISIBLE);
            bmiWeightStone().setVisibility(View.INVISIBLE);
            bmiWeightLbs().setVisibility(View.INVISIBLE);

            Log.i("Metric Measurements: ", height + weight);
        }
        else
        {
            Log.i("FUCKING HERE", "------------------------------------------------------------------------------");
            //Changing the text to Imperial and turning the toggle on
            bmiMeasurementToggle().setText("Imperial");
            bmiMeasurementToggle().setChecked(Boolean.TRUE);
            bmiHeightFoot().setText(sharedPreferences.getString("Height FT", ""));
            bmiHeightInches().setText(sharedPreferences.getString("Height INCH", ""));
            bmiWeightStone().setText(sharedPreferences.getString("Weight ST", ""));
            bmiWeightLbs().setText(sharedPreferences.getString("Weight LBS", ""));

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