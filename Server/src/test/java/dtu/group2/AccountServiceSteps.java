package dtu.group2;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import dtu.group2.Application.AccountServiceServer;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountServiceSteps {
 
  User user;
  String balance;
  String accountID;
  
  Exception exception;
  
  AccountServiceServer ass = new AccountServiceServer(); 
  
  BankService bank = new BankServiceService().getBankServicePort();

  
  @After
  public void cleanUsersAfterRun() throws BankServiceException_Exception {
	  try {
		  bank.retireAccount(accountID);
	  } catch (Exception e){
		  
	  }
  }
  
  
  @Given("a user with first name {string}, last name {string} and cpr number {string}")
  public void aUserWithFirstNameLastNameAndCprNumber(String firstName, String lastName, String cprNumber) {
	  user = new User();
      user.setFirstName(firstName);
      user.setLastName(lastName);
	  user.setCprNumber(cprNumber);
  }

  @Given("the user have a balance of {string}")
  public void theUserHaveABalanceOf(String balance) {
      this.balance = balance;
  }

  @When("the bank creates an account with an accountID")
  public void theBankCreatesAnAccountWithAnAccountID() throws BankServiceException_Exception {
      try {
    	  this.accountID = ass.CreateAccount(user, new BigDecimal(balance));
      } catch (Exception e) {
    	  exception = e;
      }
  }

  @Then("an account exists with that accountID")
  public void anAccountExistsWithThatAccountID() throws BankServiceException_Exception {
	  assertNotNull( ass.GetAccount(accountID) );		  
	  assertEquals( ass.GetAccount(accountID).getId(), accountID );
  }

  @Given("a user with no first name, last name or cpr number")
  public void aUserWithNoFirstNameLastNameOrCprNumber() {
      user = new User();
  }

  @Then("A {string} exception is raised")
  public void aExceptionIsRaised(String expectedException) {
      assertEquals(expectedException, this.exception.getMessage());
  }

  @When("the account is deleted")
  public void theAccountWithAccountIDIsDeleted() {
	  try {
		  ass.DeleteAccount(accountID);
	  } catch (Exception e) {
		  this.exception = e;
	  }
  }

  @When("the account is fetched")
  public void theAccountIsFetched() {
      try {
    	  ass.GetAccount(accountID);
      } catch (Exception e) {
    	  this.exception = e;
      }
  }

  @Given("account ID {string}")
  public void accountID(String accountId) {
      this.accountID = accountId;
  }
  
}