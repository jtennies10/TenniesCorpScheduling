/*
 * Defines the CustomMonth class, which is used to display the month 
 * by week or by whole month
 */
package tenniescorpscheduling;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

/**
 *
 * @author Joshua
 */
public class CustomMonth {

    final private Day[] daysInPreviousMonth;
    final private Day[] daysInCurrentMonth;
    final private Day[] daysInNextMonth;
    final private User currentUser;

    private LocalDate currentDate;
    private boolean monthlyViewActive = true;

    public CustomMonth(LocalDate currentDate, User currentUser) {
        this(currentDate, currentUser, true);
    }
    //TODO: add code to get appointments from database that are in this month

    private CustomMonth(LocalDate currentDate, User currentUser, boolean monthlyViewActive) {
        this.currentDate = currentDate;
        this.monthlyViewActive = monthlyViewActive;
        this.currentUser = currentUser;

        //populate the Day[] arrays
        daysInCurrentMonth = populateDaysInMonth(currentDate);
        daysInPreviousMonth = populateDaysInMonth(currentDate.minusMonths(1));
        daysInNextMonth = populateDaysInMonth(currentDate.plusMonths(1));

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
        return new CustomMonth(currentDate.plusMonths(1), currentUser);
    }

    public CustomMonth nextWeek() {
        if (doesMonthChange(1)) {
            return new CustomMonth(currentDate.plusWeeks(1), currentUser, false);
        }

        currentDate = currentDate.plusWeeks(1);
        return this;
    }

    public CustomMonth previousMonth() {
        return new CustomMonth(currentDate.minusMonths(1), currentUser);
    }

    public CustomMonth previousWeek() {
        if (doesMonthChange(-1)) {
            return new CustomMonth(currentDate.plusWeeks(-1), currentUser, false);
        }

        currentDate = currentDate.plusWeeks(-1);
        return this;
    }

    private Day[] populateDaysInMonth(LocalDate date) {
        Day[] month = null;
        
        try (Connection conn = DatabaseConnection.makeConnection();
                Statement stmt = DatabaseConnection.conn.createStatement()) {
            
            //get the first and last date of the month
            LocalDate firstOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate lastOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());

            //create temporary Day[] with size of month
            month = new Day[lastOfMonth.getDayOfMonth()];

            //get all the appointments that fall within the month
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM appointment "
                    + "WHERE (start BETWEEN '%s' AND '%s') AND userId=%d", firstOfMonth.toString(),
                    lastOfMonth.toString(), currentUser.getUserId()));

            //create the Day objects and add them into month
            Period p = Period.ofDays(1);
            while(firstOfMonth.getDayOfMonth() != lastOfMonth.getDayOfMonth()) {
                month[firstOfMonth.getDayOfMonth()-1] 
                        = new Day(firstOfMonth.getDayOfMonth(), firstOfMonth.getDayOfWeek());
                firstOfMonth = firstOfMonth.plus(p);
            }
            
            //create Day object for last day of the month
            month[month.length-1] = new Day(lastOfMonth.getDayOfMonth(), lastOfMonth.getDayOfWeek());
            
            //add their appointments to their appropriate days
            while(rs.next()) {
                //create an Appointment object from the current record
                Appointment appt = new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), 
                        rs.getInt("userId"), rs.getString("title"), rs.getString("description"), 
                        rs.getString("location"), rs.getString("contact"), rs.getString("type"), 
                        rs.getString("url"), rs.getTimestamp("start").toLocalDateTime(), 
                        rs.getTimestamp("end").toLocalDateTime());
                
                //add appt to the corresponding day in month
                int currentDay = rs.getTimestamp("start").toLocalDateTime().getDayOfMonth() - 1;
                month[currentDay].addAppointment(appt);   
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicatng with the database. Appointments "
                    + "retrieval failed");
            System.out.println(e.getMessage());
        }
       
        return month;
    }

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
        for (int i = 0; i < 7; i++) {
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

    //TODO:convert so that printDay prints the actual Date object
    private void printDay(LocalDate date) {
        System.out.println(String.valueOf(date.getDayOfMonth()) + " "
                + date.getDayOfWeek());
    }

}
