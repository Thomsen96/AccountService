package dtu.group2;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import dtu.group2.Application.AccountServiceServer;
import dtu.group2.Presentation.Resources.AccountMessageService;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AccountServiceSteps {

    User user;
    String balance;
    String accountID;
    Exception exception;

    private static AccountServiceServer ass = new AccountServiceServer();
    MessageQueue mq = mock(RabbitMqQueue.class);
    AccountMessageService messageService = new AccountMessageService(mq, ass);
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
        ass.CreateCustomer(accountID);
    }

    @Then("a customer account exists with that accountID")
    public void anAccountExistsWithThatAccountID() {
        assertNotNull(ass.GetCustomer(accountID));
        assertEquals(ass.GetCustomer(accountID).getId(), accountID);
    }

    @Given("a user with no first name, last name or cpr number")
    public void aUserWithNoFirstNameLastNameOrCprNumber() {
        user = new User();
    }

    @Then("the customer no longer exists")
    public void theCustomerNoLongerExists() {
        assertNull(ass.GetCustomer(accountID));
    }

    @When("the customer account is deleted")
    public void theAccountWithAccountIDIsDeleted() {
        ass.DeleteCustomer(accountID);
    }

    @When("the customer account is fetched")
    public void theAccountIsFetched() {
        try {
            ass.GetCustomer(accountID);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Given("account ID {string}")
    public void accountID(String accountId) {
        this.accountID = accountId;
    }
    
    @Then("a messagequeue message is produced")
    public void aMessagequeueMessageIsProduced() {	
		Event event = new Event("getCustomer", new Object[] {accountID});
		mq.publish(event);
    }

    @When("a request is received")
    public void aRequestIsReceived() {
        Event e = new Event("GetCustomer", new Object[] { accountID } );
        messageService.handleGetCustomer(e);
    }

    @Then("a uid is received and customer returned")
    public void aUidIsReceived() {
        Account customer = ass.GetCustomer(accountID);
        Event event = new Event("ResponseCustomer", new Object[]{customer});
        verify(mq).publish(event);
    }

    @When("a request is received for verification")
    public void aRequestIsReceivedForVerification() {
        Event e = new Event("CustomerVerificationRequested", new Object[]{ accountID });
        messageService.handleVerifyCustomer(e);
    }

    @Then("a uid is received and verification of the custumer is returned")
    public void aUidIsReceivedAndVerificationOfTheCustumerIsReturned() {
        Event e = new Event("CustomerVerified", new Object[] { ass.verifyCustomer(accountID) } );
        verify(mq).publish(e);
    }

    @When("a merchant tries to create an account")
    public void aMerchantTriesToCreateAnAccount() throws BankServiceException_Exception {
        ass.CreateMerchant(accountID);
    }

    @Then("a uid is received and merchant returned")
    public void aUidIsReceivedAndMerchantReturned() {
        assertNotNull(ass.GetMerchant(accountID));
        assertEquals(ass.GetMerchant(accountID).getId(), accountID);
    }
}