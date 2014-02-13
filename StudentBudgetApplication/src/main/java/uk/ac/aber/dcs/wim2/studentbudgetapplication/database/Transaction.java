package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;


import java.io.Serializable;

/**
 * Created by wim2 on 13/02/2014.
 */
public class Transaction implements Serializable{

    private int id;
    private int accountId;
    private Float amount;

    public Transaction(){}

    public Transaction(int accountIden, Float amount){
        super();
        this.accountId = accountIden;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction [id="+id+", accountId="+accountId+", amount="+amount+"]";
    }

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }


}
