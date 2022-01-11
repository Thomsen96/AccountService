package dtu.services;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleDTUPaySteps {
  String cid = "cid1", mid = "mid1";
  SimpleDtuService dtuPay = new SimpleDtuService();
  Response res;
  ArrayList<Payment> paymentList;
  BankService bankService = new BankServiceService().getBankServicePort();
  
  @Given("a customer with id {string}")
  public void aCustomerWithId(String cid) {
    System.out.println(dtuPay.createCustomer(cid, "1000").getStatus());
    this.cid = cid;
  }

  @Given("a merchant with id {string}")
  public void aMerchantWithId(String mid) {
    System.out.println(dtuPay.createMerchant(mid, "2000").getStatus());
    this.mid = mid;
  }

  @When("the merchant initiates a payment for {int} kr by the customer")
  public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
    var res = dtuPay.pay(amount, cid, mid);
    System.out.println(res.getStatus());
    this.res = res;
  }

  @Then("the response status code is {int}")
  public void theResponseStatusCodeIs(int statusCode) {
    assertEquals(statusCode, res.getStatus());
  }

  @Given("a successful payment of {int} kr from customer {string} to merchant {string}")
  public void aSuccessfulPaymentOfKrFromCustomerToMerchant(int amount, String cid, String mid) {
    res = dtuPay.pay(amount, cid, mid);
  }

  @When("the manager asks for a list of payments")
  public void theManagerAsksForAListOfPayments() {
    var res = dtuPay.getPayments();
    System.out.println(res);
    var payments = res.readEntity(new GenericType<ArrayList<Payment>>() {});
    paymentList = payments.stream().filter(p -> p.cid.equals(cid) && p.mid.equals(mid)).collect(Collectors.toCollection(ArrayList::new));
  }

  @Then("the list contains a payments where customer {string} paid {int} kr to merchant {string}")
  public void theListContainsAPaymentsWhereCustomerPaidKrToMerchant(String cid, int amount, String mid) {
    Payment payment = new Payment(amount, cid, mid);
    System.out.println(paymentList.size());
    assertTrue(paymentList.stream().anyMatch(p -> p.equals(payment)));
  }

  @Then("the payment is not successful")
  public void thePaymentIsNotSuccessful() {
    assertEquals(400, res.getStatus());
  }

  @And("an error message is returned saying {string}")
  public void anErrorMessageIsReturnedSaying(String arg0) {
    assertEquals(arg0, res.readEntity(String.class));
  }

  @Given("a customer with a bank account with balance {int}")
  public void aCustomerWithABankAccountWithBalance(int balance) {

  }

  @And("that the customer is registered with DTU Pay")
  public void thatTheCustomerIsRegisteredWithDTUPay() {
    assertNotNull(cid);
  }

  @Given("a merchant with a bank account with balance {int}")
  public void aMerchantWithABankAccountWithBalance(int balance) {
  }

  @And("that the merchant is registered with DTU Pay")
  public void thatTheMerchantIsRegisteredWithDTUPay() {
    assertNotNull(mid);
  }

  @Then("the payment is successful")
  public void thePaymentIsSuccessful() {
    assertEquals(204, res.getStatus());
  }

  @And("the balance of the customer at the bank is {string} kr")
  public void theBalanceOfTheCustomerAtTheBankIsKr(String balance) {
    var res = dtuPay.getCustomer(cid);
    System.out.println(res);
    var customer = res.readEntity(Customer.class);
    assertEquals(new BigDecimal(balance), customer.balance);
  }

  @And("the balance of the merchant at the bank is {string} kr")
  public void theBalanceOfTheMerchantAtTheBankIsKr(String balance) {
    var res = dtuPay.getMerchant(mid);
    System.out.println(res);
    var merchant = res.readEntity(Merchant.class);
    assertEquals(new BigDecimal(balance), merchant.balance);
  }

}