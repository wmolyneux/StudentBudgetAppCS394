package uk.ac.aber.dcs.wim2.studentbudgetapplication.oldCode;

import java.io.Serializable;

/**
 * Created by wim2 on 25/02/2014.
 */
public class Budget implements Serializable{
    private int id;
    private String category;
    private int weekly;
    private int max;
    private String date;

    public Budget(){}

    public Budget(String cat, int week, int max, String date){
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

    public String getCategory() {
        return category;
    }

    public int getWeekly() {
        return weekly;
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
