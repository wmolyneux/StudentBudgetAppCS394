package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;


import java.io.Serializable;

/**
 * Created by wim2 on 13/02/2014.
 */
public class Transaction implements Serializable{

    private int id;
    private int accountId;
    private Float amount;
    private String category;
    private String date; //dd/mm/yyyy

    public Transaction(){}

    public Transaction(int accountIden, Float amount, String cat, String date){
        super();
        this.accountId = accountIden;
        this.amount = amount;
        this.category = cat;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction [id="+id+", accountId="+accountId+", amount="+amount+", category="+category+", date="+date+"]";
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

    public String getCategory(){
        return category;
    }

    public String getDate() {
        return date;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
