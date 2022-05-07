package com.w18024358.fitnesscalculator;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

//Activity's purpose is to capture some important user details once they have logged in
//will save the details to the SharedPreferences and will be used for the Calorie Total and Fitness Page
//User will be given the option to continue without completing the necessary fields and can then
//add these from the profile page when the user deems more appropriate (accessible through swiping right on three main activities)
public class FirstUseLoginActivity extends AppCompatActivity
{
    //TODO still shows both fields
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use_login);

        getHeightFt().setVisibility(View.GONE);
        getHeightInch().setVisibility(View.GONE);
        getWeightStone().setVisibility(View.GONE);
        getWeightLbs().setVisibility(View.GONE);

        getMetricToggle().setOnClickListener(view -> changeMetrics());

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        //Capitalising the first word
        String capUsername = username.substring(0, 1).toUpperCase() + username.substring(1);
        getWelcomeMsg().setText("Welcome " + capUsername);
        getContinueButton().setOnClickListener(view -> checkFields());
    }

    private void changeMetrics()
    {
        if(getHeightCm().getVisibility() == View.VISIBLE)
        {
            getUtility().hideMetricFields(getHeightCm(), getWeightKg());
            getUtility().showImperialFields(getHeightFt(), getHeightInch(), getWeightStone(), getWeightLbs());
            getMetricToggle().setText("Imperial");
            convertCurrentMeasurements(0);
        }
        else
        {
            getUtility().showMetricFields(getHeightCm(), getWeightKg());
            getUtility().hideImperialFields(getHeightFt(), getHeightInch(), getWeightStone(), getWeightLbs());
            getMetricToggle().setText("Metric");
            convertCurrentMeasurements(1);
        }
    }

    //Same code as BMI Activity but not sure how to keep code DRY
    private void convertCurrentMeasurements(int currentMetric)
    {
        //Metric
        if(currentMetric == 0)
        {
            //Height
            if(!getHeightCm().getText().toString().isEmpty())
            {
                //Converting CMs into Feet
                String[] answer = getMathUtility().convertHeight(currentMetric, Integer.parseInt(getHeightCm().getText().toString()), 0, 0);
                getHeightFt().setText(answer[0]);
                getHeightInch().setText(answer[1]);

                Log.i("First Login - Answer[0] CM -> Ft: ", answer[0]);
                Log.i("First Login - Answer[1]: Cm -> Ft: ", answer[1]);

                //Clearing the fields
                getHeightCm().setText("");
            }
            //Weight
            if(!getWeightKg().getText().toString().isEmpty())
            {
                if(!getWeightKg().getText().toString().isEmpty())
                {
                    //Converting KGs into Stone / Lbs
                    String[] answer = getMathUtility().convertWeight(0, Integer.parseInt(getWeightKg().getText().toString()), 0, 0);
                    getWeightStone().setText(answer[0]);
                    getWeightLbs().setText(answer[1]);

                    Log.i("First Login - Answer[0] KG -> St: ", answer[0]);
                    Log.i("First Login - Answer[1]: KG -> St: ", answer[1]);

                    //Clearing the fields
                    getWeightKg().setText("");
                }
            }
        }
        else if(currentMetric == 1)
        {
            //Height
            if(!getHeightFt().getText().toString().isEmpty() || !getHeightInch().getText().toString().isEmpty())
            {
                //Converting Feet into CM
                String[] answer = getMathUtility().convertHeight(currentMetric, 0, Integer.parseInt(getHeightFt().getText().toString()), Integer.parseInt(getHeightInch().getText().toString()));
                getHeightCm().setText(answer[0]);

                Log.i("First Login - Answer[0] Ft -> Cm: ", answer[0]);
                Log.i("First Login - Answer[1]: Ft -> Cm: ", answer[1]);

                //Clearing fields TODO Make these a method
                getHeightFt().setText("");
                getHeightInch().setText("");
            }
            //Weight
            if(!getWeightStone().getText().toString().isEmpty() || !getWeightLbs().getText().toString().isEmpty())
            {
                //Stone to KG
                String[] answer = getMathUtility().convertWeight(currentMetric, 0, Integer.parseInt(getWeightStone().getText().toString()), Integer.parseInt(getWeightLbs().getText().toString()));
                getWeightKg().setText(answer[0]);

                Log.i("First Login - Answer[0] St -> KG: ", answer[0]);
                Log.i("First Login - Answer[1]: St -> KG: ", answer[1]);

                //Clearing the fields
                getWeightStone().setText("");
                getWeightLbs().setText("");
            }
        }
    }

    private void checkFields()
    {
        //Need to check both metric / imperial but separately otherwise will not submit details correctly
        if(getMetricToggle().getText().equals("Metric") && !getHeightCm().getText().toString().isEmpty()
                                                    || !getWeightKg().getText().toString().isEmpty())
        {
            //Saving the data and this data will then be used to automatically fill in fields in BMI
            //TODO Pass in a variable to the method?
            saveData();
        }
        else if(getMetricToggle().getText().equals("Imperial") && !getHeightFt().getText().toString().isEmpty()
                || !getWeightStone().getText().toString().isEmpty())
        {
            saveData();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String msg = "If you wish to gain the best experience from this application " +
                    "please fill in the necessary details. " + " You can also add details from profile page, accessible by swiping right on the main screens!";

            builder.setMessage(msg)
                    .setPositiveButton("Yes", (dialogInterface, i) -> { openBMIActivity(); })
                    .setNegativeButton("No", null);
            builder.show();
        }
    }

    //TODO Refactor from here as this is nasty
    private void openBMIActivity()
    {
        Intent intent = new Intent(this, BMIActivity.class);
        startActivity(intent);
    }

    private void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("User Info Saved", true);

        if(!getMetricToggle().isChecked())
        {
            //Save metric data - Metric is default so switch will be false
            editor.putString("Height CM", getHeightCm().getText().toString());
            editor.putString("Weight KG", getWeightKg().getText().toString());
            editor.putBoolean("MetricToggle", true);
        }
        else
        {
            //Saving imperial data
            editor.putString("Height FT", getHeightFt().getText().toString());
            editor.putString("Height INCH", getHeightInch().getText().toString());
            editor.putString("Weight ST", getWeightStone().getText().toString());
            editor.putString("Weight LBS", getWeightLbs().getText().toString());
            editor.putBoolean("MetricToggle", false);
        }
        editor.apply();
        openBMIActivity();
    }

    //Helper methods
    private Switch getMetricToggle() { return findViewById(R.id.firstLoginMetricSwitch); }
    private TextView getWelcomeMsg()
    {
        return findViewById(R.id.firstLoginWelcomeMessage);
    }
    private EditText getHeightCm()
    {
        return findViewById(R.id.firstLoginHeightCm);
    }
    private EditText getWeightKg()
    {
        return findViewById(R.id.firstLoginWeightKg);
    }
    private EditText getHeightFt()
    {
        return findViewById(R.id.firstLoginHeightFeet);
    }
    private EditText getHeightInch()
    {
        return findViewById(R.id.firstLoginHeightInches);
    }
    private EditText getWeightStone()
    {
        return findViewById(R.id.firstLoginWeightStone);
    }
    private EditText getWeightLbs()
    {
        return findViewById(R.id.firstLoginWeightLbs);
    }
    private Button getContinueButton()
    {
        return findViewById(R.id.firstLoginContinueButton);
    }
    private Utility getUtility() { return new Utility(); }
    private MathUtility getMathUtility() { return new MathUtility(); }
}