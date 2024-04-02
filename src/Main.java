import java.util.Scanner;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        Customer.addCustomer();
        Customer.updateCustomer();

        InsuranceCard insuranceCard = new InsuranceCard();



        // Close the scanner
        scanner.close();
    }
}