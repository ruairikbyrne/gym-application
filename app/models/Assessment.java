package models;

import javax.persistence.Entity;
import java.util.Date;

import play.db.jpa.Model;

@Entity
public class Assessment extends Model {
    public Date assessmentDate = new Date();
    public double weight;
    public double chest;
    public double thigh;
    public double upperArm;
    public double waist;
    public double hips;
    public boolean positiveTrend;
    public String comment;

    public Assessment(Date asseessmentDate, double weight, double chest, double thigh, double upperArm, double waist, double hips, boolean positiveTrend){
        this.assessmentDate = assessmentDate;
        this.weight = weight;
        this.chest = chest;
        this.thigh = thigh;
        this.upperArm = upperArm;
        this.waist = waist;
        this.hips = hips;
        this.comment = "";
        this.positiveTrend = positiveTrend;
    }
}
