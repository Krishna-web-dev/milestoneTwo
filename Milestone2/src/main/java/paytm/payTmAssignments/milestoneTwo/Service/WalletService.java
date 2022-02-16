package paytm.payTmAssignments.milestoneTwo.Service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import paytm.payTmAssignments.milestoneTwo.Dao.*;
import paytm.payTmAssignments.milestoneTwo.Dto.ReceiversDataTransferObject;
import paytm.payTmAssignments.milestoneTwo.Dto.SendersDataTransferObject;
import paytm.payTmAssignments.milestoneTwo.Dto.TransactionSummaryDataTransferObject;
import paytm.payTmAssignments.milestoneTwo.Dto.WalletDataTransferObject;
import paytm.payTmAssignments.milestoneTwo.Model.Receivers;
import paytm.payTmAssignments.milestoneTwo.Model.Senders;
import paytm.payTmAssignments.milestoneTwo.Model.Transaction_summary;
import paytm.payTmAssignments.milestoneTwo.Model.Wallet;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class WalletService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(WalletService.class);


    @Autowired
    private WalletRepository wallet_repo;
    @Autowired
    private TransactionRepository trans_repo;
    @Autowired
    private SenderUserRepository sendersRepository;
    @Autowired
    private RecievingUserRepository receiversRepository;
    @Autowired
    private UserRepository userRepository;



    @Autowired
    private ModelMapper modelMapper;


    public WalletDataTransferObject convertEntityToDto_wallet(Wallet wallet){
        return modelMapper.map(wallet,WalletDataTransferObject.class);
    }
    public Wallet convertDtoToEntity_wallet(WalletDataTransferObject walletDataTransferObject){
        return modelMapper.map(walletDataTransferObject,Wallet.class);
    }
    public TransactionSummaryDataTransferObject convertEntityToDto_transaction(Transaction_summary transaction_summary){
        return  modelMapper.map(transaction_summary,TransactionSummaryDataTransferObject.class);
    }
    public Transaction_summary convertDtoToEntity_transaction(TransactionSummaryDataTransferObject transactionSummaryDataTransferObject){
        return modelMapper.map(transactionSummaryDataTransferObject,Transaction_summary.class);
    }
    public ReceiversDataTransferObject convertEntityToDto_receivers(Receivers receivers){
        return modelMapper.map(receivers,ReceiversDataTransferObject.class);
    }
    public Receivers convertDtoToEntity_receivers(ReceiversDataTransferObject receiversDataTransferObject){
        return modelMapper.map(receiversDataTransferObject,Receivers.class);
    }
    public SendersDataTransferObject convertEntityToDto_senders(Senders senders){
        return modelMapper.map(senders,SendersDataTransferObject.class);
    }
    public Senders convertDtoToEntity_senders(SendersDataTransferObject sendersDataTransferObject){
        return modelMapper.map(sendersDataTransferObject,Senders.class);
    }

    public List<WalletDataTransferObject> listAll_wallet() {
        return wallet_repo.findAll()
                .stream()
                .map(this::convertEntityToDto_wallet)
                .collect(Collectors.toList());
    }
    public List<TransactionSummaryDataTransferObject> get_currentTransaction(String tranx_id) {
        /*System.out.println("obtaining tranx_id"+tranx_id);
        System.out.println("fetching tranx_id"+trans_repo.getListBytranxid(tranx_id));


        return  convertEntityToDto_transaction(trans_repo.getListBytranxid(tranx_id));
*/
        List<Transaction_summary> transaction_summaries=new ArrayList<>();

           transaction_summaries.add(trans_repo.getListBytranxid(tranx_id));

        return transaction_summaries
                .stream()
                .map(this::convertEntityToDto_transaction)
                .collect(Collectors.toList());
    }


    public List<List<?>> get_All_transactions(String payernumber) {
        List<Senders> sendersList=  sendersRepository.getByUserPhonenumber(payernumber);
        List<Receivers> receiversList= receiversRepository.getByUserPhonenumber(payernumber);

        List<List<?>> list = new ArrayList<>();
        list.add(sendersList);
        list.add(receiversList);
        System.out.println(list + "transaction list");
        return list;
    }


    public Wallet save_wallet(WalletDataTransferObject walletdto) {

        return wallet_repo.save(convertDtoToEntity_wallet(walletdto));
    }

    public Wallet save_wallet2(Wallet wallet) {

        return wallet_repo.save(wallet);
    }
    //Function for saving the wallet


    //Function for finding the wallet by id
    public WalletDataTransferObject get_wallet(String mobilenumber) {
        Wallet temp= wallet_repo.findByMobilenumber(mobilenumber);
        return convertEntityToDto_wallet(temp);
    }


    public Transaction_summary save_transaction(TransactionSummaryDataTransferObject transactionSummaryDataTransferObject) {

        logger.info("transaction is saved");

        return trans_repo.save(convertDtoToEntity_transaction(transactionSummaryDataTransferObject));
    }

    public Senders SavingToDbSender(SendersDataTransferObject sendersDTO) {
        logger.info("transaction is saved to database sender");

        return sendersRepository.save(convertDtoToEntity_senders(sendersDTO));

    }
    public Receivers SavingToDbReceivers(ReceiversDataTransferObject receiversDTO) {
        logger.info("transaction is saved from the reciever side ");

        return receiversRepository.save(convertDtoToEntity_receivers(receiversDTO));
    }

    //Function for deleting the wallet
    public void delete(String walletname) {
        wallet_repo.deleteById(walletname);
    }

    public boolean checkforDuplicateEntry(WalletDataTransferObject walletDTO){
        Wallet wallet= wallet_repo.findByMobilenumber(walletDTO.getMobilenumber());
        return wallet != null;

    }
    public boolean checkForSufficientAmount(String payer_mobile_number,String amount){

        logger.info("checking="+wallet_repo.getByAmount(payer_mobile_number));

       // System.out.println("checking="+wallet_repo.getByAmount(payer_mobile_number));
        return (Integer.parseInt(amount) > Integer.parseInt(wallet_repo.getByAmount(payer_mobile_number)));
       // return true;
    }



    public boolean containsLetters(String str)
        {
            String regex = "[0-9]+";
            Pattern p = Pattern.compile(regex);

            // If the string is empty
            // return false
            if (str == null) {
                return false;
            }

            // Find match between str and amount and regular expression
            Matcher m = p.matcher(str);

            // Return if the string matched the ReGex
            return !m.matches() ;
        }


    public void SaveToSender(String payer, String payee, String amount,long T_D) {
        SendersDataTransferObject sendersDTO=new SendersDataTransferObject();
        sendersDTO.setAmount_sent(amount);
        String timestamp = String.valueOf(Instant.ofEpochMilli(T_D)
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        sendersDTO.setCreated_timestamp(timestamp);
        sendersDTO.setModified_timestamp(timestamp);
        sendersDTO.setUser_phonenumber(payer);
        sendersDTO.setSent_to(payee);
        sendersDTO.setTransaction_id(String.valueOf(T_D));
        SavingToDbSender(sendersDTO);
    }
    public void SaveToReceiver(String payer, String payee, String amount,long T_D) {
        ReceiversDataTransferObject receiversDTO=new ReceiversDataTransferObject();
        receiversDTO.setAmount_received(amount);
        String timestamp = String.valueOf(Instant.ofEpochMilli(T_D)
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        receiversDTO.setCreated_timestamp(timestamp);
        receiversDTO.setModified_timestamp(timestamp);
        receiversDTO.setUser_phonenumber(payee);
        receiversDTO.setReceived_from(payer);
        receiversDTO.setTransaction_id(String.valueOf(T_D));
        SavingToDbReceivers(receiversDTO);
    }
    public void deductAmount(String mobilenumber,String amount)
    {
        WalletDataTransferObject walletDTO= get_wallet(mobilenumber);
        walletDTO.setAmount(String.valueOf(Integer.parseInt(walletDTO.getAmount()) - Integer.parseInt(amount)));
        save_wallet(walletDTO);

    }
    public void addAmount(String mobilenumber,String amount)
    {
        WalletDataTransferObject walletDTO= get_wallet(mobilenumber);
        walletDTO.setAmount(String.valueOf(Integer.parseInt(walletDTO.getAmount()) + Integer.parseInt(amount)));
        save_wallet(walletDTO);
    }
    public void saveToTransactionSummary(String payer, String payee, String amount, long T_D) {
        TransactionSummaryDataTransferObject transactionSummaryDTO =new TransactionSummaryDataTransferObject();
        transactionSummaryDTO.setAmount(amount);
        transactionSummaryDTO.setPayment_from_mobilenumber(payer);
        transactionSummaryDTO.setPayment_to_mobilenumber(payee);
        transactionSummaryDTO.setTranx_id(String.valueOf(T_D));
        transactionSummaryDTO.setStatus("Success");
        String timestamp = String.valueOf(Instant.ofEpochMilli(T_D)
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        transactionSummaryDTO.setDate(timestamp);
        save_transaction(transactionSummaryDTO);


        SaveToSender(payer,payee,amount,T_D);
        SaveToReceiver(payer,payee,amount,T_D);

    }



    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        //Logic to get the user form the Database

        return new User("admin","password",new ArrayList<>());
        /*User user = userRepository.findBymobilenumber(userName);

        if(user == null)
            throw new UsernameNotFoundException("User Not found");

        return new CustomUserAuth(user);*/
    }
}


