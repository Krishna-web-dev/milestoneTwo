package paytm.payTmAssignments.milestoneTwo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paytm.payTmAssignments.milestoneTwo.Dto.TransactionSummaryDataTransferObject;
import paytm.payTmAssignments.milestoneTwo.Dto.WalletDataTransferObject;
import paytm.payTmAssignments.milestoneTwo.Kafka.Producer;
import paytm.payTmAssignments.milestoneTwo.Model.JwtRequest;
import paytm.payTmAssignments.milestoneTwo.Model.JwtResponse;
import paytm.payTmAssignments.milestoneTwo.Service.WalletService;
import paytm.payTmAssignments.milestoneTwo.JWTutility.JWTUtility;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class Controller {


	Logger logger = LoggerFactory.getLogger(Controller.class);

	@Autowired
	Producer producer;
	@Autowired
	private WalletService service;

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private AuthenticationManager authenticationManager;

	public static long t_d;

	@RequestMapping(value="/post",method = RequestMethod.GET)
	public void sendMessage(@RequestParam("mobilenumber") String msg) {
		logger.info("sending message to " + msg + " created ");
		producer.publishToTopic(msg);
	}

	//

	@GetMapping("/wallet")
	public List<WalletDataTransferObject> list() {
		logger.info("all wallet are being listed on the screen ");

		return service.listAll_wallet();
	}

	@PostMapping("/create_wallet")
	public ResponseEntity<Object> add(@RequestBody WalletDataTransferObject walletDTO) {



		//displaying if user makes invalid entry then simply exit with message
		if(service.containsLetters(walletDTO.getMobilenumber()) ||
				service.containsLetters(walletDTO.getAmount())) {
			logger.warn("WRONG DETAILS ENTERED");
			return new ResponseEntity<>("Enter correct details", HttpStatus.BAD_REQUEST);
		}

		//displaying if user make duplicate entry
		if (service.checkforDuplicateEntry(walletDTO)) {
			logger.warn("Wallet Already Present");

			return new ResponseEntity<>("Wallet already present", HttpStatus.MULTI_STATUS);
			//producer.publishToTopic2("User created");
		}
		logger.info("wallet succesfully saved");

		service.save_wallet(walletDTO);

		return new ResponseEntity<>("Success \n-"+walletDTO,HttpStatus.ACCEPTED);

	}


	@PostMapping("/dotransaction")
	public ResponseEntity<String> transfer(@RequestBody ObjectNode ndj) {

		String payer_phone_number = ndj.get("payer_phone_number").asText();
		String payee_phone_number = ndj.get("payee_phone_number").asText();
		String amount=ndj.get("amount").asText();

		//displaying if user makes invalid entry then simply exit with message
		if(service.containsLetters(payer_phone_number)
				|| service.containsLetters(payee_phone_number)
				|| service.containsLetters(amount)) {
			logger.warn("WRONG DETAILS ENTERED");

			return new ResponseEntity<String>("Enter correct details", HttpStatus.BAD_REQUEST);
		}
		//checking funds
		if(service.checkForSufficientAmount(payer_phone_number,amount)) {
			logger.warn("Insufficient Funds");

			return new ResponseEntity<String>("Insufficient Funds", HttpStatus.BAD_REQUEST);
		}
		service.deductAmount(payer_phone_number,amount); //deducting amount of payer
		service.addAmount(payee_phone_number,amount);   //adding amount to payee

		t_d=Instant.now().toEpochMilli();
		String temp="Transaction Success\nTransaction ID:"+t_d;

		//saving transcation by transx_id
		logger.info("Transaction saved to the transaction summary");

		service.saveToTransactionSummary(payer_phone_number,payee_phone_number,amount,t_d);

		//pushing event to Kafka
		producer.publishToTopic(temp);
		logger.info("Pushing event to Kafa");


		return new ResponseEntity<String>(temp,HttpStatus.ACCEPTED);


	}

	@RequestMapping(value="/transaction",method = RequestMethod.GET)
	public List<TransactionSummaryDataTransferObject> getTransactionSummary(@RequestParam("transactionID") String tranx_id)
	{

		logger.info("getting transaction id " + tranx_id);

		System.out.println("getting tranx_id"+tranx_id);

		logger.info("transaction succesfully saved");

		return service.get_currentTransaction(tranx_id);
	}

	@RequestMapping(value="/all_transactions",method = RequestMethod.GET)
	public List<List<?>> get_transactions(@RequestBody String mobilenumber)
	{
		logger.info("all transcations are listed on the screen");

		return service.get_All_transactions(mobilenumber);
	}

	@PostMapping("/authenticate")
	public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							jwtRequest.getUsername(),
							jwtRequest.getPassword()
					)
			);
		} catch (BadCredentialsException e) {
			logger.warn("INVALID_CREDENTIALS");

			throw new Exception("INVALID_CREDENTIALS", e);
		}

		final UserDetails userDetails
				= service.loadUserByUsername(jwtRequest.getUsername());

		final String token =
				jwtUtility.generateToken(userDetails);

		logger.info("token successfully returned to jwt ");


		return  new JwtResponse(token);
	}


	//basic api's
	@PutMapping("/wallet/{id}")
	public ResponseEntity<?> update(@RequestBody WalletDataTransferObject walletDTO, @PathVariable String id) {
		try {
			WalletDataTransferObject existProduct = service.get_wallet(id);

			service.save_wallet(walletDTO);
			logger.info("updation successfull");

			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
		catch (NoSuchElementException e) {
			logger.warn("NO such wallet id found");

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (NumberFormatException e) {
			logger.warn("Invalid number format enter correct format ");

			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/wallet/{id}")
	public void delete(@PathVariable String id) {
		logger.warn("wallet with id = " + id + " succesfullly DELETED");

		service.delete(id);
	}

	@GetMapping("/wallet/{id}")
	public ResponseEntity<WalletDataTransferObject> get(@PathVariable String id) {
		try {
			logger.info("using service layer get the wallet id ");

			WalletDataTransferObject user = service.get_wallet(id);

			logger.info("wallet succesfully printed ");

			return new ResponseEntity<WalletDataTransferObject>(user, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			logger.warn("INVALID_CREDENTIALS no such wallet exist");

			return new ResponseEntity<WalletDataTransferObject>(HttpStatus.NOT_FOUND);
		}
	}



}
