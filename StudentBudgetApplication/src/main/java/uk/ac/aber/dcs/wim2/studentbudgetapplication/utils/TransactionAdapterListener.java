package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.MainActivity;
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

    public TransactionAdapterListener(Context con, List<Transaction> trans, SQLiteHelper database, ArrayAdapter<String> adap){
        context = con;
        transactions = trans;
        db = database;
        adapter = adap;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Intent intent = new Intent(context, MainActivity.class);
//
//        intent.putExtra("TRANSACTION", transactions.get(i));
//        context.startActivity(intent);
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
                .setMessage(context.getString(R.string.remove_warning))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close current activity
                        String item = adapter.getItem(transactionToRemove);
                        for(Transaction transaction : transactions){
                            if(transaction.getAmount().toString() == item){
                                db.deleteTransaction(transaction);
                                adapter.remove(transaction.getAmount().toString());
                                transactions.remove(transaction);
                            }
                        }
                        adapter.notifyDataSetInvalidated();

//                        Toast.makeText(context, ""+adapter.getItem(transactionToRemove), Toast.LENGTH_LONG).show();
//                        db.deleteTransaction(transactions.get(transactionToRemove));
//                        adapter.remove(transactions.get(transactionToRemove).getAmount().toString());
//                        transactions.remove(transactionToRemove);
//                        adapter.notifyDataSetInvalidated();
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
