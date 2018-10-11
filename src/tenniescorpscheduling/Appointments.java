/*
 * Defines Appointments class, used to add, update, delete, and
 * view Appointments.
 */
package tenniescorpscheduling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author Joshua
 */
public class Appointments {
    static Scanner sc = new Scanner(System.in);
    
    public static void printAppointmentOptions() {
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
                if(deleteAppointment()) {
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


            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
            return false;
        }
        
        return false;
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
    
    
}
