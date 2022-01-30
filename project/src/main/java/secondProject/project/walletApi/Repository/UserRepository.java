package secondProject.project.walletApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import secondProject.project.walletApi.Entity.User;

public interface UserRepository  extends JpaRepository<User,String> {
 public  User findByMoblie(String mobile);
 public  User findByEmailOrPhoneNumber(String email, String mobile);

}
