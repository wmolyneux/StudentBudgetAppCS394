package uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities;

import java.io.Serializable;

/**
 * Created by wim2 on 26/02/2014.
 */
public class Detail implements Serializable {
    private int id;
    private String startDate;
    private String endDate;
    private int weeksRemaining;
    private Float weeklyIncome;
    private Float weeklyExpense;
    private Float weeklyBalance;

    public Detail(){}

    public Detail(String startDate, String endDate, int weeksRemaining, Float weeklyIncome,
                  Float weeklyExpense, Float weeklyBalance) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.weeksRemaining = weeksRemaining;
        this.weeklyIncome = weeklyIncome;
        this.weeklyExpense = weeklyExpense;
        this.weeklyBalance = weeklyBalance;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", weeksRemaining=" + weeksRemaining +
                ", weeklyIncome=" + weeklyIncome +
                ", weeklyExpense=" + weeklyExpense +
                ", weeklyBalance=" + weeklyBalance +
                '}';
    }



    /**
     * Gets and sets
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getWeeksRemaining() {
        return weeksRemaining;
    }

    public void setWeeksRemaining(int weeksRemaining) {
        this.weeksRemaining = weeksRemaining;
    }

    public Float getWeeklyIncome() {
        return weeklyIncome;
    }

    public void setWeeklyIncome(Float weeklyIncome) {
        this.weeklyIncome = weeklyIncome;
    }

    public Float getWeeklyExpense() {
        return weeklyExpense;
    }

    public void setWeeklyExpense(Float weeklyExpense) {
        this.weeklyExpense = weeklyExpense;
    }

    public Float getWeeklyBalance() {
        return weeklyBalance;
    }

    public void setWeeklyBalance(Float weeklyBalance) {
        this.weeklyBalance = weeklyBalance;
    }
}
