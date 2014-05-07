package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;


import java.io.Serializable;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;

/**
 * This class contains the functionality for the Transaction object
 *
 * @author wim2
 * @version 1.0
 */
public class Transaction implements Serializable{

    private int id;
    private Float amount;
    private String shortDesc;
    private String type;
    private int category;
    private String date;

    public Transaction(){}

    public Transaction(Float amount, String desc, String type, int cat, String date){
        super();
        this.amount = amount;
        this.shortDesc = desc;
        this.type = type;
        this.category = cat;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction [id="+id+", amount="+amount+", shortDesc="+shortDesc+", type="+type+", category="+category+", date="+date+"]";
    }

    public int getId() {
        return id;
    }

    public Float getAmount() {
        return amount;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getType() {
        return type;
    }

    public int getCategory(){
        return category;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }
}
