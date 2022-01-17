package dtu.group2.Repositories;

import dtu.group2.Interfaces.IAccountRepository;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankServiceException_Exception;

import java.util.HashMap;

public class CustomerRepository implements IAccountRepository {

    private static final HashMap<String, Account> customers = new HashMap<>();

    @Override
    public Account get(String id) {
        return customers.get(id);
    }

    @Override
    public void delete(String id) {
        customers.remove(id);
    }

    @Override
    public void create(String id, Account account) throws BankServiceException_Exception {
        customers.put(id, account);
    }

    @Override
    public Boolean verify(String id) {
        return customers.get(id) != null;
    }
}
