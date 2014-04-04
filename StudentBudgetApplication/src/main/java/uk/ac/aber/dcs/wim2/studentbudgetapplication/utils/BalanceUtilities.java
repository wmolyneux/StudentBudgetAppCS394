package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.ExpenseActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.widget.AppWidgetProvider;

/**
 * Created by wim2 on 04/03/2014.
 */
public class BalanceUtilities {

    private static Detail staticDetail;


    public static Detail recalculateBalance(Detail detail, SQLiteDatabaseHelper db){
        staticDetail = detail;
        //calculate balance using incomes and expense constants
        calculateBalanceWithConstants(staticDetail.getTotalWeeks(), db);

        int daysRemaining = calculateWeeksTillEndOfYear();

        ArrayList<Transaction> thisWeekTransaction;

        thisWeekTransaction = calculateBalanceWithTransactionsOnDifferentWeeks(daysRemaining, db);

        staticDetail.setWeeklyBalance(calculateWeeklyBalance(daysRemaining));

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
        ArrayList<Transaction> tempTransactions = new ArrayList<Transaction>();
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);

        for (Transaction trans : db.getAllTransactions()){
            String[] transSplit = trans.getDate().split("/");
            DateTime transDate = new DateTime(Integer.valueOf(transSplit[2]),
                    Integer.valueOf(transSplit[1]), Integer.valueOf(transSplit[0]), 0, 0);

            today.withDayOfWeek(1);
            transDate.withDayOfWeek(1);

            if(Days.daysBetween(today.withDayOfWeek(1), transDate.withDayOfWeek(1)).getDays() == 0
                    && trans.getType().equalsIgnoreCase("minus")){
                tempTransactions.add(trans);
            }
            else if(trans.getType().equalsIgnoreCase("minus")){
                staticDetail.setBalance(staticDetail.getBalance()-trans.getAmount());
            }
            else{
                staticDetail.setBalance(staticDetail.getBalance()+trans.getAmount());
            }


        }
        return tempTransactions;
    }

    public static Float calculateWeeklyBalance(int daysRemaining){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);

        String[] endSplit = staticDetail.getEndDate().split("/");
        DateTime end = new DateTime(Integer.valueOf(endSplit[2]),
                Integer.valueOf(endSplit[1]), Integer.valueOf(endSplit[0]), 0, 0);


        DateTime thisMonday = today.withDayOfWeek(1);
        int daysBetweenThisMondayAndEnd = Days.daysBetween(thisMonday, end).getDays()+1;

        String[] startSplit = staticDetail.getStartDate().split("/");
        DateTime start = new DateTime(Integer.valueOf(startSplit[2]),
                Integer.valueOf(startSplit[1]), Integer.valueOf(startSplit[0]), 0, 0);

        DateTime thisSunday = today.withDayOfWeek(7);

        int daysBetweenThisSundayAndStart = Days.daysBetween(start, thisSunday).getDays()+1;

        Float weeklyBalance = (float) 0;

        if(daysBetweenThisSundayAndStart < 7){
            weeklyBalance = (staticDetail.getBalance()/(staticDetail.getTotalWeeks()))*daysBetweenThisSundayAndStart;
        }
        else if(daysBetweenThisSundayAndStart >= 7 && daysBetweenThisMondayAndEnd >= 7){
            weeklyBalance = (staticDetail.getBalance()/(Days.daysBetween(thisMonday, end).getDays()+1)*7);
        }
        else if(daysBetweenThisMondayAndEnd < 7){
            weeklyBalance = staticDetail.getBalance();
        }
        return weeklyBalance;
    }

    private static int calculateWeeksTillEndOfYear() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);

        String[] startSplit = staticDetail.getStartDate().split("/");
        DateTime start = new DateTime(Integer.valueOf(startSplit[2]),
                Integer.valueOf(startSplit[1]), Integer.valueOf(startSplit[0]), 0, 0);

        if (today.isAfter(start)){
            start = today;
        }

        String[] endSplit = staticDetail.getEndDate().split("/");
        DateTime end = new DateTime(Integer.valueOf(endSplit[2]),
                Integer.valueOf(endSplit[1]), Integer.valueOf(endSplit[0]), 0, 0);


        return Days.daysBetween(start, end).getDays();

    }

    private static void calculateBalanceWithConstants(int days, SQLiteDatabaseHelper db) {
        staticDetail.setBalance(Float.valueOf(0));
        for (Constant con : db.getAllConstants()){
            Float temp;
            if (con.getRecurr().equalsIgnoreCase("weekly")){
                temp = (con.getAmount()/7)*days;
            }
            else if(con.getRecurr().equalsIgnoreCase("monthly")){
                temp = (con.getAmount()/28)*days;
            }
            else if(con.getRecurr().equalsIgnoreCase("yearly")){
                temp = (con.getAmount()/365)*days;
            }
            else{
                temp = con.getAmount();
            }
            if (con.getType().equalsIgnoreCase("expense")){
                staticDetail.setBalance(staticDetail.getBalance()-temp);
            }
            else{
                staticDetail.setBalance(staticDetail.getBalance()+temp);
            }
        }

    }

    public static void updateWidget(Activity activity) {
        Intent intent = new Intent(activity, AppWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(activity.getApplication())
                .getAppWidgetIds(new ComponentName(activity.getApplication(), AppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        activity.sendBroadcast(intent);
    }

    public static String getValueAs2dpString(Float value){
        return String.format("%.2f", value);
    }

    public static String getValueAs0dpString(Float value){
        return String.format("%.0f", value);
    }

}
