package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;

import java.io.Serializable;

/**
 * This class contains the functionality for the Budget object
 *
 * @author wim2
 * @version 1.0
 */
public class Budget implements Serializable{
    private int id;
    private int category;
    private int weekly;
    private int max;
    private String date;

    public Budget(){}

    public Budget(int cat, int week, int max, String date){
        super();
        this.category = cat;
        this.weekly = week;
        this.max = max;
        this.date = date;
    }

    @Override
    public String toString(){
        return "Budget [id="+id+", category="+category+", weekly="+weekly+", max="+max+", date="+date+"]";
    }

    public int getId() {
        return id;
    }

    public int getCategory() {
        return category;
    }

    public int getWeekly() {
        return weekly;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setWeekly(int weekly) {
        this.weekly = weekly;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
