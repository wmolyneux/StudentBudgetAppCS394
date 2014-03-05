package uk.ac.aber.dcs.wim2.studentbudgetapplication.database;

import java.io.Serializable;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;

/**
 * Created by wim2 on 26/02/2014.
 */
public class Detail implements Serializable {
    private int id;
    private String startDate;
    private String endDate;
    private int totalWeeks;
    private int weeksRemaining;
    private Float weeklyIncome;
    private Float weeklyExpense;
    private Float weeklyBalance;
    private Float balance;
    private String flag;

    public Detail(){}

    public Detail(String startDate, String endDate,int totalWeeks, int weeksRemaining, Float weeklyIncome,
                  Float weeklyExpense, Float weeklyBalance, Float balance, String flag) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalWeeks = totalWeeks;
        this.weeksRemaining = weeksRemaining;
        this.weeklyIncome = weeklyIncome;
        this.weeklyExpense = weeklyExpense;
        this.weeklyBalance = weeklyBalance;
        this.balance = balance;
        this.flag = flag;
    }


    @Override
    public String toString() {
        return "Detail{" +
                "id=" + id +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", totalWeeks=" + totalWeeks +
                ", weeksRemaining=" + weeksRemaining +
                ", weeklyIncome=" + weeklyIncome +
                ", weeklyExpense=" + weeklyExpense +
                ", weeklyBalance=" + weeklyBalance +
                ", balance=" + balance +
                ", flag=" + flag +
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

    public int getTotalWeeks() {
        return totalWeeks;
    }

    public void setTotalWeeks(int totalWeeks) {
        this.totalWeeks = totalWeeks;
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

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
