/*
 * Defines Appointments class, used to add, update, delete, and
 * view Appointments.
 */
package tenniescorpscheduling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class Appointments {

    static Scanner sc = new Scanner(System.in);

    private Appointments() {
    }

    public static void printAppointmentOptions() {
        System.out.println("\nAppointment Options");
        System.out.println("1. View all appointments");
        System.out.println("2. Add appointment");
        System.out.println("3. Update appointment");
        System.out.println("4. Delete appointment");
    }

    public static void executeAppointmentChoice(int userChoice, User currentUser) {
        switch (userChoice) {
            case 1:
                //view all Appointments
                viewAllAppointments();
                break;
            case 2:
                //add Appointment
                if (addAppointment(currentUser)) {
                    System.out.println("Appointment added successfully");
                } else {
                    System.out.println("Appointment add failed");
                }
                break;
            case 3:
                //update Appointment
                if (updateAppointment(currentUser)) {
                    System.out.println("Appointment updated successfully");
                } else {
                    System.out.println("Appointment update failed");
                }
                break;
            case 4:
                //delete Appointment
                if (deleteAppointment()) {
                    System.out.println("Appointment deleted successfully");
                } else {
                    System.out.println("Appointment delete failed");
                }
                break;
            default:
                System.out.println("Invalid choice, returning to general options");

        }
    }

    public static void viewAllAppointments() {
        try {
            DatabaseConnection.makeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {

            printAppointmentRecord("Appointment ID", "Customer ID", "User ID",
                    "Title", "Location", "Type", "Start", "End");

            ResultSet rs = stmt.executeQuery("SELECT * FROM appointment");

            while (rs.next()) {
                //print out each appointment record
                printAppointmentRecord(String.valueOf(rs.getInt("appointmentId")),
                        String.valueOf(rs.getInt("customerId")), String.valueOf(rs.getInt("userId")),
                        rs.getString("title"), rs.getString("location"), rs.getString("type"),
                        rs.getString("start"), rs.getString("end"));
            }

            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
        }
    }

    public static boolean addAppointment(User currentUser) {
        try {
            DatabaseConnection.makeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {

            System.out.print("Enter ID of customer the appointment is for: ");
            int customerId = Integer.parseInt(sc.nextLine());

            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM customer WHERE customerId=%d",
                    customerId));

            if (!rs.next()) {
                System.out.println("The customer ID does not exist. "
                        + "Please create the customer in the customer options first");
                return false;
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

            String type = getAppointmentType();
            
            //get the start time in UTC and then break it down into 
            //localdate and localtime
            ZonedDateTime start = getStartTimeInUTC();
            LocalTime startTimeInUTC = start.toLocalTime();
            LocalDate dateInUTC = start.toLocalDate();
            
            //get appointment length and add it to startTimeInUTC
            //to get endTimeInUTC
            System.out.print("Enter length of appointment in minutes: ");
            int lengthInMinutes = Integer.parseInt(sc.nextLine());
            LocalTime endTimeInUTC = startTimeInUTC.plusMinutes(lengthInMinutes);
            
            
            int result = stmt.executeUpdate(String.format("INSERT INTO "
                    + "appointment(customerId, userId, title, description, location, "
                    + "contact, type, start, end, createDate, createdBy, lastUpdateBy) "
                    + "VALUES(%d, %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', NOW(),"
                    + "'%s', '%s')", customerId, currentUser.getUserId(), title, description, 
                    location, contact, type, (dateInUTC + " " + startTimeInUTC), 
                    (dateInUTC + " " + endTimeInUTC), currentUser.getUserName(), 
                    currentUser.getUserName()));
             
            if(result != 1) return false;

            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean updateAppointment(User CurrentUser) {
        try {
            DatabaseConnection.makeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {

            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
            return false;
        }

        return false;
    }

    public static boolean deleteAppointment() {
        try {
            DatabaseConnection.makeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {

            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
            return false;
        }

        return false;
    }

    private static void printAppointmentRecord(String appointmentId, String customerId, String userId,
            String title, String location, String type, String start, String end) {
        System.out.printf("%-14s| %-13s| %-13s| %-50s| %-50s| %-50s| %-25s| %-25s|\n",
                appointmentId, customerId, userId, title, location, type, start, end);
    }

    private static String getAppointmentType() {
        System.out.println("What type of appointment is this?");
        System.out.println("1. New Customer Appointment");
        System.out.println("2. Returning Customer Appointment");
        System.out.println("3. Customer Complaint Appointment");
        System.out.print("Choice: ");
        return sc.nextLine();
    }

    private static ZonedDateTime getStartTimeInUTC() {
        //first get the local date and time
        System.out.print("Enter appointment start time in the following format"
                + "\n(YYYY MM DD HH mm): ");
        ZonedDateTime local = ZonedDateTime.of(sc.nextInt(), sc.nextInt(),
                sc.nextInt(), sc.nextInt(), sc.nextInt(), 0, 0, ZoneId.systemDefault());
        
        sc.nextLine(); //just clears the new line character from the input stream
        
        //get the zone offset
        ZoneOffset offset = local.getOffset();
        
        //return the converted time
        return local.minusSeconds(offset.getTotalSeconds());
    }
}
