package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.AdapterView;


import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Budget;

/**
 * Created by wim2 on 18/03/2014.
 */
public class BudgetAdapterListener implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener{

    Context context;
    List<Budget> budgets;
    SQLiteDatabaseHelper db;
    BudgetArrayAdapter adapter;

    public BudgetAdapterListener(Context con, List<Budget> budgs, SQLiteDatabaseHelper database, BudgetArrayAdapter adap){
        context = con;
        budgets = budgs;
        db = database;
        adapter = adap;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        deleteAlert(i);
        return true;
    }

    private void deleteAlert(final int budgetToRemove) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        TypedArray categoryArray = context.getResources().obtainTypedArray(R.array.categories);
        // set title
        alertDialogBuilder.setTitle("Remove "+categoryArray.getString(budgets.get(budgetToRemove).getCategory())+" budget?");

        // set dialog message
        alertDialogBuilder
                .setMessage(context.getString(R.string.budget_remove_msg))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //remove the selected item from the necessary arrays and database
                        db.deleteBudget(budgets.get(budgetToRemove));
                        budgets.remove(budgetToRemove);

                        adapter.notifyDataSetInvalidated();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
