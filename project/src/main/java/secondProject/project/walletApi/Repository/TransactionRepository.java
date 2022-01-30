package secondProject.project.walletApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import secondProject.project.walletApi.Entity.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions,Integer> {
}
