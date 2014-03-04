package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

/**
 * Created by wim2 on 04/03/2014.
 */
public class BalanceUtilities {

    private static Detail staticDetail;


    public static Detail recalculateBalance(Detail detail, SQLiteDatabaseHelper db){
        staticDetail = detail;
        //calculate balance using incomes and expense constants
        calculateBalanceWithConstants(staticDetail.getTotalWeeks(), db);

        int weeks = calculateWeeksTillEndOfYear();
        staticDetail.setWeeksRemaining(weeks);

        //Array for this weeks transactions
        ArrayList<Transaction> thisWeekTransaction;

        //do all transactions from other weeks
        thisWeekTransaction = calculateBalanceWithTransactionsOnDifferentWeeks(weeks, db);

        //calculate weekly balance from the current balance
        staticDetail.setWeeklyBalance(calculateWeeklyBalance());

        //for the remaining transaction that occured this week.
        calculateWeeklyBalanceWithTransactionsFromCurrentWeek(thisWeekTransaction);

        db.updateDetail(staticDetail);
        return staticDetail;
    }

    private static void calculateWeeklyBalanceWithTransactionsFromCurrentWeek(ArrayList<Transaction> thisWeekTransaction) {
        for (Transaction trans : thisWeekTransaction){
            //if transaction type is minus then remove from the weekly balance and the total balance
            if(trans.getType().equalsIgnoreCase("minus")){
                staticDetail.setBalance(staticDetail.getBalance()-trans.getAmount());
                staticDetail.setWeeklyBalance(staticDetail.getWeeklyBalance()-trans.getAmount());
            }

        }
    }

    private static ArrayList<Transaction> calculateBalanceWithTransactionsOnDifferentWeeks(int weeks, SQLiteDatabaseHelper db) {
        ArrayList<Transaction> tempUnusedTransactions = new ArrayList<Transaction>();
        for(Transaction trans : db.getAllTransactions()){
            //date object for transaction date
            String[] split = trans.getDate().split("/");
            DateTime transDate = new DateTime(Integer.valueOf(split[2]),
                    Integer.valueOf(split[1]), Integer.valueOf(split[0]), 0, 0);

            //date object for end of year
            String[] endSplit = staticDetail.getEndDate().split("/");
            DateTime end = new DateTime(Integer.valueOf(endSplit[2]),
                    Integer.valueOf(endSplit[1]), Integer.valueOf(endSplit[0]), 0, 0);

            //weeks between transaction date and end of year.
            int transWeeks = Weeks.weeksBetween(transDate, end).getWeeks();

            //if the transactions are on the same week AND is a minus type
            // (plus types are included in the total)
            if(transWeeks==weeks && trans.getType().equalsIgnoreCase("minus")){
                //add to the temp array for later use
                tempUnusedTransactions.add(trans);
            }
            //otherwise if a minus transaction
            else if(trans.getType().equalsIgnoreCase("minus")){
                //minus the amount from total balance
                staticDetail.setBalance(staticDetail.getBalance()-trans.getAmount());
            }
            //else if the transaction type is plus basically
            else{
                //add the value to the balance
                staticDetail.setBalance(staticDetail.getBalance()+trans.getAmount());
            }

        }
        return tempUnusedTransactions;
    }

    public static Float calculateWeeklyBalance(){
        return (staticDetail.getBalance()/staticDetail.getWeeksRemaining());
    }

    private static int calculateWeeksTillEndOfYear() {
        String[] startSplit = staticDetail.getStartDate().split("/");
        DateTime start = new DateTime(Integer.valueOf(startSplit[2]),
                Integer.valueOf(startSplit[1]), Integer.valueOf(startSplit[0]), 0, 0);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);
        if(today.isAfter(start)){
            start = today;
        }

        String[] endSplit = staticDetail.getEndDate().split("/");
        DateTime end = new DateTime(Integer.valueOf(endSplit[2]),
                Integer.valueOf(endSplit[1]), Integer.valueOf(endSplit[0]), 0, 0);

        return Weeks.weeksBetween(start, end).getWeeks();
    }

    private static void calculateBalanceWithConstants(int weeks, SQLiteDatabaseHelper db) {
        staticDetail.setBalance(Float.valueOf(0));
        for (Constant con : db.getAllConstants()){
            if(con.getType().equalsIgnoreCase("income")){
                if(con.getRecurr().equalsIgnoreCase("weekly")){
                    Float temp = con.getAmount()*weeks;
                    staticDetail.setBalance(staticDetail.getBalance()+temp);
                }
                else if(con.getRecurr().equalsIgnoreCase("monthly")){
                    Float temp = con.getAmount()/4;
                    temp = temp*weeks;
                    staticDetail.setBalance(staticDetail.getBalance()+temp);
                }
                else{
                    staticDetail.setBalance(staticDetail.getBalance()+con.getAmount());
                }
            }
            else{
                if(con.getRecurr().equalsIgnoreCase("weekly")){
                    Float temp = con.getAmount()*weeks;
                    staticDetail.setBalance(staticDetail.getBalance()-temp);
                }
                else if(con.getRecurr().equalsIgnoreCase("monthly")){
                    Float temp = con.getAmount()/4;
                    temp = temp*weeks;
                    staticDetail.setBalance(staticDetail.getBalance()-temp);
                }
                else{
                    staticDetail.setBalance(staticDetail.getBalance()-con.getAmount());
                }


            }
        }

    }
}
