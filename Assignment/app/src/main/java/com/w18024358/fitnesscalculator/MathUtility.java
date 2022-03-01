package com.w18024358.fitnesscalculator;

import static java.lang.String.*;

import android.renderscript.Sampler;

import java.io.StringBufferInputStream;
import java.text.DecimalFormat;

//Creating a helper class that will convert all necessary measurement conversions for us
public class MathUtility
{
    //TODO Ensure to correctly round numbers up or down
    //TODO Ensure that edge cases are correct, i.e, inches over 12 should become a foot etc
    //----------------------------------------------------------------------------------------------

    //Metric to Imperial (Height) -> Centimeters to inches
    double CmToInch(double cm)
    {
        DecimalFormat df = new DecimalFormat("##");
        return Double.parseDouble(df.format(cm / 2.54));
    }

    //Now need to convert the inches into feet
    //Feet = inch / 12
    double InchToFoot(double inches)
    {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(inches / 12.0));
    }

    //Metric to Imperial (Weight) -> Kilograms to pounds
    //Approx evaluation = kg * 2.205
    double KgToLbs(double kg)
    {
        //Creating a formatter that will ensure the final value is rounded to 1 decimal place
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        //Returning the converted value
        return Double.parseDouble(decimalFormat.format(kg * 2.205));
    }

    //Now need to convert the lbs into stone
    //Stone = mass / 14
    double LbsToStone(double lbs)
    {
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        return Double.parseDouble(decimalFormat.format(lbs / 14));
    }

    //Checks to ensure that the measurements are correct, i.e, anything above 12 inches becomes a foot etc
    boolean StoneNeedsRoundingCheck(double stone)
    {
        String st = String.valueOf(stone);
        System.out.println("String value of stone: " + st);
        int index = st.compareToIgnoreCase(".");
        String stAfterDecimalPoint = st.substring(index, st.length());
        System.out.println("Value after decimal place: " + stAfterDecimalPoint);

        if(Double.parseDouble(stAfterDecimalPoint) > 13)
        {
            System.out.println("Greater than 13");
            return true;
        }
        System.out.println("Less than 13");
        return false;
    }

    //Messier?? Than how I was actually doing it??
    double NewStone(double stone)
    {
        String st = String.valueOf(stone);
        int index = st.compareToIgnoreCase(".");
        String stAfterDecimalPoint = st.substring(index + 1, st.length());

        double newStone = 1.0;
        double newLbs = stAfterDecimalPoint.charAt(1);

        String str = String.valueOf(stone) + String.valueOf(newStone) + "." + String.valueOf(newLbs);

        System.out.println("Stone + new Stone + lbs: " + ((stone + newStone) + newLbs));
        System.out.println("NEW: " + str);
        return Double.parseDouble(str);
    }

    //Imperial to Metric (Height) -> Foot to Cm
    double FootToCm(double foot)
    {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        return Double.parseDouble(decimalFormat.format(foot * 30.48));
    }

    //Imperial to Metric (Weight) -> Stone to Kg
    double StoneToKg(double stone)
    {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        return Double.parseDouble(decimalFormat.format(stone * 6.35));
    }

    //How can I do this hear so that I can return the value twice????
    String[] BeforeAfterDecimalPoint(double footNotSplit)
    {
        String foot = String.valueOf(footNotSplit);
        System.out.println("Value of foot before split: " + foot);

        String splitString[] = foot.split("\\.");
        String ft = splitString[0];
        String inch = splitString[1];

        System.out.println("Foot: " + ft + " Inch: " + inch);

        return new String[] {ft, inch};
    }

    String[] SplitStoneAndLbs(double weightToSplit)
    {
        String measurementToSplit = String.valueOf(weightToSplit);
        System.out.println("Value of weight before split: " + measurementToSplit);

        String splitString[] = measurementToSplit.split("\\.");
        String stone = splitString[0];
        String lbs = splitString[1];

        System.out.println("Stone: " + stone + " Lbs: " + lbs);

        return new String[] { stone, lbs };
    }
}
