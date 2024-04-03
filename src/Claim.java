import java.io.*;
import java.util.*;

public class Claim implements ClaimProcessManager {

    private String claimID;
    private Date claimDate;
    private Customer insuredPerson;
    private InsuranceCard cardNumber;
    private Date examDate;
    private List<String> documents; // Changed type to List<String>
    private double claimAmount;
    private String status;
    private List<String> receiverBankInfo;

    // Constructor
    public Claim() {
        this.claimID = "";
        this.claimDate = new Date();
        this.insuredPerson = null;
        this.cardNumber = null;
        this.examDate = new Date();
        this.documents = new ArrayList<>();
        this.claimAmount = 0.0;
        this.status = "";
        this.receiverBankInfo = new ArrayList<>();
    }

    public Claim(String claimID, Date claimDate,
                 Customer insuredPerson, InsuranceCard cardNumber,
                 Date examDate, List<String> documents, double claimAmount,
                 String status, List<String>  receiverBankInfo) {
        this.claimID = claimID;
        this.claimDate = claimDate;
        this.insuredPerson = insuredPerson;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.documents = documents;
        this.claimAmount = claimAmount;
        this.status = status;
        this.receiverBankInfo = receiverBankInfo;
    }

    // Getters & Setters
    public String getClaimID() {
        return claimID;
    }

    public void setClaimID(String claimID) {
        this.claimID = claimID;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public Customer getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(Customer insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public InsuranceCard getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(InsuranceCard cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getReceiverBankInfo() {
        return receiverBankInfo;
    }

    public void setReceiverBankInfo(List<String> receiverBankInfo) {
        this.receiverBankInfo = receiverBankInfo;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "claimID='" + claimID + '\'' +
                ", claimDate=" + claimDate +
                ", insuredPerson=" + insuredPerson +
                ", cardNumber=" + cardNumber +
                ", examDate=" + examDate +
                ", documents=" + documents +
                ", claimAmount=" + claimAmount +
                ", status='" + status + '\'' +
                ", receiverBankInfo='" + receiverBankInfo + '\'' +
                '}';
    }

    static final String CLAIM_FILE = "resources/Claim.txt";

    // Function to find and return a customer by ID
    private static Customer findCustomerById(String customerId, List<Customer> customers) {
        Optional<Customer> foundCustomer = customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst();

        return foundCustomer.orElse(null);
    }

    static boolean isValidClaimId(String claimID) {
        // Check if the ID starts with "c-" and has 7 digits after that
        return claimID.matches("^f-\\d{10}$");
    }

    @Override
    public void addClaim() {
        Scanner scanner = new Scanner(System.in);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLAIM_FILE, true))) {
            List<Claim> claims = getAllClaims(); // Load existing claims

            String claimID;
            Claim claim = new Claim(); // Declare the claim variable here


            // Prompt the user to input claim ID until a valid one is entered
            do {
                // Prompt the user to input claim ID
                System.out.println("Enter claim ID (format: f-10 numbers):");
                claimID = scanner.nextLine();
                if (!isValidClaimId(claimID)) {
                    System.out.println("Invalid ID format. Please enter a valid ID.");
                    addClaim();
                } else {
                    String finalClaimID = claimID;
                    if (claims.stream().anyMatch(c -> getClaimID().equals(finalClaimID))) {
                        System.out.println("Claim with ID " + claimID + " already exists. Please choose a different ID.");
                        claimID = null; // Reset claimID to prompt the user again
                    }
                }
                // Check if the entered ID already exists

            } while (claimID == null);


            // Automatically set claim date to the current date
            Date claimDate = new Date(); // Current date





            // Read through the Customer.txt file and find the customer with the provided ID
            System.out.println("Enter customer id:");
            String customerId = scanner.nextLine();

            Customer insuredPerson = null;
            try (BufferedReader reader = new BufferedReader(new FileReader(Customer.CUSTOMER_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Customer customer = Customer.stringToCustomer(line);
                    if (customer != null && customer.getId().equals(customerId)) {
                        insuredPerson = customer;
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading customer file: " + e.getMessage());
            }
            // Check if the insured person was found
            if (insuredPerson == null) {
                System.out.println("Customer with ID " + customerId + " not found. Claim cannot be added.");
                return; // Exit the method without adding the claim
            }
            // Continue with the claim creation process
            claim.setInsuredPerson(insuredPerson);



            System.out.println("Enter card number:");
            String enteredCardNumber = scanner.nextLine();
            // Check if the entered card number matches any insurance card ID in the Customer.txt file
            InsuranceCard cardNumber = null;
            try (BufferedReader reader = new BufferedReader(new FileReader(Customer.CUSTOMER_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    customerId = parts[0];
                    String customerCardNumber = parts[2]; // Assuming the insurance card ID is the third element
                    if (customerCardNumber.equals(enteredCardNumber)) {
                        cardNumber = new InsuranceCard(); // Create a new InsuranceCard object
                        cardNumber.setCardNumber(enteredCardNumber); // Set the card number
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading customer file: " + e.getMessage());
            }
            // Check if the insurance card was found
            if (cardNumber == null) {
                System.out.println("Insurance card with number " + enteredCardNumber + " not found. Claim cannot be added.");
                return; // Exit the method without adding the claim
            }
            // Continue with the claim creation process
            claim.setCardNumber(cardNumber);





            // Prompt the user to input exam date
            System.out.println("Enter exam date (format: dd/MM/yyyy):");
            String examDateString = scanner.nextLine();
            Date examDate = Customer.parseDate(examDateString);



            // Prompt the user to input documents list
            System.out.println("Enter documents information:");
            // Scanner for name
            System.out.print("Enter document name: ");
            String documentName = scanner.nextLine();
            // Create a List to store the bank information
            List<String> document = new ArrayList<>();
            // Add bank, name, and number to the list
            document.add(claimID);
            document.add(String.valueOf(cardNumber));
            document.add(documentName);
            // Set the bank information to the claim
            claim.setReceiverBankInfo(document);


            // Prompt the user to input claim amount
            System.out.println("Enter claim amount:");
            double claimAmount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline character

            // Prompt the user to input claim status
            System.out.println("Select claim status: (1. New, 2. Processing, 3. Done)");
            int statusChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            String status;
            switch (statusChoice) {
                case 1:
                    status = "New";
                    break;
                case 2:
                    status = "Processing";
                    break;
                case 3:
                    status = "Done";
                    break;
                default:
                    System.out.println("Invalid status choice. Setting status to New.");
                    status = "New";
            }



            // Prompt the user to input receiver bank information
            System.out.println("Enter receiver bank information (Bank – Name – Number):");
            // Scanner for bank
            System.out.print("Enter bank: ");
            String bank = scanner.nextLine();
            // Scanner for name
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            // Scanner for number
            System.out.print("Enter number: ");
            String number = scanner.nextLine();
            // Create a List to store the bank information
            List<String> bankInfoList = new ArrayList<>();
            // Add bank, name, and number to the list
            bankInfoList.add(bank);
            bankInfoList.add(name);
            bankInfoList.add(number);
            // Set the bank information to the claim
            claim.setReceiverBankInfo(bankInfoList);




            // Create the Claim object using the input
            Claim newClaim = new Claim(claimID, claimDate, insuredPerson, cardNumber, examDate, documents, claimAmount, status, bankInfoList);

            // Write the new claim data to the file
            writer.write(Customer.claimToString(newClaim));
            writer.newLine();
            System.out.println("Claim added successfully.");
        } catch (IOException e) {
            System.err.println("Error adding claim: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }


    @Override
    public void updateClaim() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Claim> claims = getAllClaims();
            System.out.println("Enter claim ID:");
            String id = scanner.nextLine();

            // Find the existing claim to update
            Optional<Claim> existingClaim = claims.stream()
                    .filter(c -> c.getClaimID().equals(id))
                    .findFirst();

            if (existingClaim.isPresent()) {
                // Print claim details and prompt for updates
                Claim selectedClaim = existingClaim.get();
                System.out.println("Claim found:");
                System.out.println(selectedClaim);
                System.out.println("What do you want to update? Enter 'date', 'amount', 'status', or 'bank info':");
                String updateOption = scanner.nextLine();

                switch (updateOption.toLowerCase()) {
                    case "date":
                        // Prompt for a new claim date
                        System.out.println("Enter new claim date (format: dd/MM/yyyy):");
                        String dateString = scanner.nextLine();
                        Date newDate = Customer.parseDate(dateString);
                        // Update the claim date
                        selectedClaim.setClaimDate(newDate);
                        break;
                    case "amount":
                        // Prompt for a new claim amount
                        System.out.println("Enter new claim amount:");
                        double newAmount = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline character
                        // Update the claim amount
                        selectedClaim.setClaimAmount(newAmount);
                        break;
                    case "status":
                        // Prompt for a new claim status
                        System.out.println("Enter new claim status:");
                        String newStatus = scanner.nextLine();
                        // Update the claim status
                        selectedClaim.setStatus(newStatus);
                        break;
                    case "bank info":
                        // Prompt for receiver bank information
                        System.out.println("Enter new receiver bank information (Bank – Name – Number, separated by commas):");
                        String receiverBankInfo = scanner.nextLine();
                        // Split the input by commas to get individual pieces of bank information
                        String[] bankInfoArray = receiverBankInfo.split(",");
                        // Create a List to store the bank information
                        List<String> bankInfoList = Arrays.asList(bankInfoArray);
                        // Set the bank information to the claim
                        selectedClaim.setReceiverBankInfo(bankInfoList);
                        break;
                    default:
                        System.out.println("Invalid update option.");
                        return;
                }

                // Update the claim in the list and save to file
                saveClaimsToFile(claims);
                System.out.println("Claim updated successfully.");
            } else {
                System.out.println("Claim with ID " + id + " not found.");
            }
        } catch (IOException e) {
            System.err.println("Error updating claim: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }


    @Override
    public void deleteClaim() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Claim> claims = getAllClaims(); // Load existing claims

            // Prompt the user to input the ID of the claim to delete
            System.out.println("Enter the ID of the claim to delete:");
            String claimIdToDelete = scanner.nextLine();

            // Find the claim with the provided ID
            Optional<Claim> claimToDelete = claims.stream()
                    .filter(claim -> claim.getClaimID().equals(claimIdToDelete))
                    .findFirst();

            if (claimToDelete.isPresent()) {
                // Confirm deletion with the user
                System.out.println("Claim found:");
                System.out.println(claimToDelete.get());
                System.out.println("Are you sure you want to delete this claim? (yes/no)");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("yes")) {
                    // Remove the claim from the list
                    claims.remove(claimToDelete.get());
                    // Save the updated list of claims to the file
                    saveClaimsToFile(claims);
                    System.out.println("Claim deleted successfully.");
                } else {
                    System.out.println("Deletion canceled.");
                }
            } else {
                System.out.println("Claim with ID " + claimIdToDelete + " not found.");
            }
        } catch (IOException e) {
            System.err.println("Error deleting claim: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }


    @Override
    public Claim getClaimById() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Claim> claims = getAllClaims();
            System.out.println("Enter claim ID:");
            String id = scanner.nextLine();

            // Find the existing claim
            Optional<Claim> existingClaim = claims.stream()
                    .filter(c -> c.getClaimID().equals(id))
                    .findFirst();
            if (existingClaim.isPresent()) {
                // Return the found claim
                System.out.println(existingClaim);
                return existingClaim.get();
            } else {
                System.out.println("Claim with ID " + id + " not found.");
                return null;
            }
        } finally {
            scanner.close();
        }
    }


    @Override
    public List<Claim> getAllClaims() {
        List<Claim> claims = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CLAIM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Claim claim = Customer.stringToClaim(line);
                if (claim != null) {
                    claims.add(claim);
                    System.out.println(claims);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading customers: " + e.getMessage());
        }
        return claims;
    }

    // Function to check if the entered card number exists in the InsuranceCard.txt file


    static void saveClaimsToFile(List<Claim> claims) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLAIM_FILE))) {
            for (Claim claim : claims) {
                writer.write(Customer.claimToString(claim));
                writer.newLine();
            }
        }
    }
}
