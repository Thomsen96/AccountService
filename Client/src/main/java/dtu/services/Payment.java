package dtu.services;

import java.util.Objects;

public class Payment {
  public int amount;
  public String cid;
  public String mid;
  public String id;

  public Payment(int amount, String cid, String mid) {
    this.amount = amount;
    this.cid = cid;
    this.mid = mid;
  }

  public Payment() {
  }

  @Override
  public String toString() {
    return "Payment{" +
            "amount=" + amount +
            ", cid='" + cid + '\'' +
            ", mid='" + mid + '\'' +
            '}';
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public String getMid() {
    return mid;
  }

  public void setMid(String mid) {
    this.mid = mid;
  }

  @Override
  public boolean equals(Object o) {
    Payment payment = (Payment) o;
    return amount == payment.amount && Objects.equals(cid, payment.cid) && Objects.equals(mid, payment.mid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, cid, mid);
  }
}
