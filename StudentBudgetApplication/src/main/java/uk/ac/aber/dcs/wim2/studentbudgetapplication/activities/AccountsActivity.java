package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.AdapterListener;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;

public class AccountsActivity extends ListActivity {

    List<Account> accounts = null;
    Context context =  this;
    SQLiteHelper db;
    ArrayAdapter<String> adapter;


    @Override
    protected void onRestart() {
        super.onRestart();
        if(db.getAllAccounts().size() > 0){
            Account newAcc = db.getAllAccounts().get(db.getAllAccounts().size() -1);
            Toast.makeText(this, newAcc.getAccountName(), Toast.LENGTH_LONG).show();
            boolean check = false;
            for(int i = 0; i < accounts.size(); i++){
                if(accounts.get(i).getAccountName() == newAcc.getAccountName()){
                    check = true;
                    break;
                }

            }
            if(!check){
                accounts.add(newAcc);
                adapter.add(newAcc.getAccountName());
                adapter.notifyDataSetInvalidated();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        //connect to database and retrieve all accounts
        db = new SQLiteHelper(this);
        accounts = db.getAllAccounts();

        //prepare values for including in the listView
        ArrayList<String> values = new ArrayList<String>();
        for (int i = 0; i < accounts.size(); i++){
            values.add(accounts.get(i).getAccountName());
        }

        //setup list adapter
        adapter =
                new ArrayAdapter<String>(this, R.layout.listview_accounts, values);
        setListAdapter(adapter);

        //setup onclick listeners using adapter listener.
        AdapterListener listen = new AdapterListener(context, accounts, db, adapter);
        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(listen);
        this.getListView().setOnItemClickListener(listen);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.accounts, menu);
        MenuItem addAccount = menu.findItem(R.id.action_addAccount);
        if(addAccount != null){
            addAccount.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(context, NewAccountActivity.class);
                    context.startActivity(intent);

                    return true;
                }
            });
        }
        return true;


    }




}
