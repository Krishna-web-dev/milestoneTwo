package secondProject.project.walletApi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import secondProject.project.walletApi.Exception.UserAlreadyHasWalletException;
import secondProject.project.walletApi.Exception.UserDoesNotHaveWallet;
import secondProject.project.walletApi.Exception.UserNotFoundException;
import secondProject.project.walletApi.Helper.WalletUpdationResponse;
import secondProject.project.walletApi.Helper.walletCreationResponse;
import secondProject.project.walletApi.Service.walletService;
import secondProject.project.walletApi.Entity.wallet;

@RestController
public class WalletController {
    @Autowired
    private walletService walletService;




    @PutMapping("/add-money/{mobile}/{money}")
    public ResponseEntity<WalletUpdationResponse> addMoneyToWallet(@PathVariable("mobile") String mobile,@PathVariable("money") Float money)
    {
        WalletUpdationResponse respo = new WalletUpdationResponse();
        respo.setMobile(mobile);
        try {
            walletService.addMoney(mobile,money);
            wallet w = walletService.getWalletByMobile(mobile);
            respo.setBalance(Float.toString(w.getBalance()));
            return new ResponseEntity<>(respo,HttpStatus.CREATED);

        }
        catch (UserNotFoundException e )
        {
            respo.setError(e.getLocalizedMessage());
            respo.setStatus("Failed");
            return  new ResponseEntity<>(respo,HttpStatus.BAD_REQUEST);
        }
        catch (UserDoesNotHaveWallet e)
        {
            respo.setError(e.getLocalizedMessage());
            respo.setStatus("Failed");
            return  new ResponseEntity<>(respo,HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/create-wallet/{mobile}")
    public ResponseEntity<walletCreationResponse> createWallet(@PathVariable("mobile") String mobilenumber){
    walletCreationResponse respo = new walletCreationResponse();
    respo.setMobile(mobilenumber);
     try{
         walletService.createWallet(mobilenumber);
         respo.setStatus("Successfull");
         return new ResponseEntity<>(respo, HttpStatus.CREATED);
     }
     catch (UserNotFoundException e)
     {
         respo.setError(e.getLocalizedMessage());
         respo.setStatus("Failed");
         return new ResponseEntity<>(respo,HttpStatus.BAD_REQUEST);
     }
     catch (UserAlreadyHasWalletException e){
         respo.setError(e.getLocalizedMessage());
         respo.setStatus("Failed");
         return new ResponseEntity<>(respo,HttpStatus.BAD_REQUEST);
     }

    }


}
