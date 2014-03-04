package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;

public class OverviewFragment extends Fragment {

    Detail detail = null;
    SQLiteDatabaseHelper db;
    TextView weeklyIncome;
    TextView weeklyExpense;
    TextView weeklyBalance;
    TextView totalBalance;
    ArrayList<Transaction> thisWeekTransaction;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_overview, container, false);
        registerViews(inflate);

        return inflate;
    }

    private void registerViews(View view) {
        weeklyIncome = (TextView) view.findViewById(R.id.overWeeklyIncome);
        weeklyExpense = (TextView) view.findViewById(R.id.overWeeklyExpense);
        weeklyBalance = (TextView) view.findViewById(R.id.overWeeklyBalance);
        totalBalance = (TextView) view.findViewById(R.id.totalBalance);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteDatabaseHelper(getActivity());
        detail = db.getAllDetails().get(0);
        setup();


    }

    private void setup() {
        weeklyIncome.setText(detail.getWeeklyIncome().toString());
        weeklyExpense.setText(detail.getWeeklyExpense().toString());

        detail = BalanceUtilities.recalculateBalance(detail, db);
//        //calculate balance using incomes and expense constants
//        calculateBalanceWithConstants(detail.getTotalWeeks());
//
//        int weeks = calculateWeeksTillEndOfYear();
//        detail.setWeeksRemaining(weeks);
//
//        //Array for this weeks transactions
//        thisWeekTransaction = new ArrayList<Transaction>();
//
//        //do all transactions from other weeks
//        calculateBalanceWithTransactionsOnDifferentWeeks(weeks);
//
//        //calculate weekly balance from the current balance
//        detail.setWeeklyBalance(calculateWeeklyBalance());
//
//        //for the remaining transaction that occured this week.
//        calculateWeeklyBalanceWithTransactionsFromCurrentWeek();

        totalBalance.setText(detail.getBalance()+"");
        weeklyBalance.setText(detail.getWeeklyBalance() + "");
        db.updateDetail(detail);


    }

//    private void calculateWeeklyBalanceWithTransactionsFromCurrentWeek() {
//        for (Transaction trans : thisWeekTransaction){
//            //if transaction type is minus then remove from the weekly balance and the total balance
//            if(trans.getType().equalsIgnoreCase("minus")){
//                detail.setBalance(detail.getBalance()-trans.getAmount());
//                detail.setWeeklyBalance(detail.getWeeklyBalance()-trans.getAmount());
//            }
//
//        }
//    }

//    private void calculateBalanceWithTransactionsOnDifferentWeeks(int weeks) {
//        for(Transaction trans : db.getAllTransactions()){
//            //date object for transaction date
//            String[] split = trans.getDate().split("/");
//            DateTime transDate = new DateTime(Integer.valueOf(split[2]),
//                    Integer.valueOf(split[1]), Integer.valueOf(split[0]), 0, 0);
//
//            //date object for end of year
//            String[] endSplit = detail.getEndDate().split("/");
//            DateTime end = new DateTime(Integer.valueOf(endSplit[2]),
//                    Integer.valueOf(endSplit[1]), Integer.valueOf(endSplit[0]), 0, 0);
//
//            //weeks between transaction date and end of year.
//            int transWeeks = Weeks.weeksBetween(transDate, end).getWeeks();
//
//            //if the transactions are on the same week AND is a minus type
//            // (plus types are included in the total)
//            if(transWeeks==weeks && trans.getType().equalsIgnoreCase("minus")){
//                //add to the temp array for later use
//                thisWeekTransaction.add(trans);
//            }
//            //otherwise if a minus transaction
//            else if(trans.getType().equalsIgnoreCase("minus")){
//                //minus the amount from total balance
//                detail.setBalance(detail.getBalance()-trans.getAmount());
//            }
//            //else if the transaction type is plus basically
//            else{
//                //add the value to the balance
//                detail.setBalance(detail.getBalance()+trans.getAmount());
//            }
//
//        }
//    }

//    public Float calculateWeeklyBalance(){
//        return (detail.getBalance()/detail.getWeeksRemaining());
//    }

//    private int calculateWeeksTillEndOfYear() {
//        String[] startSplit = detail.getStartDate().split("/");
//        DateTime start = new DateTime(Integer.valueOf(startSplit[2]),
//                Integer.valueOf(startSplit[1]), Integer.valueOf(startSplit[0]), 0, 0);
//
//        Calendar cal = Calendar.getInstance();
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//        int month = cal.get(Calendar.MONTH);
//        int year = cal.get(Calendar.YEAR);
//        DateTime today = new DateTime(year, month+1, day, 0, 0);
//        if(today.isAfter(start)){
//            start = today;
//        }
//
//        String[] endSplit = detail.getEndDate().split("/");
//        DateTime end = new DateTime(Integer.valueOf(endSplit[2]),
//                Integer.valueOf(endSplit[1]), Integer.valueOf(endSplit[0]), 0, 0);
//
//        return Weeks.weeksBetween(start, end).getWeeks();
//    }

//    private void calculateBalanceWithConstants(int weeks) {
//        detail.setBalance(Float.valueOf(0));
//        for (Constant con : db.getAllConstants()){
//            if(con.getType().equalsIgnoreCase("income")){
//                if(con.getRecurr().equalsIgnoreCase("weekly")){
//                    Float temp = con.getAmount()*weeks;
//                    detail.setBalance(detail.getBalance()+temp);
//                }
//                else if(con.getRecurr().equalsIgnoreCase("monthly")){
//                    Float temp = con.getAmount()/4;
//                    temp = temp*weeks;
//                    detail.setBalance(detail.getBalance()+temp);
//                }
//                else{
//                    detail.setBalance(detail.getBalance()+con.getAmount());
//                }
//            }
//            else{
//                if(con.getRecurr().equalsIgnoreCase("weekly")){
//                    Float temp = con.getAmount()*weeks;
//                    detail.setBalance(detail.getBalance()-temp);
//                }
//                else if(con.getRecurr().equalsIgnoreCase("monthly")){
//                    Float temp = con.getAmount()/4;
//                    temp = temp*weeks;
//                    detail.setBalance(detail.getBalance()-temp);
//                }
//                else{
//                    detail.setBalance(detail.getBalance()-con.getAmount());
//                }
//
//
//            }
//        }
//
//    }


}
