package secondProject.project.walletApi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import secondProject.project.walletApi.Exception.UserAlreadyExist;
import secondProject.project.walletApi.Helper.UserCreationResponse;
import secondProject.project.walletApi.Service.walletService;
import secondProject.project.walletApi.Entity.User;

import java.util.Date;
import java.util.List;

@RestController
public class UserController {

  @Autowired
  private walletService service;

  @GetMapping("/users")
  public List<User> getAllUsers()
  {
    return service.listAll();
  }

  @PostMapping("/create-user")
  public ResponseEntity<UserCreationResponse> createUser(@RequestBody User user) {
    UserCreationResponse respo = new UserCreationResponse();
    respo.setFname(user.getFirstname());
    respo.setLname(user.getLastname());

    if (user.getEmail() == null || user.getPassword() == null || user.getMobile() == null) {
      respo.setStatus("Failed");
      respo.setError("Please enter the blank entry ,email password or mobile missing");

      return new ResponseEntity<>(respo, HttpStatus.BAD_REQUEST);
    }

    try {

      service.existByEmailOrMobile(user);

      user.setCreatedAt(new Date());

      service.saveUser(user);

      respo.setStatus("SUCCESS");
      respo.setError(" ");

      return new ResponseEntity<>(respo, HttpStatus.CREATED);
    } catch (UserAlreadyExist e) {
      respo.setStatus("Failed");
      respo.setError(e.getLocalizedMessage());
      return new ResponseEntity<>(respo, HttpStatus.BAD_REQUEST);
    }

  }
}

