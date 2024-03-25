package Claim;

import Customer.Customer;
import InsuranceCard.InsuranceCard;
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
    public boolean addClaim(Claim claim) {
        if (documents.contains(claim.claimID)){
            return false;
        }
        documents.add(claim);
        return true;
    }

    @Override
    public void removeClaim(Claim claim) {
            documents.remove(claim);
    }

    @Override
    public void updateClaim(Claim claim) {
        if (documents.contains(claim.claimID)){
            removeClaim(claim);
            addClaim(claim);
        }
    }

    @Override
    public void getOne(Claim claim) {
        this.get(claim);
    }
    @Override
    public void getAll(Claim claim) {

    }






}
