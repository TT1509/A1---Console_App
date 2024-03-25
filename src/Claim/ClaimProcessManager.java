package Claim;

public interface ClaimProcessManager {

    boolean addClaim(Claim claim);
    void updateClaim(Claim claim);
    void removeClaim(Claim claim);
    void getOne(Claim claim);
    void getAll(Claim claim);
}
