package InsuranceCard;


import Customer.Customer;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class InsuranceCard implements InsuranceCardManager {

    //Attributes
    private String cardNumber;
    private Customer cardHolder;
    private String policyOwner;
    private Date expirationDate;


    // Constructors
    public InsuranceCard() {

    }

    public InsuranceCard(String cardNumber, Customer cardHolder, String policyOwner, Date expirationDate) {
        // Check if the provided cardHolder exists
        if (cardHolder == null) {
            throw new IllegalArgumentException("Card holder cannot be null.");
        }
        if (!isCustomerNameExist(cardHolder.getFullName())) {
            throw new IllegalArgumentException("Card holder's name must be an existing customer's name.");
        }
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.policyOwner = policyOwner;
        this.expirationDate = expirationDate;
    }

    // Method to check if a customer name exists in the file
    private boolean isCustomerNameExist(String cardHolderName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line in the file represents a customer's full name
                if (line.trim().equals(cardHolderName)) {
                    return true;
                }
            }
            return false; // Customer name not found in the file
        } catch (IOException e) {
            System.err.println("Error reading customer file: " + e.getMessage());
            return false; // Unable to read the file
        }
    }


    // Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Customer getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(Customer cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "InsuranceCard{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardHolder=" + cardHolder +
                ", policyOwner=" + policyOwner +
                ", expirationDate=" + expirationDate +
                '}';
    }

    @Override
    public InsuranceCard createInsuranceCard(Customer cardHolder) {
        Scanner scanner = new Scanner(System.in);

        // Use the provided card holder's name
        String cardHolderName = cardHolder.getFullName();

        // Check if the provided card holder exists
        if (!isCustomerNameExist(cardHolderName)) {
            System.out.println("Card holder does not exist. Cannot create insurance card.");
            return null;
        }

        // Prompt the admin to input policy owner's name
        System.out.println("Enter policy owner's name:");
        String policyOwnerName = scanner.nextLine();

        // Prompt the admin to input expiration date
        System.out.println("Enter expiration date (format: dd/MM/yyyy):");
        String expirationDateString = scanner.nextLine();
        Date expirationDate = parseDate(expirationDateString);

        // Prompt the admin to input card number
        System.out.println("Enter card number:");
        String cardNumber = scanner.nextLine();

        // Create the InsuranceCard object using the input
        InsuranceCard insuranceCard = new InsuranceCard(cardNumber, cardHolder, policyOwnerName, expirationDate);

        // Close the Scanner object
        scanner.close();

        // Write the insurance card data to a .txt file
        writeInsuranceCardToFile(insuranceCard);

        return insuranceCard;
    }

    // Method to write insurance card data to a .txt file
    private void writeInsuranceCardToFile(InsuranceCard insuranceCard) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("insurance_cards.txt", true))) {
            writer.write(insuranceCard.toString());
            writer.newLine();
            System.out.println("Insurance card data written to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing insurance card data to file: " + e.getMessage());
        }
    }

    // Helper method to parse date from string
    private Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(dateString);
        } catch (java.text.ParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy format.");
            return null;
        }
    }
}
