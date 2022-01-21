package dtu.presentation;

import dtu.application.AccountService;
import dtu.ws.fastmoney.Account;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

import static messaging.GLOBAL_STRINGS.ACCOUNT_SERVICE.HANDLE.*;
import static messaging.GLOBAL_STRINGS.ACCOUNT_SERVICE.PUBLISH.*;
import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.PUBLISH.CUSTOMER_RESPONDED;
import static messaging.GLOBAL_STRINGS.TOKEN_SERVICE.PUBLISH.CUSTOMER_VERIFICATION_RESPONDED;

public class AccountEventHandler {
	
    private MessageQueue messageQueue;

    private final AccountService accountService;

    public AccountEventHandler(MessageQueue messageQueue, AccountService as) {
        this.messageQueue = messageQueue;
        this.accountService = as;

        this.messageQueue.addHandler(ACCOUNT_STATUS_REQUESTED, this::handleAccountStatusRequest);

        this.messageQueue.addHandler(GET_CUSTOMER, this::handleGetCustomer);
        this.messageQueue.addHandler(GET_MERCHANT, this::handleGetMerchant);

        this.messageQueue.addHandler(CUSTOMER_VERIFICATION_REQUESTED, this::handleCustomerVerificationRequest);
        this.messageQueue.addHandler(MERCHANT_VERIFICATION_REQUESTED, this::handleMerchantVerificationRequest);

        this.messageQueue.addHandler(CUSTOMER_CREATION_REQUESTED, this::createCustomerAccountRequest);
        this.messageQueue.addHandler(MERCHANT_CREATION_REQUESTED, this::createMerchantAccountRequest);

        this.messageQueue.addHandler(MERCHANT_ID_TO_ACCOUNT_NUMBER_REQUESTED, this::handleMerchantIdToAccountNumberRequest);
        this.messageQueue.addHandler(CUSTOMER_ID_TO_ACCOUNT_NUMBER_REQUESTED, this::handleCustomerIdToAccountNumberRequest);
    }

    public void handleAccountStatusRequest(Event e) {
        System.out.println("Received a request to send back to status the service");
        var eventRes = e.getArgument(0, EventResponse.class);
        String sessionId = eventRes.getSessionId();
        EventResponse eventResponse = new EventResponse(sessionId, true, null, accountService.getStatus());
        Event event = new Event(ACCOUNT_STATUS_RESPONDED + sessionId, eventResponse);
        messageQueue.publish(event);
    }

    public void createCustomerAccountRequest(Event event) {
        System.out.println("Creating customer in DTU Pay");
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String accountNumber = res.getArgument(0, String.class);
        String userId = null;
        System.out.println("Handling createCustomerAccountRequest with accountNumber: " + accountNumber);
        try {
        	userId = accountService.createCustomer(accountNumber);
        	System.out.println("Customer created with userId " + userId);
        } catch (Exception e) { e.printStackTrace(); }
        EventResponse eventResponse = new EventResponse(sessionId, true, null, userId);
        if(accountService.getCustomer(userId) == null) {
        	System.out.println("The customer with id " + userId + " was not created properly");
            eventResponse = new EventResponse(sessionId, false, "AN ERROR HAS OCCURED - COULD NOT CREATE CUSTOMER");
        }
        Event response = new Event(CUSTOMER_CREATION_RESPONDED + sessionId, eventResponse);
        messageQueue.publish(response);
    }

    public void createMerchantAccountRequest(Event event) {
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String accountNumber = res.getArgument(0, String.class);
        String userId = "";
        try{ 
        	userId = accountService.createMerchant(accountNumber);
        	System.out.println("Merchant created with userId " + userId);
        } catch (Exception e) { e.printStackTrace(); }
        EventResponse eventResponse = new EventResponse(sessionId, true, null, userId);
        if(accountService.getMerchant(userId) == null) {
        	System.out.println("The merchant with id " + userId + " was not created properly");
            eventResponse = new EventResponse(sessionId, false, "AN ERROR HAS OCCURED - COULD NOT CREATE MERCHANT");
        }
        Event response = new Event(MERCHANT_CREATION_RESPONDED + sessionId, eventResponse);
        messageQueue.publish(response);
    }



    public void handleMerchantVerificationRequest(Event event) {
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String id = res.getArgument(0, String.class);
        EventResponse eventResponse = new EventResponse(sessionId, false, "Merchant is not verified");
        if(accountService.verifyMerchant(id)){
            eventResponse = new EventResponse(sessionId, true, null);
        }
        Event response = new Event(MERCHANT_VERIFICATION_RESPONDED + sessionId, eventResponse );
        messageQueue.publish(response);
    }

    public void handleGetCustomer(Event e) {
        var res = e.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String id = res.getArgument(0, String.class);
        Account customer = accountService.getCustomer(id);
        EventResponse eventResponse = new EventResponse(sessionId, true, "No customer exists with that id");
        if(customer != null){
            eventResponse = new EventResponse(sessionId, true, null, customer);
        }
        Event event = new Event(CUSTOMER_RESPONDED + sessionId, eventResponse);
        System.out.println("handler: " + event.getArgument(0, EventResponse.class));
        messageQueue.publish(event);
    }

    private void handleGetMerchant(Event e) {
        var res = e.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String id = res.getArgument(0, String.class);
        Account merchant = accountService.getMerchant(id);
        EventResponse eventResponse = new EventResponse(sessionId, true, "No merchant exists with that id");
        if(merchant != null){
            eventResponse = new EventResponse(sessionId, true, null, merchant);
        }
        Event event = new Event(MERCHANT_RESPONDED+sessionId, eventResponse);
        messageQueue.publish(event);
    }
    
    public void handleCustomerVerificationRequest(Event event)  {
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String id = res.getArgument(0, String.class);
        EventResponse eventResponse = new EventResponse(sessionId, false, "Customer is not verified");
        if(accountService.verifyCustomer(id)){
            eventResponse = new EventResponse(sessionId, true, null);
        }
        Event response = new Event(CUSTOMER_VERIFICATION_RESPONDED + sessionId, eventResponse );
        messageQueue.publish(response);
    }

    public void handleMerchantIdToAccountNumberRequest(Event event) {
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String id = res.getArgument(0, String.class);
        System.out.println("Incoming merchantId: " + id);
        String merchantAccountId = accountService.getMerchantBankAccountId(id);
        System.out.println("merchantAccountId: " + merchantAccountId);
        EventResponse eventResponse = new EventResponse(sessionId, true, null, merchantAccountId);
        if(merchantAccountId == null){
            eventResponse = new EventResponse(sessionId, false, "No merchant exists with the provided id");
        }
        Event response = new Event(MERCHANT_TO_ACCOUNT_NUMBER_RESPONDED + sessionId, eventResponse);
        messageQueue.publish(response);
    }

    public void handleCustomerIdToAccountNumberRequest(Event event) {
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String id = res.getArgument(0, String.class);
        System.out.println("Incoming customerId: " + id);
        String customerAccountId = accountService.getCustomerBankAccountId(id);
        System.out.println("customerAccountId: " + customerAccountId);
        EventResponse eventResponse = new EventResponse(sessionId, true, null, customerAccountId);
        if(customerAccountId == null){
            eventResponse = new EventResponse(sessionId, false, "No customer exists with the provided id");
        }
        Event response = new Event(CUSTOMER_TO_ACCOUNT_NUMBER_RESPONDED + sessionId, eventResponse);
        messageQueue.publish(response);
    }
}
