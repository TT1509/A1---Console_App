import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Claim implements ClaimProcessManager {

    private String claimID;
    private Date claimDate;
    private Customer insuredPerson;
    private InsuranceCard cardNumber;
    private Date examDate;
    private List<Claim> documents;
    private double claimAmount;
    private String status;
    private String receiverBankInfo;

    // Constructor
    public Claim() {
        this.claimID = "";
        this.claimDate = new Date();
        this.insuredPerson = null;
        this.cardNumber = null;
        this.examDate = new Date();
        this.documents = new ArrayList<>();
        this.claimAmount = 0.0;
        this.status = "";
        this.receiverBankInfo = "";
    }

    public Claim(String claimID, Date claimDate,
                 Customer insuredPerson, InsuranceCard cardNumber,
                 Date examDate, List<Claim> documents, double claimAmount,
                 String status, String receiverBankInfo) {
        this.claimID = claimID;
        this.claimDate = claimDate;
        this.insuredPerson = insuredPerson;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.documents = documents;
        this.claimAmount = claimAmount;
        this.status = status;
        this.receiverBankInfo = receiverBankInfo;
    }

    // Getters & Setters
    public String getClaimID() {
        return claimID;
    }

    public void setClaimID(String claimID) {
        this.claimID = claimID;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public Customer getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(Customer insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public InsuranceCard getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(InsuranceCard cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverBankInfo() {
        return receiverBankInfo;
    }

    public void setReceiverBankInfo(String receiverBankInfo) {
        this.receiverBankInfo = receiverBankInfo;
    }

    @Override
    public void addClaim(Claim claim) {
        documents.add(claim);
    }

    @Override
    public void updateClaim(Claim updatedClaim) {
        for (int i = 0; i < documents.size(); i++) {
            Claim claim = documents.get(i);
            if (claim.getClaimID().equals(updatedClaim.getClaimID())) {
                documents.set(i, updatedClaim);
                return;
            }
        }
        // If the claim with the given ID is not found
        throw new IllegalArgumentException("Claim not found: " + updatedClaim.getClaimID());
    }

    @Override
    public void deleteClaim(String claimID) {
        for (int i = 0; i < documents.size(); i++) {
            Claim claim = documents.get(i);
            if (claim.getClaimID().equals(claimID)) {
                documents.remove(i);
                return;
            }
        }
        // If the claim with the given ID is not found
        throw new IllegalArgumentException("Claim not found: " + claimID);
    }

    @Override
    public Claim getOneClaim(String claimID) {
        for (Claim claim : documents) {
            if (claim.getClaimID().equals(claimID)) {
                return claim;
            }
        }
        return null; // Claim not found
    }

    @Override
    public List<Claim> getAllClaims() {
        return documents;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "claimID='" + claimID + '\'' +
                ", claimDate=" + claimDate +
                ", insuredPerson=" + insuredPerson +
                ", cardNumber=" + cardNumber +
                ", examDate=" + examDate +
                ", documents=" + documents +
                ", claimAmount=" + claimAmount +
                ", status='" + status + '\'' +
                ", receiverBankInfo='" + receiverBankInfo + '\'' +
                '}';
    }

}
