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

            if (currentUser == null) {
                System.out.println(rb.getString("invalid"));
            }

        } while (currentUser == null);

        System.out.println(rb.getString("success"));

        printGeneralOptions();

        int generalChoice = -1;
        do {
            generalChoice = currentUser.getUserChoice();
            switch (generalChoice) {
                case 1:
                //Customer options
                    Customers.printCustomerOptions();
                    int choice = currentUser.getUserChoice();
                    Customers.executeCustomerChoice(choice, currentUser);
                    break;
                case 2:
                //appointment options
                    break;
                case 3:
                //calendar options
                    break;
                case 4:
                //report options
                    break;
                case 5:
                //exit program
                    break;
                default:
                //invalid value entered
            }
        } while (generalChoice != 5);

    }

    private static void printGeneralOptions() {
        System.out.println("\n\nOptions Menu");
        System.out.println("1. Customer Options");
        System.out.println("2. Appointment Options");
        System.out.println("3. View calendar");
        System.out.println("4. Generate report");
        System.out.println("5. Log out");
    }

}
