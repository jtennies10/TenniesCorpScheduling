/*
 * Defines the Calendar class, which can display the calendar by month or week.
 */
package tenniescorpscheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;

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
        CustomMonth cm = new CustomMonth(LocalDate.now());
        do {
            printChoices();
            choice = currentUser.getUserChoice();
            
            switch (choice) {
                case 1:
                    //switch calendar and display
                    cm.changeViewType();
                    cm.displayView();

                    break;
                case 2:
                    //next week or month
                    if (cm.isMonthlyViewActive()) {
                        cm.nextMonth();
                        cm.displayView();
                    } else {
                        cm.nextWeek();
                        cm.displayView();
                    }

                    break;
                case 3:
                    //previous week or month
                    if (cm.isMonthlyViewActive()) {
                        cm.previousMonth();
                        cm.displayView();
                    } else {
                        cm.previousWeek();
                        cm.displayView();
                    }

                    break;
                case 4: //quit calendar, do nothing
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

//    private void displayMonthView() {
//        LocalDateTime currentMonthDateTime = startDateTime.minusMonths(monthOffset);
//        
//        LocalDateTime firstOfMonth = currentMonth.minusDays(
//                (currentMonth.getDayOfMonth()+1));
//        
//        LocalDateTime lastOfMonth = firstOfMonth.plusDays(monthOffset)
//    }
//
//    private void displayNextMonth() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    private void displayPreviousMonth() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    private void displayWeekView() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    private void displayNextWeek() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    private void displayPreviousWeek() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

}
