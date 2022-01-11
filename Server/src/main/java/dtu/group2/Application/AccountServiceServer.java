package dtu.group2.Application;

import java.math.BigDecimal;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;

public class AccountServiceServer {

	BankService bank = new BankServiceService().getBankServicePort();
	
	public AccountServiceServer() { }


	public String CreateAccount(User user, BigDecimal balance) throws BankServiceException_Exception {
		return bank.createAccountWithBalance(user, balance);
	}


	public Account GetAccount(String accountID) throws BankServiceException_Exception {
		return bank.getAccount(accountID);
	}


	public void DeleteAccount(String accountID) throws BankServiceException_Exception {
		bank.retireAccount(accountID);
	}


}
