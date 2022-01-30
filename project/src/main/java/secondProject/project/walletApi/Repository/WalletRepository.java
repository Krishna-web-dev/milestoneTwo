package secondProject.project.walletApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import secondProject.project.walletApi.Entity.wallet;

public interface WalletRepository extends JpaRepository<wallet,Integer> {
}
