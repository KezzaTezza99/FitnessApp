package com.w18024358.fitnesscalculator;

import static java.lang.String.*;

import android.renderscript.Sampler;

import java.io.StringBufferInputStream;
import java.text.DecimalFormat;
import java.util.function.DoubleToLongFunction;

//Creating a helper class that will convert all necessary measurement conversions
public class MathUtility
{
    //Metric to Imperial (Height) -> Centimeters to inches
    double CmToInch(double cm)
    {
        //Formatting the final value
        DecimalFormat decimalFormat = new DecimalFormat("##");
        return Double.parseDouble(decimalFormat.format(cm / 2.54));
    }

    //Converting Imperial Inches into Feet (was more accurate to do this than to go CM->FT, when testing)
    double InchToFoot(double inches)
    {
        //Formatting the amount after a decimal point
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format( inches / 12.0));
    }

    //Metric to Imperial (Weight) -> Kilograms to pounds -- Approx evaluation = kg * 2.205
    double KgToLbs(double kg)
    {
        //Creating a formatter that will ensure the final value is rounded to 1 decimal place
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        //Returning the converted value
        return Double.parseDouble(decimalFormat.format(kg * 2.205));
    }

    //Now need to convert the lbs into stone
    double LbsToStone(double lbs)
    {
        //Formatting to 2 decimal places
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        return Double.parseDouble(decimalFormat.format(lbs / 14));
    }

    //Imperial to Metric (Height) -> Foot to Cm
    double FootToCm(double foot)
    {
        //Formatting the value to a whole number
        DecimalFormat decimalFormat = new DecimalFormat("###");
        return Double.parseDouble(decimalFormat.format(foot * 30.48));
    }

    //Imperial to Metric (Weight) -> Stone to Kg
    double StoneToKg(double stone)
    {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        return Double.parseDouble(decimalFormat.format(stone * 6.35));
    }

    String[] BeforeAfterDecimalPoint(double valueToSplit)
    {
        System.out.println("Value of unit before split: " + valueToSplit);

        String measurementString[] = String.valueOf(valueToSplit).split("\\.");
        String valueBeforeDecimalPoint = measurementString[0];
        String valueAfterDecimalPoint = measurementString[1];

        System.out.println("Value before Point: " + valueBeforeDecimalPoint + "\n" +
                "Value after Point: " + valueAfterDecimalPoint);

        return new String[] {valueBeforeDecimalPoint, valueAfterDecimalPoint};
    }


    String[] convertHeight(int currentMetric, int currentCM, int currentFt, int currentInch)
    {
        //Metric Measurements
        if(currentMetric == 0)
        {
            //Getting the currently inputted cm and converting it to inches
            double inches = CmToInch(currentCM);
            //Now changing the inches into feet
            double foot = InchToFoot(inches);

            //Storing the result in a string that will be split into feet and inches
            String results[] = BeforeAfterDecimalPoint(foot);

            return new String[] {results[0], results[1]};
        }
        //Imperial Measurements
        else if(currentMetric == 1)
        {
            double ft;
            //Setting this value to 0 so if the user doesn't add a measurement for the inch EditText field and then changes metrics the application would crash
            //with out ensuring this value is set if bmiHeightInches() == null -> this way also means no necessary check
            double inches = 0;

            //Need to check the foot and the inch fields before calculating the CM
            if(currentInch != 0)
            {
                //As inches here will not be accurate, i.e lets say this returns 10 inches it should be treat as 0.10 inches and not 10
                //as the conversion back to cm will lead to a value to high so need to multiply this value by 0.01
                inches = currentInch * 0.01;
            }

            double foot;

            if(currentFt != 0)
            {
                foot = currentFt;
            }
            else
            {
                foot = 0;
            }

            DecimalFormat decimalFormat = new DecimalFormat("##.##");
            ft = Double.parseDouble(decimalFormat.format(foot + inches));

            int finalCm = (int)FootToCm(ft);
            return new String[] {String.valueOf(finalCm), ""};
        }
        return null;
    }

    //Refactor
    String[] convertWeight(int metric, int currentKg, int currentSt, int currentLbs)
    {
        if(metric == 0)
        {
            double lbs = KgToLbs(currentKg);
            double stone = LbsToStone(lbs);

            String results[] = BeforeAfterDecimalPoint(stone);

            return new String[] {results[0], results[1]};
        }
        else if(metric == 1)
        {
            //Add comments
            double st;  //Can I not just use this st instead of stone?
            double lbs = 0;

            if(currentLbs != 0)
            {
                lbs = currentLbs * 0.01;
            }

            double stone;

            if(currentSt != 0)
            {
                stone = currentSt;
            }
            else
            {
                stone = 0;
            }

            DecimalFormat decimalFormat = new DecimalFormat("##.##");
            st = Double.parseDouble(decimalFormat.format(stone + lbs));

            int finalKg = (int)StoneToKg(st);
            return new String[] {String.valueOf(finalKg), ""};
        }
        return null;
    }
}
