package Customer;

import java.util.List;

public interface CustomerManager {
    // Method to add a new customer
    void addCustomer(Customer customer);

    // Method to update an existing customer
    void updateCustomer(Customer customer);

    // Method to delete an existing customer
    void deleteCustomer(String customerId);

    // Method to retrieve a customer by ID
    Customer getCustomerById(String customerId);

    // Method to retrieve all customers
    List<Customer> getAllCustomers();
}
