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

import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.AccountAdapterListener;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;

public class AccountsActivity extends ListActivity {

    List<Account> accounts = null;
    Context context =  this;
    SQLiteHelper db;
    ArrayAdapter<String> adapter;
    ArrayList<String> values;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

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
                    startActivityForResult(intent, 0);
                    return true;
                }
            });
        }
        return true;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            switch (requestCode){
                case 0:
                    Account account = (Account) data.getExtras().getSerializable("newAcc");
                    accounts.add(account);
                    adapter.add(account.getAccountName());
                    db.addAccount(account);
                    adapter.notifyDataSetInvalidated();
                    break;

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new SQLiteHelper(this);
        accounts = db.getAllAccounts();

        //prepare values for including in the listView
        values = new ArrayList<String>();
        for (Account account : accounts) {
            values.add(account.getAccountName());
        }

        //setup adapter items
        adapter =
                new ArrayAdapter<String>(this, R.layout.listview_accounts, values);
        setListAdapter(adapter);

        //setup onclick listeners using adapter listener.
        AccountAdapterListener listen = new AccountAdapterListener(context, accounts, db, adapter);

        this.getListView().setOnItemLongClickListener(listen);
        this.getListView().setOnItemClickListener(listen);
    }
}
