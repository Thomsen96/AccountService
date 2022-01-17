package dtu.group2;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import dtu.group2.Application.AccountService;
import dtu.group2.Presentation.Resources.AccountEventHandler;
import dtu.group2.Repositories.CustomerRepository;
import dtu.group2.Repositories.MerchantRepository;
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

    private static AccountService ass = new AccountService(new CustomerRepository(), new MerchantRepository());
    MessageQueue mq = mock(RabbitMqQueue.class);
    AccountEventHandler messageService = new AccountEventHandler(mq, ass);
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
        ass.createCustomer(accountID);
    }

    @Then("a customer account exists with that accountID")
    public void aCustomerAccountExistsWithThatAccountID() {
        assertNotNull(ass.getCustomer(accountID));
        assertEquals(ass.getCustomer(accountID).getId(), accountID);
    }

    @Then("a merchant account exists with that accountID")
    public void aMerchantAccountExistsWithThatAccountID() {
        assertNotNull(ass.getMerchant(accountID));
        assertEquals(ass.getMerchant(accountID).getId(), accountID);
    }

    @Given("a user with no first name, last name or cpr number")
    public void aUserWithNoFirstNameLastNameOrCprNumber() {
        user = new User();
    }

    @Then("the customer no longer exists")
    public void theCustomerNoLongerExists() {
        assertNull(ass.getCustomer(accountID));
    }

    @When("the customer account is deleted")
    public void theAccountWithAccountIDIsDeleted() {
        ass.deleteCustomer(accountID);
    }

    @And("the merchant account is deleted")
    public void theMerchantAccountIsDeleted() {
        ass.deleteMerchant(accountID);
    }

    @Then("the merchant no longer exists")
    public void theMerchantNoLongerExists() {
        assertNull(ass.getMerchant(accountID));
    }

    @When("the customer account is fetched")
    public void theAccountIsFetched() {
        try {
            ass.getCustomer(accountID);
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
        Account customer = ass.getCustomer(accountID);
        Event event = new Event("ResponseCustomer", new Object[]{customer});
        verify(mq).publish(event);
    }

    @When("a request is received for verification")
    public void aRequestIsReceivedForVerification() {
        Event e = new Event("CustomerVerificationRequested", new Object[]{ accountID });
        messageService.handleCustomerVerificationRequest(e);
    }

    @Then("a uid is received and verification of the custumer is returned")
    public void aUidIsReceivedAndVerificationOfTheCustumerIsReturned() {
        Event e = new Event("CustomerVerificationRequest", new Object[] { ass.verifyCustomer(accountID) } );
        verify(mq).publish(e);
    }

    @When("a merchant tries to create an account")
    public void aMerchantTriesToCreateAnAccount() throws BankServiceException_Exception {
        ass.createMerchant(accountID);
    }

    @Then("a uid is received and merchant returned")
    public void aUidIsReceivedAndMerchantReturned() {
        assertNotNull(ass.getMerchant(accountID));
        assertEquals(ass.getMerchant(accountID).getId(), accountID);
    }
}