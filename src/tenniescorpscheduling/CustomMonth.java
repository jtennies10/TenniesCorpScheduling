/*
 * Defines the CustomMonth class, which is used to display the month 
 * by week or by whole month
 */
package tenniescorpscheduling;

import java.awt.BorderLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
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
        this(currentDate, true);
    }
    
    public CustomMonth(LocalDate currentDate, boolean monthlyViewActive) {
        this.currentDate = currentDate;
        this.monthlyViewActive = monthlyViewActive;
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
            return new CustomMonth(currentDate.plusWeeks(1), false);
        }

        currentDate = currentDate.plusWeeks(1);
        return this;
    }

    public CustomMonth previousMonth() {
        return new CustomMonth(currentDate.minusMonths(1));
    }

    public CustomMonth previousWeek() {
        if (doesMonthChange(-1)) {
            return new CustomMonth(currentDate.plusWeeks(-1), false);
        }

        currentDate = currentDate.plusWeeks(-1);
        return this;
    }

    //START HERE
    private void displayMonth() {
        printHeader();

        //obtain the first and last days of the month
        //the first is called upTo as it will iterate through the month
        LocalDate upTo = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
        Period p = Period.ofDays(1);

        while (upTo.getDayOfMonth() != lastOfMonth.getDayOfMonth()) {
            printDay(upTo);
            upTo = upTo.plus(p);
        }

        //the last day of the month still needs printed out
        printDay(lastOfMonth);

    }

    private void displayWeek() {
        printHeader();

        //find the startOfWeek
        LocalDate startOfWeek = currentDate.with(
                TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.SUNDAY));

        //special case in which the first sunday of the month comes after the
        //currentDate
        if ((currentDate.getDayOfMonth() - startOfWeek.getDayOfMonth() < 0)) {
            startOfWeek = startOfWeek.minusWeeks(1);
        } else {
            //if not special case then find the start of the month
            while (!(currentDate.getDayOfMonth() - startOfWeek.getDayOfMonth() < 7)) {
                startOfWeek = startOfWeek.plusWeeks(1);
            }
        }

        //print out each day of the week
        for(int i = 0; i < 7; i++) {
            printDay(startOfWeek.plusDays(i));
        }
    }

    private boolean doesMonthChange(int additionalWeeks) {
        if (currentDate.getMonth()
                != currentDate.plusWeeks(additionalWeeks).getMonth()) {
            return true;
        }

        return false;
    }

    private void printHeader() {
        System.out.println("Month: " + currentDate.getMonth());
        System.out.printf("%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s\n",
                "", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
                "15:00", "16:00");

        //print a separator
        for (int i = 0; i < 160; i++) {
            System.out.print("-");
        }
        System.out.println("");
    }

    private void printDay(LocalDate date) {
        System.out.println(String.valueOf(date.getDayOfMonth()) + " "
                + date.getDayOfWeek());
    }

}
