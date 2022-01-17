package dtu.group2.Application;

import java.math.BigDecimal;
import java.util.HashMap;

import dtu.group2.Interfaces.IAccountRepository;
import dtu.ws.fastmoney.*;

public class AccountService {
	
	BankService bank = new BankServiceService().getBankServicePort();

	private IAccountRepository customers;
	private IAccountRepository merchants;

//	private static HashMap<String,Account> merchants = new HashMap<>();
//	private static HashMap<String,Account> customers = new HashMap<>();
	
	
	public AccountService(IAccountRepository customers, IAccountRepository merchants) {
		this.customers = customers;
		this.merchants = merchants;
	}

	public String createCustomer(String uid) throws BankServiceException_Exception {
		customers.create(uid, bank.getAccount(uid));
		return uid;
	}

	public String createMerchant(String id) throws BankServiceException_Exception {
		merchants.create(id, bank.getAccount(id));
		return id;
	}

	public Account getCustomer(String id) {
		return customers.get(id);
	}

	public Account getMerchant(String id) {
		return merchants.get(id);
	}

	public void deleteCustomer(String id) {
		customers.delete(id);
	}

	public void deleteMerchant(String id) {
		merchants.delete(id);
	}

	public boolean verifyCustomer(String id){
		return customers.verify(id);
	}

	public boolean verifyMerchant(String id){
		return merchants.verify(id);
	}

	public Object getStatus() {
		return "Sanitity check for account service";
	}
}
