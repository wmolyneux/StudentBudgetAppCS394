package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;

import java.io.Serializable;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;

/**
 * Created by wim2 on 26/02/2014.
 */
public class Constant implements Serializable{
    private int id;
    private String type;
    private Float amount;
    private String recurr;

    public Constant(){}

    public Constant(String type, Float amount, String recurr) {
        super();
        this.type = type;
        this.amount = amount;
        this.recurr = recurr;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getRecurr() {
        return recurr;
    }

    public void setRecurr(String recurr) {
        this.recurr = recurr;
    }

    @Override
    public String toString() {
        return "Constant{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                ", recurr='" + recurr + '\'' +
                '}';
    }
}
