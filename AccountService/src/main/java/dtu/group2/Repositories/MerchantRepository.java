package dtu.group2.Repositories;

import dtu.group2.Interfaces.IAccountRepository;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import java.util.HashMap;

public class MerchantRepository implements IAccountRepository {
    private static final HashMap<String, Account> merchantsAccounts = new HashMap<>();
    private static final HashMap<String, String> merchantsIds = new HashMap<>();
    BankService bank = new BankServiceService().getBankServicePort();

    @Override
    public Account getAccount(String id) {
        return merchantsAccounts.get(id);
    }

    @Override
    public String getAccountId(String id) {
        return merchantsIds.get(id);
    }

    @Override
    public void delete(String id) {
        merchantsAccounts.remove(id);
    }

    @Override
    public void create(String accountId, String merchantId, Account account) throws BankServiceException_Exception {
        merchantsAccounts.put(accountId, account);
        merchantsIds.put(merchantId, accountId);
    }

    @Override
    public Boolean verify(String id) {
        return merchantsAccounts.get(id) != null;
    }
}
