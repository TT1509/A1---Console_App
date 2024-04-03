import java.util.Scanner;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        Customer.addCustomer();


//        InsuranceCard.getInsuranceCardById();
        Claim claim = new Claim();
        claim.addClaim();


        // Close the scanner
        scanner.close();
    }
}