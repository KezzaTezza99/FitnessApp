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
}
