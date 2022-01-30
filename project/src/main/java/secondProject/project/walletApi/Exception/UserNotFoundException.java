package secondProject.project.walletApi.Exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(){
        super("user does not exist");
    }
}
