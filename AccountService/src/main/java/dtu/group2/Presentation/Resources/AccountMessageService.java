package dtu.group2.Presentation.Resources;

import dtu.group2.Application.AccountServiceServer;
import dtu.ws.fastmoney.Account;
import messaging.Event;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;

public class AccountMessageService {
	
    private MessageQueue messageQueue;
    
    private CompletableFuture<Account> accountPending;
    private AccountServiceServer accountServiceServer;

    public AccountMessageService(MessageQueue messageQueue, AccountServiceServer ass){
        this.messageQueue = messageQueue;
        this.accountServiceServer = ass;
        this.messageQueue.addHandler("GetCustomer", this::handleGetCustomer);
        this.messageQueue.addHandler("CustomerVerificationRequested", this::handleVerifyCustomer);
    }

    public void handleVerifyCustomer(Event event) {
        String id = event.getArgument(0, String.class);
        Event respEvent = new Event("CustomerVerified", new Object[] { accountServiceServer.verifyCustomer(id) } );
        messageQueue.publish(respEvent);
    }

    public void handleGetCustomer(Event e) {
        try {
            String id = e.getArgument(0, String.class);
            Account customer = accountServiceServer.getCustomer(id);
            Event event = new Event("ResponseCustomer", new Object[]{customer});
            messageQueue.publish(event);
        } catch(Exception ex){

        }
    }

}
