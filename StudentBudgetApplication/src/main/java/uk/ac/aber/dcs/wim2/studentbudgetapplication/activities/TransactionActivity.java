package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

public class TransactionActivity extends Activity implements View.OnClickListener{

    TextView account;
    TextView amount;
    TextView shortDesc;
    TextView type;
    TextView category;
    TextView date;
    Button doneButton;

    Transaction transaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initialiseViews();

        transaction = (Transaction)getIntent().getSerializableExtra("TRANSACTION");
        SQLiteHelper db = new SQLiteHelper(this);
        Account current = db.getAccount(transaction.getAccountId());

        //Enter the transaction information into the layout
        enterTransactionInformation(current);





    }

    private void enterTransactionInformation(Account current) {
        Toast.makeText(this, current.getId()+"", Toast.LENGTH_LONG).show();
        account.setText(current.getAccountName());
        amount.setText(transaction.getAmount().toString());
        shortDesc.setText(transaction.getShortDesc());
        if(!transaction.getType().equalsIgnoreCase("minus")){
            type.setText("Income");
        }
        else{
            type.setText("Expense");
        }
        category.setText(transaction.getCategory());
        date.setText(transaction.getDate());
    }

    private void initialiseViews() {
        account = (TextView) findViewById(R.id.accountValue);
        amount = (TextView) findViewById(R.id.amountValue);
        shortDesc = (TextView) findViewById(R.id.descValue);
        type = (TextView) findViewById(R.id.typeValue);
        category = (TextView) findViewById(R.id.categoryValue);
        date = (TextView) findViewById(R.id.dateValue);

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transaction, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.doneButton){
            this.finish();
        }
    }
}
