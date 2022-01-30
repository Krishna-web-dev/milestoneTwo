package secondProject.project.walletApi.Exception;

public class UserAlreadyHasWalletException extends Throwable{
    public UserAlreadyHasWalletException() {
        super("User already has a wallet");
    }

}
