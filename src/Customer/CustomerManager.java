package Customer;

import java.util.List;

public interface CustomerManager {
    // Method to add a new customer

    void addCustomer();

    // Method to update an existing customer
    void updateCustomer();

    // Method to delete an existing customer
    void deleteCustomer();

    // Method to retrieve a customer by ID
    Customer getCustomerById();

    // Method to retrieve all customers
    List<Customer> getAllCustomers();
}
