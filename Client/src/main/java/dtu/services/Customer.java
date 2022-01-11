package dtu.services;


import javax.json.bind.annotation.JsonbProperty;
import java.math.BigDecimal;

/**
 * Account
 */
public class Customer {
  public Customer(String id, String balance) {
    this.id = id;
    this.balance = new BigDecimal(balance);
  }

  public Customer() {

  }
  @JsonbProperty("id")
  public String id;
  @JsonbProperty("balance")
  public BigDecimal balance;



}

