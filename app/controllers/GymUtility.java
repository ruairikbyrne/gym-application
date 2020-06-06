package controllers;

import models.Member;
import models.Assessment;
import java.text.DecimalFormat;

/**
 * This class:
 *      - Calculates a members BMI based on most recent assessment.
 *      - Determines a members BMI category based on most recent assessment.
 *      - Determines if a member has an ideal body weight based on most recent aseessment.
 *      - Converts height from metres to inches
 *      - Converts weight from kgs to lbs
 *      - formats numbers to two decimal places
 */

public class GymUtility {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    /**
     * Calculate the members BMI (Weight divided by the square of the height).
     *
     * @param member     the member
     * @param assessment the members most recent assessment
     * @return the double formatted to two decimal places
     */

    public static double calculateBMI(Member member, Assessment assessment){
        return convertToTwoDecimalsPlaces(assessment.weight / (member.height * member.height));
    }


    /**
     * Determines what bmi category a user falls into.
     *
     * @param bmiValue the bmi value calculated using the method calculateBMI
     * @return the string
     */

    public static String determineBMICategory(double bmiValue) {
        String determinedCategory = "";

        if (bmiValue < 16) {
            determinedCategory = "SEVERELY UNDERWEIGHT";
        }
        else if ((bmiValue >= 16) && (bmiValue < 18.5)) {
            determinedCategory =  "UNDERWEIGHT";
        }
        else if ((bmiValue >= 18.5) && (bmiValue < 25)){
            determinedCategory = "NORMAL";
        }
        else if  ((bmiValue >= 25) && (bmiValue < 30)) {
            determinedCategory = "OVERWEIGHT";
        }
        else if  ((bmiValue >= 30) && (bmiValue < 35)) {
            determinedCategory = "MODERATELY OBESE";
        }
        else if  (bmiValue >= 35) {
            determinedCategory = "SEVERELY OBESE";
        }
        return determinedCategory;
    }

    /**
     * Uses the devine formula to determine if the member has an ideal body weight based on the members gender, height and
     * weight from their latest assessment.
     *
     * @param member     the member
     * @param assessment the assessment
     * @return the boolean
     */

    public static boolean isIdealBodyWeight(Member member, Assessment assessment){

        double workingHeight;               //hold converted height
        double baseHeight = 60.0;           //base height of 5ft converted to inches
        double baseMaleWeight = 50.0;       //base male weight is 50kgs
        double baseFemaleWeight = 45.5;     //base female weight is 45.5kgs
        double addWeight = 2.3;             //additional weight for every additional inch over baseHeight
        double devineWeight;

        workingHeight = convertMeterstoInches(member.height);   //convert height from metres to inches
        System.out.println("Converted height: " + workingHeight);
        //if the member is male and his height is less than 60 inches then return 50kgs
        if (member.gender.equals("Male")) {
            if (workingHeight <= baseHeight){
                devineWeight = baseMaleWeight;
            }
            else {
                //if the male member height is greater than 60 inches for every inch over 60 add 2.3kg to 50kg.
                devineWeight = ((workingHeight - baseHeight)*addWeight) + baseMaleWeight;
            }
        }
        //if the member is female and her height is less than 60 inches then return 45.5kgs
        else if (member.gender.equals("Female")) {
            if (workingHeight <= baseHeight){
                devineWeight = baseFemaleWeight;
            }
            else {
                //if the female member height is greater than 60 inches for every inch over 60 add 2.3kg to 45.5kg.
                devineWeight = ((workingHeight - baseHeight)*addWeight) + baseFemaleWeight;
            }
        }
        else {
            //if the member gender is unspecified and height is less than 60 inches then return 45.5kgs
            if (workingHeight <= baseHeight){
                devineWeight = baseFemaleWeight;
            }
            else {
                //if the members gender is unspecified and height is greater than 60 inches for every inch over 60 add 2.3kg to 45.5kg.
                devineWeight = ((workingHeight - baseHeight)*addWeight) + baseFemaleWeight;
            }
        }
        System.out.println("Devine weight: " + devineWeight);
        //check tolerance in rounding of +/- 0.2 to determine if member has an ideal body weight
        if ((assessment.weight >= (devineWeight - 0.2)) && (assessment.weight <= (devineWeight + 0.2))) {
            return true; //has an ideal body weight
        }
        else {
            return false; //does not have an ideal body weight
        }
    }


    /**
     * Convert meters to inches.
     *
     * @param metres the metres
     * @return the double
     */

    public static double convertMeterstoInches(double metres){
        double conversion = 39.3701; //39.3701 inches is the equivalent of 1 metre.
        return (metres * conversion);
    }


    /**
     * Convert to two decimals places.
     *
     * @param num the num
     * @return the double
     */

    public static double convertToTwoDecimalsPlaces(double num){
        return Double.parseDouble(df2.format(num));
    }
}
