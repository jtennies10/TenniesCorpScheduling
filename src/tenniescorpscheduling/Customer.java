/*
 * Defines the customer class which models the customer table in the database
 */
package tenniescorpscheduling;

public class Customer {
    //customerId is final because it is the primary key in customer
    //table in database and therefore cannot change
    final private int customerId;
    private String customerName;
    private int addressId;

    public Customer(int customerId, String customerName, int addressId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
    }

    public int getCustomerId() {
        return customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
        
}
