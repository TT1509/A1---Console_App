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

    public Customer(String id, String fullName, InsuranceCard insuranceCard, List<Claim> claims) {
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

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public List<Claim> getClaims() {
        return claims;
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

    static boolean isValidCustomerId(String id) {
        // Check if the ID starts with "c-" and has 7 digits after that
        return id.matches("^c-\\d{7}$");
    }

    static void addCustomer() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Customer> customers = getAllCustomers(); // Load existing customers

            String id;
            // Prompt the user to input customer details until a valid ID is entered
            do {
                System.out.println("Enter customer ID (format: c-7 numbers>):");
                id = scanner.nextLine();
                if (!isValidCustomerId(id)) {
                    System.out.println("Invalid ID format. Please enter a valid ID.");
                    continue;
                }
                String finalId = id;
                if (customers.stream().anyMatch(c -> c.getId().equals(finalId))) {
                    System.out.println("Customer with ID " + id + " already exists. Please choose a different ID.");
                    return; // Exit the method without adding the customer
                }
            } while (id == null);

            System.out.println("Enter customer full name:");
            String fullName = scanner.nextLine();

            // Create a new InsuranceCard object for the customer using the addInsuranceCard function
            InsuranceCard insuranceCard = InsuranceCard.addInsuranceCard(new Customer("", "", null, null));

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
        if (customer.getClaims() != null) {
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
            List<Claim> claims = null;
            if (parts.length >= 4 && !parts[3].isEmpty()) {
                claims = stringToClaims(parts[3]);
            }
            return new Customer(id, fullName, insuranceCard, claims);
        }
        return null;
    }


    private static String insuranceCardToString(InsuranceCard insuranceCard) {
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

    private static String claimsToString(List<Claim> claims) {
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

    static String claimToString(Claim claim) {
        // Convert Claim object to a string representation
        // Format: id;claimDate;insuredPersonId;cardNumber;examDate;documents;claimAmount;status;receiverBankInfo

        // Convert the lists to string representations
        String documentsAsString = String.join(",", claim.getDocuments());
        String receiverBankInfoAsString = String.join(";", claim.getReceiverBankInfo());

        // Check if cardNumber is null
        String cardNumber = claim.getCardNumber() != null ? claim.getCardNumber().getCardNumber() : "";

        // Join all attributes together using the semicolon separator
        return String.join(";", claim.getClaimID(), claim.getClaimDate().toString(),
                claim.getInsuredPerson().getId(), cardNumber,
                claim.getExamDate().toString(), documentsAsString,
                String.valueOf(claim.getClaimAmount()), claim.getStatus(),
                receiverBankInfoAsString);
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
