package dtu.group2;

import dtu.application.AccountService;
import dtu.infrastructure.AccountRepository;
import dtu.presentation.AccountEventHandler;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.en.*;
import messaging.Event;
import messaging.EventResponse;
import messaging.implementations.MockMessageQueue;

import static messaging.GLOBAL_STRINGS.ACCOUNT_SERVICE.HANDLE.*;
import static messaging.GLOBAL_STRINGS.ACCOUNT_SERVICE.PUBLISH.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class HandlerSteps {
	
    private static AccountService accountService = new AccountService(new AccountRepository(), new AccountRepository());
    static MockMessageQueue messageQueue = new MockMessageQueue();
    AccountEventHandler messageService = new AccountEventHandler(messageQueue, accountService);
    BankService bank = new BankServiceService().getBankServicePort();

    String uid;
    Event event;
    EventResponse eventResponse;
    Event outgoingEvent;
    
	@Given("an AccountStatusRequest event is received")
	public void anAccountStatusRequestEvent() {
		uid = UUID.randomUUID().toString();
		EventResponse er = new EventResponse(uid, true, null, null);
		er.setSessionId(uid);;
		event = new Event(ACCOUNT_STATUS_REQUESTED, er);
		
		messageService.handleAccountStatusRequest(event);
	}
	
	@Given("an invalid createCustomerAccountRequest event is received")
	public void anInvalidCreateCustomerAccountRequestEvent() {
		uid = UUID.randomUUID().toString();
		EventResponse er = new EventResponse(uid, true, null, null);
		er.setSessionId(uid);
		event = new Event(CUSTOMER_CREATION_REQUESTED, er);
		
		messageService.createCustomerAccountRequest(event);
	}
	
	@Given("an invalid createMerchantAccountRequest event is received")
	public void anInvalidCreateMerchantAccountRequestEvent() {
		uid = UUID.randomUUID().toString();
		EventResponse er = new EventResponse(uid, true, null, null);
		er.setSessionId(uid);
		event = new Event(CUSTOMER_CREATION_REQUESTED, er);
		
		messageService.createMerchantAccountRequest(event);
	}
	

	@Then("an AccountStatusResponse is sent")
	public void anAccountStatusResponseIsSent() {
		outgoingEvent = messageQueue.getEvent(ACCOUNT_STATUS_RESPONDED + uid);
		assertNotNull(outgoingEvent);
	}

	@Then("a createCustomerAccountRequest is sent")
	public void aCreateCustomerAccountRequestIsSent() {
	    outgoingEvent = messageQueue.getEvent(CUSTOMER_CREATION_RESPONDED + uid);
		assertNotNull(outgoingEvent);
	}

	@Then("a createMerchantAccountResponse is sent")
	public void aCreateMerchantAccountResponseIsSent() {
	    outgoingEvent = messageQueue.getEvent(MERCHANT_CREATION_RESPONDED+ uid);
		assertNotNull(outgoingEvent);
	}

	
	@Then("the event response in the message was success {string}")
	public void theEventResponseInTheMessageWasSuccess(String string) throws InterruptedException {
		boolean success = Boolean.parseBoolean(string);
		
		var a = outgoingEvent.getArgument(0, EventResponse.class);
		boolean didItGo = a.isSuccess();
		System.out.println("Test: " + (success ? "true" : "false") + " == " + (didItGo? "true" : "false"));

		if(success) {
			assertTrue(true);
		}
		
		assertEquals(success, didItGo);
	}
	
}
