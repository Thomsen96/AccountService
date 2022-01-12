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

	public String CreateCustomer(String uid) throws BankServiceException_Exception {
		customers.put(uid, bank.getAccount(uid));
		return uid;
	}

	public String CreateMerchant(String uid) throws BankServiceException_Exception {
		merchants.put(uid, bank.getAccount(uid));
		return uid;
	}

	public Account GetCustomer(String accountID) {
		return customers.get(accountID);
	}

	public Account GetMerchant(String accountID) {
		return merchants.get(accountID);
	}

	public void DeleteCustomer(String accountID) {
		customers.remove(accountID);
	}

	public void DeleteMerchant(String accountID) throws BankServiceException_Exception {
		bank.retireAccount(accountID);
		merchants.remove(accountID);
	}

	public boolean verifyCustomer(String accountID){
		return customers.get(accountID) != null;
	}

}
