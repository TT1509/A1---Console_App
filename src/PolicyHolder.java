import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PolicyHolder extends Customer {
    static Scanner scanner = new Scanner(System.in);
    private ArrayList<String> dependents;

    public PolicyHolder() {
        super();
        dependents = null;
    }
    public PolicyHolder(String id, String fullName, InsuranceCard insuranceCard, ArrayList<Claim> claims,  ArrayList<String> dependents) {
        super(id, fullName, insuranceCard, claims);
        this.dependents = dependents;
    }



    public ArrayList<String> getDependents() {
        return dependents;
    }


    public void setDependents(ArrayList<String> dependents) {
        this.dependents = dependents;
    }

    public void addDependent(Dependent dependent) {
        // Update the dependents list
        dependents.add(dependent.getId() + " - " + dependent.getFullName());

        // Update the file with the new dependent information
        updateDependentsInFile(dependent);
    }

    private void updateDependentsInFile(Dependent dependent) {
        try (FileWriter writer = new FileWriter(CUSTOMER_FILE, true)) {
            writer.write(dependent.getId() + " - " + dependent.getFullName() + "\n");
            System.out.println("Dependent added to file successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteDependent(Dependent dependent) {
        this.dependents.remove(dependent.getId() + " - " + dependent.getFullName());
    }

    public void printPolicyHolder() {
        System.out.println(toString());
    }

    public static PolicyHolder createPolicyHolder() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<PolicyHolder> policyHolders = getAllPolicyHolders(); // Load existing policy holders

            String policyHolderId = generateCustomerId();
            System.out.println("Generated Policy Holder ID: " + policyHolderId);

            System.out.println("Enter policy holder full name:");
            String fullName = scanner.nextLine();

            // Create a new InsuranceCard object for the policy holder using the addInsuranceCard function
            InsuranceCard insuranceCard = InsuranceCard.addInsuranceCard(new PolicyHolder("", "", null, null, null));



            // Create a new PolicyHolder object with the provided details
            PolicyHolder newPolicyHolder = new PolicyHolder(policyHolderId, fullName, insuranceCard, new ArrayList<>(), new ArrayList<>());

            // Add the new policy holder to the list of policy holders
            policyHolders.add(newPolicyHolder);

            // Write the new policy holder data to the file
            savePolicyHoldersToFile(policyHolders);
            System.out.println("Policy holder added successfully.");

            return newPolicyHolder;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
    }






    public void addDependent() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter policy holder ID: ");
        String policyHolderId = scanner.nextLine();

        // Check if the provided policy holder ID exists
        PolicyHolder policyHolder = PolicyHolder.getPolicyHolderById(policyHolderId);
        if (policyHolder == null) {
            System.out.println("Policy holder with ID " + policyHolderId + " not found.");
            return;
        }

        // Create dependent
        Dependent dependent = Dependent.createDependent(policyHolderId);

        // Add dependent to the policy holder
        if (dependent != null) {
            policyHolder.addDependent(dependent);
            System.out.println("Dependent added successfully!");
        } else {
            System.out.println("Failed to create dependent.");
        }
    }


    public static void deletePolicyHolder(PolicyHolder policyHolder) {
        // Perform deletion logic, such as removing from a database or collection
    }

    public static PolicyHolder getPolicyHolderById(String id) {
        List<PolicyHolder> policyHolders = getAllPolicyHolders(); // Assuming getAllPolicyHolders() retrieves all policy holders
        for (PolicyHolder policyHolder : policyHolders) {
            if (policyHolder.getId().equals(id)) {
                return policyHolder;
            }
        }
        return null; // Return null if policy holder with the specified ID is not found
    }


    public static List<PolicyHolder> getAllPolicyHolders() {
        List<PolicyHolder> policyHolders = new ArrayList<>();
        List<Customer> customers = getAllCustomers(); // Assuming getAllCustomers() is a static method in the Customer class

        for (Customer customer : customers) {
            if (customer instanceof PolicyHolder) {
                policyHolders.add((PolicyHolder) customer);
            }
        }
        return policyHolders;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PolicyHolder {");
        sb.append("id=").append(getId());
        sb.append(", fullName=").append(getFullName());
        sb.append(", insuranceCard=").append(getInsuranceCard());
        sb.append(", dependents=[");
        for (String dependent : dependents) {
            sb.append(dependent).append(", ");
        }
        sb.append("]");
        sb.append(", claims=").append(getClaims());
        sb.append("}");
        return sb.toString();
    }



    // Modify customerToString to make it compatible with PolicyHolder
    static String customerToString(PolicyHolder policyHolder) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(policyHolder.getId()).append(";");
        stringBuilder.append(policyHolder.getFullName()).append(";");
        if (policyHolder.getInsuranceCard() != null) {
            // Append insurance card details, including ID
            stringBuilder.append(insuranceCardToString(policyHolder.getInsuranceCard())).append(";");
        } else {
            // Append empty field if no insurance card
            stringBuilder.append(";");
        }
        if (policyHolder.getClaims() != null && !policyHolder.getClaims().isEmpty()) {
            stringBuilder.append(claimsToString(policyHolder.getClaims()));
        }
        // Append dependents
        if (policyHolder.getDependents() != null && !policyHolder.getDependents().isEmpty()) {
            stringBuilder.append(Dependent.dependentsToString(policyHolder.getDependents()));
        }
        return stringBuilder.toString();
    }

    // Modify saveCustomersToFile to make it compatible with PolicyHolder
    static void savePolicyHoldersToFile(List<PolicyHolder> policyHolders) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (PolicyHolder policyHolder : policyHolders) {
                writer.write(customerToString(policyHolder));
                writer.newLine();
            }
        }
    }

}
