package Customer;
import Claim.Claim;
import InsuranceCard.InsuranceCard;

import java.io.*;
import java.util.*;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Customer implements CustomerManager{

    private String id;
    private String fullName;
    private InsuranceCard insuranceCard;
    private List<Claim> claims;

    //Constructors
    public Customer() {
    }
    public Customer(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
        this.insuranceCard = null;
        this.claims = new ArrayList<>();
    }

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

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", insuranceCard=" + insuranceCard +
                ", claims=" + claims +
                '}';
    }

    private static final String FILENAME = "Customer.txt";

    @Override
    public void addCustomer() {
        Scanner scanner = new Scanner(System.in);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, true))) {
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

            // Create a new Customer object with the provided details
            Customer newCustomer = new Customer(id, fullName);
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


    @Override
    public void updateCustomer() {
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


    @Override
    public void deleteCustomer() {
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


    @Override
    public Customer getCustomerById() {
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



    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
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

    private String customerToString(Customer customer) {
        // Convert Customer object to a string representation for writing to file
        // Format: id;fullName;insuranceCard;claims
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(customer.getId()).append(";");
        stringBuilder.append(customer.getFullName()).append(";");
        if (customer.getInsuranceCard() != null) {
            stringBuilder.append(insuranceCardToString(customer.getInsuranceCard()));
        }
        if (customer.getClaims() != null) {
            stringBuilder.append(claimsToString(customer.getClaims()));
        }
        return stringBuilder.toString();
    }

    private Customer stringToCustomer(String line) {
        // Convert string from file to Customer object
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

    private String claimsToString(List<Claim> claims) {
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

    private List<Claim> stringToClaims(String line) {
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

    private String claimToString(Claim claim) {
        // Convert Claim object to a string representation
        // Format: id;claimDate;insuredPersonId;cardNumber;examDate;documents;claimAmount;status;receiverBankInfo
        return String.join(";", claim.getClaimID(), claim.getClaimDate().toString(), claim.getInsuredPerson().getId(), claim.getCardNumber().getCardNumber(), claim.getExamDate().toString(),String.valueOf(claim.getClaimAmount()), claim.getStatus(), claim.getReceiverBankInfo());
    }

    private Claim stringToClaim(String line) {
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

    private Date parseDate(String dateString) {
        // Parse string to Date
        // You can implement your date parsing logic here
        return new Date(); // Placeholder implementation, replace with actual parsing
    }

    private void saveCustomersToFile(List<Customer> customers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (Customer customer : customers) {
                writer.write(customerToString(customer));
                writer.newLine();
            }
        }
    }
}
