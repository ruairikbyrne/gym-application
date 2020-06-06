package controllers;

import models.Assessment;
import models.Member;
import play.Logger;
import play.mvc.Controller;
import java.util.List;


/**
 * Gets information used to populate the members dashboard with BMI, BMI Category, ideal weight indicators and assessment details.
 */
public class Dashboard extends Controller
{
  /**
   * Index.
   */
  public static void index() {
    double calculatedBMI;
    String categoryBMI;
    boolean idealBodyWeight = true;

    Logger.info("Rendering Dashboard");
    Member member = MemberCtrl.getLoggedInMember();
    List<Assessment> assessments = member.assessments;  // Create a list of all associated member assessment objects.
    Assessment newestAssessment = assessments.get(assessments.size()-1);  //get newest assessment from list
    Logger.info("Assessments Data:  " + newestAssessment);
    calculatedBMI = GymUtility.calculateBMI(member, newestAssessment);  // calculate members BMI
    Logger.info("Calculated BMI:"  + calculatedBMI);
    categoryBMI = GymUtility.determineBMICategory(calculatedBMI);       // use members BMI to determine BMI category
    Logger.info("Category BMI:"  + categoryBMI);
    idealBodyWeight = GymUtility.isIdealBodyWeight(member, newestAssessment);
    Logger.info("Ideal body weight:"  + idealBodyWeight);
    render ("dashboard.html", member, assessments, calculatedBMI, categoryBMI, idealBodyWeight);
  }
}
