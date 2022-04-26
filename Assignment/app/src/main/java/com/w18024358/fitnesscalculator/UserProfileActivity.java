package com.w18024358.fitnesscalculator;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getMetricToggle().setOnClickListener(view -> convertMetrics());
        getSaveDetails().setOnClickListener(view -> saveUserDetails());
    }

    private void convertMetrics()
    {
        Utility utility = new Utility();

        //Hiding the Metric Fields and showing Imperial Fields
        if(getMetricToggle().getText().equals("Metric"))
        {
            utility.hideMetricFields(getUserHeightCM(), getUserWeightKG());
            utility.showImperialFields(getUserHeightFT(), getUserHeightInch(), getUserWeightST(), getUserWeightLBS());
            getMetricToggle().setText("Imperial");
        }
        else
        {
            utility.hideImperialFields(getUserHeightFT(), getUserHeightInch(), getUserWeightST(), getUserWeightLBS());
            utility.showMetricFields(getUserHeightCM(), getUserWeightKG());
            getMetricToggle().setText("Metric");
        }
    }

    private void saveUserDetails()
    {
        //Allow users to save just personal details or fitness details - but cannot submit half of each etc
        if(!getUserHeightCM().getText().toString().isEmpty() && !getUserWeightKG().getText().toString().isEmpty()
            || !getUserHeightFT().getText().toString().isEmpty() && !getUserHeightInch().toString().isEmpty()
            && !getUserWeightST().getText().toString().isEmpty() && !getUserWeightLBS().toString().isEmpty())
        {
            Toast.makeText(this, "Subbed Personal", Toast.LENGTH_SHORT).show();

            //TODO now that I have saved the data need to automatically display it on the BMI page
            if(getMetricToggle().equals("Metric"))
            {
                //Need to save the metric measurements
                getSharedPreferencesEditor().putString("User Height CM", getUserHeightCM().toString());
                getSharedPreferencesEditor().putString("User Weight KG", getUserWeightKG().toString());
            }
            else if(getMetricToggle().equals("Imperial"))
            {
                //Need to save the imperial measurements
                getSharedPreferencesEditor().putString("User Height FT", getUserHeightFT().toString());
                getSharedPreferencesEditor().putString("User Height Inches", getUserHeightInch().toString());
                getSharedPreferencesEditor().putString("User Weight ST", getUserWeightST().toString());
                getSharedPreferencesEditor().putString("User Weight LBS", getUserWeightLBS().toString());
            }

        }
        else if(!getBench1RM().getText().toString().isEmpty() && !getBench3RM().getText().toString().isEmpty() && !getBench5RM().getText().toString().isEmpty()
            || !getOverhead1RM().getText().toString().isEmpty() && !getOverhead3RM().getText().toString().isEmpty() && !getOverhead5RM().toString().isEmpty()
            || !getSquat1RM().getText().toString().isEmpty() && !getSquat3RM().getText().toString().isEmpty() && !getSquat5RM().toString().isEmpty()
            || !getDeadlift1RM().getText().toString().isEmpty() && !getDeadlift3RM().getText().toString().isEmpty() && !getDeadlift5RM().toString().isEmpty())
        {
            Toast.makeText(this, "Subbed Fitness", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "To save details ensure at least one subsection is completely filled out", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserDetails()
    {
        //Getting the username
        //String username
        //getUser().setText("Message" + username);
    }

    private SharedPreferences getSharedPreferences()
    {
        return getSharedPreferences("sharedPrefs", MODE_PRIVATE);
    }

    private SharedPreferences.Editor getSharedPreferencesEditor()
    {
        return getSharedPreferences().edit();
    }

    private TextView getUser() { return findViewById(R.id.userProfileUsernameMessage); }
    private Switch getMetricToggle() { return findViewById(R.id.userProfileMetricToggle); }
    private EditText getUserHeightFT() { return findViewById(R.id.userProfileHeightFT); }
    private EditText getUserHeightCM(){ return findViewById(R.id.userProfileHeightCM); }
    private EditText getUserHeightInch() { return findViewById(R.id.userProfileHeightInches); }
    private EditText getUserWeightKG() { return findViewById(R.id.userProfileWeightKG); }
    private EditText getUserWeightST() { return findViewById(R.id.userProfileWeightST); }
    private EditText getUserWeightLBS() { return findViewById(R.id.userProfileWeightLBS); }
    private EditText getBench1RM() { return findViewById(R.id.userProfileBP1RM); }
    private EditText getBench3RM() { return findViewById(R.id.userProfileBP3RM); }
    private EditText getBench5RM() { return findViewById(R.id.userProfileBP5RM); }
    private EditText getOverhead1RM() { return findViewById(R.id.userProfileOP1RM); }
    private EditText getOverhead3RM() { return findViewById(R.id.userProfileOP3RM); }
    private EditText getOverhead5RM() { return findViewById(R.id.userProfileOP5RM); }
    private EditText getSquat1RM() { return findViewById(R.id.userProfileSquat1RM); }
    private EditText getSquat3RM() { return findViewById(R.id.userProfileSquat3RM); }
    private EditText getSquat5RM() { return findViewById(R.id.userProfileSquat5RM); }
    private EditText getDeadlift1RM() { return findViewById(R.id.userProfileDeadlift1RM); }
    private EditText getDeadlift3RM() { return findViewById(R.id.userProfileDeadlift3RM); }
    private EditText getDeadlift5RM() { return findViewById(R.id.userProfileDeadlift5RM); }
    private Button getSaveDetails() { return findViewById(R.id.userProfileSaveDetailsButton); }
}