package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;


import java.io.Serializable;

/**
 * Created by wim2 on 13/02/2014.
 */
public class Transaction implements Serializable{

    private int id;
    private Float amount;
    private String shortDesc;
    private String type;
    private String category;
    private String date; //dd/mm/yyyy

    public Transaction(){}

    public Transaction(Float amount, String desc, String type, String cat, String date){
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

    public String getCategory(){
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }
}
