package dtu.group2.Repositories;

import dtu.group2.Interfaces.IAccountRepository;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import java.util.HashMap;

public class MerchantRepository implements IAccountRepository {
//    private static final HashMap<String, Account> merchantsAccounts = new HashMap<>();
//    private static final HashMap<String, String> merchantsIds = new HashMap<>();
    private static final HashMap<String, Account> merchantId = new HashMap<>();
//    BankService bank = new BankServiceService().getBankServicePort();

    @Override
    public Account getAccount(String id) {
        return merchantId.get(id);
//        return merchantsAccounts.get(id);
    }

//    @Override
//    public String getAccountId(String id) {
//        return merchantsIds.get(id);
//    }

    @Override
    public void delete(String id) {
//        merchantsAccounts.remove(id);
        merchantId.remove(id);
    }

    @Override
    public void create(String id, Account account) {
//        merchantsAccounts.put(accountId, account);
//        merchantsIds.put(merchantId, accountId);
        merchantId.put(id, account);
    }

    @Override
    public Boolean verify(String id) {
        return merchantId.get(id) != null;
//        return merchantsAccounts.get(id) != null;
    }
}
