import java.io.*;
import java.util.*;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Customer{

    private String id;
    private String fullName;
    private InsuranceCard insuranceCard;
    private List<Claim> claims;

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

    static final String CUSTOMER_FILE = "Customer.txt";

    static void addCustomer() {
        Scanner scanner = new Scanner(System.in);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE, true))) {
            List<Customer> customers = getAllCustomers(); // Load existing customers

            // Prompt the user to input customer details
            System.out.println("Enter customer ID:");
            String id = scanner.nextLine();

            // Check if the entered ID already exists
            if (customers.stream().anyMatch(c -> c.getId().equals(id))) {
                System.out.println("Customer with ID " + id + " already exists. Please choose a different ID.");
                return; // Exit the method without adding the customer
            }

            System.out.println("Enter customer full name:");
            String fullName = scanner.nextLine();

            // Create a new InsuranceCard object for the customer using the addInsuranceCard function
            InsuranceCard insuranceCard = InsuranceCard.addInsuranceCard(new Customer("", "", null, null));

            // Write the ID of the insurance card to the file
            Customer newCustomer = new Customer(id, fullName, insuranceCard, null);

            // Write the new customer data to the file
            writer.write(customerToString(newCustomer));
            writer.newLine();
            System.out.println("Customer added successfully.");
        } catch (IOException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }




    static void updateCustomer() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Customer> customers = getAllCustomers();
            // Prompt the user to input the customer ID
            System.out.println("Enter customer ID:");
            String id = scanner.nextLine();

            // Find the index of the existing customer in the list
            int index = -1;
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getId().equals(id)) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                // Prompt the user to input the updated full name
                System.out.println("Enter new full name for customer with ID " + id + ":");
                String fullName = scanner.nextLine();

                // Update the full name of the existing customer in the list
                customers.get(index).setFullName(fullName);
                // Save the updated list of customers back to the file
                saveCustomersToFile(customers);
                System.out.println("Customer's full name updated successfully.");
            } else {
                System.out.println("Customer with ID " + id + " not found.");
            }
        } catch (IOException e) {
            System.err.println("Error updating customer: " + e.getMessage());
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
            InsuranceCard insuranceCard = null;
            if (parts.length >= 3 && !parts[2].isEmpty()) {
                insuranceCard = stringToInsuranceCard(parts[2]);
            }
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
        return "";
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
            Customer cardHolder = new Customer("", cardHolderName, null, null);
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

    private static String claimToString(Claim claim) {
        // Convert Claim object to a string representation
        // Format: id;claimDate;insuredPersonId;cardNumber;examDate;documents;claimAmount;status;receiverBankInfo
        return String.join(";", claim.getClaimID(), claim.getClaimDate().toString(), claim.getInsuredPerson().getId(), claim.getCardNumber().getCardNumber(), claim.getExamDate().toString(),String.valueOf(claim.getClaimAmount()), claim.getStatus(), claim.getReceiverBankInfo());
    }

    private static Claim stringToClaim(String line) {
        // Convert string from file to Claim object
        String[] parts = line.split(";");
        if (parts.length >= 9) {
            String id = parts[0];
            Date claimDate = parseDate(parts[1]);
            String insuredPersonId = parts[2]; // Get the ID directly from the file
            InsuranceCard cardNumber = stringToInsuranceCard(parts[3]);
            Date examDate = parseDate(parts[4]);
            List<Claim> documents = stringToClaims(parts[5]);
            double claimAmount = Double.parseDouble(parts[6]);
            String status = parts[7];
            String receiverBankInfo = parts[8];
            // Create a dummy Customer with the insuredPersonId
            Customer insuredPerson = new Customer(insuredPersonId, "", null, null);
            return new Claim(id, claimDate, insuredPerson, cardNumber, examDate, documents, claimAmount, status, receiverBankInfo);
        }
        return null;
    }



    private static Date parseDate(String dateString) {
        // Parse string to Date
        // You can implement your date parsing logic here
        return new Date(); // Placeholder implementation, replace with actual parsing
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
