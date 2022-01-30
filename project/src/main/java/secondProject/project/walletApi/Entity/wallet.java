package secondProject.project.walletApi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer walletID;

    private float balance;

    @JsonIgnore
    @OneToOne(mappedBy = "wallet")
    private User walletOfUser;

    @JsonIgnore
    @OneToMany(mappedBy = "txnToWallet")
    private List<Transactions> toWalletTxn;

    @JsonIgnore
    @OneToMany(mappedBy = "txnFromWallet")
    private List<Transactions> fromWalletTxn;

    public wallet()
    {
        this.balance=0;
    }

    public wallet(Integer walletID, float balance, User walletOfUser, List<Transactions> toWalletTxn, List<Transactions> fromWalletTxn) {

        this.balance = balance;
        this.walletOfUser = walletOfUser;
        this.toWalletTxn = toWalletTxn;
        this.fromWalletTxn = fromWalletTxn;
    }

    public Integer getWalletID() {
        return walletID;
    }

    public void setWalletID(Integer walletID) {
        this.walletID = walletID;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public User getWalletOfUser() {
        return walletOfUser;
    }

    public void setWalletOfUser(User walletOfUser) {
        this.walletOfUser = walletOfUser;
    }

    public List<Transactions> getToWalletTxn() {
        return toWalletTxn;
    }

    public void setToWalletTxn(List<Transactions> toWalletTxn) {
        this.toWalletTxn = toWalletTxn;
    }

    public List<Transactions> getFromWalletTxn() {
        return fromWalletTxn;
    }

    public void setFromWalletTxn(List<Transactions> fromWalletTxn) {
        this.fromWalletTxn = fromWalletTxn;
    }
}
