package controllers;

import java.util.List;
import java.util.Date;
import models.Member;
import models.Trainer;
import models.Assessment;
import play.Logger;
import play.mvc.Controller;

/**
 * The MemberCtrl class includes methods for:
 *      - rendering the signup page.
 *      - rendering the login page.
 *      - registering an new user.
 *      - retrieving member object.
 *      - update member object.
 *      - update assessment comment.
 *      - authenticate user login.
 *      - retrieve logged in user.
 *      - logout user.
 *      - add a new assessment object for a user
 *      - delete an assessment object for a user
 *      - delete a member object
 *
 */
public class MemberCtrl extends Controller {

    /**
     * Renders the Signup form.
     */
    public static void signup()
    {
        render("signup.html");
    }

    /**
     * Renders the Login form.
     */
    public static void login()
    {
        render("login.html");
    }

    /**
     * Creates a new member object.
     *
     * @param name           the members name
     * @param gender         the members gender
     * @param email          the members email
     * @param password       the members password
     * @param address        the members address
     * @param height         the members height
     * @param startingWeight the members starting weight
     */
    public static void register(String name, String gender, String email, String password, String address, double height, double startingWeight)
    {
        Logger.info("Registering new user " + email);
        Member member = new Member(name, gender, email, password, address, height, startingWeight);
        member.save();
        redirect("/");
    }

    /**
     * Renders dashboard based on the member logged into the application.
     *
     * @param id the members id
     */
    public static void index(Long id) {
        Member member = Member.findById(id);
        Logger.info ("Member id = " + id);
        render("dashboard.html", member);
    }

    /**
     * Used to create a listing of members for the trainer.
     *
     * @param id the members id
     */
    public static void assessmentIndex(Long id) {
        //
        Member member = Member.findById(id);

        Logger.info ("Member id = " + id);
        render("assessments.html", member);
    }

    /**
     * Renders the member form so that the logged in member can change profile details.
     */
    public static void memberRetrieve() {
        Member member = MemberCtrl.getLoggedInMember();
        Logger.info ("Logged in Member id = " + member.id);
        render("member.html", member);
    }

    /**
     * Facilitates the update of existing member object properties.
     *
     * @param name           the members name
     * @param gender         the members gender
     * @param email          the members email address
     * @param password       the members password
     * @param address        the members address
     * @param height         the members height
     * @param startingWeight the members starting weight
     */
    public static void memberUpdate(String name, String gender, String email, String password, String address, double height, double startingWeight)
    {
        Member member = MemberCtrl.getLoggedInMember();
        Logger.info("Updating existing member " + member.email);
        member.name = name;
        member.gender = gender;
        member.email = email;
        member.password = password;
        member.address = address;
        member.height = height;
        member.startingWeight = startingWeight;
        member.save();
        redirect("/member");
    }

    /**
     * Allows trainers to update the comment property of an users associated assessment object.
     *
     * @param id       the member id
     * @param assessid the assessment id
     * @param comment  the comment
     */
    public static void updateComment(Long id, Long assessid, String comment){
        Member member = Member.findById(id);
        Assessment assessment = Assessment.findById(assessid);
        assessment.comment = comment;
        assessment.save();
        member.save();
        Logger.info("Updating assessment comment " + assessid);
        render("assessments.html", member);
    }


    /**
     * Authenticate verifies if the email address is either a member or a trainer object.
     * It then verifies if the password provides matches the members/trainers password property.
     *
     * @param email    the members email
     * @param password the members password
     */
    public static void authenticate(String email, String password)
    {
        Logger.info("Attempting to authenticate with " + email + ": " + password);
        Trainer trainer = Trainer.findByEmail(email);
        Member member = Member.findByEmail(email);
        //check if email address is registered against a trainer object and if password matches trainer object property.
        if ((trainer != null) && (trainer.checkPassword(password) == true)) {
            Logger.info("Authentication successful");
            session.put("logged_in_trainer id", trainer.id);
            redirect("/trainer");
        }
        //check if email address is registered against a member object and if password matches member object property.
        if ((member != null) && (member.checkPassword(password) == true)) {
            Logger.info("Authentication successful");
            session.put("logged_in_Memberid", member.id);
            redirect ("/dashboard");
        } else {
            //email address and/or password not found
            Logger.info("Authentication failed");
            redirect("/login");
        }
    }

    /**
     * Gets logged in member.
     *
     * @return the logged in member
     */
    public static Member getLoggedInMember()
    {
        Member member = null;
        if (session.contains("logged_in_Memberid")) {
            String memberId = session.get("logged_in_Memberid");
            member = Member.findById(Long.parseLong(memberId));
        } else {
            login();
        }
        return member;
    }

    /**
     * Logout.
     */
    public static void logout()
    {
        session.clear();
        redirect ("/");
    }

    /**
     * Add an assessment object to be associated with a member object.
     *
     * @param id       the id
     * @param weight   the weight
     * @param chest    the chest
     * @param thigh    the thigh
     * @param upperArm the upper arm
     * @param waist    the waist
     * @param hips     the hips
     */
    public static void addAssessment(Long id, Double weight, Double chest, Double thigh, Double upperArm, Double waist, Double hips){
        boolean positiveTrend;      //used to show if there has been an improvement between current weight and previous weight record.
        Date assessmentDate = new Date();   //records date of new assessment record creation.
        Member member = MemberCtrl.getLoggedInMember();
        List<Assessment> assessments = member.assessments;
        Assessment newestAssessment = assessments.get(assessments.size()-1);    //Pull back the last assessment recorded.
        if (member.assessments.size() == 0) {
            if (member.startingWeight > weight) {
                positiveTrend = true;   //member loses weight
            } else {
                positiveTrend = false;  //member maintains or gains weight
            }
        }
        else {
                if (newestAssessment.weight > weight) {
                    positiveTrend = true;  //member loses weight
                }
                else {
                    positiveTrend = false; //member maintains or gains weight
                }
            }
        //create a new assessment object
        Assessment assessment = new Assessment(assessmentDate, weight, chest, thigh, upperArm, waist, hips, positiveTrend);
        member.assessments.add(assessment);  //associates new assessment object with member object
        member.save();
        Logger.info ("Member id = " + id);
        redirect("/dashboard");
    }

    /**
     * Allows a member to delete an assessment object.
     *
     * @param id       the members id
     * @param assessid the assessment id
     */
    public static void deleteAssessment(Long id, Long assessid){
        Assessment assessment = Assessment.findById(assessid);
        Member member = Member.findById(id);
        Logger.info ("Removing assessment " + assessment.id);
        member.assessments.remove(assessment);
        member.save();
        assessment.delete();
        render("dashboard.html", member);
    }

    /**
     * Allows a trainer to delete a member object.
     *
     * @param id the members id
     */
    public static void deleteMember(Long id){
        Member member = Member.findById(id);
        Logger.info ("Remvoing member " + member.name);
        member.delete();
        redirect("/trainer");
    }
}
