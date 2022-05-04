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
    //TODO Refactor
    //TODO Save all the data and automatically get it in BMI if not null
    Switch metricToggle;

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

        metricToggle = findViewById(R.id.firstLoginMetricSwitch);
        metricToggle.setOnClickListener(view -> changeMetrics());

        //TODO Need to save this information
        //Getting the username through savedPreferences
        //TODO need to do this differently look at cals
        getWelcomeMsg().setText(getSharedPreferences().getString("EmailTest", ""));
        getContinueButton().setOnClickListener(view -> checkFields());
    }

    private void changeMetrics()
    {
        if(getHeightCm().getVisibility() == View.VISIBLE)
        {
            getHeightCm().setVisibility(View.INVISIBLE);
            getWeightKg().setVisibility(View.INVISIBLE);

            getHeightFt().setVisibility(View.VISIBLE);
            getHeightInch().setVisibility(View.VISIBLE);
            getWeightStone().setVisibility(View.VISIBLE);
            getWeightLbs().setVisibility(View.VISIBLE);

            metricToggle.setText("Imperial");

            convertCurrentMeasurements(0);
        }
        else
        {
            getHeightCm().setVisibility(View.VISIBLE);
            getWeightKg().setVisibility(View.VISIBLE);

            getHeightFt().setVisibility(View.INVISIBLE);
            getHeightInch().setVisibility(View.INVISIBLE);
            getWeightStone().setVisibility(View.INVISIBLE);
            getWeightLbs().setVisibility(View.INVISIBLE);

            metricToggle.setText("Metric");

            convertCurrentMeasurements(1);
        }
    }


    //TODO Think of a way to add this to Util class. Maybe have a case and pass info about what fields we have?
    //Same code as BMI Activity but not sure how to keep code DRY
    private void convertCurrentMeasurements(int currentMetric)
    {
        //Make this a class?
        MathUtility utility = new MathUtility();

        //Metric
        if(currentMetric == 0)
        {
            //Height
            if(!getHeightCm().getText().toString().isEmpty())
            {
                //Converting CMs into Feet
                String answer[] = utility.convertHeight(currentMetric, Integer.parseInt(getHeightCm().getText().toString()), 0, 0);
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
                    String answer[] = utility.convertWeight(0, Integer.parseInt(getWeightKg().getText().toString()), 0, 0);
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
                String answer[] = utility.convertHeight(currentMetric, 0, Integer.parseInt(getHeightFt().getText().toString()), Integer.parseInt(getHeightInch().getText().toString()));
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
                String answer[] = utility.convertWeight(currentMetric, 0, Integer.parseInt(getWeightStone().getText().toString()), Integer.parseInt(getWeightLbs().getText().toString()));
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
        if(metricToggle.getText().equals("Metric") && !getHeightCm().getText().toString().isEmpty()
                                                    || !getWeightKg().getText().toString().isEmpty())
        {
            Toast.makeText(this, "Subbed Metric", Toast.LENGTH_SHORT).show();
            //Saving the data and this data will then be used to automatically fill in fields in BMI
            saveData();
        }
        else if(metricToggle.getText().equals("Imperial") && !getHeightFt().getText().toString().isEmpty()
                || !getWeightStone().getText().toString().isEmpty())
        {
            Toast.makeText(this, "Subbed Imperial", Toast.LENGTH_SHORT).show();
            //TODO Save Details
            saveData();
        }
        else
        {
            Toast.makeText(this, "Not subbed", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String msg = "If you wish to gain the best experience from this application " +
                    "please fill in the necessary details. " + " You can also add details from profile page";

            builder.setMessage(msg)
                    .setPositiveButton("Yes", (dialogInterface, i) -> { openBMIActivity(false); })
                    .setNegativeButton("No", null);
            builder.show();
        }
    }

    private void openBMIActivity(boolean dataSaved)
    {
        Intent intent = new Intent(this, BMIActivity.class);
        intent.putExtra("DataSaved", dataSaved);
        startActivity(intent);
    }

    private void openBMIActivityWithData(String heightCm, String weightKg, String heightFt, String heightInch,
                                 String weightSt, String weightLbs, Boolean metric, Boolean imperial)
    {
        Intent intent = new Intent(this, BMIActivity.class);

        if(metric)
        {
            //Metric Data needs to be sent to the Intent
            intent.putExtra("HeightCM", heightCm);
            intent.putExtra("WeightKG", weightKg);
            intent.putExtra("Metric", true);

        }
        else if(imperial)
        {
            intent.putExtra("HeightFT", heightFt);
            intent.putExtra("HeightInch", heightInch);
            intent.putExtra("WeightST", weightSt);
            intent.putExtra("WeightLBS", weightLbs);
            intent.putExtra("Imperial", true);

        }
        intent.putExtra("DataSaved", true);
        startActivity(intent);
    }

    private void saveData()
    {
        //Intent method
        //REFACTOR
        String heightCM = null;
        String weightKG = null;
        String heightFT = null;
        String heightInch = null;
        String weightST = null;
        String weightLBS = null;
        Boolean metric = false;
        Boolean imperial = false;

        if(!metricToggle.isChecked())
        {
            //Save metric data - Metric is default so switch will be false
            heightCM =  getHeightCm().getText().toString();
            weightKG = getWeightKg().getText().toString();
            metric = true;
        }
        else
        {
            //Save imperial data
            heightFT = getHeightFt().getText().toString();
            heightInch = getHeightInch().getText().toString();
            weightST =  getWeightStone().getText().toString();
            weightLBS = getWeightLbs().getText().toString();
            imperial = true;
        }
        openBMIActivityWithData(heightCM, weightKG, heightFT, heightInch, weightST, weightLBS, metric, imperial);
    }

    //Helper methods
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

     //Why doesn't this work??
     private SharedPreferences getSharedPreferences()
     {
         //TO DELETE SHARED PREFERENCES NEED TO USE yourSharedPreferences.edit().clear().apply()
         return getSharedPreferences("sharedPrefs", MODE_PRIVATE);
     }
     private SharedPreferences.Editor getSharedPreferencesEditor()
     {
         return getSharedPreferences().edit();
     }
}