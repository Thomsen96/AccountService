package dtu.group2.Application;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

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

	public String createCustomer(String id) throws BankServiceException_Exception {
		String customerId = UUID.randomUUID().toString();
		customers.create(customerId, bank.getAccount(id));
		return customerId;
	}

	public String createMerchant(String id) throws BankServiceException_Exception {
		String merchantId = UUID.randomUUID().toString();
		merchants.create(merchantId, bank.getAccount(id));
		return merchantId;
	}

	public Account getCustomer(String accountId) {
		return customers.getAccount(accountId);
	}

	public Account getMerchant(String accountId) {
		return merchants.getAccount(accountId);
	}

	public String getCustomerId(String customerId) {
		Account customerAccount = customers.getAccount(customerId);
		if(customerAccount != null) return customerAccount.getId();
		else return "Couldn't find customer";
//		return customers.getAccount(customerId).getId();
	}

	public String getMerchantId(String merchantId) {
		Account merchantAccount = merchants.getAccount(merchantId);
		if(merchantAccount != null) return merchantAccount.getId();
		else return "Couldn't find merchant";
//		return merchants.getAccount(merchantId).getId();
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

	public String getStatus() {
		return "Sanitity check for account service";
	}
}
