package dtu.group2.Presentation.Resources;

import dtu.group2.Application.AccountServiceServer;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankServiceException_Exception;
import messaging.Event;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;

public class AccountMessageService {
    private MessageQueue messageQueue;
    private CompletableFuture<Account> accountPending;
    private AccountServiceServer accountServiceServer;

    public AccountMessageService(MessageQueue messageQueue, AccountServiceServer ass){
        this.messageQueue = messageQueue;
        this. accountServiceServer = ass;
    }

    // Send
    public Account sendCustomer(Account customer){
        accountPending = new CompletableFuture<>();
        Event event = new Event("getCustomer", new Object[] {customer});
        messageQueue.publish(event);
        return accountPending.join();
    }

    // Recieve
    public void getCustomer(Event e) throws BankServiceException_Exception {
       String id = e.getArgument(0, String.class);
       Account customer = accountServiceServer.GetCustomer(id);
       messageQueue.publish(new Event("Customer", new Object[] {customer} ));
    }
}
