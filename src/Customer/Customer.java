package Customer;
import Claim.Claim;
import InsuranceCard.InsuranceCard;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Customer implements CustomerManager{

    private String id;
    private String fullName;
    private InsuranceCard insuranceCard;
    private List<Claim> claims;
    public Customer(String id, String fullName, InsuranceCard insuranceCard, List<Claim> claims) {
        this.id = id;
        this.fullName = fullName;
        this.insuranceCard = insuranceCard;
        this.claims = claims;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    // Method to write customer data to file
    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customer.txt", true))) {
            writer.write("ID: " + id + ", Full Name: " + fullName + ", Insurance Card: " + insuranceCard.getCardNumber() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", insuranceCard=" + insuranceCard +
                ", claims=" + claims +
                '}';
    }

    @Override
    public void addCustomer(Customer customer) {

    }

    @Override
    public void updateCustomer(Customer customer) {

    }

    @Override
    public void deleteCustomer(String customerId) {

    }

    @Override
    public Customer getCustomerById(String customerId) {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return null;
    }
}
