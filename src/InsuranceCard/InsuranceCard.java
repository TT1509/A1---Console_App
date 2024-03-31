package InsuranceCard;


import Customer.Customer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class InsuranceCard implements InsuranceCardManager{

    //Attributes
    private String cardNumber;
    private String cardHolder;
    private String policyOwner;
    private Date expirationDate;


    // Constructors
    public InsuranceCard() {

    }

    public InsuranceCard(String cardNumber, String cardHolder, String policyOwner, Date expirationDate) {
//        // Check if the provided cardHolder exists
//        if (cardHolder == null) {
//            throw new IllegalArgumentException("Card holder cannot be null.");
//        }
//        if (!isCustomerNameExist(cardHolder.getFullName())) {
//            throw new IllegalArgumentException("Card holder's name must be an existing customer's name.");
//        }
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.policyOwner = policyOwner;
        this.expirationDate = expirationDate;
    }

//    // Method to check if a customer name exists in the file
//    private boolean isCustomerNameExist(String cardHolderName) {
//        try (BufferedReader reader = new BufferedReader(new FileReader("Customer.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Assuming each line in the file represents a customer's full name
//                if (line.trim().equals(cardHolderName)) {
//                    return true;
//                }
//            }
//            return false; // Customer name not found in the file
//        } catch (IOException e) {
//            System.err.println("Error reading customer file: " + e.getMessage());
//            return false; // Unable to read the file
//        }
//    }


    // Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
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

    private static final String FILENAME = "insuranceCard.txt";

    @Override
    public void addInsuranceCard() {
        Scanner scanner = new Scanner(System.in);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, true))) {
            // Prompt the user to input insurance card details
            System.out.println("Enter card number:");
            String cardNumber = scanner.nextLine();

            // Prompt the user to card holder name
            System.out.println("Enter card holder name:");
            String cardHolder = scanner.nextLine();

            // Prompt the admin to input policy owner's name
            System.out.println("Enter policy owner's name:");
            String policyOwner = scanner.nextLine();

            // Prompt the admin to input expiration date
            System.out.println("Enter expiration date (format: dd/MM/yyyy):");
            String expirationDateString = scanner.nextLine();
            Date expirationDate = parseDate(expirationDateString);

            // Create the InsuranceCard object using the input
            InsuranceCard insuranceCard = new InsuranceCard(cardNumber, cardHolder, policyOwner, expirationDate);

            // Write the insurance card data to the file
            writer.write(insuranceCardToString(insuranceCard));
            writer.newLine();
            System.out.println("Insurance card added successfully.");
        } catch (IOException e) {
            System.err.println("Error adding insurance card: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    @Override
    public void updateInsuranceCard() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<InsuranceCard> insuranceCards = getAllInsuranceCards();

            if (insuranceCards == null) {
                System.out.println("Error: Unable to retrieve insurance card data.");
                return;
            }

            // Prompt the user to input the card number
            System.out.println("Enter card number:");
            String cardNumber = scanner.nextLine();

            // Find the index of the existing insurance card in the list
            int index = -1;
            for (int i = 0; i < insuranceCards.size(); i++) {
                if (insuranceCards.get(i).getCardNumber().equals(cardNumber)) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                // Prompt the admin to input policy owner's name
                System.out.println("Enter new policy owner's name:");
                String policyOwner = scanner.nextLine();

                // Prompt the admin to input expiration date
                System.out.println("Enter new expiration date (format: dd/MM/yyyy):");
                String expirationDateString = scanner.nextLine();
                Date expirationDate = parseDate(expirationDateString);

                // Update the insurance card details in the list
                insuranceCards.get(index).setPolicyOwner(policyOwner);
                insuranceCards.get(index).setExpirationDate(expirationDate);

                // Save the updated list of insurance cards back to the file
                saveInsuranceCardsToFile(insuranceCards);
                System.out.println("Insurance card updated successfully.");
            } else {
                System.out.println("Insurance card with the specified number not found.");
            }
        } catch (IOException e) {
            System.err.println("Error updating insurance card: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }


    @Override
    public void deleteInsuranceCard() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<InsuranceCard> insuranceCards = getAllInsuranceCards();
            System.out.println("Enter card number:");
            String cardNumber = scanner.nextLine();

            // Find the existing insurance card to delete
            Optional<InsuranceCard> existingInsuranceCard = insuranceCards.stream()
                    .filter(ic -> ic.getCardNumber().equals(cardNumber))
                    .findFirst();
            if (existingInsuranceCard.isPresent()) {
                // Remove the insurance card from the list
                insuranceCards.remove(existingInsuranceCard.get());
                saveInsuranceCardsToFile(insuranceCards);
                System.out.println("Insurance card deleted successfully.");
            } else {
                System.out.println("Insurance card with the specified number not found.");
            }
        } catch (IOException e) {
            System.err.println("Error deleting insurance card: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    @Override
    public InsuranceCard getInsuranceCardById() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<InsuranceCard> insuranceCards = getAllInsuranceCards();
            System.out.println("Enter card number:");
            String cardNumber = scanner.nextLine();

            // Find the existing insurance card
            Optional<InsuranceCard> existingInsuranceCard = insuranceCards.stream()
                    .filter(ic -> ic.getCardNumber().equals(cardNumber))
                    .findFirst();
            if (existingInsuranceCard.isPresent()) {
                // Return the found insurance card
                System.out.println(existingInsuranceCard);
                return existingInsuranceCard.get();

            } else {
                System.out.println("Insurance card with the specified number not found.");
                return null;
            }
        } finally {
            scanner.close();
        }
    }

    @Override
    public List<InsuranceCard> getAllInsuranceCards() {
        List<InsuranceCard> insuranceCards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                InsuranceCard insuranceCard = stringToInsuranceCard(line);
                if (insuranceCard != null) {
                    insuranceCards.add(insuranceCard);
                    System.out.println(insuranceCards);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading insurance cards: " + e.getMessage());
        }
        return insuranceCards;
    }


    private String insuranceCardToString(InsuranceCard insuranceCard) {
        // Convert InsuranceCard object to a string representation
        // Format: cardNumber;cardHolderName;policyOwnerName;expirationDate
        if (insuranceCard != null) {
            return String.join(";", insuranceCard.getCardNumber(), insuranceCard.getCardHolder(), insuranceCard.getPolicyOwner(), insuranceCard.getExpirationDate().toString());
        }
        return "";
    }

    private InsuranceCard stringToInsuranceCard(String line) {
        // Convert string from file to InsuranceCard object
        String[] parts = line.split(";");
        if (parts.length >= 4) {
            String cardNumber = parts[0];
            String cardHolder = parts[1];
            String policyOwnerName = parts[2];
            Date expirationDate = parseDate(parts[3]);
            return new InsuranceCard(cardNumber, cardHolder, policyOwnerName, expirationDate);
        }
        return null;
    }

    // Method to match insurance card ID with customer ID and card holder name with customer name
    public void matchAndWriteInsuranceCardIdToCustomer(String insuranceCardId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("insuranceCard.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("Customer.txt", true))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String cardNumber = parts[0];
                    String customerID = parts[1];
                    String cardHolderName = parts[2];
                    String policyOwner = parts[3];

                    // Match insurance card ID with customer ID and card holder name with customer name
                    if (cardNumber.equals(insuranceCardId) && getCardHolder().equals(cardHolderName)) {
                        // Write insurance card ID to Customer.txt
                        writer.write(customerID);
                        writer.newLine();
                        System.out.println("Insurance card ID " + insuranceCardId + " matched with customer ID " + customerID + ". Written to Customer.txt");
                        return; // Exit loop once a match is found
                    }
                }
            }
            System.out.println("No matching insurance card found for ID " + insuranceCardId);
        } catch (IOException e) {
            System.err.println("Error reading or writing files: " + e.getMessage());
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

    // Method to write insurance card data to a .txt file
    // Method to write insurance card data to the Customer.txt file and update existing customer
    private void saveInsuranceCardsToFile(List<InsuranceCard> insuranceCards) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (InsuranceCard insuranceCard : insuranceCards) {
                writer.write(insuranceCardToString(insuranceCard));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing insurance card data to file: " + e.getMessage());
        }
    }
}
