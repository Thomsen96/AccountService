package dtu.group2.Application;

import java.math.BigDecimal;
import java.util.HashMap;

import dtu.ws.fastmoney.*;

public class AccountServiceServer {
	
	BankService bank = new BankServiceService().getBankServicePort();

	private static HashMap<String,Account> merchants = new HashMap<>();
	private static HashMap<String,Account> customers = new HashMap<>();
	
	
	public AccountServiceServer() {

	}

	public String createCustomer(String uid) throws BankServiceException_Exception {
		customers.put(uid, bank.getAccount(uid));
		return uid;
	}

	public String createMerchant(String uid) throws BankServiceException_Exception {
		merchants.put(uid, bank.getAccount(uid)); 
		return uid;
	}

	public Account getCustomer(String accountID) {
		return customers.get(accountID);
	}

	public Account getMerchant(String accountID) {
		return merchants.get(accountID);
	}

	public void deleteCustomer(String accountID) {
		customers.remove(accountID);
	}

	public void deleteMerchant(String accountID) {
		merchants.remove(accountID);
	}

	public boolean verifyCustomer(String accountID){
		return customers.get(accountID) != null;
	}
}
