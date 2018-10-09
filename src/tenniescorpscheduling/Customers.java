/*
 * Class to provide the methods used for viewing, creating, updating,
 * and deleting customers from the database.
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
public class Customers {
    static Scanner sc = new Scanner(System.in);
    
    private Customers() {}
    
    
    public static void printCustomerOptions() {
        System.out.println("\nCustomer Options");
        System.out.println("1. View Customers");
        System.out.println("2. Add Customer");
        System.out.println("3. Update Customer");
        System.out.println("4. Delete Customer");
    }
    
    public static void executeCustomerChoice(int userChoice, User currentUser) {
        switch(userChoice) {
            case 1:
                //view customers
                viewCustomers();
                break;
            case 2:
                //add customer
                if(addCustomer(currentUser)) {
                    System.out.println("Customer added successfully");
                } else {
                    System.out.println("Customer add failed");
                }
                break;
            case 3:
                //update customer
                if(updateCustomer(currentUser)) {
                    System.out.println("Customer updated successfully");
                } else {
                    System.out.println("Customer update failed");
                }
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
                    "Last Update By");
            //add a line for separting the header from the records
            System.out.println("----------------------------------------------"
                    + "--------------------------------------------------------" 
                    + "--------------------------------------------------------"
                    + "------------------------------");
            
            while(rs.next()) {
                //print out each customer info
                printCustomerRecord(String.valueOf(rs.getInt("customerid")), 
                        rs.getString("customerName"), String.valueOf(rs.getInt("addressId")), 
                        rs.getString("lastUpdateBy"));
                
            }   

            DatabaseConnection.closeConnection();
            
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
        }
    }
    
    public static boolean updateCustomer(User currentUser) {
        try {
            DatabaseConnection.makeConnection();
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("Error cocmmunicating with the database.");
            return false;
        }
        
        try(Statement stmt = DatabaseConnection.getConn().createStatement()) {
            //ask for customer id
            System.out.print("Enter the ID of the customer you would like to update: ");
            int customerid = sc.nextInt();
            sc.nextLine(); //moves past enter character in input stream
            
            //check if customer exists
            ResultSet rs = stmt.executeQuery(
                    String.format("SELECT * FROM customer where customerid=%d", customerid));
             
            if(!rs.first()) { //then there is no result
                System.out.println("No customer exists with that ID.");
                return false;
            }
            
            int customerAddressId = rs.getInt("addressId");
            
            //ask for updated field
            System.out.print("Enter customer name: ");
            String newName = sc.nextLine();
            
            System.out.print("Enter customer address: ");
            String newAddress = sc.nextLine();
            
            System.out.print("Enter customer address 2(or enter for none): ");
            String newAddress2 = sc.nextLine();
            if(newAddress2.isEmpty()) {
                newAddress2 = " ";
            }
            
            System.out.print("Enter customer phone: ");
            String newPhoneNumber = sc.nextLine();
            
            
            int result = stmt.executeUpdate(String.format("UPDATE customer"
                    + " SET customerName='%s', lastUpdate=NOW(), lastUpdateBy='%s'"
                    + " WHERE customerid=%d", newName, currentUser.getUserName(), customerid));
            
            if(result != 1) {
                System.out.println("Error updating customer information");
                return false;
            }
           
            result = stmt.executeUpdate(String.format("UPDATE address SET"
                    + " address='%s', address2='%s', phone='%s'"
                    + ", lastUpdate=NOW(), lastUpdateBy='%s' WHERE addressId=%d",
                    newAddress, newAddress2, newPhoneNumber, currentUser.getUserName(),
                    customerAddressId));
           
            if(result != 1) {
                System.out.println("Error updating customer information");
                return false;
            }
            
            DatabaseConnection.closeConnection();
            
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public static boolean addCustomer(User currentUser) {
        return false;
    }
    
    public static boolean deleteCustomer() {
        return false;
    }
    
    public static boolean findCustomer(int customerid) {
        return false;
    }
    
    private static void printCustomerRecord(String customerid, String customerName,
            String addressId, String lastUpdateBy) {
        System.out.printf("%-40s| %-40s| %-45s| %-40s\n", customerid, customerName,
                addressId, lastUpdateBy);
    }
}
