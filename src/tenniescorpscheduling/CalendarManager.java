/*
 * Defines the Calendar class, which can display the calendar by month or week.
 */
package tenniescorpscheduling;

import java.time.LocalDate;


public class CalendarManager {
    
    public CalendarManager() {
    }

    /*
    Opens the calendar and loops through, performing various modifications
    until the user chooses to quit the calendar
    */
    public void openCalendar(User currentUser) {
        int choice = -1;
        //create a CustomMonth object with the current date
        CustomMonth cm = new CustomMonth(LocalDate.now(), currentUser);
        do {
            
            cm.displayView();
            
            printChoices();
            choice = currentUser.getUserChoice();
            
            switch (choice) {
                case 1:
                    //switch calendar display type
                    cm.changeViewType();

                    break;
                case 2:
                    //next week or month
                    if (cm.isMonthlyViewActive()) {
                        cm = cm.nextMonth();
                    } else {
                        cm = cm.nextWeek();
                    }

                    break;
                case 3:
                    //previous week or month
                    if (cm.isMonthlyViewActive()) {
                        cm = cm.previousMonth();
                    } else {
                        cm = cm.previousWeek();
                    }

                    break;
                case 4: //quit calendar, do nothing
                    System.out.println("Quit");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }

    private void printChoices() {
        System.out.println("1. Switch calendar display");
        System.out.println("2. Next");
        System.out.println("3. Previous");
        System.out.println("4. Quit calendar");
    }

}
