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

    private LocalDate currentDate;
    private int monthlyWeekNumber = 0;
    private boolean monthlyViewActive = true;

    //TODO: add code to get appointments from database that are in this month
    public CustomMonth(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public boolean isMonthlyViewActive() {
        return monthlyViewActive;
    }

    public void changeViewType() {
        monthlyViewActive = !monthlyViewActive;
    }

    public void displayView() {
        if (monthlyViewActive) {
            displayMonth();
        } else {
            displayWeek();
        }
    }

    public CustomMonth nextMonth() {
        return new CustomMonth(currentDate.plusMonths(1));
    }

    public CustomMonth nextWeek() {
        if (doesMonthChange(1)) {
            return new CustomMonth(currentDate.plusWeeks(1));
        }

        currentDate = currentDate.plusWeeks(1);
        return this;
    }

    public CustomMonth previousMonth() {
        return new CustomMonth(currentDate.minusMonths(1));
    }

    public CustomMonth previousWeek() {
        if (doesMonthChange(-1)) {
            return new CustomMonth(currentDate.plusWeeks(-1));
        }

        currentDate = currentDate.plusWeeks(-1);
        return this;
    }

    private void displayMonth() {
        System.out.println("Month: " + currentDate.getMonth());
        
    }

    private void displayWeek() {

    }

    private boolean doesMonthChange(int additionalWeeks) {
        if (currentDate.getMonth()
                != currentDate.plusWeeks(additionalWeeks).getMonth()) {
            return true;
        }

        return false;
    }

}
