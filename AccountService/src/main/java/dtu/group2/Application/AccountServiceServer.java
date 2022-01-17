package dtu.group2.Application;

import java.math.BigDecimal;
import java.util.HashMap;

import dtu.group2.Interfaces.IAccountRepository;
import dtu.ws.fastmoney.*;

public class AccountServiceServer {
	
	BankService bank = new BankServiceService().getBankServicePort();

	private IAccountRepository customers;
	private IAccountRepository merchants;

//	private static HashMap<String,Account> merchants = new HashMap<>();
//	private static HashMap<String,Account> customers = new HashMap<>();
	
	
	public AccountServiceServer(IAccountRepository customers, IAccountRepository merchants) {
		this.customers = customers;
		this.merchants = merchants;
	}

	public String createCustomer(String uid) throws BankServiceException_Exception {
		customers.create(uid, bank.getAccount(uid));
		return uid;
	}

	public String createMerchant(String uid) throws BankServiceException_Exception {
		merchants.create(uid, bank.getAccount(uid));
		return uid;
	}

	public Account getCustomer(String accountID) {
		return customers.get(accountID);
	}

	public Account getMerchant(String accountID) {
		return merchants.get(accountID);
	}

	public void deleteCustomer(String accountID) {
		customers.delete(accountID);
	}

	public void deleteMerchant(String accountID) {
		merchants.delete(accountID);
	}

	public boolean verifyCustomer(String accountID){
		return customers.verify(accountID);
	}
}
