package dtu.group2.Presentation.Resources;

import dtu.group2.Application.AccountService;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankServiceException_Exception;
import messaging.Event;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;

public class AccountEventHandler {
	
    private MessageQueue messageQueue;
    
    private CompletableFuture<Account> accountPending;
    private final AccountService accountService;

    public AccountEventHandler(MessageQueue messageQueue, AccountService ass) {
        this.messageQueue = messageQueue;
        this.accountService = ass;

        this.messageQueue.addHandler("AccountStatusResponse", this::handleAccountStatusRequest);

        this.messageQueue.addHandler("GetCustomer", this::handleGetCustomer);
        this.messageQueue.addHandler("GetMerchant", this::handleGetMerchant);

        this.messageQueue.addHandler("CustomerVerificationRequested", this::handleCustomerVerificationRequest);
        this.messageQueue.addHandler("MerchantVerificationRequested", this::handleMerchantVerificationRequest);

        this.messageQueue.addHandler("CustomerVerificationRequested", this::handleCustomerVerificationRequest);
        this.messageQueue.addHandler("CustomerVerificationRequested", this::handleCustomerVerificationRequest);

        this.messageQueue.addHandler("CustomerCreationRequested", this::createCustomerAccountRequest);
        this.messageQueue.addHandler("MerchantCreationRequested", this::createMerchantAccountRequest);


    }

    private void handleGetMerchant(Event event) {
    }

    public void handleAccountStatusRequest(Event e) {
        System.out.println("Received a request to send back to status the service");
        Event event = new Event("AccountStatusResponse", new Object[] {accountService.getStatus()});
        messageQueue.publish(event);
    }

    public void createCustomerAccountRequest(Event event) {
        String customerId = event.getArgument(0, String.class);
        String sessionId = event.getArgument(1, String.class);
        try{
            accountService.createCustomer(customerId);
        } catch (Exception e){
            e.printStackTrace();
        }
        Event response = new Event("MerchantCreationResponse." + sessionId, new Object[]{customerId});;
        if(accountService.getMerchant(customerId) == null) {
            response = new Event("MerchantCreationResponse." + sessionId, new Object[]{"AN ERROR HAS OCCURED - COULD NOT CREATE CUSTOMER"});
        }
        messageQueue.publish(response);
    }

    public void createMerchantAccountRequest(Event event) {
        String merchantId = event.getArgument(0, String.class);
        String sessionId = event.getArgument(1, String.class);
        try{
            accountService.createMerchant(merchantId);
        } catch (Exception e){
            e.printStackTrace();
        }
        Event response = new Event("MerchantCreationResponse." + sessionId, new Object[]{merchantId});;
        if(accountService.getMerchant(merchantId) == null) {
            response = new Event("MerchantCreationResponse." + sessionId, new Object[]{"AN ERROR HAS OCCURED - COULD NOT CREATE MERCHANT"});
        }
        messageQueue.publish(response);
    }

    public void handleCustomerVerificationRequest(Event event)  {
        String id = event.getArgument(0, String.class);
        Event response = new Event("CustomerVerificationResponse", new Object[] { accountService.verifyCustomer(id) } );
        messageQueue.publish(response);
    }

    public void handleMerchantVerificationRequest(Event event) {
        String id = event.getArgument(0, String.class);
        Event response = new Event("MerchantVerificationResponse", new Object[] { accountService.verifyMerchant(id) } );
        messageQueue.publish(response);
    }

    public void handleGetCustomer(Event e) {
        try {
            String id = e.getArgument(0, String.class);
            Account customer = accountService.getCustomer(id);
            Event event = new Event("ResponseCustomer", new Object[]{customer});
            messageQueue.publish(event);
        } catch(Exception ex){

        }
    }
}
