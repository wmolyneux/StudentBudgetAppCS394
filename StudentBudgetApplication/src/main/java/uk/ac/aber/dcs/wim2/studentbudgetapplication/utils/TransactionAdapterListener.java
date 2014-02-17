package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.MainActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.TransactionActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

import static android.widget.AdapterView.OnItemClickListener;
import static android.widget.AdapterView.OnItemLongClickListener;

/**
 * Created by wim2 on 12/02/2014.
 */
public class TransactionAdapterListener implements OnItemLongClickListener, OnItemClickListener{

    Context context;
    List<Transaction> transactions;
    SQLiteHelper db;
    ArrayAdapter<String> adapter;
    ArrayList<String> values;

    public TransactionAdapterListener(Context con, List<Transaction> trans, SQLiteHelper database, ArrayAdapter<String> adap, ArrayList<String> vals){
        context = con;
        transactions = trans;
        db = database;
        adapter = adap;
        values = vals;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.putExtra("TRANSACTION", transactions.get(i));
        context.startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        deleteAlert(i);

        return true;
    }

    private void deleteAlert(final int transactionToRemove) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Remove "+transactions.get(transactionToRemove).getShortDesc()+"?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Transaction will be permanently removed!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //remove the selected item from the necessary arrays and database
                        values.remove(transactionToRemove);
                        db.deleteTransaction(transactions.get(transactionToRemove));
                        transactions.remove(transactionToRemove);

                        //re-validate the adapter and close the dialog
                        adapter.notifyDataSetInvalidated();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
