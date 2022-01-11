package dtu.group2.Domain.Entities;

import dtu.group2.Domain.Entities.Interfaces.IEntity;

import javax.json.bind.annotation.JsonbProperty;
import java.math.BigDecimal;

public class Merchant implements IEntity {
    public Merchant(String id, BigDecimal balance) {
        setId(id);
        this.balance = balance;
    }

    public Merchant() {

    }

    @JsonbProperty("id")
    public String id;
    public BigDecimal balance;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
