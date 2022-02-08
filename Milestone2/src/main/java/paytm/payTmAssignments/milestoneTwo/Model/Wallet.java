package paytm.payTmAssignments.milestoneTwo.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Wallet {
    @Id
    String mobilenumber;
    String amount;

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
