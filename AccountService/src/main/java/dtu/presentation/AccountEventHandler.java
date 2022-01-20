package dtu.presentation;

import dtu.application.AccountService;
import dtu.domain.DTO;
import dtu.ws.fastmoney.Account;
import messaging.Event;
import messaging.EventResponse;
import messaging.MessageQueue;

public class AccountEventHandler {
	
    private MessageQueue messageQueue;

    private final AccountService accountService;

    public AccountEventHandler(MessageQueue messageQueue, AccountService as) {
        this.messageQueue = messageQueue;
        this.accountService = as;

        this.messageQueue.addHandler("AccountStatusRequest", this::handleAccountStatusRequest);

        this.messageQueue.addHandler("GetCustomer", this::handleGetCustomer);
        this.messageQueue.addHandler("GetMerchant", this::handleGetMerchant);

        this.messageQueue.addHandler("CustomerVerificationRequest", this::handleCustomerVerificationRequest);
        this.messageQueue.addHandler("MerchantVerificationRequest", this::handleMerchantVerificationRequest);

        this.messageQueue.addHandler("CustomerCreationRequest", this::createCustomerAccountRequest);
        this.messageQueue.addHandler("MerchantCreationRequest", this::createMerchantAccountRequest);

        this.messageQueue.addHandler("MerchantIdToAccountNumberRequest", this::handleMerchantIdToAccountNumberRequest);
        this.messageQueue.addHandler("CustomerIdToAccountNumberRequest", this::handleCustomerIdToAccountNumberRequest);
    }

    public void handleAccountStatusRequest(Event e) {
        System.out.println("Received a request to send back to status the service");
        var eventRes = e.getArgument(0, EventResponse.class);
        String sessionId = eventRes.getSessionId();
        EventResponse eventResponse = new EventResponse(sessionId, true, null, accountService.getStatus());
        Event event = new Event("AccountStatusResponse." + sessionId, eventResponse);
        messageQueue.publish(event);
    }

    public void createCustomerAccountRequest(Event event) {
        System.out.println("Creating customer in DTU Pay");
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String accountNumber = res.getArgument(0, DTO.CreateAccount.class).accountId;
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
        Event response = new Event("CustomerCreationResponse." + sessionId, eventResponse);
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
        Event response = new Event("MerchantCreationResponse." + sessionId, eventResponse);
        messageQueue.publish(response);
    }

    public void handleCustomerVerificationRequest(Event event)  {
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String id = res.getArgument(0, String.class);
        EventResponse eventResponse = new EventResponse(sessionId, false, "Customer is not verified");
        if(accountService.verifyCustomer(id)){
            eventResponse = new EventResponse(sessionId, true, null);
        }
        Event response = new Event("CustomerVerificationResponse." + sessionId, eventResponse );
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
        Event response = new Event("MerchantVerificationResponse." + sessionId, eventResponse );
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
        Event event = new Event("ResponseCustomer." + sessionId, eventResponse);
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
        Event event = new Event("ResponseMerchant."+sessionId, eventResponse);
        messageQueue.publish(event);
    }

    public void handleMerchantIdToAccountNumberRequest(Event event) {
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String id = res.getArgument(0, String.class);
        String merchantAccountId = accountService.getMerchantBankAccountId(id);
        EventResponse eventResponse = new EventResponse(sessionId, true, null, merchantAccountId);
        if(merchantAccountId == null){
            eventResponse = new EventResponse(sessionId, false, "No merchant exists with the provided id");
        }
        Event response = new Event("MerchantIdToAccountNumberResponse." + sessionId, eventResponse);
        messageQueue.publish(response);
    }

    public void handleCustomerIdToAccountNumberRequest(Event event) {
        var res = event.getArgument(0, EventResponse.class);
        String sessionId = res.getSessionId();
        String id = res.getArgument(0, String.class);
        String customerAccountId = accountService.getCustomerBankAccountId(id);
        EventResponse eventResponse = new EventResponse(sessionId, true, null, customerAccountId);
        if(customerAccountId == null){
            eventResponse = new EventResponse(sessionId, false, "No customer exists with the provided id");
        }
        Event response = new Event("CustomerIdToAccountNumberResponse." + sessionId, eventResponse);
        messageQueue.publish(response);
    }
}
