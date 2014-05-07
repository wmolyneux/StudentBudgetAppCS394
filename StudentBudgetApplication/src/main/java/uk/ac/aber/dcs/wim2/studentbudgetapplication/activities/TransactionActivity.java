package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

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

/**
 * This class contains the functionality for displaying transactions details on the screen.
 *
 * @author wim2
 * @version 1.0
 */
public class TransactionActivity extends Activity implements View.OnClickListener{

    private TextView amount;
    private TextView shortDesc;
    private TextView type;
    private TextView category;
    private TextView date;
    private Button doneButton;

    private Transaction transaction;


    /**
     * Called when the activity is created to instantiate the objects required.
     *
     * @param savedInstanceState - saved bundled state including any variables passed from other activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initialiseViews();

        //retrieve the serialized transaction object passed to this activity
        transaction = (Transaction)getIntent().getSerializableExtra("TRANSACTION");

        //Enter the transaction information into the layout
        enterTransactionInformation();
    }

    /**
     * Strips transaction information from the object displaying in the correct format.
     */
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

    /**
     * Register views with onClick and OnTextChanged listeners
     */
    private void initialiseViews() {
        amount = (TextView) findViewById(R.id.amountValue);
        shortDesc = (TextView) findViewById(R.id.descValue);
        type = (TextView) findViewById(R.id.typeValue);
        category = (TextView) findViewById(R.id.categoryValue);
        date = (TextView) findViewById(R.id.dateValue);


        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);
    }

    /**
     * Called on creation of the activity to create options menu.
     *
     * @param menu - Menu on the screen
     *
     * @return true if passed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return FragmentUtilities.menuItemSetup(menu, this);
    }

    /**
     * Called when an item with an OnClickListener is clicked.
     * Used for when the next button is pressed to redirect to the next screen.
     *
     * @param view - View that has been pressed.
     */
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.doneButton){
            this.finish();
        }
    }
}
