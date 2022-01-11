package dtu.group2.Domain.Entities;

import dtu.group2.Domain.Entities.Interfaces.IEntity;

import javax.json.bind.annotation.JsonbProperty;

public class Payment implements IEntity {
  @JsonbProperty("amount")
  public int amount;
  @JsonbProperty("cid")
  public String cid;
  @JsonbProperty("mid")
  public String mid;
  @JsonbProperty("id")
  private String id;

  public Payment(int amount, String cid, String mid) {
    this.amount = amount;
    this.cid = cid;
    this.mid = mid;
  }

  public Payment() {
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
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }
}
