
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Customer{

    private String id;
    private String fullName;
    private static InsuranceCard insuranceCard;


    private ArrayList<Claim> claims;

    //Constructors
    public Customer() {
    }

    public Customer(String id, String fullName, InsuranceCard insuranceCard, ArrayList<Claim> claims) {
        this.id = id;
        this.fullName = fullName;
        this.insuranceCard = insuranceCard;
        this.claims = new ArrayList<>();
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

    public static InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public ArrayList<Claim> getClaims() {
        return claims;
    }

    public void setClaims(ArrayList<Claim> claims) {
        this.claims = claims;
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

    static final String CUSTOMER_FILE = "resources/Customer.txt";


    private static final Set<String> generatedIds = new HashSet<>();

    public static String generateCustomerId() {
        Random random = new Random();
        String customerId;
        do {
            StringBuilder sb = new StringBuilder("c-");
            for (int i = 0; i < 7; i++) {
                sb.append(random.nextInt(10)); // Generate a random digit from 0 to 9
            }
            customerId = sb.toString();
        } while (generatedIds.contains(customerId)); // Check uniqueness
        generatedIds.add(customerId); // Add generated ID to set
        return customerId;
    }
    static void addCustomer() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Customer> customers = getAllCustomers(); // Load existing customers


            String id = generateCustomerId();
            System.out.println("Generated Customer Id :" + id);

            System.out.println("Enter customer full name:");
            String fullName = scanner.nextLine();

            // Create a new InsuranceCard object for the customer using the addInsuranceCard function
            InsuranceCard insuranceCard = InsuranceCard.addInsuranceCard();

            // Create a new Customer object with the provided details
            Customer newCustomer = new Customer(id, fullName, insuranceCard, new ArrayList<>());

            // Add the new customer to the list of customers
            customers.add(newCustomer);

            // Write the new customer data to the file
            saveCustomersToFile(customers);
            System.out.println("Customer added successfully.");
        } catch (IOException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }



    static void deleteCustomer() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Customer> customers = getAllCustomers();
            System.out.println("Enter customer ID:");
            String id = scanner.nextLine();

            // Find the existing customer to delete
            Optional<Customer> existingCustomer = customers.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst();

            if (existingCustomer.isPresent()) {
                // Print customer details and confirm deletion
                Customer customerToDelete = existingCustomer.get();
                System.out.println("Customer found:");
                System.out.println(customerToDelete);
                System.out.println("Are you sure you want to delete this customer? (yes/no)");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("yes")) {
                    // Remove the customer from the list
                    customers.remove(customerToDelete);
                    saveCustomersToFile(customers);
                    System.out.println("Customer deleted successfully.");
                } else {
                    System.out.println("Deletion canceled.");
                }
            } else {
                System.out.println("Customer with ID " + id + " not found.");
            }
        } catch (IOException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }


    static void updateCustomer() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Customer> customers = getAllCustomers();
            System.out.println("Enter customer ID:");
            String id = scanner.nextLine();

            // Find the existing customer to update
            Optional<Customer> existingCustomer = customers.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst();

            if (existingCustomer.isPresent()) {
                // Print customer details and prompt for updates
                Customer selectedCustomer = existingCustomer.get();
                System.out.println("Customer found:");
                System.out.println(selectedCustomer);
                System.out.println("What do you want to update? Enter 'name' for full name or 'insurance' for insurance card (name or expiration date):");
                String updateOption = scanner.nextLine();

                switch (updateOption.toLowerCase()) {
                    case "name":
                        // Prompt for a new name
                        System.out.println("Enter new full name:");
                        String newName = scanner.nextLine();

                        // Update the customer name
                        selectedCustomer.setFullName(newName);
                        break;
                    case "insurance":
                        // Copy existing insurance card details to a new one
                        InsuranceCard existingInsuranceCard = selectedCustomer.getInsuranceCard();
                        InsuranceCard updatedInsuranceCard = new InsuranceCard(existingInsuranceCard.getCardNumber(),
                                existingInsuranceCard.getCardHolder(), existingInsuranceCard.getPolicyOwner(),
                                existingInsuranceCard.getExpirationDate());
                        // Prompt for any updates to insurance card
                        System.out.println("Do you want to update the policy owner's name? (yes/no)");
                        String updatePolicyOwner = scanner.nextLine();
                        if (updatePolicyOwner.equalsIgnoreCase("yes")) {
                            System.out.println("Enter new policy owner name:");
                            String newPolicyOwner = scanner.nextLine();
                            updatedInsuranceCard.setPolicyOwner(newPolicyOwner);
                        }
                        System.out.println("Do you want to update the expiration date? (yes/no)");
                        String updateExpirationDate = scanner.nextLine();
                        if (updateExpirationDate.equalsIgnoreCase("yes")) {
                            System.out.println("Enter new expiration date (format: dd/MM/yyyy):");
                            String expirationDateString = scanner.nextLine();
                            Date expirationDate = parseDate(expirationDateString);
                            updatedInsuranceCard.setExpirationDate(expirationDate);
                        }
                        // Set the updated insurance card to the customer
                        selectedCustomer.setInsuranceCard(updatedInsuranceCard);
                        break;
                    default:
                        System.out.println("Invalid update option.");
                        return;
                }

                // Update the customer in the list and save to file
                saveCustomersToFile(customers);
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("Customer with ID " + id + " not found.");
            }
        } catch (IOException e) {
            System.err.println("Error updating customer: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }




    static Customer getCustomerById() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Customer> customers = getAllCustomers();
            System.out.println("Enter customer ID:");
            String id = scanner.nextLine();

            // Find the existing customer
            Optional<Customer> existingCustomer = customers.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst();
            if (existingCustomer.isPresent()) {
                // Return the found customer
                System.out.println(existingCustomer);
                return existingCustomer.get();
            } else {
                System.out.println("Customer with ID " + id + " not found.");
                return null;
            }
        } finally {
            scanner.close();
        }
    }



    static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = stringToCustomer(line);
                if (customer != null) {
                    customers.add(customer);
                    System.out.println(customers);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading customers: " + e.getMessage());
        }
        return customers;
    }

    static String customerToString(Customer customer) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(customer.getId()).append(";");
        stringBuilder.append(customer.getFullName()).append(";");
        if (customer.getInsuranceCard() != null) {
            // Append insurance card details, including ID
            stringBuilder.append(insuranceCardToString(customer.getInsuranceCard())).append(";");
        } else {
            // Append empty field if no insurance card
            stringBuilder.append(";");
        }
        if (customer.getClaims() != null && !customer.getClaims().isEmpty()) {
            stringBuilder.append(claimsToString(customer.getClaims()));
        }
        return stringBuilder.toString();
    }


    static Customer stringToCustomer(String line) {
        String[] parts = line.split(";");
        if (parts.length >= 2) {
            String id = parts[0];
            String fullName = parts[1];
            InsuranceCard insuranceCard = InsuranceCard.stringToInsuranceCard(parts[2]);
            ArrayList<Claim> claims = null;
            if (parts.length >= 4 && !parts[3].isEmpty()) {
                claims = (ArrayList<Claim>) stringToClaims(parts[3]);
            }
            return new Customer(id, fullName, insuranceCard, claims);
        }
        return null;
    }


    static String insuranceCardToString(InsuranceCard insuranceCard) {
        // Convert InsuranceCard object to a string representation
        // Format: cardNumber;cardHolderName;policyOwnerName;expirationDate
        if (insuranceCard != null) {
            Customer cardHolder = insuranceCard.getCardHolder();
            String cardHolderName = (cardHolder != null) ? cardHolder.getFullName() : "";
            return String.join(";", insuranceCard.getCardNumber(), cardHolderName, insuranceCard.getPolicyOwner(), insuranceCard.getExpirationDate().toString());
        }
        return null;
    }

    private static InsuranceCard stringToInsuranceCard(String line) {
        // Convert string from file to InsuranceCard object
        String[] parts = line.split(";");
        if (parts.length >= 3) {
            String cardNumber = parts[0];
            String cardHolderName = parts[1]; // Assuming parts[1] contains the card holder's name
            String policyOwnerName = parts[2];
            Date expirationDate = parseDate(parts[3]); // Assuming parts[3] contains the expiration date
            // Create a dummy Customer with the card holder's name
            Customer cardHolder = new Customer("", cardHolderName, insuranceCard, null);
            return new InsuranceCard(cardNumber, cardHolder, policyOwnerName, expirationDate);
        }
        return null;
    }

    static String claimsToString(List<Claim> claims) {
        // Convert List<Claim> to a string representation
        // Format: claim1,claim2,claim3...
        if (claims != null && !claims.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Claim claim : claims) {
                sb.append(claimToString(claim)).append(",");
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
        return "";
    }

    private static List<Claim> stringToClaims(String line) {
        // Convert string from file to List<Claim>
        List<Claim> claims = new ArrayList<>();
        String[] claimStrings = line.split(",");
        for (String claimString : claimStrings) {
            Claim claim = stringToClaim(claimString);
            if (claim != null) {
                claims.add(claim);
            }
        }
        return claims;
    }

    public static String claimToString(Claim claim) {
        if (claim == null) {
            return ""; // or throw an exception, depending on your requirements
        }

        // Check for null values in each attribute and handle them appropriately
        String claimID = claim.getClaimID() != null ? claim.getClaimID() : "";
        String claimDate = claim.getClaimDate() != null ? claim.getClaimDate().toString() : "";
        String insuredPersonId = claim.getInsuredPerson() != null ? claim.getInsuredPerson().getId() : "";
        String cardNumber = Customer.getInsuranceCard() != null ? Customer.getInsuranceCard().getCardNumber() : "";
        String examDate = claim.getExamDate() != null ? claim.getExamDate().toString() : "";
        String documentsAsString = claim.getDocuments() != null ? String.join(",", claim.getDocuments()) : "";
        double claimAmount = claim.getClaimAmount(); // Assuming this cannot be null
        String status = claim.getStatus() != null ? claim.getStatus() : "";
        String receiverBankInfoAsString = claim.getReceiverBankInfo() != null ? String.join(";", claim.getReceiverBankInfo()) : "";

        // Join all attributes together using the semicolon separator
        return String.join(";", claimID, claimDate, insuredPersonId, cardNumber, examDate,
                documentsAsString, String.valueOf(claimAmount), status, receiverBankInfoAsString);
    }




    static Claim stringToClaim(String line) {
        // Convert string from file to Claim object
        String[] parts = line.split(";");
        if (parts.length >= 9) {
            String id = parts[0];
            Date claimDate = parseDate(parts[1]);
            String insuredPersonId = parts[2]; // Get the ID directly from the file
            InsuranceCard cardNumber = stringToInsuranceCard(parts[3]);
            Date examDate = parseDate(parts[4]);
            List<String> documents = Collections.singletonList(parts[5]);
            double claimAmount = Double.parseDouble(parts[6]);
            String status = parts[7];
            List<String> receiverBankInfo = Collections.singletonList(parts[8]);
            // Create a dummy Customer with the insuredPersonId
            Customer insuredPerson = new Customer(insuredPersonId, "", insuranceCard, null);
            return new Claim(id, claimDate, insuredPerson, cardNumber, examDate, documents, claimAmount, status, receiverBankInfo);
        }
        return null;
    }



    static Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(dateString);
        } catch (java.text.ParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy format.");
            return null;
        }
    }

    static void saveCustomersToFile(List<Customer> customers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (Customer customer : customers) {
                writer.write(customerToString(customer));
                writer.newLine();
            }
        }
    }
}
