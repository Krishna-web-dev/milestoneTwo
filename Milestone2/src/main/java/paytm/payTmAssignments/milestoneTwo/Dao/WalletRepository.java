package paytm.payTmAssignments.milestoneTwo.Dao;

import paytm.payTmAssignments.milestoneTwo.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {

    @Query(value = "select amount from wallet where wallet.mobilenumber=:mobilenumber", nativeQuery = true)
    public String getByAmount(String mobilenumber);

    public Wallet findByMobilenumber(String mobilenumber);

}
