package dtu.group2.Application;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dtu.group2.Infrastructure.Repositories.LocalCustomerIRepository;
import dtu.group2.Infrastructure.Repositories.LocalMerchantIRepository;
import dtu.ws.fastmoney.*;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class AccountServiceServer {

	BankService bank = new BankServiceService().getBankServicePort();

	private HashMap<String,Account> merchants = new HashMap<>();
	private HashMap<String,Account> customers = new HashMap<>();
	
	// Create queue 
	private Channel msgQueue;
	
	public void SetMessageQueue(Channel queue) {
		msgQueue = queue;
	}
	
	
	public AccountServiceServer() throws Exception {
//		ConnectionFactory mqFactory = new ConnectionFactory();
//		mqFactory.setHost("rabbitmq.server.dtu.dk");
//		Connection connection = mqFactory.newConnection();
//		msgQueue = connection.createChannel();
	}

	public String CreateCustomer(User user, BigDecimal balance) throws BankServiceException_Exception {
		String uid = bank.createAccountWithBalance(user, balance);
		customers.put(uid, bank.getAccount(uid));
		return uid;
	}

	public String CreateMerchant(User user, BigDecimal balance) throws BankServiceException_Exception {
		String uid = bank.createAccountWithBalance(user, balance);
		merchants.put(uid, bank.getAccount(uid));
		return uid;
	}


	public Account GetCustomer(String accountID) throws BankServiceException_Exception {
		if(customers.get(accountID) != null) {
			return customers.get(accountID);
		} else {
			throw new BankServiceException_Exception("Account does not exist", new BankServiceException());
		}
	}

	public Account GetMerchant(String accountID) throws BankServiceException_Exception {
		if(merchants.get(accountID) != null) {
			return merchants.get(accountID);
		} else {
			throw new BankServiceException_Exception("Account does not exist", new BankServiceException());
		}
	}

	public void DeleteCustomer(String accountID) throws BankServiceException_Exception {
		try {
			bank.retireAccount(accountID);
		}
		catch(Exception e){
			throw e;
		}
		finally {
			customers.remove(accountID);
		}
	}

	public void DeleteMerchant(String accountID) throws BankServiceException_Exception {
		bank.retireAccount(accountID);
		merchants.remove(accountID);
	}

}
