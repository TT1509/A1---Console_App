
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
public class SystemManager {
    public void addDependentToPolicyHolder(Scanner scanner) {
        // Read dependent ID from user input
        System.out.print("Enter dependent ID: ");
        String dependentId = scanner.nextLine();

        // Read policy holder ID from user input
        System.out.print("Enter policy holder ID: ");
        String policyHolderId = scanner.nextLine();

        // Read dependent data from file
        Dependent dependent = findDependentById(dependentId);
        if (dependent == null) {
            System.out.println("Dependent not found.");
            return;
        }

        // Read policy holder data from file
        PolicyHolder policyHolder = findPolicyHolderById(policyHolderId);
        if (policyHolder == null) {
            System.out.println("Policy holder not found.");
            return;
        }

        // Check if the dependent is already associated with another policy holder
        if (dependent.getPolicyHolder() != null) {
            System.out.println("Dependent is already associated with a policy holder.");
            return;
        }

        // Check if the policy holder already has this dependent
        if (policyHolder.getDependents() != null && policyHolder.getDependents().contains(dependentId)) {
            System.out.println("Policy holder already has this dependent.");
            return;
        }

        // Assign the dependent to the policy holder
        dependent.setPolicyHolder(policyHolder.getId());
        policyHolder.addDependent(dependentId);
    }


    // Method to find a dependent by ID
    private Dependent findDependentById(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Dependent.DEPENDENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Dependent dependent = Dependent.stringToDependent(line, null);
                if (dependent != null && dependent.getId().equals(id)) {
                    return dependent;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to find a policy holder by ID
    private PolicyHolder findPolicyHolderById(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PolicyHolder.POLICYHOLDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PolicyHolder policyHolder = PolicyHolder.stringToPolicyHolder(line);
                if (policyHolder != null && policyHolder.getId().equals(id)) {
                    return policyHolder;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
