package com.w18024358.fitnesscalculator;

import static java.lang.String.*;

import android.renderscript.Sampler;
import android.util.Log;

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
    //Made code messier so need to clean it up but now values are accurately displayed (i.e., 5ft 8 and not 5ft 88 etc.)
    double InchToFoot(double inches)
    {
        Log.i("InchToFoot", "Inches: " + inches);

        //Formatting the amount after a decimal point
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double height = Double.parseDouble(decimalFormat.format( inches / 12));
        String[] heightRounded = BeforeAfterDecimalPoint(height);

        int ft = Integer.parseInt(heightRounded[0]);
        int temp = Integer.parseInt(heightRounded[1]);
        int temp2 = (int) RoundToNearestDecimalPoint(temp);

        String finalHeight = ft + "." + temp2;

        Log.i("Final Height", finalHeight);

        Log.i("Before CheckFtAndInchesNeedsRounding -> InchToFoot()", finalHeight);
        if(CheckFtAndInchesNeedRounding(Double.parseDouble(finalHeight)))
        {
            return RoundFtAndInches(Double.parseDouble(finalHeight));
        }
        return Double.parseDouble(finalHeight);
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
        Log.i("LbsToStone", "Lbs " + lbs);

        //Formatting to 2 decimal places
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        double weight = Double.parseDouble(decimalFormat.format(lbs / 14));

        String[] weightRounded = BeforeAfterDecimalPoint(weight);

        int st = Integer.parseInt(weightRounded[0]);
        int temp = Integer.parseInt(weightRounded[1]);
        int temp2 = (int) RoundToNearestDecimalPoint(temp);

        String finalWeight = st + "." + temp2;

        Log.i("Final Weight", finalWeight);

        if(CheckStAndLbsNeedsRounding(Double.parseDouble(finalWeight)))
        {
            return RoundStAndLbs(Double.parseDouble(finalWeight));
        }
        return Double.parseDouble(finalWeight);
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

    //Checking to see if ft / inches needs rounding (i.e, 5ft 12 inches should be 6ft)
    boolean CheckFtAndInchesNeedRounding(double height)
    {
        String[] splitHeight = BeforeAfterDecimalPoint(height);
        return Integer.parseInt(splitHeight[1]) > 11;
    }

    //Checking to see if st / lbs needs rounding (i.e, 5st 14 lbs should be 6st)
    boolean CheckStAndLbsNeedsRounding(double weight)
    {
        String[] splitWeight = BeforeAfterDecimalPoint(weight);
        return Integer.parseInt(splitWeight[1]) > 13;
    }

    //Ft and inches needs rounding
    double RoundFtAndInches(double heightNeedsRounding)
    {
        Log.i("MathUtility -> RoundFtAndInches()", String.valueOf(heightNeedsRounding));
        String[] height = BeforeAfterDecimalPoint(heightNeedsRounding);
        int ft = Integer.parseInt(height[0]);
        int inches = Integer.parseInt(height[1]);
        Log.i("RoundFtAndInches -> FT / Inches", ft + " " + inches);

        ft++;
        inches -= 12;
        Log.i("MathUtility -> RoundFtAndInches()", String.valueOf(Double.parseDouble(ft + "." + inches)));
        return Double.parseDouble(ft + "." + inches);
    }

    //St and lbs needs rounding
    double RoundStAndLbs(double weightNeedsRounding)
    {
        String[] weight = BeforeAfterDecimalPoint(weightNeedsRounding);
        int st = Integer.parseInt(weight[0]);
        int lbs = Integer.parseInt(weight[1]);

        st++;
        lbs -= 14;
        Log.i("MathUtility -> RoundStAndLbs()", String.valueOf(Double.parseDouble(st + "." + lbs)));
        return Double.parseDouble(st + "." + lbs);
    }

    //Rounding a value to the nearest decimal point
    double RoundToNearestDecimalPoint(int valueToRound)
    {
        if(String.valueOf(valueToRound).length() == 1 || String.valueOf(valueToRound).length() == 0)
        {
            return valueToRound;
        }

        Log.i("MathUtility -> RoundToNearestDecimalPoint", "Value to round " + String.valueOf(valueToRound));

        //Splitting the number to round
        String splitValue = String.valueOf(valueToRound);

        //A value could be 5ft 7.2 inches which will need to round up or down depending on the value after this point
        //but this may result in a loss of accuracy but is necessary to ensure I don't get values such as 5ft 17 etc
        String valueToCheck = valueOf(splitValue.charAt(1));         //This will be the value that decides if the value needs to be rounded up or down
        String newHeight = valueOf(splitValue.charAt(0));            //This will be the new value that is rounded up or down

        Log.i("RoundToNearestDecimalPoint", "Value to check: " + valueToCheck + " " + "New Height: " + newHeight);
        if(Integer.parseInt(valueToCheck) >= 5)
        {
            Integer.parseInt(newHeight + 1);
        }
        Log.i("Returning ", newHeight);
        return Double.parseDouble(newHeight);
    }

    String[] BeforeAfterDecimalPoint(double valueToSplit)
    {
        System.out.println("Value of unit before split: " + valueToSplit);

        String[] measurementString = String.valueOf(valueToSplit).split("\\.");
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
            String[] results = BeforeAfterDecimalPoint(foot);

            Log.i("MathUtility -> convertHeight()", results[0] + " " + results[1]);

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
                //As inches here will not be accurate, i.e lets say this returns 2 inches it should be treat as 0.2 inches and not 2
                //as the conversion back to cm will lead to a value to high so need to multiply this value by 0.1
                inches = currentInch * 0.1;
            }

            double foot;

            foot = currentFt;

            DecimalFormat decimalFormat = new DecimalFormat("##.##");
            ft = Double.parseDouble(decimalFormat.format(foot + inches));

            Log.i("MathUtility -> Metric", "Ft: " + ft); //should be 2.9 but is actually 2.09
            int finalCm = (int)FootToCm(ft);
            Log.i("Final CM", String.valueOf(finalCm));
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

            String[] results = BeforeAfterDecimalPoint(stone);

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

            stone = currentSt;

            DecimalFormat decimalFormat = new DecimalFormat("##.##");
            st = Double.parseDouble(decimalFormat.format(stone + lbs));

            int finalKg = (int)StoneToKg(st);
            return new String[] {String.valueOf(finalKg), ""};
        }
        return null;
    }
}
