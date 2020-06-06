package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Trainer extends Model {
    public String name;
    public String email;
    public String password;


    public Trainer(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;

    }

    public static Trainer findByEmail(String email){
        return find("email", email).first();
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
