package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
public class AccountAdapterListener implements OnItemLongClickListener, OnItemClickListener{

    Context context;
    List<Account> accounts;
    SQLiteHelper db;
    ArrayAdapter<String> adapter;

    public AccountAdapterListener(Context con, List<Account> acc, SQLiteHelper database, ArrayAdapter<String> adap){
        context = con;
        accounts = acc;
        db = database;
        adapter = adap;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, MainActivity.class);

        intent.putExtra("ACCOUNT", accounts.get(i));
        context.startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        deleteAlert(i);

        return true;
    }

    private void deleteAlert(final int accountToRemove) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Remove "+accounts.get(accountToRemove).getAccountName()+"?");

        // set dialog message
        alertDialogBuilder
                .setMessage(context.getString(R.string.remove_warning))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        removeAccountsTransactions(accounts.get(accountToRemove));
                        db.deleteAccount(accounts.get(accountToRemove));
                        adapter.remove(accounts.get(accountToRemove).getAccountName());
                        accounts.remove(accountToRemove);
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

    private void removeAccountsTransactions(Account account) {
        for(Transaction transaction : db.getAllTransactions()){
            System.out.println(transaction.getAccountId());
            if(transaction.getAccountId()==account.getId()){
                db.deleteTransaction(transaction);

            }
        }
    }
}
