package InsuranceCard;
import Customer.Customer;

import java.util.List;

public interface InsuranceCardManager {

    void addInsuranceCard();

    void updateInsuranceCard();
    void deleteInsuranceCard();
    InsuranceCard getInsuranceCardById();
    List<InsuranceCard> getAllInsuranceCards();
}


