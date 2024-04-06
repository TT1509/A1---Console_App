import java.io.*;
import java.util.*;

public class Dependent extends Customer {
    static final String DEPENDENTS_FILE = "resources/Dependents.txt";
    private String policyHolder;

    public Dependent() {
        super();
        policyHolder = null;
    }

    public Dependent(String id, String fullName, InsuranceCard insuranceCard, ArrayList<Claim> claims, String policyHolder) {
        super(id, fullName, insuranceCard, claims);
        this.policyHolder = policyHolder;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(String policyHolder) {
        this.policyHolder = policyHolder;
    }

    public void printDependent() {
        System.out.println(toString());
    }

    public void addPolicyHolderToDependent(String policyHolderId) {
        this.policyHolder = policyHolderId;
    }


    public static PolicyHolder getPolicyHolder(String dependentId) throws IOException {
        // Get all dependents
        List<Dependent> dependents = getAllDependents();

        // Iterate through dependents to find the matching one
        for (Dependent dependent : dependents) {
            if (dependent.getId().equals(dependentId)) {
                // Get the policy holder ID associated with this dependent
                String policyHolderId = dependent.getPolicyHolder();

                // Find the policy holder using the ID
                return PolicyHolder.getPolicyHolderById(policyHolderId);
            }
        }

        // If no matching dependent is found, return null
        return null;
    }

    public static Dependent addDependent() throws IOException {
        List<Dependent> dependents = getAllDependents();

        String id = generateCustomerId();
        System.out.println("Dependent ID " + id);
        System.out.println("Enter dependent full name: ");
        String fullName = PolicyHolder.scanner.nextLine();
        ArrayList<Claim> claims = new ArrayList<>();

        // Create a new InsuranceCard object for the dependent
        InsuranceCard insuranceCard = InsuranceCard.addInsuranceCard();

        // Create a new Dependent object with the provided details
        Dependent newDependent = new Dependent(id, fullName, insuranceCard, claims, null);

        // Add the new dependent to the list of dependents
        dependents.add(newDependent);

        // Write the new dependent data to the file
        saveDependentsToFile(dependents);
        System.out.println("Dependent added successfully.");
        return newDependent;
    }


    static List<Dependent> getAllDependents() throws IOException {
        List<Dependent> dependents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DEPENDENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Dependent dependent = stringToDependent(line, null);
                if (dependent != null) {
                    dependents.add(dependent);
                }
            }
        }
        return dependents;
    }

    static Dependent stringToDependent(String data, List<Customer> customers) {
        String[] parts = data.split(";");
        if (parts.length == 7) {
            String id = parts[0];
            String fullName = parts[1];
            InsuranceCard insuranceCard = InsuranceCard.stringToInsuranceCard(parts[2]);
            ArrayList<Claim> claims = Claim.parseClaims(parts[3], customers); // Passing the list of customers
            String policyHolderId = parts[4];
            return new Dependent(id, fullName, insuranceCard, claims, policyHolderId);
        }
        return null;
    }

    public static boolean deleteDependent() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<Dependent> dependents = getAllDependents(); // Load existing dependents

            // Prompt the user to input the ID of the dependent to delete
            System.out.println("Enter the ID of the dependent to delete:");
            String dependentIdToDelete = scanner.nextLine();

            // Find the dependent with the provided ID
            Optional<Dependent> dependentToDelete = dependents.stream()
                    .filter(dependent -> dependent.getId().equals(dependentIdToDelete))
                    .findFirst();

            if (dependentToDelete.isPresent()) {
                // Confirm deletion with the user
                System.out.println("Dependent found:");
                System.out.println(dependentToDelete.get());
                System.out.println("Are you sure you want to delete this dependent? (yes/no)");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("yes")) {
                    // Remove the dependent from the list
                    dependents.remove(dependentToDelete.get());
                    // Save the updated list of dependents to the file
                    saveDependentsToFile(dependents);
                    System.out.println("Dependent deleted successfully.");
                    return true;
                } else {
                    System.out.println("Deletion canceled.");
                    return false;
                }
            } else {
                System.out.println("Dependent with ID " + dependentIdToDelete + " not found.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error deleting dependent: " + e.getMessage());
            return false;
        } finally {
            scanner.close();
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dependent {");
        sb.append("id=").append(getId());
        sb.append(", fullName=").append(getFullName());
        sb.append(", insuranceCard=").append(getInsuranceCard());
        sb.append(", policyHolder=").append(policyHolder);
        sb.append(", claims=").append(getClaims());
        sb.append("}");
        return sb.toString();
    }

    // Method to serialize dependents into a string representation
    static String dependentsToString(ArrayList<String> dependents) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String dependent : dependents) {
            stringBuilder.append(dependent).append(";"); // Assuming dependent is represented as a string
        }
        return stringBuilder.toString();
    }



    static void saveDependentsToFile(List<Dependent> dependents) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DEPENDENTS_FILE))) {
            for (Dependent dependent : dependents) {
                writer.write("Dependent:" + customerToString(dependent));
                writer.newLine();
            }
        }
    }
}
