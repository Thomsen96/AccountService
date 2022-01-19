package dtu.group2;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.UUID;

import dtu.application.AccountService;
import dtu.presentation.AccountEventHandler;
import dtu.infrastructure.AccountRepository;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.EventResponse;
import messaging.implementations.MockMessageQueue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AccountServiceSteps {

    User user;
    String balance;
    String accountID;
    String userId;
    Exception exception;
    String status;
    String sessionId = UUID.randomUUID().toString();

//    private CompletableFuture<String> statusMessage = new CompletableFuture<>();

    private static AccountService accountService = new AccountService(new AccountRepository(), new AccountRepository());
    static MockMessageQueue mq = new MockMessageQueue();
    AccountEventHandler messageService = new AccountEventHandler(mq, accountService);
    BankService bank = new BankServiceService().getBankServicePort();

    @After
    public void cleanUsersAfterRun() {
        try {
            bank.retireAccount(accountID);
        } catch (Exception e) {

        }
    }

    @Given("a user with first name {string}, last name {string} and cpr number {string} and balance of {string}")
    public void aCustomerWithFirstNameLastNameAndCprNumberAndBalanceOf(String firstName, String lastName, String cprNumber, String balance) {
        user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCprNumber(cprNumber);
        this.balance = balance;
        try {
            this.accountID = bank.createAccountWithBalance(user, new BigDecimal(balance));
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("a customer tries to create an account")
    public void aCustomerTriesToCreateAnAccount() throws BankServiceException_Exception {
        this.userId = accountService.createCustomer(accountID);
    }

    @When("a merchant tries to create an account")
    public void aMerchantTriesToCreateAnAccount() throws BankServiceException_Exception {
        this.userId = accountService.createMerchant(accountID);
    }

    @Then("a customer account exists with that accountID")
    public void aCustomerAccountExistsWithThatAccountID() {
        assertNotNull(accountService.getCustomer(userId));
        assertEquals(accountService.getCustomerId(userId), accountID);
    }

    @Then("a merchant account exists with that accountID")
    public void aMerchantAccountExistsWithThatAccountID() {
        assertNotNull(accountService.getMerchant(userId));
        assertEquals(accountService.getMerchantId(userId), accountID);
    }

    @Given("a user with no first name, last name or cpr number")
    public void aUserWithNoFirstNameLastNameOrCprNumber() {
        user = new User();
    }

    @Then("the customer no longer exists")
    public void theCustomerNoLongerExists() {
        assertNull(accountService.getCustomer(userId));
    }

    @When("the customer account is deleted")
    public void theAccountWithAccountIDIsDeleted() {
        accountService.deleteCustomer(userId);
    }

    @And("the merchant account is deleted")
    public void theMerchantAccountIsDeleted() {
        accountService.deleteMerchant(userId);
    }

    @Then("the merchant no longer exists")
    public void theMerchantNoLongerExists() {
        assertNull(accountService.getMerchant(userId));
    }

    @When("the customer account is fetched")
    public void theAccountIsFetched() {
        try {
            accountService.getCustomer(userId);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Given("user ID {string}")
    public void accountID(String userId) {
        this.userId = userId;
    }
    
    @Then("a messagequeue message is produced")
    public void aMessagequeueMessageIsProduced() {
        EventResponse eventResponse = new EventResponse(sessionId, true, null, userId);
		Event event = new Event("GetCustomer", eventResponse);
		mq.publish(event);
    }

    @When("a request is received")
    public void aRequestIsReceived() {
        EventResponse eventResponse = new EventResponse(sessionId, true, null, userId);
        Event e = new Event("GetCustomer", eventResponse );
        messageService.handleGetCustomer(e);
    }

    @Then("a uid is received and customer returned")
    public void aUidIsReceived() {
        Account customer = accountService.getCustomer(userId);
        EventResponse eventResponse = new EventResponse(sessionId, true, null, customer);
        Event event = new Event("ResponseCustomer." + sessionId, eventResponse);
        System.out.println("Test: " + event.getArgument(0, EventResponse.class));
        assertEquals(mq.getEvent("ResponseCustomer." + sessionId), event);
//        verify(mq).publish(event);
    }

    @When("a request is received for verification")
    public void aRequestIsReceivedForVerification() {
        EventResponse eventResponse = new EventResponse(sessionId, true, null, userId);
        Event e = new Event("CustomerVerificationRequested", eventResponse);
        messageService.handleCustomerVerificationRequest(e);
    }

    @Then("a uid is received and verification of the custumer is returned")
    public void aUidIsReceivedAndVerificationOfTheCustumerIsReturned() {
        EventResponse eventResponse;
        if(accountService.verifyCustomer(userId)){
            eventResponse = new EventResponse(sessionId, true, null);
        } else {
            eventResponse = new EventResponse(sessionId, false, "Customer is not verified");
        }
        Event e = new Event("CustomerVerificationResponse." + sessionId, eventResponse);
        assertEquals(mq.getEvent("CustomerVerificationResponse." + sessionId), e);
//        mq.verify(mq).publish(e);
    }

    @Then("a uid is received and merchant returned")
    public void aUidIsReceivedAndMerchantReturned() {
        assertNotNull(accountService.getMerchant(userId));
        assertEquals(accountService.getMerchantId(userId), accountID);
    }

    @When("the account service is requested for its status")
    public void theAccountServiceIsRequestedForItsStatus() {
        this.status = accountService.getStatus();
    }

    @Then("the status message is {string}")
    public void theStatusMessageIs(String expectedStatus) {
        assertEquals(this.status, expectedStatus);
    }

    @When("the account services is requested for a merchant accountNumber")
    public void theAccountServicesIsRequestedForAMerchantAccountNumber() {
        EventResponse eventResponse = new EventResponse(sessionId, true, null, userId);
        Event e = new Event("MerchantIdToAccountNumberRequest", eventResponse);
        messageService.handleMerchantIdToAccountNumberRequest(e);
    }

    @Then("the merchant accountNumber is placed back on the queue")
    public void theMerchantAccountNumberIsPlacedBackOnTheQueue() {
        EventResponse eventResponse = new EventResponse(sessionId, true, null, accountID);
        Event e = new Event("MerchantIdToAccountNumberResponse." + sessionId, eventResponse);
        assertEquals(mq.getEvent("MerchantIdToAccountNumberResponse." + sessionId), e);
//        verify(mq).publish(e);
    }

    @When("the account services is requested for a customer accountNumber")
    public void theAccountServicesIsRequestedForACustomerAccountNumber() {
        EventResponse eventResponse = new EventResponse(sessionId, true, null, userId);
        Event e = new Event("CustomerIdToAccountNumberRequest", eventResponse);
        messageService.handleCustomerIdToAccountNumberRequest(e);
    }

    @Then("the customer accountNumber is placed back on the queue")
    public void theCustomerAccountNumberIsPlacedBackOnTheQueue() {
        EventResponse eventResponse = new EventResponse(sessionId, true, null, accountID);
        Event e = new Event("CustomerIdToAccountNumberResponse." + sessionId, eventResponse);
        assertEquals(mq.getEvent("CustomerIdToAccountNumberResponse." + sessionId), e);
//        verify(mq).publish(e);
    }

    @Then("The merchant errormessage {string} is placed back on the queue")
    public void theMerchantErrormessageIsPlacedBackOnTheQueue(String errorMessage) {
        EventResponse eventResponse = new EventResponse(sessionId, false, errorMessage);
        Event e = new Event("MerchantIdToAccountNumberResponse." + sessionId, eventResponse);
        assertEquals(mq.getEvent("MerchantIdToAccountNumberResponse." + sessionId), e);
//        verify(mq).publish(e);
    }

    @Then("The customer errormessage {string} is placed back on the queue")
    public void theCustomerErrormessageIsPlacedBackOnTheQueue(String errorMessage) {
        EventResponse eventResponse = new EventResponse(sessionId, false, errorMessage);
        Event e = new Event("CustomerIdToAccountNumberResponse." + sessionId, eventResponse);
        assertEquals(mq.getEvent("CustomerIdToAccountNumberResponse." + sessionId), e);
//        verify(mq).publish(e);
    }
}