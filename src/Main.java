import InsuranceCard.InsuranceCard;
import Claim.Claim;
import Customer.Customer;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Main {
    public static void main(String[] args) {
        // Create sample Customer
        Customer customer = new Customer("c1234567", "John Doe", null, null);

        // Create sample InsuranceCard for the customer
        InsuranceCard insuranceCard = new InsuranceCard();

        // Create sample InsuranceCard for the customer
        insuranceCard = insuranceCard.createInsuranceCard(customer);

        // Output the created Customer and InsuranceCard
        System.out.println("Sample Customer:");
        System.out.println("ID: " + customer.getId());
        System.out.println("Full Name: " + customer.getFullName());

        System.out.println("\nSample Insurance Card:");
        System.out.println("Card Number: " + insuranceCard.getCardNumber());
        System.out.println("Card Holder: " + insuranceCard.getCardHolder().getFullName());
        System.out.println("Policy Owner: " + insuranceCard.getPolicyOwner());
        System.out.println("Expiration Date: " + insuranceCard.getExpirationDate());
    }



}