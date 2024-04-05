import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dependent extends Customer {
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

    public static Dependent createDependent(String policyHolderId) {
        String id = generateCustomerId();
        System.out.print("Enter dependent full name: ");
        String fullName = PolicyHolder.scanner.nextLine();
        InsuranceCard insuranceCard = null;
        ArrayList<Claim> claims = new ArrayList<>();
        return new Dependent(id, fullName, insuranceCard, claims, policyHolderId);
    }

    public static boolean deleteDependent(ArrayList<Dependent> dependents, String id) {
        for (Dependent dependent : dependents) {
            if (dependent.getId().equals(id)) {
                dependents.remove(dependent);
                return true; // Dependent found and deleted
            }
        }
        return false; // Dependent not found
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
}
