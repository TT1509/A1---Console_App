import java.util.List;

public interface ClaimProcessManager {

    void addClaim();
    void updateClaim();
    void deleteClaim();
    Claim getClaimById();
    List<Claim> getAllClaims();
}
