/*
 * Defines the CustomMonth class, which is used to display the month 
 * by week or by whole month
 */
package tenniescorpscheduling;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 *
 * @author Joshua
 */
public class CustomMonth {

    final private LocalDate firstOfMonth;
    final private LocalDate lastOfMonth;
    private int monthlyWeekNumber = 0;
    private boolean monthlyViewActive = true;

    public CustomMonth(LocalDate currentDate) {
        firstOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        lastOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
    }
    
    public CustomMonth(LocalDate currentDate, int monthlyWeekNumber) {
        firstOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        lastOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
        this.monthlyWeekNumber = monthlyWeekNumber;              
    }

    public boolean isMonthlyViewActive() {
        return monthlyViewActive;
    }
    
    public void changeViewType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void displayView() {
        if(monthlyViewActive) displayMonth();
        else displayWeek();
    }

    public void nextMonth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void nextWeek() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void previousMonth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void previousWeek() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void displayMonth() {
        
    }
    
    private void displayWeek() {
        
    }
    
    
}
