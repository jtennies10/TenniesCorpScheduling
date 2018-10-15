/*
 * Defines the Calendar class, which can display the calendar by month or week.
 */
package tenniescorpscheduling;

import java.time.LocalDate;

/**
 *
 * @author Joshua
 */
public class CalendarManager {

    final LocalDate startDate;
    int monthOffset;
    
 

    public CalendarManager(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void openCalendar(User currentUser) {
        int choice = -1;
        CustomMonth cm = new CustomMonth(LocalDate.now(), currentUser);
        do {
            
            cm.displayView();
            
            printChoices();
            choice = currentUser.getUserChoice();
            
            switch (choice) {
                case 1:
                    //switch calendar and display
                    System.out.println("Switch");
                    cm.changeViewType();

                    break;
                case 2:
                    //next week or month
                    System.out.println("Next");
                    if (cm.isMonthlyViewActive()) {
                        cm = cm.nextMonth();
                    } else {
                        cm = cm.nextWeek();
                    }

                    break;
                case 3:
                    //previous week or month
                    System.out.println("Previous");
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
