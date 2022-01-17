package dtu.group2.Interfaces;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import java.util.Collection;
import java.util.HashMap;

public interface IAccountRepository {
    final HashMap<String,Account> user = null;
//    final HashMap<String,String> id = null;
    BankService bank = null;

    Account getAccount(String id);
//    String getAccountId(String id);
    void delete(String id);
    String create(Account account);
    Boolean verify(String id);
    Collection<Account> getAll();
}
