/*
 * Contains the program main method and controls the program.
 */
package tenniescorpscheduling;

import java.sql.ResultSet;
import java.util.Scanner;

/**
 *
 * @author Joshua
 */
public class TenniesCorpScheduling {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to TenniesCorp Scheduling!");
        System.out.println("Please log in below.");

        User currentUser = null;
        do {
            currentUser = DataRetriever.attemptLogIn(getLogInUserName(), 
                    getLogInPassword());
            
            if(currentUser == null) {
                System.out.println("Invalid username and password. "
                        + "Please try again");
            }
        
        } while(currentUser == null);
        
        System.out.println("Log In successful!");
        
    }

    public static String getLogInUserName() {
        System.out.print("Username: ");
        return sc.nextLine();

    }

    public static String getLogInPassword() {
        System.out.print("Password: ");
        return sc.nextLine();
    }
}
