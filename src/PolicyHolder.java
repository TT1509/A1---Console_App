import java.util.ArrayList;
import java.util.List;

public class PolicyHolder extends Customer {
    private ArrayList<String> dependents;

    public PolicyHolder() {
        super();
        dependents = null;
    }
    public PolicyHolder(String id, String fullName, InsuranceCard insuranceCard, List<Claim> claims) {
        super(id, fullName, insuranceCard, claims);
        this.dependents = new ArrayList<>();
    }

    public ArrayList<String> getDependents() {
        return dependents;
    }

    public void setDependents(ArrayList<String> dependents) {
        this.dependents = dependents;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PolicyHolder {");
        sb.append("id=").append(getId());
        sb.append(", fullName=").append(getFullName());
        sb.append(", insuranceCard=").append(getInsuranceCard());
        sb.append(", dependents=").append(dependents);
        sb.append(", claims=").append(getClaims());
        sb.append("}");
        return sb.toString();
    }
}
