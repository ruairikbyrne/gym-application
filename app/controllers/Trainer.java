package controllers;

import models.Member;
import play.Logger;
import play.mvc.Controller;

import java.util.List;


public class Trainer extends Controller
{
    public static void index() {
        Logger.info("Render Trainer Dashboard");
        List<Member> member = Member.findAll();  //  Create a listing of all member objects
        render ("trainer.html", member);
    }


}
