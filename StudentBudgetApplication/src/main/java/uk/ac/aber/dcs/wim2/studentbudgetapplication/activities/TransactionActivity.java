package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

public class TransactionActivity extends Activity implements View.OnClickListener{

    private TextView amount;
    private TextView shortDesc;
    private TextView type;
    private TextView category;
    private TextView date;
    private Button doneButton;

    private Transaction transaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initialiseViews();

        transaction = (Transaction)getIntent().getSerializableExtra("TRANSACTION");


        //Enter the transaction information into the layout
        enterTransactionInformation();

    }

    private void enterTransactionInformation() {
        amount.setText(FragmentUtilities.getCurrency(this)+ BalanceUtilities.getValueAs2dpString(transaction.getAmount()));
        shortDesc.setText(transaction.getShortDesc());
        if(!transaction.getType().equalsIgnoreCase("minus")){
            type.setText(this.getString(R.string.transaction_expense));
        }
        else{
            type.setText(this.getString(R.string.history_income));
        }
        category.setText(getResources().obtainTypedArray(R.array.categories).getString(transaction.getCategory()));
        date.setText(transaction.getDate());
    }

    private void initialiseViews() {
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
        return FragmentUtilities.menuItemSetup(menu, this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.doneButton){
            this.finish();
        }
    }
}
