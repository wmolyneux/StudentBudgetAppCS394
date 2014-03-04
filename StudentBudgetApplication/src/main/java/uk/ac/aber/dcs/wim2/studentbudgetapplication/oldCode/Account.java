package uk.ac.aber.dcs.wim2.studentbudgetapplication.oldCode;


import java.io.Serializable;

/**
 * Created by wim2 on 05/02/2014.
 */
public class Account implements Serializable{

    private int id;
    private String accountName;
    private Float balance;
    private Float overdraft;

    public Account(){}

    public Account(String name, Float accountBalance, Float accountOverdraft){
        super();
        this.accountName = name;
        this.balance = accountBalance;
        this.overdraft = accountOverdraft;
    }

    @Override
    public String toString() {
        return "Account [id="+id+", name="+accountName+", balance="+balance+", overdraft="+overdraft+"]";
    }

    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public Float getBalance() {
        return balance;
    }

    public Float getOverdraft() {
        return overdraft;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public void setOverdraft(Float overdraft) {
        this.overdraft = overdraft;
    }


}
