package paytm.payTmAssignments.milestoneTwo.Dao;

import paytm.payTmAssignments.milestoneTwo.Model.Receivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceiversRepository extends JpaRepository<Receivers, String> {



    @Query(value = "select * from receivers where receivers.user_phonenumber=:userPhonenumber", nativeQuery = true)
    public List<Receivers> getByUserPhonenumber(String userPhonenumber);
}
