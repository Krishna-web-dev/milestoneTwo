package secondProject.project.walletApi.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import secondProject.project.walletApi.Exception.UserAlreadyExist;
import secondProject.project.walletApi.Exception.UserAlreadyHasWalletException;
import secondProject.project.walletApi.Exception.UserDoesNotHaveWallet;
import secondProject.project.walletApi.Exception.UserNotFoundException;
import secondProject.project.walletApi.Repository.TransactionRepository;
import secondProject.project.walletApi.Repository.UserRepository;
import secondProject.project.walletApi.Repository.WalletRepository;
import secondProject.project.walletApi.Entity.User;
import secondProject.project.walletApi.Entity.wallet;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional

public class walletService {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private WalletRepository walletRepository;

   @Autowired
   private TransactionRepository transactionRepository;

   public  void saveUser( User user)
   {
      userRepository.save(user);
   }

   public List<User> listAll(){
      return userRepository.findAll();
   }

   public void existByEmailOrMobile(User user) throws UserAlreadyExist {
      User existsByEmailOrMobile = userRepository.findByEmailOrPhoneNumber(user.getEmail(),user.getMobile());

      if(existsByEmailOrMobile != null) throw  new UserAlreadyExist();
   }

   public wallet getWalletByMobile(String mobile)
   {
      User user = userRepository.findByMoblie((mobile));
      return user.getWallet();
   }

   public void createWallet(String userMobile) throws UserNotFoundException, UserAlreadyHasWalletException {

      boolean status = userRepository.existsById(userMobile);

      if(!status){
         throw  new UserNotFoundException();
      }
      User user = userRepository.findByMoblie(userMobile);

      if(user.getWallet()!=null)
      {
         throw new UserAlreadyHasWalletException();
      }
      user.setWallet(new wallet());
   }

   // how to add money
   public void addMoney( String mobile, Float moneySent) throws UserNotFoundException, UserDoesNotHaveWallet {
      boolean status = userRepository.existsById(mobile);

      if(!status)
      {
         throw new UserNotFoundException();
      }
      User user = userRepository.findByMoblie(mobile);

      if(user.getWallet()==null){
         throw new UserDoesNotHaveWallet();
      }

      wallet tempwallet = user.getWallet();
      tempwallet.setBalance(tempwallet.getBalance() + moneySent);
   }
}
