import InsuranceCard.InsuranceCard;
import Claim.Claim;
import Customer.Customer;
import Customer.CustomerManager;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Customer customer= new Customer(); // Instantiate the Customer class
        customer.addCustomer();
        InsuranceCard insuranceCard = new InsuranceCard();
//        insuranceCard.addInsuranceCard();


        // Close the scanner
        scanner.close();
    }
}