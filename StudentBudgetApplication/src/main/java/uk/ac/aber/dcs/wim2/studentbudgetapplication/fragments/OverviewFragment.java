package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;
import java.util.List;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Budget;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BudgetAdapterListener;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BudgetArrayAdapter;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

public class OverviewFragment extends ListFragment {

    private Detail detail = null;
    private SQLiteDatabaseHelper db;
    private TextView weeklyIncome;
    private TextView weeklyExpense;
    private TextView weeklyBalance;
    private ListView list;
    private BudgetArrayAdapter listAdapter;
    private BudgetAdapterListener listen;
    private List<Budget> budgets;
    private String currency;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_overview, container, false);
        registerViews(inflate);

        return inflate;
    }

    private void registerViews(View view) {
//        weeklyIncome = (TextView) view.findViewById(R.id.overWeeklyIncome);
//        weeklyExpense = (TextView) view.findViewById(R.id.overWeeklyExpense);
        weeklyBalance = (TextView) view.findViewById(R.id.overWeeklyBalance);
        list = (ListView)view.findViewById(android.R.id.list);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteDatabaseHelper(getActivity());
        detail = db.getAllDetails().get(0);

    }

    @Override
    public void onResume() {
        super.onResume();
        setup();
    }

    private void setup() {
        currency = FragmentUtilities.getCurrency(getActivity());
//        weeklyIncome.setText(currency+BalanceUtilities.getValueAs2dpString(detail.getWeeklyIncome()));
//        weeklyExpense.setText(currency+BalanceUtilities.getValueAs2dpString(detail.getWeeklyExpense()));

        setStartAndEndDates();


        detail = BalanceUtilities.recalculateBalance(detail, db);
        weeklyBalance.setText(currency+BalanceUtilities.getValueAs2dpString(detail.getWeeklyBalance()));
        db.updateDetail(detail);

        checkBudgets();
        budgets = db.getAllBudgets();
        listAdapter = new BudgetArrayAdapter(getActivity(), budgets);
        listen = new BudgetAdapterListener(getActivity(), budgets, db, listAdapter);
        list.setOnItemLongClickListener(listen);
        list.setOnItemClickListener(listen);

        checkMainBudget();

        setListAdapter(listAdapter);
    }

    private void checkMainBudget() {
        TextView overviewPercent = (TextView)getActivity().findViewById(R.id.overviewBudgetPercent);
        ProgressBar overviewProgress = (ProgressBar)getActivity().findViewById(R.id.overviewProgressBar);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);

        String[] endSplit = detail.getStartDate().split("/");
        DateTime startDate = new DateTime(Integer.valueOf(endSplit[2]), Integer.parseInt(endSplit[1]), Integer.valueOf(endSplit[0]), 0, 0);

        float percent = ((Days.daysBetween(startDate, today).getDays()+1) * 100)/Float.valueOf(detail.getTotalWeeks());

        overviewPercent.setText(BalanceUtilities.getValueAs2dpString(percent)+"%");
        overviewProgress.setProgress((int)percent);


    }

    private void setStartAndEndDates() {
        TextView weekStart = (TextView)getActivity().findViewById(R.id.overWeekStart);
        TextView weekEnd = (TextView)getActivity().findViewById(R.id.overWeekEnd);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);
        today = today.withDayOfWeek(1);

        String[] splitStart = detail.getStartDate().split("/");
        DateTime startDate = new DateTime(Integer.valueOf(splitStart[2]), Integer.parseInt(splitStart[1]), Integer.valueOf(splitStart[0]), 0, 0);
        if(startDate.isAfter(today)){
            weekStart.setText(detail.getStartDate()+"  ");
        }
        else{
            weekStart.setText(today.getDayOfMonth()+"/"+today.getMonthOfYear()+"/"+today.getYear()+"  ");
        }

        today = today.withDayOfWeek(7);
        String[] splitEnd = detail.getEndDate().split("/");
        DateTime endDate = new DateTime(Integer.valueOf(splitEnd[2]), Integer.parseInt(splitEnd[1]), Integer.valueOf(splitEnd[0]), 0, 0);
        if(endDate.isBefore(today)){
            weekEnd.setText("  "+detail.getEndDate());
        }
        else{
            weekEnd.setText("  "+today.getDayOfMonth()+"/"+today.getMonthOfYear()+"/"+today.getYear());
        }
    }

    private void checkBudgets() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);
        for(Budget budget : db.getAllBudgets()){
            String[] budgetSplit = budget.getDate().split("/");
            DateTime budgetDate = new DateTime(Integer.valueOf(budgetSplit[2]),
                    Integer.valueOf(budgetSplit[1]), Integer.valueOf(budgetSplit[0]), 0, 0);
            if(Days.daysBetween(budgetDate, today).getDays() > 6){
                db.deleteBudget(budget);
            }
        }
    }

}
