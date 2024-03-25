package Customer;
import Claim.Claim;
import InsuranceCard.InsuranceCard;
import java.util.List;

/**
 * @author Tran Luu Quang Tung - s3978481
 */
public class Customer {

    private String id;
    private String fullName;
    private InsuranceCard insuranceCard;
    private List<Claim> claims;
    public Customer(String id, String fullName, InsuranceCard insuranceCard, List<Claim> claims) {
        this.id = id;
        this.fullName = fullName;
        this.insuranceCard = insuranceCard;
        this.claims = claims;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public List<Claim> getClaims() {
        return claims;
    }



}
