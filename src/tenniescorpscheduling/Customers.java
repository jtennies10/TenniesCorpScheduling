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
                if(deleteCustomer()) {
                    System.out.println("Customer deleted successfully");
                } else {
                    System.out.println("Customer delete failed");
                }
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
            System.out.println("Error communicating with the database.");
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM customer INNER JOIN address "
                    + "ON customer.addressId = address.addressId INNER JOIN city ON "
                    + "address.cityId = city.cityId INNER JOIN country ON "
                    + "city.countryId = country.countryId");

            printCustomerRecord("Customer ID", "Customer Name", "Address ID",
                    "Address", "City ID", "City", "Country ID", "Country");
            //add a line for separting the header from the records
            System.out.println("----------------------------------------------"
                    + "--------------------------------------------------------"
                    + "--------------------------------------------------------"
                    + "--------------------------------------------------------");

            while (rs.next()) {
                //print out each customer info
                printCustomerRecord(String.valueOf(rs.getInt("customerid")),
                        rs.getString("customerName"), String.valueOf(rs.getInt("addressId")),
                        rs.getString("address"), rs.getString("cityId"), rs.getString("city"),
                        String.valueOf(rs.getString("countryId")), rs.getString("country"));

            }

            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
        }
    }

    public static boolean updateCustomer(User currentUser) {
        try {
            DatabaseConnection.makeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error ccmmunicating with the database.");
            return false;
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {
            String currentUserName = currentUser.getUserName();

            //ask for customer id
            System.out.print("Enter the ID of the customer you would like to update: ");
            int customerid = Integer.parseInt(sc.nextLine());

            //check if customer exists
            if (!findCustomer(stmt, customerid)) {
                System.out.println("No customer exists with that ID.");
                return false;
            }

            //at this point the customerid must exist
            //acquire updated customer table fields for all tables
            System.out.print("Enter customer name: ");
            String newName = sc.nextLine();

            System.out.print("Enter customer addressId: ");
            int newAddressId = Integer.parseInt(sc.nextLine());

            //check if the customerAddressId already exists for an address record,
            //if so return true
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM address WHERE "
                    + "addressId=%d", newAddressId));

            if (!rs.next()) { //then an address was not found and needs created

                System.out.println("Address Id does not exist, a new address must be created");

                System.out.print("Enter address: ");
                String newAddress = sc.nextLine();

                System.out.print("Enter cityId: ");
                int newCityId = Integer.parseInt(sc.nextLine());

                System.out.print("Enter postal code: ");
                String newPostalCode = sc.nextLine();

                System.out.print("Enter phone: ");
                String newPhone = sc.nextLine();

                //check if the cityId already exists for a city record,
                //if so return true
                rs = stmt.executeQuery(String.format("SELECT * FROM city WHERE "
                        + "cityId=%d", newCityId));

                if (!rs.next()) { //then a city was not found and needs created

                    System.out.println("City Id does not exist, a new city must be created");

                    System.out.print("Enter city: ");
                    String newCity = sc.nextLine();

                    System.out.print("Enter countryId: ");
                    int newCountryId = Integer.parseInt(sc.nextLine());

                    //check if the countryId already exists for a country record,
                    //if so return true
                    rs = stmt.executeQuery(String.format("SELECT * FROM country WHERE "
                            + "countryId=%d", newCountryId));

                    if (!rs.next()) { //then a country was not found and needs created

                        System.out.println("City Id does not exist, a new country must be created");

                        System.out.print("Enter country: ");
                        String newCountry = sc.nextLine();

                        addCountry(stmt, newCountryId, newCountry, currentUser);

                    }

                    //the city can now be added since country has been added
                    addCity(stmt, newCityId, newCity, newCountryId, currentUser);

                }

                //the address can now be added since city has been added
                addAddress(stmt, newAddressId, newAddress, newCityId, newPostalCode, newPhone, currentUser);

            }

            //update customer table
            int result = stmt.executeUpdate(String.format("UPDATE customer"
                    + " SET customerName='%s', addressId=%d, lastUpdate=NOW(), lastUpdateBy='%s'"
                    + " WHERE customerid=%d", newName, newAddressId, currentUserName, customerid));

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
            System.out.println("Error communicating with the database.");
            return false;
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {

            //get customer info
            System.out.print("Enter customer name: ");
            String customerName = sc.nextLine();

            System.out.print("Enter customer address id: ");
            int customerAddressId = Integer.parseInt(sc.nextLine());

            //check if the customerAddressId already exists for an address record
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM address WHERE "
                    + "addressId=%d", customerAddressId));

            if (!rs.next()) { //then an address was not found and needs created

                System.out.println("Address Id does not exist, a new address must be created");

                System.out.print("Enter address: ");
                String address = sc.nextLine();

                System.out.print("Enter cityId: ");
                int cityId = Integer.parseInt(sc.nextLine());

                System.out.print("Enter postal code: ");
                String postalCode = sc.nextLine();

                System.out.print("Enter phone: ");
                String phone = sc.nextLine();

                //check if the cityId already exists for a city record
                rs = stmt.executeQuery(String.format("SELECT * FROM city WHERE "
                        + "cityId=%d", cityId));

                if (!rs.next()) { //then a city was not found and needs created

                    System.out.println("City Id does not exist, a new city must be created");

                    System.out.print("Enter city: ");
                    String city = sc.nextLine();

                    System.out.print("Enter countryId: ");
                    int countryId = Integer.parseInt(sc.nextLine());

                    //check if the countryId already exists for a country record
                    rs = stmt.executeQuery(String.format("SELECT * FROM country WHERE "
                            + "countryId=%d", countryId));

                    if (!rs.next()) { //then a country was not found and needs created

                        System.out.println("City Id does not exist, a new country must be created");

                        System.out.print("Enter country: ");
                        String country = sc.nextLine();

                        addCountry(stmt, countryId, country, currentUser);

                    }

                    //the city can now be added since country has been added
                    addCity(stmt, cityId, city, countryId, currentUser);
                }

                //the address can now be added since city has been added
                addAddress(stmt, customerAddressId, address, cityId, postalCode, phone, currentUser);
            }

            //add customer to the database
            stmt.executeUpdate(String.format("INSERT INTO customer(customerName, "
                    + "addressId, active, createDate, createdBy, lastUpdateBy) "
                    + "VALUES('%s', %d, 1, NOW(), '%s', '%s')", customerName,
                    customerAddressId, currentUser.getUserName(), currentUser.getUserName()));

            //close the connection
            DatabaseConnection.closeConnection();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean deleteCustomer() {
        //open database connection
        try {
            DatabaseConnection.makeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            return false;
        }

        try (Statement stmt = DatabaseConnection.getConn().createStatement()) {
            //get cutomerid
            System.out.print("Enter ID of the customer you would like to delete:  ");
            int customerid = Integer.parseInt(sc.nextLine());
            
            if(!findCustomer(stmt, customerid)) {
                System.out.println("No customer exists with that ID.");
                return false;
            }
            
            //at this point, the customerid must exist
            stmt.executeUpdate(String.format("DELETE FROM customer WHERE customerId=%d", customerid));
            
            DatabaseConnection.closeConnection();
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }

    public static boolean findCustomer(Statement stmt, int customerid) throws SQLException {
        boolean customerFound = false;

        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM customer "
                + "WHERE customerId=%d", customerid));

        if (rs.next()) {
            customerFound = true;
        }

        rs.close();
        return customerFound;
    }

    private static void printCustomerRecord(String customerid, String customerName,
            String addressId, String address, String cityId, String city, String countryId, String country) {
        System.out.printf("%-12s| %-45s| %-12s| %-50s| %-12s| %-50s| %-12s| %-50s|\n",
                customerid, customerName, addressId, address, cityId, city, countryId, country);
    }

    private static void addAddress(Statement stmt, int addressId, String address, int cityId, String postalCode,
            String phone, User currentUser) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO address(addressId, address, cityId, postalCode, phone, "
                + "createDate, createdBy, lastUpdateBy) VALUES(%d, '%s', %d, '%s', '%s', NOW(), '%s', '%s')",
                addressId, address, cityId, postalCode, phone, currentUser.getUserName(), currentUser.getUserName()));

    }

    private static void addCity(Statement stmt, int cityId, String city, int countryId,
            User currentUser) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO city(cityId, city, countryId, createDate, "
                + "createdBy, lastUpdateBy) VALUES(%d, '%s', %d, NOW(), '%s', '%s')", cityId, city, countryId,
                currentUser.getUserName(), currentUser.getUserName()));

    }

    private static void addCountry(Statement stmt, int countryId, String country,
            User currentUser) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO country(countryId, country, createDate, "
                + "createdBy, lastUpdateBy) VALUES(%d, '%s', NOW(), '%s', '%s')", countryId, country,
                currentUser.getUserName(), currentUser.getUserName()));
    }
}
