package dtu.infrastructure;

import dtu.infrastructure.interfaces.IAccountRepository;
import dtu.ws.fastmoney.Account;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class AccountRepository implements IAccountRepository {

    private static final HashMap<String, Account> customers = new HashMap<>();

    @Override
    public Account getAccount(String id) {
        return customers.get(id);
    }

    @Override
    public void delete(String id) {
        customers.remove(id);
    }

    @Override
    public String create(Account account) {
        String uuid = UUID.randomUUID().toString();
        customers.put(uuid, account);
        return uuid;
    }

    @Override
    public Boolean verify(String id) {
        return customers.containsKey(id);
    }

    @Override
    public Collection<Account> getAll() {
        return customers.values();
    }
}
