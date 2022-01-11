package dtu.group2.Domain.Entities;

import dtu.group2.Domain.Entities.Interfaces.IEntity;

import javax.json.bind.annotation.JsonbProperty;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Account
 */
public class Customer implements IEntity {

  @JsonbProperty("balance")
  private BigDecimal balance = null;

  @JsonbProperty("id")
  private String id;


  //@JsonbProperty("transactions")
  //private Transaction transactions = null;

  //@JsonbProperty("user")
  //private User user = null;

  public Customer balance(String balance) {
    this.balance = new BigDecimal(balance);
    return this;
  }

  public Customer() {

  }

  public Customer(String id, String balance) {
    this.setId(id);
    this.balance = new BigDecimal(balance);
  }

   /**
   * Get balance
   * @return balance
  **/
  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public Customer id(String id) {
    this.setId(id);
    return this;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Customer account = (Customer) o;
    return Objects.equals(this.balance, account.balance) &&
        Objects.equals(this.getId(), account.getId());

  }

  @Override
  public int hashCode() {
    return Objects.hash(balance, this.getId());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Account {\n");
    
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    id: ").append(toIndentedString(this.getId())).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }
}

