/*
 * Defines the Calendar class, which can display the calendar by month or week.
 */
package tenniescorpscheduling;

import java.time.ZonedDateTime;

/**
 *
 * @author Joshua
 */
public class CalendarManager {
    final ZonedDateTime startTime;
    int monthOffset = 0;
    int monthlyWeekNumber = 0;
    boolean monthlyViewActive = true;
    
    public CalendarManager(ZonedDateTime startTime) {
        this.startTime = startTime;
    }
    
    public void openCalendar(User currentUser) {
        int choice = -1;
        do {
            printChoices();
            choice = currentUser.getUserChoice();
            switch(choice) {
                case 1:
                    //switch calendar
                    break;
                case 2:
                    //next week or month
                    break;
                case 3:
                    //previous week or month
                    break;
                case 4:
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
