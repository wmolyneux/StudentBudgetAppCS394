package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.TransactionActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.OverviewFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.widget.AppWidgetProvider;

import static android.widget.AdapterView.OnItemClickListener;
import static android.widget.AdapterView.OnItemLongClickListener;

/**
 * This class contains the functionality for assisting with deleting a transaction from a list
 *
 * @author wim2
 * @version 1.0
 */
public class TransactionAdapterListener implements OnItemLongClickListener, OnItemClickListener{

    Context context;
    List<Transaction> transactions;
    SQLiteDatabaseHelper db;
    HistoryArrayAdapter adapter;

    public TransactionAdapterListener(Context con, List<Transaction> trans, SQLiteDatabaseHelper database, HistoryArrayAdapter adap){
        context = con;
        transactions = trans;
        db = database;
        adapter = adap;

    }

    /**
     * Called when a list item is clicked
     *
     * @param adapterView - adapter
     * @param view - list item
     * @param i - position in list
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.putExtra("TRANSACTION", transactions.get(i));
        context.startActivity(intent);
    }

    /**
     * Called when a list item is clicked for an extended period of time
     *
     * @param adapterView - adapter
     * @param view - list item
     * @param i - position in list
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        deleteAlert(i);
        return true;
    }

    /**
     * Opens an alert dialog to allow a transaction to be deleted
     * @param transactionToRemove - position of transaction to be removed
     */
    private void deleteAlert(final int transactionToRemove) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle(context.getString(R.string.transaction_removed)+" "+transactions.get(transactionToRemove).getShortDesc()+"?");

        // set dialog message
        alertDialogBuilder
                .setMessage(context.getString(R.string.transaction_remove_msg))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //remove the selected item from the necessary arrays and database
                        db.deleteTransaction(transactions.get(transactionToRemove));
                        transactions.remove(transactionToRemove);

                        //re-validate the adapter and close the dialog
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

}
