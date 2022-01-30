package secondProject.project.walletApi.Exception;

public class UserDoesNotHaveWallet extends Exception{
    public UserDoesNotHaveWallet() {
        super("user does not have a wallet associated with this number");
    }

}
