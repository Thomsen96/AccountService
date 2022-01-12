package dtu.group2.Application;

import java.math.BigDecimal;
import java.util.HashMap;

import dtu.ws.fastmoney.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class AccountServiceServer {

	private static final String EXCHANGE_NAME = "pub-sub-queue";
	private static final String ROUTING_KEY = "account.*";
	
	BankService bank = new BankServiceService().getBankServicePort();

	private HashMap<String,Account> merchants = new HashMap<>();
	private HashMap<String,Account> customers = new HashMap<>();
	
	// Create queue 
	private Channel channel;
	
	public void SetMessageQueue(Channel queue) {
		channel = queue;
	}
	
	
	public AccountServiceServer() {
		try {
			ConnectionFactory mqFactory = new ConnectionFactory();
			mqFactory.setHost("localhost");
			Connection connection = mqFactory.newConnection();

			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	        String queueName = channel.queueDeclare().getQueue();
	        channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);

	        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	            String message = new String(delivery.getBody(), "UTF-8");
	            System.out.println("Received: " + message);
	        };
	        
	        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
