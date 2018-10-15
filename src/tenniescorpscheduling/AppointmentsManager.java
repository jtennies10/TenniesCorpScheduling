/*
 * Defines AppointmentsManager class, used to add, update, delete, and
 * view AppointmentsManager.
 */
package tenniescorpscheduling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Scanner;

/**
 *
 * @author Joshua
 */
public class AppointmentsManager {

    static Scanner sc = new Scanner(System.in);

    public AppointmentsManager() {
    }

    public void printAppointmentOptions() {
        System.out.println("\nAppointment Options");
        System.out.println("1. View all appointments");
        System.out.println("2. Add appointment");
        System.out.println("3. Update appointment");
        System.out.println("4. Delete appointment");
        System.out.println("5. Return to general options");
    }

    public void executeAppointmentChoice(int userChoice, User currentUser) {
        try (Connection conn = DatabaseConnection.makeConnection();
                Statement stmt = DatabaseConnection.conn.createStatement()) {

            switch (userChoice) {
                case 1:
                    //view all AppointmentsManager
                    viewAllAppointments(stmt);
                    break;
                case 2:
                    //add Appointment
                    if (addAppointment(stmt, currentUser)) {
                        System.out.println("Appointment added successfully");
                    } else {
                        System.out.println("Appointment add failed");
                    }
                    break;
                case 3:
                    //update Appointment
                    if (updateAppointment(stmt, currentUser)) {
                        System.out.println("Appointment updated successfully");
                    } else {
                        System.out.println("Appointment update failed");
                    }
                    break;
                case 4:
                    //delete Appointment
                    if (deleteAppointment(stmt)) {
                        System.out.println("Appointment deleted successfully");
                    } else {
                        System.out.println("Appointment delete failed");
                    }
                    break;
                default: //case 5 and default
                    System.out.println("Returning to general options");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
        } catch (InvalidAppointmentException e) {
            System.out.println("Appoiment details are not valid.");
            System.out.println(e.getMessage());
        }
        
    }

    public void viewAllAppointments(Statement stmt) throws SQLException {

        printAppointmentRecord("Appointment ID", "Customer ID", "Customer Name", "User ID",
                "UserName", "Type", "Start", "End");
        //add a line for separting the header from the records
        System.out.println("----------------------------------------------"
                + "--------------------------------------------------------"
                + "--------------------------------------------------------"
                + "--------------------------------------------------------");

        ResultSet rs = stmt.executeQuery("SELECT * FROM appointment INNER JOIN "
                + "customer ON appointment.customerId=customer.customerId INNER JOIN "
                + "user ON appointment.userId=user.userId ORDER BY appointmentId");

        while (rs.next()) {
            //convert stored UTC times to the users time zone
            Timestamp storedStartTimeStamp = rs.getTimestamp("start");
            LocalDateTime localStartDateTime = storedStartTimeStamp.toLocalDateTime();

            Timestamp storedEndTimeStamp = rs.getTimestamp("end");
            LocalDateTime localEndDateTime = storedEndTimeStamp.toLocalDateTime();

            //print out each appointment record
            printAppointmentRecord(String.valueOf(rs.getInt("appointmentId")),
                    String.valueOf(rs.getInt("customerId")), rs.getString("customerName"),
                    String.valueOf(rs.getInt("userId")), rs.getString("userName"),
                    rs.getString("type"), localStartDateTime.toString(), localEndDateTime.toString());
        }

    }

    public boolean addAppointment(Statement stmt, User currentUser) throws SQLException {

        Appointment appt = getAppointmentInformation(stmt, currentUser);
        if (appt == null) {
            return false;
        }
        validate(stmt, appt);

        int result = stmt.executeUpdate(String.format("INSERT INTO "
                + "appointment(customerId, userId, title, description, location, "
                + "contact, type, start, end, createDate, createdBy, lastUpdateBy) "
                + "VALUES(%d, %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', NOW(),"
                + "'%s', '%s')", appt.getCustomerId(), currentUser.getUserId(), appt.getTitle(), appt.getDescription(),
                appt.getLocation(), appt.getContact(), appt.getType(), appt.getStart().toString(),
                appt.getEnd().toString(), currentUser.getUserName(),
                currentUser.getUserName()));

        if (result != 1) {
            return false;
        }

        return true;
    }

    public boolean updateAppointment(Statement stmt, User currentUser) throws SQLException {

        System.out.print("Enter appointment ID: ");
        int appointmentId = Integer.parseInt(sc.nextLine());

        if (!findAppointment(stmt, appointmentId)) {
            System.out.println("No appointment exists with that ID.");
            return false;
        }
        
        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM appointment "
                + "WHERE appoinmentId=%d", appointmentId));
        
        rs.first();
        int userId = rs.getInt("userId");
        
        Appointment appt = getAppointmentInformation(stmt, userId, currentUser);
        validate(stmt, appt);

        int result = stmt.executeUpdate(String.format(
                "UPDATE appointment SET title='%s', description='%s', location='%s', "
                + "type='%s', start='%s', end='%s', lastUpdate=NOW(), lastUpdateBy='%s' "
                + "WHERE appointmentId='%s'",
                appt.getTitle(), appt.getDescription(), appt.getLocation(), appt.getType(),
                appt.getStart(), appt.getEnd(), currentUser.getUserName(), appointmentId));

        if (result != 1) {
            return false;
        }

        return true;
    }

    public static boolean deleteAppointment(Statement stmt) throws SQLException {

        System.out.print("Enter appointment ID: ");
        int appointmentId = Integer.parseInt(sc.nextLine());

        if (!findAppointment(stmt, appointmentId)) {
            System.out.println("No appointment exists with that ID.");
            return false;
        }

        int result = stmt.executeUpdate(String.format(
                "DELETE FROM appointment WHERE appointmentId=%d", appointmentId));

        if (result != 1) {
            return false;
        }

        return true;
    }

    private static void printAppointmentRecord(String appointmentId, String customerId,
            String customerName, String userId, String userName, String type, String start, String end) {
        System.out.printf("%-14s| %-13s| %-35s| %-13s| %-35s|  %-20s| %-25s| %-25s\n",
                appointmentId, customerId, customerName, userId, userName, type, start, end);
    }

    private static String getAppointmentType() {

        System.out.println("What type of appointment is this?");
        System.out.println("1. New Customer Appointment");
        System.out.println("2. Returning Customer Appointment");
        System.out.println("3. Customer Complaint Appointment");
        System.out.println("4(or other). Custom Appointment Type");
        System.out.print("Choice: ");

        int choice = Integer.parseInt(sc.nextLine());
        switch (choice) {
            case 1:
                return "New Customer";
            case 2:
                return "Returning Customer";

            case 3:
                return "Customer Complaint";
            default: //covers 4 or other input
                System.out.println("Enter appointment type: ");
                return sc.nextLine();
        }
    }

    private static ZonedDateTime getStartTimeInUTC() {
        //first get the local date and time
        System.out.print("Enter appointment start time in the following format"
                + "\nNote - Start time must be at 00, 15, 30, or 45 minutes"
                + "\n(YYYY MM DD HH mm): ");
        ZonedDateTime local = ZonedDateTime.of(sc.nextInt(), sc.nextInt(),
                sc.nextInt(), sc.nextInt(), sc.nextInt(), 0, 0, ZoneId.systemDefault());

        sc.nextLine(); //just clears the new line character from the input stream

        //get the zone offset
        ZoneOffset offset = local.getOffset();

        //return the converted time
        return local.minusSeconds(offset.getTotalSeconds());
    }

    private static boolean findAppointment(Statement stmt, int appointmentId) throws SQLException {

        boolean apptFound = false;

        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM appointment WHERE "
                + "appointmentId=%d", appointmentId));

        if (rs.first()) {
            apptFound = true;
        }

        return apptFound;
    }

    private static Appointment getAppointmentInformation(Statement stmt, User currentUser) throws SQLException {
        return getAppointmentInformation(stmt, -1, currentUser);
    }

    private static Appointment getAppointmentInformation(Statement stmt,
            int customerId, User currentUser) throws SQLException {

        ResultSet rs;

        if (customerId == -1) {
            System.out.print("Enter ID of customer the appointment is for: ");
            customerId = Integer.parseInt(sc.nextLine());

            rs = stmt.executeQuery(String.format("SELECT * FROM customer WHERE customerId=%d",
                    customerId));

            if (!rs.next()) {
                System.out.println("The customer ID does not exist. "
                        + "Please create the customer in the customer options first");
                return null;
            }
        }

        System.out.print("Enter appointment title: ");
        String title = sc.nextLine();

        System.out.print("Enter appointment description: ");
        String description = sc.nextLine();

        System.out.print("Enter appointment location: ");
        String location = sc.nextLine();

        //get customer number to store as the contact field
        rs = stmt.executeQuery(String.format("SELECT phone FROM address WHERE addressId="
                + "(SELECT addressId FROM customer WHERE customerId=%d)", customerId));

        //move to first record and get the phone field
        rs.first();
        String contact = rs.getString("phone");

        rs.close();

        String type = getAppointmentType();

        //get the start time in UTC and then break it down into 
        //localdate and localtime
        ZonedDateTime start = getStartTimeInUTC();
        LocalDateTime localStartTimeInUTC = start.toLocalDateTime();

        //get appointment length and add it to startTimeInUTC
        //to get endTimeInUTC
        System.out.print("Enter length of appointment in minutes(15, 30, 45, or 60): ");
        int lengthInMinutes = Integer.parseInt(sc.nextLine());
        LocalDateTime localEndTimeInUTC = localStartTimeInUTC.plusMinutes(lengthInMinutes);

        //the appointmentId is set to zero to denote it is not currently known
        //by the program
        return new Appointment(0, customerId, currentUser.getUserId(), title, description,
                location, contact, type, "", localStartTimeInUTC, localEndTimeInUTC);
    }

    private static void validate(Statement stmt, Appointment appt) 
            throws InvalidAppointmentException, SQLException {
        //check if appointment is outside business hours
        LocalDateTime start = appt.getStart().plusSeconds(
                ZonedDateTime.now().getOffset().getTotalSeconds());
        LocalDateTime end = appt.getEnd().plusSeconds(
                ZonedDateTime.now().getOffset().getTotalSeconds());
        
        if(start.getHour() < 8 || start.getHour() > 17 
                || end.getHour() < 8 || end.getHour() > 17) {
            throw new InvalidAppointmentException("Appointment times "
                    + "outside business hours");
        }
        
        if(start.getMinute() % 15 != 0 || end.getMinute() % 15 != 0) {
            throw new InvalidAppointmentException("Start or end time "
                    + "is not a increment of 15 minutes");
        }
        
        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM appointment WHERE "
                + "((start BETWEEN '%s' AND '%s') OR (end BETWEEN '%s' AND '%s')) "
                + "AND (userId=%d)",
                start.toString(), end.toString(), start.toString(), end.toString(),
                appt.getUserId()));
        
        if(rs.first()) {
            throw new InvalidAppointmentException("Appointment overlaps existing "
                    + "appointment for the given user");
        }
        
        rs.close();
        
    }
}
