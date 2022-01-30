package secondProject.project.walletApi.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class User {

    @Id
    private String firstname;
    private String lastname;
    private String mobile;

    @Column(unique = true)
    private String email;
    private  String password;
    private Date createdAt;
    private Date deletedAt;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "walletId", referencedColumnName = "walletId")
    public  wallet wallet;

    public secondProject.project.walletApi.Entity.wallet getWallet() {
        return wallet;
    }

    public void setWallet(secondProject.project.walletApi.Entity.wallet wallet) {
        this.wallet = wallet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public User() {
    }

    public User(String firstname, String lastname, String mobile) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mobile = mobile;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
