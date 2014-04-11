package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Budget;

/**
 * Created by wim2 on 12/03/2014.
 */
public class BudgetArrayAdapter extends ArrayAdapter<Budget> {
    private final Context context;
    private List<Budget> budgets;
    private SQLiteDatabaseHelper db;



    public BudgetArrayAdapter(Context context, List<Budget> budgets) {
        super(context, R.layout.listview_budgets, budgets);
        this.context = context;
        this.budgets = budgets;
        db = new SQLiteDatabaseHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.listview_budgets, parent, false);
        TextView category = (TextView) row.findViewById(R.id.homeBudgetCategory);
        TextView percent = (TextView) row.findViewById(R.id.homeBudgetPercent);
        ProgressBar bar = (ProgressBar) row.findViewById(R.id.progressBar);

        Budget current = budgets.get(position);
        float percentage = calculatePercentage(budgets.get(position));
        TypedArray categoryArray = context.getResources().obtainTypedArray(R.array.categories);

        category.setText(FragmentUtilities.getCurrency(context)+current.getWeekly()+" "+categoryArray.getString(current.getCategory())+" "+context.getString(R.string.budget_array_text));
        if(percentage > 100){
            category.setTextColor(Color.RED);
            percent.setTextColor(Color.RED);
        }
        percent.setText(BalanceUtilities.getValueAs2dpString(percentage)+"%");
        bar.setProgress((int)percentage);

        return row;
    }

    public float calculatePercentage(Budget budget){
        Float amount = (float) 0;
        for (Transaction trans : db.getAllTransactions()){
            if(trans.getCategory() == (budget.getCategory())
                    && trans.getType().equalsIgnoreCase("minus")){
                String[] transSplit = trans.getDate().split("/");
                DateTime transDate = new DateTime(Integer.valueOf(transSplit[2]),
                        Integer.valueOf(transSplit[1]), Integer.valueOf(transSplit[0]), 0, 0);
                transDate = transDate.withDayOfWeek(1);

                String[] budgetSplit = budget.getDate().split("/");
                DateTime budgetDate = new DateTime(Integer.valueOf(budgetSplit[2]),
                        Integer.valueOf(budgetSplit[1]), Integer.valueOf(budgetSplit[0]), 0, 0);

                if(Days.daysBetween(budgetDate, transDate).getDays() == 0){
                    amount += trans.getAmount();
                }
            }
        }

        float percent = ((amount*100)/budget.getWeekly());
        return percent;
    }

}
