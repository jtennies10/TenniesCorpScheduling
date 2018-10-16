/*
 * Contains the program main method and controls the program.
 */
package tenniescorpscheduling;

import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;


public class Main {

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
        LoginManager lm = new LoginManager();
        do {
            currentUser = lm.attemptLogIn(rb);
            currentUser.printLogInAttemptToFile();

            if (!currentUser.isLoginSuccessful()) {
                System.out.println(rb.getString("invalid"));
            }

        } while (!currentUser.isLoginSuccessful());

        System.out.println(rb.getString("success"));
        lm.checkUpcomingAppointment(currentUser);

        
        CustomersManager customersManager = new CustomersManager();
        AppointmentsManager apptsManager = new AppointmentsManager();
        CalendarManager calendarManager = new CalendarManager();
        ReportManager reportManager = new ReportManager();
        
        int generalChoice = -1;
        do {
            
            printGeneralOptions();
            generalChoice = currentUser.getUserChoice();
            int choice = -1; //for specific user choice
            switch (generalChoice) {
                case 1:
                //Customer options
                    customersManager.printCustomerOptions();
                    choice = currentUser.getUserChoice();
                    customersManager.executeCustomerChoice(choice, currentUser);
                    break;
                case 2:
                //appointment options
                    apptsManager.printAppointmentOptions();
                    choice = currentUser.getUserChoice();
                    apptsManager.executeAppointmentChoice(choice, currentUser);
                    break;
                case 3:
                //calendar options
                    calendarManager.openCalendar(currentUser);
                    break;
                case 4:
                //report options
                    reportManager.printCustomerOptions();
                    choice = currentUser.getUserChoice();
                    reportManager.executeReportChoice(choice);
                    break;
                case 5:
                //exit program, do nothing
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
