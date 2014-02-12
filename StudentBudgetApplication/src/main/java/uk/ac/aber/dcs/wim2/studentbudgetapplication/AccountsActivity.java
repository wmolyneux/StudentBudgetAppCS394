package uk.ac.aber.dcs.wim2.studentbudgetapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;

public class AccountsActivity extends ListActivity {

    List<Account> accounts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        SQLiteHelper db = new SQLiteHelper(this);
        accounts = db.getAllAccounts();

        ArrayList<String> values = new ArrayList<String>();
        for (int i = 0; i < accounts.size(); i++){
            values.add(accounts.get(i).getAccountName());
        }


        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.listview_accounts, values);

        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.accounts, menu);
        return true;


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ACCOUNT", accounts.get(position));
        startActivity(intent);

    }
    
}
