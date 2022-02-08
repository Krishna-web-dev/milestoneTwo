package paytm.payTmAssignments.milestoneTwo.Dao;

import paytm.payTmAssignments.milestoneTwo.Model.Senders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SendersRepository extends JpaRepository<Senders, String> {



    @Query(value = "select * from senders where senders.user_phonenumber=:userPhonenumber", nativeQuery = true)
    public List<Senders> getByUserPhonenumber(String userPhonenumber);
}
