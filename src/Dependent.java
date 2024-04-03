import java.util.List;

public class Dependent extends Customer {
    private String policyHolder;
    public Dependent() {
        super();
        policyHolder = null;
    }

    public Dependent(String id, String fullName, InsuranceCard insuranceCard, List<Claim> claims, String policyHolder) {
        super(id, fullName, insuranceCard, claims);
        this.policyHolder = policyHolder;
    }
    public String getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(String policyHolder) {
        this.policyHolder = policyHolder;
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

}
