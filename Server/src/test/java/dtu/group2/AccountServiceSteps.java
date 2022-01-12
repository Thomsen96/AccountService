package dtu.group2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import dtu.group2.Application.AccountServiceServer;
import dtu.group2.Presentation.Resources.AccountMessageService;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import static org.mockito.Mockito.mock;

public class AccountServiceSteps {
 
  User user;
  String balance;
  String accountID;
  
  Exception exception;



  AccountServiceServer ass = new AccountServiceServer();
    MessageQueue mq = mock(RabbitMqQueue.class);
  AccountMessageService messageService = new AccountMessageService(mq,ass);
  
  BankService bank = new BankServiceService().getBankServicePort();

  
  @After
  public void cleanUsersAfterRun() {
	  try {
		  bank.retireAccount(accountID);
	  } catch (Exception e){
		  
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
	  assertNotNull( ass.GetCustomer(accountID) );
	  assertEquals( ass.GetCustomer(accountID).getId(), accountID );
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
  
//  @Given("a messagequeue produces the message {string}")
//  public void aMessagequeueProducesTheMessage(String string) throws UnsupportedEncodingException, IOException {
//
//  }
//
//    @Then("reads a new message {string}")
//    public void readsANewMessage(String arg0) {
//
//    }

    @Then("sends the account to the queue")
    public void sendsTheAccountToTheQueue() throws BankServiceException_Exception {
        messageService.sendCustomer(ass.GetCustomer(accountID));
    }
}