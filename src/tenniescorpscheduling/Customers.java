/*
 * Class to provide the methods used for viewing, creating, updating,
 * and deleting customers from the database.
 */
package tenniescorpscheduling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Joshua
 */
public class Customers {
    private Customers() {}
    
    public static void printCustomerOptions() {
        System.out.println("\nCustomer Options");
        System.out.println("1. View Customers");
        System.out.println("2. Add Customer");
        System.out.println("3. Update Customer");
        System.out.println("4. Delete Customer");
    }
    
    public static void executeCustomerChoice(int userChoice) {
        switch(userChoice) {
            case 1:
                //view customers
                viewCustomers();
                break;
            case 2:
                //add customer
                addCustomer();
                break;
            case 3:
                //update customer
                updateCustomer();
                break;
            case 4:
                //delete customer
                deleteCustomer();
                break;
            default:
                System.out.println("Invalid choice, returning to general options");
                
        }
    }
    
    public static void viewCustomers() {
        try {
            DatabaseConnection.makeConnection();
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("Error cocmmunicating with the database.");
        }
        
        try(Statement stmt = DatabaseConnection.getConn().createStatement()) {
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
            
            printCustomerRecord("Customer ID", "Customer Name", "Address ID",
                    "Phone Number", "Last Update By");
            //add a line for separting the header from the records
            System.out.println("----------------------------------------------"
                    + "--------------------------------------------------------" 
                    + "--------------------------------------------------------"
                    + "--------------------------------------------------------");
            
            while(rs.next()) {
                //print out each customer info
                printCustomerRecord(String.valueOf(rs.getInt("customerid")), 
                        rs.getString("customerName"), String.valueOf(rs.getInt("addressId")), 
                        rs.getString("phoneNumber"), rs.getString("lastUpdateBy"));
                
            }   

            DatabaseConnection.closeConnection();
            
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
        }
    }
    
    public static boolean updateCustomer() {
        return false;
    }
    
    public static boolean addCustomer() {
        return false;
    }
    
    public static boolean deleteCustomer() {
        return false;
    }
    
    public static boolean findCustomer(int customerid) {
        return false;
    }
    
    private static void printCustomerRecord(String customerid, String customerName,
            String addressId, String phoneNumber, String lastUpdateBy) {
        System.out.printf("%-40s| %-40s| %-45s| %-40s| %-40s\n", customerid, customerName,
                addressId, phoneNumber, lastUpdateBy);
    }
}
