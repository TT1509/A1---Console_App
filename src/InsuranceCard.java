import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class InsuranceCard {

    //Attributes
    private String cardNumber;
    private Customer cardHolder;
    private String policyOwner;
    private Date expirationDate;


    // Constructors
    public InsuranceCard() {

    }

    public InsuranceCard(String cardNumber, Customer cardHolder, String policyOwner, Date expirationDate) {
//        // Check if the provided cardHolder exists
//        if (cardHolder == null) {
//            throw new IllegalArgumentException("Card holder cannot be null.");
//        }
//        if (!isCustomerNameExist(cardHolder.getFullName())) {
//            throw new IllegalArgumentException("Card holder's name must be an existing customer's name.");
//        }
        this.cardNumber = cardNumber;;
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

    public Customer getCardHolder() {
        return cardHolder;
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

    @Override
    public String toString() {
        return "InsuranceCard{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardHolder=" + cardHolder +
                ", policyOwner=" + policyOwner +
                ", expirationDate=" + expirationDate +
                '}';
    }

    static final String INSURANCE_FILE = "resources/insuranceCard.txt";

    public static InsuranceCard addInsuranceCard() {
        Scanner scanner = new Scanner(System.in);
        InsuranceCard insuranceCard = null; // Initialize insurance card
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INSURANCE_FILE, true))) {

            // Generate insurance card number with 10 digits
            String cardNumber;
            boolean validCardNumber;
            do {
                cardNumber = generateCardNumber();
                validCardNumber = true; // Assuming generated card number is valid
                // You can add additional validation logic here if required
            } while (!validCardNumber);

            // Prompt the admin to input policy holder's name
            System.out.println("Enter policy owner name:");
            String policyOwner = scanner.nextLine();

            // Prompt the admin to input expiration date
            System.out.println("Enter expiration date (format: dd/MM/yyyy):");
            String expirationDateString = scanner.nextLine();
            Date expirationDate = Customer.parseDate(expirationDateString);

            // Create the InsuranceCard object using the input
            insuranceCard = new InsuranceCard(cardNumber, null, policyOwner, expirationDate);

            // Write the insurance card data to the file
            writer.write(insuranceCardToString(insuranceCard));
            writer.newLine();
            System.out.println("Insurance card added successfully.");

        } catch (IOException e) {
            System.err.println("Error adding insurance card: " + e.getMessage());
        } finally {
            scanner.close();
        }
        return insuranceCard;
    }

    private static String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return sb.toString();
    }





    public static void deleteInsuranceCard() {
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
        } finally {
            scanner.close();
        }
    }


    public static InsuranceCard getInsuranceCardById() {
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

    // Get all insurance cards in the text file
    public static List<InsuranceCard> getAllInsuranceCards() {
        List<InsuranceCard> insuranceCards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(INSURANCE_FILE))) {
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


    private static String insuranceCardToString(InsuranceCard insuranceCard) {
        // Convert InsuranceCard object to a string representation
        // Format: cardNumber;cardHolderName;policyOwnerName;expirationDate
        if (insuranceCard != null) {
            Customer cardHolder = insuranceCard.getCardHolder();
            String cardHolderName = (cardHolder != null) ? cardHolder.getFullName() : "";
            return String.join(";", insuranceCard.getCardNumber(), cardHolderName, insuranceCard.getPolicyOwner(), insuranceCard.getExpirationDate().toString());
        }
        return "";
    }

    static InsuranceCard stringToInsuranceCard(String line) {
        // Convert string from file to InsuranceCard object
        String[] parts = line.split(";");
        if (parts.length >= 4) {
            String cardNumber = parts[0];
            String customerId = parts[1]; // Assuming parts[1] contains the customer ID
            String policyOwnerName = parts[2];
            Date expirationDate = Customer.parseDate(parts[3]);
            // Retrieve the customer by ID
            Customer customer = new Customer(); // Instantiate an object that implements CustomerManager
            Customer cardHolder = Customer.getCustomerById(); // Call the getCustomerById method

            // Check if the customer exists
            if (cardHolder != null) {
                return new InsuranceCard(cardNumber, cardHolder, policyOwnerName, expirationDate);
            } else {
                System.err.println("Customer with ID " + customerId + " not found.");
            }
        }
        return null;
    }


    // Method to write insurance card data to insuranceCard.txt file

    static void saveInsuranceCardsToFile(List<InsuranceCard> insuranceCards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INSURANCE_FILE, true))) {
            for (InsuranceCard insuranceCard : insuranceCards) {
                writer.write(insuranceCardToString(insuranceCard));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing insurance card data to file: " + e.getMessage());
        }
    }
}