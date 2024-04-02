import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Claim implements ClaimProcessManager {

    private String claimID;
    private Date claimDate;
    private Customer insuredPerson;
    private InsuranceCard cardNumber;
    private Date examDate;
    private List<Claim> documents;
    private double claimAmount;
    private String status;
    private List<String> receiverBankInfo;

    // Constructor
    public Claim(String claimID, Date claimDate, Customer insuredPerson, String cardNumber, Date examDate, double claimAmount, String status) {
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
                 Date examDate, List<Claim> documents, double claimAmount,
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

    @Override
    public void addClaim(Claim claim) {
        Scanner scanner = new Scanner(System.in);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLAIM_FILE, true))) {
            List<Claim> claims = getAllClaims(); // Load existing claims
            List<Customer> customers = Customer.getAllCustomers(); // Load existing customers

            String claimID;
            // Prompt the user to input claim ID until a valid one is entered
            do {
                // Prompt the user to input claim ID
                System.out.println("Enter claim ID (f-xxxxxxxxxx):");
                claimID = scanner.nextLine();

                // Check if the entered ID already exists
                String finalClaimID = claimID;
                if (claims.stream().anyMatch(c -> c.getClaimID().equals(finalClaimID))) {
                    System.out.println("Claim with ID " + claimID + " already exists. Please choose a different ID.");
                    claimID = null; // Reset claimID to prompt the user again
                }
            } while (claimID == null);

            // Check if the entered ID already exists
            String finalClaimID1 = claimID;
            if (claims.stream().anyMatch(c -> c.getClaimID().equals(finalClaimID1))) {
                System.out.println("Claim with ID " + claimID + " already exists. Please choose a different ID.");
                return; // Exit the method without adding the claim
            }

            // Prompt the user to input claim date
            System.out.println("Enter claim date (format: dd/MM/yyyy):");
            String claimDateString = scanner.nextLine();
            Date claimDate = parseDate(claimDateString);

            // Prompt the user to input the customer ID of the insured person
            System.out.println("Enter customer ID of the insured person (format: c-<7 numbers>):");
            String customerId = scanner.nextLine();
            // Find the customer with the provided ID
            Customer insuredPerson = findCustomerById(customerId, customers);
            // Check if the customer with the provided ID exists
            if (insuredPerson != null) {
                // Extract the insurance card ID from the customer information
                String insuranceCardId = insuredPerson.getInsuranceCard().getCardNumber();
                // Retrieve the insurance card details based on the insurance card ID
                InsuranceCard insuranceCard = getInsuranceCardById(insuranceCardId);
                // Continue with the claim creation process
                claim.setInsuredPerson(insuredPerson);
                claim.setCardNumber(insuranceCard);
            } else {
                System.out.println("Customer with ID " + customerId + " not found. Claim cannot be added.");
                return; // Exit the method without adding the claim
            }


            // Prompt the user to input exam date
            System.out.println("Enter exam date (format: dd/MM/yyyy):");
            String examDateString = scanner.nextLine();
            Date examDate = parseDate(examDateString);

            // Prompt the user to input documents list
            System.out.println("Enter documents list (comma-separated, e.g., document1.pdf,document2.pdf):");
            String[] documentsArray = scanner.nextLine().split(",");
            List<Claim> documents = new ArrayList<>();
            for (String document : documentsArray) {
                claim = Customer.stringToClaim(document);
                if (claim != null) {
                    documents.add(claim);
                }
            }

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
            System.out.println("Enter receiver bank information (Bank – Name – Number, separated by commas):");
            String receiverBankInfo = scanner.nextLine();
            // Split the input by commas to get individual pieces of bank information
            String[] bankInfoArray = receiverBankInfo.split(",");
            // Create a List to store the bank information
            List<String> bankInfoList = Arrays.asList(bankInfoArray);
            // Set the bank information to the claim
            claim.setReceiverBankInfo(bankInfoList);


            // Create the Claim object using the input
            claim = new Claim(claimID, claimDate, insuredPerson, cardNumber, examDate, documents, claimAmount, status, bankInfoList);

            // Write the new claim data to the file
            writer.write(Customer.claimToString(claim));
            writer.newLine();
            System.out.println("Claim added successfully.");
        } catch (IOException e) {
            System.err.println("Error adding claim: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private InsuranceCard getInsuranceCardById(String insuranceCardId) {
        // Load all customers to search for the insurance card ID
        List<Customer> customers = Customer.getAllCustomers();

        // Iterate through each customer to find the insurance card with the provided ID
        for (Customer customer : customers) {
            InsuranceCard insuranceCard = customer.getInsuranceCard();
            if (insuranceCard != null && insuranceCard.getCardNumber().equals(insuranceCardId)) {
                // If the insurance card ID matches, return the insurance card
                return insuranceCard;
            }
        }

        // If no matching insurance card is found, return null
        return null;
    }



    @Override
    public void updateClaim(Claim updatedClaim) {
        for (int i = 0; i < documents.size(); i++) {
            Claim claim = documents.get(i);
            if (claim.getClaimID().equals(updatedClaim.getClaimID())) {
                documents.set(i, updatedClaim);
                return;
            }
        }
        // If the claim with the given ID is not found
        throw new IllegalArgumentException("Claim not found: " + updatedClaim.getClaimID());
    }

    @Override
    public void deleteClaim(String claimID) {
        for (int i = 0; i < documents.size(); i++) {
            Claim claim = documents.get(i);
            if (claim.getClaimID().equals(claimID)) {
                documents.remove(i);
                return;
            }
        }
        // If the claim with the given ID is not found
        throw new IllegalArgumentException("Claim not found: " + claimID);
    }

    @Override
    public Claim getOneClaim(String claimID) {
        for (Claim claim : documents) {
            if (claim.getClaimID().equals(claimID)) {
                return claim;
            }
        }
        return null; // Claim not found
    }

    @Override
    public List<Claim> getAllClaims() {
        return documents;
    }

    private static Date parseDate(String dateString) {
        // Parse string to Date
        // You can implement your date parsing logic here
        return new Date(); // Placeholder implementation, replace with actual parsing
    }

}
