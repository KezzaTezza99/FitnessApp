package com.w18024358.fitnesscalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FitnessActivity extends AppCompatActivity {
    static final int RETURNED_VALUES = 1;

    TextView currentDay;

    ImageView bmiButton;
    ImageView calorieButton;
    ImageView fitnessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        currentDay = findViewById(R.id.fitnessCurrentDayLabel);

        //
        bmiButton = findViewById(R.id.fitnessBMIButton);
        calorieButton = findViewById(R.id.fitnessCalorieButton);
        fitnessButton = findViewById(R.id.fitnessFitnessButton);

        bmiButton.setOnClickListener(view -> openBMI());
        calorieButton.setOnClickListener(view -> openCalorie());
        fitnessButton.setOnClickListener(view -> openFitness());
        //

        //Getting the current day
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        Date date = calendar.getTime();
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        currentDay.setText(today);

        currentDay.setOnLongClickListener(view -> {
            openCalendar();
            return true;
        });
    }

    private void openBMI()
    {
        Intent intent = new Intent(this, BMIActivity.class);
        startActivity(intent);
    }

    private void openCalorie()
    {
        Intent intent = new Intent(this, CalorieActivity.class);
        startActivity(intent);
    }

    private void openFitness()
    {
        Intent intent = new Intent(this, FitnessActivity.class);
        startActivity(intent);
    }

    private void openCalendar()
    {
        Intent intent = new Intent(this, Calendar.class);
        intent.putExtra("ActivityID", "Fitness");
        startActivityForResult(intent, RETURNED_VALUES);
    }
}