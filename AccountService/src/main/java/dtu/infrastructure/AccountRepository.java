package dtu.infrastructure;

import dtu.infrastructure.interfaces.IAccountRepository;
import dtu.ws.fastmoney.Account;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class AccountRepository implements IAccountRepository {

    private static final HashMap<String, Account> users = new HashMap<>();

    @Override
    public Account getAccount(String id) {
    	System.out.println("Checking if id is in the hashmap: " + id);
        return users.get(id);
    }

    @Override
    public void delete(String id) {
        users.remove(id);
    }

    @Override
    public String create(Account account) {
        String uuid = UUID.randomUUID().toString();
        users.put(uuid, account);
        return uuid;
    }

    @Override
    public Boolean verify(String id) {
        return users.containsKey(id);
    }

    @Override
    public Collection<Account> getAll() {
        return users.values();
    }
}
