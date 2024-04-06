import java.io.IOException;
import java.util.Scanner;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Main {
    public static void main(String[] args) throws IOException {

//        PolicyHolder.createPolicyHolder();
//        System.out.println(PolicyHolder.getAllPolicyHolders());



//        Dependent dependent = Dependent.createDependent();
//        systemManager.addDependentToPolicyHolder(scanner);

//        PolicyHolder policyHolder = PolicyHolder.createPolicyHolder();

//        System.out.println(Dependent.getAllDependents());
//        dependent.getPolicyHolder();
//        PolicyHolder.addDependentToPolicyHolder();

        SystemManager systemManager = new SystemManager();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMainMenu();
            String input = scanner.nextLine();

            if (handleChoice(input)) {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        handleClaimMenu(scanner);
                        break;
                    case 2:
                        handleDependentMenu(scanner);
                        break;
                    case 3:
                        handlePolicyHolderMenu(scanner);
                        break;
                    case 4:
                        handleInsuranceCardMenu(scanner);
                        break;
                    case 0:
                        exit = true;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                        break;
                }
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("Main menu:");
        System.out.println("1. Claim");
        System.out.println("2. Dependent");
        System.out.println("3. Policy Holder");
        System.out.println("4. Insurance Card");
        System.out.println("0. Exit");
        System.out.print("Enter your choice:");
    }

    private static boolean handleChoice(String choice) {
        boolean containsNumber = choice.matches("\\d");
        if (!containsNumber) {
            System.out.println("Invalid choice. Please choose again.");
        }
        return containsNumber;
    }

    private static void handleClaimMenu(Scanner scanner) throws IOException {
        Claim claim = new Claim();
        System.out.println("Claim menu:");
        System.out.println("1. Add claim");
        System.out.println("2. Update claim");
        System.out.println("3. Delete claim");
        System.out.println("4. View claims");
        System.out.println("5. Add document");
        System.out.println("6. Delete document");
        System.out.println("0. Exit");
        System.out.print("Enter your choice:");
        String input = scanner.nextLine();

        if (handleChoice(input)) {
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    System.out.println(PolicyHolder.getAllPolicyHolders());
                    System.out.println(Dependent.getAllDependents());
                    claim.addClaim();
                    break;
                case 2:
                    claim.updateClaim();
                    break;
                case 3:
                    claim.deleteClaim();
                    break;
                case 4:
                    claim.getAllClaims();
                    break;
                case 5:
                    claim.addDocument();
                    break;
                case 6:
                    claim.deleteDocument();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
                    break;
            }
        }
    }

    private static void handleDependentMenu(Scanner scanner) throws IOException {
        System.out.println("Dependents menu:");
        System.out.println("1. Add dependent");
        System.out.println("2. Delete dependent");
        System.out.println("3. View dependents");
        System.out.println("0. Exit");
        System.out.print("Enter your choice:");
        String input = scanner.nextLine();

        if (handleChoice(input)) {
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    Dependent.addDependent();
                    break;
                case 2:
                    Dependent.deleteDependent();
                    break;
                case 3:
                    Dependent.getAllDependents();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
                    break;
            }
        }
    }

    private static void handlePolicyHolderMenu(Scanner scanner) {
        System.out.println("Policy holders menu:");
        System.out.println("1. Add policy holders");
        System.out.println("2. Delete policy holders");
        System.out.println("3. View policy holders");
        System.out.println("4. Add dependent to policy holder");
        System.out.println("5. Delete dependent from policy holder");
        System.out.println("0. Exit");
        System.out.print("Enter your choice:");
        String input = scanner.nextLine();

        if (handleChoice(input)) {
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    PolicyHolder.addPolicyHolder();
                    break;
                case 2:
                    PolicyHolder.deletePolicyHolder();
                    break;
                case 3:
                    PolicyHolder.getAllPolicyHolders();
                    break;
                case 4:
                    PolicyHolder.addDependentToPolicyHolder();
                    break;
                case 5:
                    PolicyHolder.deleteDependentFromPolicyHolder();
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
                    break;
            }
        }
    }

    private static void handleInsuranceCardMenu(Scanner scanner) {
        InsuranceCard insuranceCard = new InsuranceCard();
        System.out.println("Insurance cards menu:");
        System.out.println("1. Add insurance cards");
        System.out.println("2. Delete insurance cards");
        System.out.println("3. View insurance cards");
        System.out.println("0. Exit");
        System.out.print("Enter your choice:");
        String input = scanner.nextLine();

        if (handleChoice(input)) {
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    InsuranceCard.addInsuranceCard();
                    break;
                case 2:
                    InsuranceCard.deleteInsuranceCard();
                    break;
                case 3:
                    InsuranceCard.getAllInsuranceCards();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
                    break;
            }
        }
    }
}