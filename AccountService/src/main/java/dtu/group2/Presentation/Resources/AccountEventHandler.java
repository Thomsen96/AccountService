package dtu.group2.Presentation.Resources;

import dtu.group2.Application.AccountService;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankServiceException_Exception;
import messaging.Event;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;

public class AccountEventHandler {
	
    private MessageQueue messageQueue;

    private final AccountService accountService;

    public AccountEventHandler(MessageQueue messageQueue, AccountService as) {
        this.messageQueue = messageQueue;
        this.accountService = as;

        this.messageQueue.addHandler("AccountStatusRequest", this::handleAccountStatusRequest);

        this.messageQueue.addHandler("GetCustomer", this::handleGetCustomer);
        this.messageQueue.addHandler("GetMerchant", this::handleGetMerchant);

        this.messageQueue.addHandler("CustomerVerificationRequested", this::handleCustomerVerificationRequest);
        this.messageQueue.addHandler("MerchantVerificationRequested", this::handleMerchantVerificationRequest);

        this.messageQueue.addHandler("CustomerCreationRequested", this::createCustomerAccountRequest);
        this.messageQueue.addHandler("MerchantCreationRequested", this::createMerchantAccountRequest);

        this.messageQueue.addHandler("MerchantIdToAccountNumberRequest", this::handleMerchantIdToAccountNumberRequest);
        this.messageQueue.addHandler("CustomerIdToAccountNumberRequest", this::handleCustomerIdToAccountNumberRequest);
    }

    public void handleAccountStatusRequest(Event e) {
        System.out.println("Received a request to send back to status the service");
        String sessionId = e.getArgument(0, String.class);
        Event event = new Event("AccountStatusResponse", new Object[] {accountService.getStatus(), sessionId});
        messageQueue.publish(event);
    }

    public void createCustomerAccountRequest(Event event) {
        String sessionId = event.getArgument(0, String.class);
        String customerId = event.getArgument(1, String.class);
        try{
            accountService.createCustomer(customerId);
        } catch (Exception e){
            e.printStackTrace();
        }
        Event response = new Event("CustomerCreationResponse." + sessionId, new Object[]{sessionId, customerId});;
        if(accountService.getMerchant(customerId) == null) {
            response = new Event("CustomerCreationResponse." + sessionId, new Object[]{sessionId, "AN ERROR HAS OCCURED - COULD NOT CREATE CUSTOMER"});
        }
        messageQueue.publish(response);
    }

    public void createMerchantAccountRequest(Event event) {
        String sessionId = event.getArgument(0, String.class);
        String merchantId = event.getArgument(1, String.class);
        try{
            accountService.createMerchant(merchantId);
        } catch (Exception e){
            e.printStackTrace();
        }
        Event response = new Event("MerchantCreationResponse." + sessionId, new Object[]{sessionId, merchantId});;
        if(accountService.getMerchant(merchantId) == null) {
            response = new Event("MerchantCreationResponse." + sessionId, new Object[]{sessionId, "AN ERROR HAS OCCURED - COULD NOT CREATE MERCHANT"});
        }
        messageQueue.publish(response);
    }

    public void handleCustomerVerificationRequest(Event event)  {
        String sessionId = event.getArgument(0, String.class);
        String id = event.getArgument(1, String.class);
        Event response = new Event("CustomerVerificationResponse." + sessionId, new Object[] { sessionId, accountService.verifyCustomer(id) } );
        messageQueue.publish(response);
    }

    public void handleMerchantVerificationRequest(Event event) {
        String sessionId = event.getArgument(0, String.class);
        String id = event.getArgument(1, String.class);
        Event response = new Event("MerchantVerificationResponse." + sessionId, new Object[] { sessionId, accountService.verifyMerchant(id) } );
        messageQueue.publish(response);
    }

    public void handleGetCustomer(Event e) {
        try {
            String sessionId = e.getArgument(0, String.class);
            String id = e.getArgument(1, String.class);
            Account customer = accountService.getCustomer(id);
            Event event = new Event("ResponseCustomer."+sessionId, new Object[]{sessionId, customer});
            messageQueue.publish(event);
        } catch(Exception ex){

        }
    }

    private void handleGetMerchant(Event e) {
        try {
            String sessionId = e.getArgument(0, String.class);
            String id = e.getArgument(1, String.class);
            Account merchant = accountService.getMerchant(id);
            Event event = new Event("ResponseMerchant."+sessionId, new Object[]{sessionId, merchant});
            messageQueue.publish(event);
        } catch(Exception ex){

        }
    }

    public void handleMerchantIdToAccountNumberRequest(Event event) {
        String sessionId = event.getArgument(0, String.class);
        String id = event.getArgument(1, String.class);
        String merchantAccountId = accountService.getMerchantId(id);
        Event response = new Event("MerchantIdToAccountNumberResponse." + sessionId, new Object[]{sessionId, true, merchantAccountId});
        if(merchantAccountId == null){
            response = new Event("MerchantIdToAccountNumberResponse." + sessionId, new Object[]{sessionId, false, "No merchant exists with the provided id"});
        }
        messageQueue.publish(response);
    }

    public void handleCustomerIdToAccountNumberRequest(Event event) {
        String sessionId = event.getArgument(0, String.class);
        String id = event.getArgument(1, String.class);
        String customerAccountId = accountService.getCustomerId(id);
        Event response = new Event("CustomerIdToAccountNumberResponse." + sessionId, new Object[]{sessionId, true, customerAccountId});
        if(customerAccountId == null){
            response = new Event("CustomerIdToAccountNumberResponse." + sessionId, new Object[]{sessionId, false, "No customer exists with the provided id"});
        }
        messageQueue.publish(response);
    }
}
