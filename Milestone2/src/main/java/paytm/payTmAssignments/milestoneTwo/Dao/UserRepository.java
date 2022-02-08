package paytm.payTmAssignments.milestoneTwo.Dao;
import paytm.payTmAssignments.milestoneTwo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    public User findBymobilenumber(String phoneNumber);


}
