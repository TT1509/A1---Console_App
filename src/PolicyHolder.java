import java.io.*;
import java.util.*;

public class PolicyHolder extends Customer {
    static final String POLICYHOLDERS_FILE = "resources/PolicyHolders.txt";
    static Scanner scanner = new Scanner(System.in);
    private ArrayList<String> dependents;

    public PolicyHolder() {
        super();
        dependents = null;
    }
    public PolicyHolder(String id, String fullName, InsuranceCard insuranceCard, ArrayList<Claim> claims,  ArrayList<String> dependents) {
        super(id, fullName, insuranceCard, claims);
        this.dependents = dependents != null ? dependents : new ArrayList<>(); // Initialize dependents with provided value or as an empty ArrayList
    }

    public static void deleteDependentFromPolicyHolder() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<PolicyHolder> policyHolders = getAllPolicyHolders(); // Load existing policy holders

            // Display a list of policy holders to the user
            System.out.println("Policy Holders:");
            for (PolicyHolder policyHolder : policyHolders) {
                System.out.println(policyHolder);
            }

            // Prompt the user to input the ID of the policy holder
            System.out.println("Enter the ID of the policy holder from whom you want to delete a dependent:");
            String policyHolderId = scanner.nextLine();

            // Find the selected policy holder
            PolicyHolder selectedPolicyHolder = null;
            for (PolicyHolder policyHolder : policyHolders) {
                if (policyHolder.getId().equals(policyHolderId)) {
                    selectedPolicyHolder = policyHolder;
                    break;
                }
            }

            if (selectedPolicyHolder == null) {
                System.out.println("Policy holder not found.");
                return;
            }

            // Display the dependents associated with the selected policy holder
            System.out.println("Dependents:");
            for (String dependent : selectedPolicyHolder.getDependents()) {
                System.out.println(dependent);
            }

            // Prompt the user to input the ID of the dependent to delete
            System.out.println("Enter the ID of the dependent you want to delete:");
            String dependentId = scanner.nextLine();

            // Find and remove the dependent from the selected policy holder's list of dependents
            boolean removed = selectedPolicyHolder.removeDependent(dependentId);

            if (removed) {
                // Save the updated list of policy holders to the file
                savePolicyHoldersToFile(policyHolders);
                System.out.println("Dependent deleted successfully.");
            } else {
                System.out.println("Dependent not found in the policy holder's list.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
    }

    // Your existing methods...

    public static void main(String[] args) {
        // Your main method
    }


    public ArrayList<String> getDependents() {
        return dependents;
    }


    public void setDependents(ArrayList<String> dependents) {
        this.dependents = dependents;
    }

    public void addDependent(String dependentId) {
        if (dependents == null) {
            dependents = new ArrayList<>();
        }
        dependents.add(dependentId);
    }

    public boolean removeDependent(String dependentId) {
        if (dependents != null) {
            dependents.remove(dependentId);
        }
        return false;
    }



    public void printPolicyHolder() {
        System.out.println(toString());
    }

    public static PolicyHolder addPolicyHolder() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<PolicyHolder> policyHolders = getAllPolicyHolders(); // Load existing policy holders

            String policyHolderId = generateCustomerId();
            System.out.println("Generated Policy Holder ID: " + policyHolderId);

            System.out.println("Enter policy holder full name:");
            String fullName = scanner.nextLine();

            // Create a new InsuranceCard object for the policy holder
            InsuranceCard insuranceCard = InsuranceCard.addInsuranceCard();

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






    public static void addDependentToPolicyHolder() {
        Scanner scanner = new Scanner(System.in);

        // Input policy holder ID
        System.out.print("Enter policy holder ID: ");
        String policyHolderId = scanner.nextLine();

        // Get the policy holder object by ID
        PolicyHolder policyHolder = PolicyHolder.getPolicyHolderById(policyHolderId);
        if (policyHolder != null) {
            // Input dependent ID
            System.out.print("Enter dependent ID: ");
            String dependentId = scanner.nextLine();

            // Get the list of dependents of the policy holder
            ArrayList<String> dependents = policyHolder.getDependents();
            // Add the dependent ID to the list
            dependents.add(dependentId);
            // Update the dependents list in the policy holder object
            policyHolder.setDependents(dependents);
            System.out.println("Dependent added to policy holder successfully!");
        } else {
            System.out.println("Policy holder not found.");
        }
    }


    public static boolean deletePolicyHolder() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<PolicyHolder> policyHolders = getAllPolicyHolders(); // Load existing policy holders

            // Prompt the user to input the ID of the policy holder to delete
            System.out.println("Enter the ID of the policy holder to delete:");
            String policyHolderIdToDelete = scanner.nextLine();

            // Find the policy holder with the provided ID
            Optional<PolicyHolder> policyHolderToDelete = policyHolders.stream()
                    .filter(policyHolder -> policyHolder.getId().equals(policyHolderIdToDelete))
                    .findFirst();

            if (policyHolderToDelete.isPresent()) {
                // Confirm deletion with the user
                System.out.println("Policy holder found:");
                System.out.println(policyHolderToDelete.get());
                System.out.println("Are you sure you want to delete this policy holder? (yes/no)");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("yes")) {
                    // Remove the policy holder from the list
                    policyHolders.remove(policyHolderToDelete.get());
                    // Save the updated list of policy holders to the file
                    savePolicyHoldersToFile(policyHolders);
                    System.out.println("Policy holder deleted successfully.");
                    return true;
                } else {
                    System.out.println("Deletion canceled.");
                    return false;
                }
            } else {
                System.out.println("Policy holder with ID " + policyHolderIdToDelete + " not found.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error deleting policy holder: " + e.getMessage());
            return false;
        } finally {
            scanner.close();
        }
    }



    public static PolicyHolder getPolicyHolderById(String id) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(CUSTOMER_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(id)) {
                    // Assuming the structure of the line is ID;FullName;InsuranceCard;...
                    // You may need to adjust the index based on the actual structure of your file
                    // Here, we assume the policy holder is the first entry in the line
                    PolicyHolder policyHolder = new PolicyHolder(parts[0], parts[1], null, null, null);
                    return policyHolder;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null; // Return null if policy holder with the specified ID is not found
    }


    public static List<PolicyHolder> getAllPolicyHolders() {
        List<PolicyHolder> policyHolders = new ArrayList<>();
        String policyHolderFile = POLICYHOLDERS_FILE;

        try (BufferedReader reader = new BufferedReader(new FileReader(policyHolderFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PolicyHolder policyHolder = PolicyHolder.stringToPolicyHolder(line);
                if (policyHolder != null) {
                    policyHolders.add(policyHolder);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading policy holders: " + e.getMessage());
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

    // Static method to create a PolicyHolder object from a string
    static PolicyHolder stringToPolicyHolder(String line) {
        String[] parts = line.split(";");
        if (parts.length >= 5) { // Assuming each line has at least 5 parts
            String id = parts[0];
            String fullName = parts[1];
            InsuranceCard insuranceCard = InsuranceCard.stringToInsuranceCard("Insurance Card: " + parts[2]); // Parse insurance card from parts[2] if needed
            ArrayList<Claim> claims = null; // Parse claims from parts[3] if needed
            ArrayList<String> dependents = null; // Policy holder ID
            return new PolicyHolder(id, fullName, insuranceCard, claims, dependents);
        }
        return null; // Invalid format, return null
    }

    // Modify customerToString to make it compatible with PolicyHolder
    static String policyHolderToString(PolicyHolder policyHolder) {
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(POLICYHOLDERS_FILE))) {
            for (PolicyHolder policyHolder : policyHolders) {
                writer.write("PolicyHolder:" + policyHolderToString(policyHolder));
                writer.newLine();
            }
        }
    }

}
