/*
 * Defines the CustomMonth class, which is used to display the month 
 * by week or by whole month
 */
package tenniescorpscheduling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

/**
 *
 * @author Joshua
 */
public class CustomMonth {

    //final instance members that hold all the Day objects in a given month
    final private Day[] daysInPreviousMonth;
    final private Day[] daysInCurrentMonth;
    final private Day[] daysInNextMonth;
    final private User currentUser;

    private LocalDate currentDate;
    private boolean monthlyViewActive = true;

    //Calls the private constructor with monthlyViewActive as true
    public CustomMonth(LocalDate currentDate, User currentUser) {
        this(currentDate, currentUser, true);
    }

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

    /*
    Changes the monthlyViewActive boolean to opposite of its current value,
    which is used to determine what type of calendar view should be displayed
     */
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

    /*
    Returns a new CustomMonth corresponding to the next month in the calendar
     */
    public CustomMonth nextMonth() {
        return new CustomMonth(currentDate.plusMonths(1), currentUser);
    }

    /*
    Moves the calendar forward one week, returning a new CustomMonth if the month
    changes or the current CustomMonth if it does not
     */
    public CustomMonth nextWeek() {
        if (doesMonthChange(1)) {
            return new CustomMonth(currentDate.plusWeeks(1), currentUser, false);
        }

        currentDate = currentDate.plusWeeks(1);
        return this;
    }

    /*
    Returns a new CustomMonth corresponding to the previous month in the calendar
     */
    public CustomMonth previousMonth() {
        return new CustomMonth(currentDate.minusMonths(1), currentUser);
    }

    /*
    Moves the calendar backward one week, returning a new CustomMonth if the month
    changes or the current CustomMonth if it does not
     */
    public CustomMonth previousWeek() {
        if (doesMonthChange(-1)) {
            return new CustomMonth(currentDate.plusWeeks(-1), currentUser, false);
        }

        currentDate = currentDate.plusWeeks(-1);
        return this;
    }

    /*
    Connects to the database retrieving all the appointments for the given
    month and populates the Day array with them
    @return the populated Day array
     */
    private Day[] populateDaysInMonth(LocalDate date) {
        Day[] month = null;

        try (Connection conn = DatabaseConnection.makeConnection();
                Statement stmt = DatabaseConnection.getConn().createStatement()) {

            //get the first and last date of the month
            LocalDate firstOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate lastOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());

            //create temporary Day[] with size of month
            month = new Day[lastOfMonth.getDayOfMonth()];

            //create the Day objects and add them into month
            Period p = Period.ofDays(1);
            LocalDate monthIterator = LocalDate.from(firstOfMonth);
            while (monthIterator.getDayOfMonth() != lastOfMonth.getDayOfMonth()) {
                month[monthIterator.getDayOfMonth() - 1]
                        = new Day(monthIterator.getDayOfMonth(), monthIterator.getDayOfWeek());
                monthIterator = monthIterator.plus(p);
            }

            //create Day object for last day of the month
            month[month.length - 1] = new Day(lastOfMonth.getDayOfMonth(), lastOfMonth.getDayOfWeek());

            //get all the appointments that fall within the month
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM appointment "
                    + "WHERE (start BETWEEN '%s' AND '%s') AND userId=%d", firstOfMonth.toString(),
                    lastOfMonth.toString(), currentUser.getUserId()));

            //add their appointments to their appropriate days
            while (rs.next()) {
                //create an Appointment object from the current record
                Appointment appt = new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"),
                        rs.getInt("userId"), rs.getString("title"), rs.getString("description"),
                        rs.getString("location"), rs.getString("contact"), rs.getString("type"),
                        rs.getString("url"), rs.getTimestamp("start").toLocalDateTime(),
                        rs.getTimestamp("end").toLocalDateTime());

                //add appt to the corresponding day in month
                int currentDayIndex = rs.getTimestamp("start").toLocalDateTime().getDayOfMonth() - 1;
                month[currentDayIndex].addAppointment(appt);

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

        //lambda expression that simplifies printing out the days of the month
        //using a method reference
        Arrays.asList(daysInCurrentMonth).forEach(System.out::println);

    }

    /*
    Gets the first day of the week and prints out each day of the 
    current week using the printDayOfWeek method
     */
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
            //if not special case then find the start of the week
            while (!(currentDate.getDayOfMonth() - startOfWeek.getDayOfMonth() < 7)) {
                startOfWeek = startOfWeek.plusWeeks(1);
            }
        }

        //print out each day of the week
        for (int i = 0; i < 7; i++) {
            printDayOfWeek(startOfWeek.plusDays(i));
        }
    }

    /*
    Determines whether a month changes when weeks are added
    @return true if the month changes, false if not
    */
    private boolean doesMonthChange(int additionalWeeks) {
        if (currentDate.getMonth()
                != currentDate.plusWeeks(additionalWeeks).getMonth()) {
            return true;
        }

        return false;
    }

    private void printHeader() {
        System.out.println("Month: " + currentDate.getMonth());
        System.out.printf("%-15s|%-24s|%-24s|%-24s|%-24s|%-24s|%-24s|%-24s|%-24s|%-24s\n",
                "", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
                "15:00", "16:00");

        //prints a separator
        for (int i = 0; i < 224; i++) {
            System.out.print("-");
        }
        System.out.println("");
    }

    /*
    Prints out each day of the week
    Handles possible days outside of the current month by retrieving those
    days from the appropriate month's array
     */
    private void printDayOfWeek(LocalDate date) {
//        System.out.println(String.valueOf(date.getDayOfMonth()) + " "
//                + date.getDayOfWeek());

        if (date.getMonth() == currentDate.getMonth()) {
            System.out.println(daysInCurrentMonth[date.getDayOfMonth() - 1]);
        } else if (date.getMonthValue() > currentDate.getMonthValue()) {
            System.out.println(daysInNextMonth[date.getDayOfMonth() - 1]);
        } else {
            System.out.println(daysInPreviousMonth[date.getDayOfMonth() - 1]);
        }
    }

}
