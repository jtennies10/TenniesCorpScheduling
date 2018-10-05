/*
 * Contains the program main method and controls the program.
 */
package tenniescorpscheduling;

import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 *
 * @author Joshua
 */
public class TenniesCorpScheduling {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        //Obtain user locale and create the resource bundle using it
        //Comment/Uncomment the locale instantiations to change the language
        Locale locale = Locale.getDefault();
        //Locale locale = new Locale("es");
        
        ResourceBundle rb = ResourceBundle.getBundle("res.Login", locale);
        
        System.out.println(rb.getString("welcome"));
        System.out.println(rb.getString("login"));

        User currentUser = null;
        do {
            currentUser = Login.attemptLogIn(rb);
            
            if(currentUser == null) {
                System.out.println(rb.getString("invalid"));
            }
        
        } while(currentUser == null);
        
        System.out.println(rb.getString("success"));
    }

}
