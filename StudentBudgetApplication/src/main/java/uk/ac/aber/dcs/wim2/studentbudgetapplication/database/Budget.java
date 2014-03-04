package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;

import java.io.Serializable;

/**
 * Created by wim2 on 25/02/2014.
 */
public class Budget implements Serializable{
    private int id;
    private String category;
    private int weekly;
    private int monthly;

    public Budget(){}

    public Budget(String cat, int week, int month){
        super();
        this.category = cat;
        this.weekly = week;
        this.monthly = month;
    }

    @Override
    public String toString(){
        return "Budget [id="+id+", category="+category+", weekly="+weekly+", monthly="+monthly+"]";
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getWeekly() {
        return weekly;
    }

    public int getMonthly() {
        return monthly;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setWeekly(int weekly) {
        this.weekly = weekly;
    }

    public void setMonthly(int monthly) {
        this.monthly = monthly;
    }
}
