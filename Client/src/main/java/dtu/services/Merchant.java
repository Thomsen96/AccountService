package dtu.services;


import javax.json.bind.annotation.JsonbProperty;
import java.math.BigDecimal;

public class Merchant  {


    public Merchant() {

    }

    @JsonbProperty("id")
    public String id;
    @JsonbProperty("balance")
    public BigDecimal balance;

    public Merchant(String id, String balance) {
        this.id = id;
        this.balance = new BigDecimal(balance);
    }
}
