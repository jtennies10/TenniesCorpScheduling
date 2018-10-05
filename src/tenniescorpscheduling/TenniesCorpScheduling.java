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
        
        
        //System.out.println("Welcome to TenniesCorp Scheduling!");
        System.out.println(rb.getString("welcome"));
        //System.out.println("Please log in below.");
        System.out.println(rb.getString("login"));

        User currentUser = null;
        do {
            currentUser = DataRetriever.attemptLogIn(getLogInUserName(rb), 
                    getLogInPassword(rb), rb);
            
            if(currentUser == null) {
                //System.out.println("Invalid username and password. "
                       // + "Please try again");
                System.out.println(rb.getString("invalid"));
            }
        
        } while(currentUser == null);
        
        //System.out.println("Log In successful!");
        System.out.println(rb.getString("success"));
    }

    public static String getLogInUserName(ResourceBundle rb) {
        //System.out.print("Username: ");
        System.out.println(rb.getString("username"));
        return sc.nextLine();

    }

    public static String getLogInPassword(ResourceBundle rb) {
        //System.out.print("Password: ");
        System.out.println(rb.getString("password"));
        return sc.nextLine();
    }
}
