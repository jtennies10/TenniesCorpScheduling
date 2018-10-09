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

    private Customers() {
    }

    public static void printCustomerOptions() {
        System.out.println("\nCustomer Options");
        System.out.println("1. View Customers");
        System.out.println("2. Add Customer");
        System.out.println("3. Update Customer");
        System.out.println("4. Delete Customer");
    }

    public static void executeCustomerChoice(int userChoice, User currentUser) {
        switch (userChoice) {
            case 1:
                //view customers
                viewCustomers();
                break;
            case 2:
                //add customer
                if (addCustomer(currentUser)) {
                    System.out.println("Customer added successfully");
                } else {
                    System.out.println("Customer add failed");
                }
                break;
            case 3:
                //update customer
                if (updateCustomer(currentUser)) {
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

    //TODO: Update to new ERD
    public static void viewCustomers() {
        try {
            DatabaseConnection.makeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error cocmmunicating with the database.");
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM customer");

            printCustomerRecord("Customer ID", "Customer Name", "Address ID",
                    "Last Update By");
            //add a line for separting the header from the records
            System.out.println("----------------------------------------------"
                    + "--------------------------------------------------------"
                    + "--------------------------------------------------------"
                    + "------------------------------");

            while (rs.next()) {
                //print out each customer info
                printCustomerRecord(String.valueOf(rs.getInt("customerid")),
                        rs.getString("customerName"), String.valueOf(rs.getInt("addressId")),
                        rs.getString("lastUpdateBy"));

            }

            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
        }
    }

    //TODO: Update to new ERD
    public static boolean updateCustomer(User currentUser) {
        try {
            DatabaseConnection.makeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error cocmmunicating with the database.");
            return false;
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {
            String currentUserName = currentUser.getUserName();

            //ask for customer id
            System.out.print("Enter the ID of the customer you would like to update: ");
            int customerid = sc.nextInt();
            sc.nextLine(); //moves past enter character in input stream

            //check if customer exists
            ResultSet rs = stmt.executeQuery(
                    String.format("SELECT * FROM customer where customerid=%d", customerid));

            if (!rs.first()) { //then there is no result
                System.out.println("No customer exists with that ID.");
                return false;
            }

            //store address id for updating address table
            int customerAddressId = rs.getInt("addressId");

            //acquire updated customer table fields
            System.out.print("Enter customer name: ");
            String newName = sc.nextLine();

            //update customer table
            int result = stmt.executeUpdate(String.format("UPDATE customer"
                    + " SET customerName='%s', lastUpdate=NOW(), lastUpdateBy='%s'"
                    + " WHERE customerid=%d", newName, currentUserName, customerid));

            if (result != 1) {
                return false;
            }

            System.out.print("Enter customer address: ");
            String newAddress = sc.nextLine();

            System.out.print("Enter customer address 2(or enter for none): ");
            String newAddress2 = sc.nextLine();
            if (newAddress2.isEmpty()) {
                newAddress2 = " ";
            }

            System.out.print("Enter customer postal code: ");
            String newPostalCode = sc.nextLine();

            System.out.print("Enter customer phone: ");
            String newPhoneNumber = sc.nextLine();

            result = stmt.executeUpdate(String.format("UPDATE address SET"
                    + " address='%s', address2='%s', postalCode='%s', phone='%s'"
                    + ", lastUpdate=NOW(), lastUpdateBy='%s' WHERE addressId=%d",
                    newAddress, newAddress2, postalCode, newPhoneNumber, currentUserName,
                    customerAddressId));

            if (result != 1) {
                return false;
            }

            //acquire updated city fields
            System.out.print("Enter customer city: ");
            String newCity = sc.nextLine();

            result = stmt.executeUpdate(String.format("UPDATE address SET"
                    + " address='%s', address2='%s', postalCode='%s', phone='%s'"
                    + ", lastUpdate=NOW(), lastUpdateBy='%s' WHERE addressId=%d",
                    newAddress, newAddress2, postalCode, newPhoneNumber, currentUserName,
                    customerAddressId));

            if (result != 1) {
                return false;
            }

            //acquire updated country fields
            if (result != 1) {
                return false;
            }

            //close the connection
            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean addCustomer(User currentUser) {
        try {
            DatabaseConnection.makeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error cocmmunicating with the database.");
            return false;
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {

            //get customer info
            System.out.print("Enter customer name: ");
            String customerName = sc.nextLine();

            System.out.print("Enter customer address id: ");
            int customerAddressId = sc.nextInt();

            //add customer to the database
            stmt.executeUpdate(String.format("INSERT INTO customer(customerName, "
                    + "addressId, active, createDate, createdBy, lastUpdateBy) "
                    + "VALUES('%s', %d, 1, NOW(), '%s', '%s'", customerName,
                    customerAddressId, currentUser.getUserId(), currentUser.getUserId()));
            
            //check if the customerAddressId already exists for an address record,
            //if so return true
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM address WHERE "
                    + "addressId='%s'", customerAddressId));
            
            if(rs.next()) { //then an address was found
                return true;
            }
            
            //at this point the addressId, does not exist and an address record 
            //must be created
            System.out.println("Address Id does not exist, a new address must be created");
            
            System.out.print("Enter address: ");
            String address = sc.nextLine();
            
            System.out.print("Enter cityId: ");
            int cityId = sc.nextInt();
            
            System.out.println("Enter postal code: ");
            String postalCode = sc.nextLine();
            
            System.out.println("Enter phone");
            String phone = sc.nextLine();
            
            addAddress(stmt, customerAddressId, address, cityId, postalCode, phone, currentUser);
            
            //check if the cityId already exists for a city record,
            //if so return true
            rs = stmt.executeQuery(String.format("SELECT * FROM city WHERE "
                    + "cityId='%s'", cityId));
            
            if(rs.next()) { //then a city was found
                return true;
            }
            
            //at this point the countryId, does not exist and a country record 
            //must be created
            System.out.println("City Id does not exist, a new city must be created");
            
            System.out.print("Enter city: ");
            String city = sc.nextLine();
            
            System.out.print("Enter countryId: ");
            int countryId = sc.nextInt();
            
            addCity(stmt, cityId, city, countryId, currentUser);
            
            //check if the countryId already exists for a country record,
            //if so return true
            rs = stmt.executeQuery(String.format("SELECT * FROM country WHERE "
                    + "countryId='%s'", countryId));
            
            if(rs.next()) { //then a city was found
                return true;
            }
            
            //at this point the countryId, does not exist and a country record 
            //must be created
            System.out.println("City Id does not exist, a new country must be created");
            
            System.out.print("Enter country");
            String country = sc.nextLine();
            
            addCountry(stmt, countryId, country, currentUser);
            
            //close the connection
            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            return false;
        }

        return true;
    }

    public static boolean deleteCustomer() {
        //get cutomerid
        //delete if customer exists

        return false;
    }

    public static boolean findCustomer(Statement stmt, int customerid) throws SQLException {
        boolean customerFound = false;

        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM customer"
                + "WHERE customerId='%s'", customerid));

        if (rs.next()) {
            customerFound = true;
        }

        rs.close();
        return customerFound;
    }

    private static void printCustomerRecord(String customerid, String customerName,
            String addressId, String lastUpdateBy) {
        System.out.printf("%-40s| %-40s| %-45s| %-40s\n", customerid, customerName,
                addressId, lastUpdateBy);
    }

    private static void addAddress(Statement stmt, int addressId, String address, int cityId, String postalCode,
            String phone, User currentUser) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO address(addressId, address, cityId, postalCode, phone, "
                + "createDate, createdBy, lastUpdateBy) VALUES(%d, '%s', %d, '%s', '%s', NOW(), '%s', '%s'",
                addressId, address, cityId, postalCode, phone, currentUser.getUserName(), currentUser.getUserName()));

    }

    private static void addCity(Statement stmt, int cityId, String city, int countryId,
            User currentUser) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO city(cityId, city, countryId, createDate, "
                + "createdBy, lastUpdateBy) VALUES(%d, '%s', %d, NOW(), '%s', '%s'", cityId, city, countryId,
                currentUser.getUserId(), currentUser.getUserId()));

    }

    private static void addCountry(Statement stmt, int countryId, String country,
            User currentUser) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO country(countryId, country, createDate, "
                + "createdBy, lastUpdateBy) VALUES(%d, '%s', NOW(), '%s', '%s'", countryId, country,
                currentUser.getUserId(), currentUser.getUserId()));
    }
}
