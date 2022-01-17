package dtu.group2.Interfaces;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import java.util.HashMap;

public interface IAccountRepository {
    final HashMap<String,Account> user = null;
    BankService bank = null;

    Account get(String id);
    void delete(String id);
    void create(String id, Account account) throws BankServiceException_Exception;
    Boolean verify(String id);
}
