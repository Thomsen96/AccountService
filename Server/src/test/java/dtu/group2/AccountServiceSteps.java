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

    @Given("a customer with first name {string}, last name {string} and cpr number {string}")
    public void aUserWithFirstNameLastNameAndCprNumber(String firstName, String lastName, String cprNumber) {
        user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCprNumber(cprNumber);
    }

    @Given("the customer have a balance of {string}")
    public void theUserHaveABalanceOf(String balance) {
        this.balance = balance;
    }

    @When("the bank creates an account with an accountID")
    public void theBankCreatesAnAccountWithAnAccountID() throws BankServiceException_Exception {
        try {
            this.accountID = ass.CreateCustomer(user, new BigDecimal(balance));
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("a customer account exists with that accountID")
    public void anAccountExistsWithThatAccountID() throws BankServiceException_Exception {
        assertNotNull(ass.GetCustomer(accountID));
        assertEquals(ass.GetCustomer(accountID).getId(), accountID);
    }

    @Given("a user with no first name, last name or cpr number")
    public void aUserWithNoFirstNameLastNameOrCprNumber() {
        user = new User();
    }

    @Then("A {string} exception is raised")
    public void aExceptionIsRaised(String expectedException) {
        assertEquals(expectedException, this.exception.getMessage());
    }

    @When("the customer account is deleted")
    public void theAccountWithAccountIDIsDeleted() {
        try {
            ass.DeleteCustomer(accountID);
        } catch (Exception e) {
            this.exception = e;
        }
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
    public void aRequestIsReceived() throws BankServiceException_Exception {
        //Verify(mq)
        Event e = new Event("GetCustomer", new Object[] { accountID } );
        messageService.handleGetCustomer(e);

        //publish(event)

    }

    @Then("a uid is received and customer returned")
    public void aUidIsReceived() throws BankServiceException_Exception {
        Account customer = ass.GetCustomer(accountID);
        Event event = new Event("ResponseCustomer", new Object[]{customer});
        verify(mq).publish(event);
    }
}