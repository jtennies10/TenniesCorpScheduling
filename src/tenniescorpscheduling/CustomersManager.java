/*
 * Class to provide the methods used for viewing, creating, updating,
 * and deleting customers from the database.
 */
package tenniescorpscheduling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.function.Predicate;

public class CustomersManager implements Predicate {

    Scanner sc = new Scanner(System.in);

    public CustomersManager() {
    }

    public void printCustomerOptions() {
        System.out.println("\nCustomer Options");
        System.out.println("1. View Customers");
        System.out.println("2. Add Customer");
        System.out.println("3. Update Customer");
        System.out.println("4. Delete Customer");
        System.out.println("5. Return to general options");
    }

    /*
    Serves as the controller for all of the customer options,
    catching exceptions and performing the user choices
     */
    public void executeCustomerChoice(int userChoice, User currentUser) {
        try (Connection conn = DatabaseConnection.makeConnection();
                Statement stmt = DatabaseConnection.getConn().createStatement()) {
            switch (userChoice) {
                case 1:
                    //view customers
                    viewCustomers(stmt);
                    break;
                case 2:
                    //add customer
                    if (addCustomer(stmt, currentUser)) {
                        System.out.println("Customer added successfully");
                    } else {
                        System.out.println("Customer add failed");
                    }
                    break;
                case 3:
                    //update customer
                    if (updateCustomer(stmt, currentUser)) {
                        System.out.println("Customer updated successfully");
                    } else {
                        System.out.println("Customer update failed");
                    }
                    break;
                case 4:
                    //delete customer
                    if (deleteCustomer(stmt)) {
                        System.out.println("Customer deleted successfully");
                    } else {
                        System.out.println("Customer delete failed");
                    }
                    break;
                default: //case 5 and default
                    System.out.println("Returning to general options");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            //handles invalid customer information entered by the user
            System.out.println("Invalid customer data entered.");
            System.out.println(e.getMessage());
        }
    }

    /*
    Retrieves all customers in the database, along with their corresponding
    basic address, city, and country information, displaying each with a general
    overview
     */
    public void viewCustomers(Statement stmt) throws SQLException {

        ResultSet rs = stmt.executeQuery("SELECT * FROM customer INNER JOIN address "
                + "ON customer.addressId = address.addressId INNER JOIN city ON "
                + "address.cityId = city.cityId INNER JOIN country ON "
                + "city.countryId = country.countryId ORDER BY customerId");

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

    }

    /*
    Uses the getCustomerInformation method to get a Customer object,
    then adds the Customer to the database after validating it 
    @return true if one record is added, false otherwise
     */
    public boolean addCustomer(Statement stmt, User currentUser)
            throws SQLException {

        Customer c = getCustomerInformation(stmt, currentUser);

        //add customer to the database
        stmt.executeUpdate(String.format("INSERT INTO customer(customerName, "
                + "addressId, active, createDate, createdBy, lastUpdateBy) "
                + "VALUES('%s', %d, 1, NOW(), '%s', '%s')", c.getCustomerName(),
                c.getAddressId(), currentUser.getUserName(), currentUser.getUserName()));

        return true;
    }

    /*
    Gets the customer Id from the user, if found it then retrieves the new
    customer information and updates the database, if the customer is
    not found it returns false early
    @return true if one record is updated, false otherwise
     */
    public boolean updateCustomer(Statement stmt, User currentUser)
            throws SQLException {

        //ask for customer id
        System.out.print("Enter the ID of the customer you would like to update: ");
        int customerid = Integer.parseInt(sc.nextLine());

        //check if customer exists
        if (!findCustomer(stmt, customerid)) {
            System.out.println("No customer exists with that ID.");
            return false;
        }

        Customer c = getCustomerInformation(stmt, customerid, currentUser);

        //update customer table
        int result = stmt.executeUpdate(String.format("UPDATE customer"
                + " SET customerName='%s', addressId=%d, lastUpdate=NOW(), lastUpdateBy='%s'"
                + " WHERE customerid=%d", c.getCustomerName(), c.getAddressId(),
                currentUser.getUserName(), c.getCustomerId()));

        return result == 1;
    }

    /*
    Gets the customer Id from the user, if found it then deletes the corresponding
    customer, if the customer is not found it returns false early
    @return true if one record is deleted, false otherwise
     */
    public boolean deleteCustomer(Statement stmt) throws SQLException {
        //get cutomerid
        System.out.print("Enter ID of the customer you would like to delete:  ");
        int customerid = Integer.parseInt(sc.nextLine());

        if (!findCustomer(stmt, customerid)) {
            System.out.println("No customer exists with that ID.");
            return false;
        }

        //at this point, the customerid must exist
        stmt.executeUpdate(String.format("DELETE FROM customer WHERE customerId=%d", customerid));

        return true;
    }

    /*
    Finds the customer associated with the passed in customerId
    @return true if the customer is found, false otherwise
     */
    public boolean findCustomer(Statement stmt, int customerid) throws SQLException {
        boolean customerFound = false;

        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM customer "
                + "WHERE customerId=%d", customerid));

        if (rs.next()) {
            customerFound = true;
        }

        rs.close();
        return customerFound;
    }

    /*
    Implements the test method in the Functional Interface Predicate
     */
    @Override
    public boolean test(Object o) {
        return o.toString().isEmpty();
    }

    /*
    Prints out a customer record with the passed in information in a formatted fashion
     */
    private void printCustomerRecord(String customerid, String customerName,
            String addressId, String address, String cityId, String city, String countryId, String country) {
        System.out.printf("%-12s| %-45s| %-12s| %-50s| %-12s| %-50s| %-12s| %-50s|\n",
                customerid, customerName, addressId, address, cityId, city, countryId, country);
    }

    /*
    Checks if the passed in string is empty using the Predicate interface's 
    implementation, throwing an IllegalArgumentException if the string is empty
     */
    private void validate(String str) throws IllegalArgumentException {
        //lambda expression using a method reference
        //makes the validation test more obvious
        Predicate<String> p = String::isEmpty;

        if (p.test(str)) {
            throw new IllegalArgumentException("Empty string entered for mandatory field");
        }

    }

    /*
    Calls another version of the same method with -1 as the customerId,
    as it is not known to the program
     */
    private Customer getCustomerInformation(Statement stmt,
            User currentUser) throws SQLException {

        //use -1 when the customerId is not known by the program
        return getCustomerInformation(stmt, -1, currentUser);
    }

    /*
    Gets the customer information from the user and creates a User object
    with the information, also creates corresponding a address, city, and/or
    country when necessary
     */
    private Customer getCustomerInformation(Statement stmt, int customerId,
            User currentUser) throws SQLException {

        //at this point the customerid must exist
        //acquire updated customer table fields for all tables
        System.out.print("Enter customer name: ");
        String name = sc.nextLine();
        validate(name);

        System.out.print("Enter customer addressId: ");
        String addressIdAsString = sc.nextLine();
        validate(addressIdAsString);
        int addressId = Integer.parseInt(addressIdAsString);

        //check if the customerAddressId already exists for an address record,
        //if so return true
        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM address WHERE "
                + "addressId=%d", addressId));

        if (!rs.next()) { //then an address was not found and needs created

            System.out.println("Address Id does not exist, a new address must be created");

            System.out.print("Enter address: ");
            String address = sc.nextLine();
            validate(address);

            System.out.print("Enter cityId: ");
            String cityIdAsString = sc.nextLine();
            validate(cityIdAsString);
            int cityId = Integer.parseInt(cityIdAsString);

            //postalCode is not mandatory, so it is not validated
            System.out.print("Enter postal code: ");
            String postalCode = sc.nextLine();

            System.out.print("Enter phone: ");
            String phone = sc.nextLine();
            validate(phone);

            //check if the cityId already exists for a city record,
            //if so return true
            rs = stmt.executeQuery(String.format("SELECT * FROM city WHERE "
                    + "cityId=%d", cityId));

            if (!rs.next()) { //then a city was not found and needs created

                System.out.println("City Id does not exist, a new city must be created");

                System.out.print("Enter city: ");
                String city = sc.nextLine();
                validate(city);

                System.out.print("Enter countryId: ");
                String countryIdAsString = sc.nextLine();
                validate(countryIdAsString);
                int countryId = Integer.parseInt(countryIdAsString);

                //check if the countryId already exists for a country record,
                //if so return true
                rs = stmt.executeQuery(String.format("SELECT * FROM country WHERE "
                        + "countryId=%d", countryId));

                if (!rs.next()) { //then a country was not found and needs created

                    System.out.println("City Id does not exist, a new country must be created");

                    System.out.print("Enter country: ");
                    String country = sc.nextLine();
                    validate(country);

                    addCountry(stmt, countryId, country, currentUser);

                }

                //the city can now be added since country has been added
                addCity(stmt, cityId, city, countryId, currentUser);

            }

            //the address can now be added since city has been added
            addAddress(stmt, addressId, address, cityId, postalCode, phone, currentUser);

        }

        rs.close();

        //return -1 as customerId because it is not currently known by the program
        //and is not needed
        return new Customer(customerId, name, addressId);
    }

    /*
    Adds an address record to the database
     */
    private void addAddress(Statement stmt, int addressId, String address, int cityId, String postalCode,
            String phone, User currentUser) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO address(addressId, address, cityId, postalCode, phone, "
                + "createDate, createdBy, lastUpdateBy) VALUES(%d, '%s', %d, '%s', '%s', NOW(), '%s', '%s')",
                addressId, address, cityId, postalCode, phone, currentUser.getUserName(), currentUser.getUserName()));

    }

    /*
    Adds a city record to the database
     */
    private void addCity(Statement stmt, int cityId, String city, int countryId,
            User currentUser) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO city(cityId, city, countryId, createDate, "
                + "createdBy, lastUpdateBy) VALUES(%d, '%s', %d, NOW(), '%s', '%s')", cityId, city, countryId,
                currentUser.getUserName(), currentUser.getUserName()));

    }

    /*
    Adds a country record to the database
     */
    private void addCountry(Statement stmt, int countryId, String country,
            User currentUser) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO country(countryId, country, createDate, "
                + "createdBy, lastUpdateBy) VALUES(%d, '%s', NOW(), '%s', '%s')", countryId, country,
                currentUser.getUserName(), currentUser.getUserName()));
    }

}
