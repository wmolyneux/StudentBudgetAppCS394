package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

/**
 * This class contains the functionality for the home screen of the application.
 *
 * @author wim2
 * @version 1.0
 */
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

    /**
     * Called when creating a fragment inflating the view and instantiating objects
     *
     * @param inflater - layout inflater
     * @param container - container of the view
     * @param savedInstanceState - bundled state
     *
     * @return - view that has been inflated
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_overview, container, false);
        registerViews(inflate);

        return inflate;
    }

    /**
     * Registers views displayed on the screen, including displaying correct values, and setting up onClick listeners
     *
     * @param view - fragment view
     */
    private void registerViews(View view) {
        weeklyBalance = (TextView) view.findViewById(R.id.overWeeklyBalance);
        list = (ListView)view.findViewById(android.R.id.list);

    }

    /**
     * Called when activity is created to ensure the setup of the screen is done before the main thread
     *
     * @param savedInstanceState - bundled state
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteDatabaseHelper(getActivity());
        detail = db.getAllDetails().get(0);

    }

    /**
     * Called when the screen is brought back into view, such as bringing the application up once minimized.
     * Sets up the screen with any new values that may have changed
     */
    @Override
    public void onResume() {
        super.onResume();
        setup();
    }

    /**
     * Sets up the screen with new dates, change in preferences, or change in balance
     */
    private void setup() {
        currency = FragmentUtilities.getCurrency(getActivity());
        setStartAndEndDates();

        //recalculate balance and update Detail table
        detail = BalanceUtilities.recalculateBalance(detail, db);
        weeklyBalance.setText(currency+BalanceUtilities.getValueAs2dpString(detail.getWeeklyBalance()));
        db.updateDetail(detail);

        //check budgets are all up to date and displayed
        checkBudgets();
        budgets = db.getAllBudgets();
        listAdapter = new BudgetArrayAdapter(getActivity(), budgets);
        listen = new BudgetAdapterListener(getActivity(), budgets, db, listAdapter);
        list.setOnItemLongClickListener(listen);
        list.setOnItemClickListener(listen);

        //check main budget is calculated encase it is a new day so the % should have gone up.
        checkMainBudget();

        setListAdapter(listAdapter);
    }

    /**
     * Calculated the percentage through the overall budget period, displaying a progress bar on the screen
     */
    private void checkMainBudget() {
        TextView overviewPercent = (TextView)getActivity().findViewById(R.id.overviewBudgetPercent);
        ProgressBar overviewProgress = (ProgressBar)getActivity().findViewById(R.id.overviewProgressBar);

        //get today's date
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);

        String[] endSplit = detail.getStartDate().split("/");
        DateTime startDate = new DateTime(Integer.valueOf(endSplit[2]), Integer.parseInt(endSplit[1]), Integer.valueOf(endSplit[0]), 0, 0);

        //calculate the percent from the start date, end date, today's date and total weeks of the budget period
        float percent = ((Days.daysBetween(startDate, today).getDays()+1) * 100)/Float.valueOf(detail.getTotalWeeks());

        //display on progress bar and text fields
        overviewPercent.setText(BalanceUtilities.getValueAs0dpString(percent)+"%");
        overviewProgress.setProgress((int)percent);
    }

    /**
     * Displays start and end dates of the current week in textfields at the top of the screen.
     * Calculates which date to use as start date and end date, such as if the budget period start date
     * is before the week start.
     */
    private void setStartAndEndDates() {
        TextView weekStart = (TextView)getActivity().findViewById(R.id.overWeekStart);
        TextView weekEnd = (TextView)getActivity().findViewById(R.id.overWeekEnd);

        //get today's date
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);
        today = today.withDayOfWeek(1);

        String[] splitStart = detail.getStartDate().split("/");
        DateTime startDate = new DateTime(Integer.valueOf(splitStart[2]), Integer.parseInt(splitStart[1]), Integer.valueOf(splitStart[0]), 0, 0);

        //check if start date is after today, if yes then display start date, else display todays date at the week start date
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

    /**
     * check if any budgets are out of date and removes them.
     */
    private void checkBudgets() {
        //get today's date
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);

        //for all budgets in the budget table
        for(Budget budget : db.getAllBudgets()){
            String[] budgetSplit = budget.getDate().split("/");
            DateTime budgetDate = new DateTime(Integer.valueOf(budgetSplit[2]),
                    Integer.valueOf(budgetSplit[1]), Integer.valueOf(budgetSplit[0]), 0, 0);

            //if budget is not in this week, remove it
            if(Days.daysBetween(budgetDate, today).getDays() > 6){
                db.deleteBudget(budget);
            }
        }
    }

}
