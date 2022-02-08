package paytm.payTmAssignments.milestoneTwo.Dao;

import paytm.payTmAssignments.milestoneTwo.Model.Transaction_summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction_summary, String> {

    @Query(value = "select * from transaction_summary where transaction_summary.tranx_id=:tranx_id", nativeQuery = true)
    public Transaction_summary getListBytranxid(String tranx_id);





}
