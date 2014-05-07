package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;

/**
 * This class contains the functionality for setting up reoccurring incomes.
 *
 * @author wim2
 * @version 1.0
 */
public class IncomeActivity extends Activity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {

    private EditText balanceAmount;
    private Spinner loanSpinner;
    private EditText loanAmount;
    private Spinner grantSpinner;
    private EditText grantAmount;
    private Spinner wageSpinner;
    private EditText wageAmount;
    private Spinner otherSpinner;
    private EditText otherAmount;
    private TextView weeklyIncome;

    private SQLiteDatabaseHelper db;
    private Detail detail;
    private Context context;


    /**
     * Called when the activity is created to instantiate the objects required.
     *
     * @param savedInstanceState - saved bundled state including any variables passed from other activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        context = this;
        detail = (Detail) getIntent().getSerializableExtra("detail");
        registerViews();
    }

    /**
     * Register views with onClick and OnTextChanged listeners
     */
    public void registerViews(){
        balanceAmount = (EditText)findViewById(R.id.balanceAmount);
        balanceAmount.addTextChangedListener(this);

        loanAmount = (EditText) findViewById(R.id.loanAmount);
        loanAmount.addTextChangedListener(this);

        grantAmount = (EditText) findViewById(R.id.grantAmount);
        grantAmount.addTextChangedListener(this);

        wageSpinner = (Spinner) findViewById(R.id.wageSpinner);
        wageSpinner.setOnItemSelectedListener(this);
        wageAmount = (EditText) findViewById(R.id.wageAmount);
        wageAmount.addTextChangedListener(this);

        otherSpinner = (Spinner) findViewById(R.id.otherSpinner);
        otherSpinner.setOnItemSelectedListener(this);
        otherAmount = (EditText) findViewById(R.id.otherAmount);
        otherAmount.addTextChangedListener(this);

        weeklyIncome = (TextView) findViewById(R.id.incomeWeeklyInc);

        Button next = (Button) findViewById(R.id.incomeToExpenseButton);
        next.setOnClickListener(this);
    }

    /**
     * Called when a activity is finished with a result code sent to this activity.
     *
     * @param requestCode - request code
     * @param resultCode - result code
     * @param data - data sent from finishing activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentUtilities.refreshPreferences(this);
        Intent i = getIntent();
        finish();
        startActivity(i);
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
        switch (view.getId()){
            case R.id.incomeToExpenseButton:
                if(validate()){
                    detail.setWeeklyIncome(Float.valueOf(weeklyIncome.getText().toString()));
                    Intent intent = new Intent(this, ExpenseActivity.class);
                    intent.putExtra("detail", detail);
                    intent.putExtra("balance", new Constant("income", Float.valueOf(balanceAmount.getText().toString()), "remaining"));
                    intent.putExtra("loan", new Constant("income", Float.valueOf(loanAmount.getText().toString()), "remaining"));
                    intent.putExtra("grant", new Constant("income", Float.valueOf(grantAmount.getText().toString()), "remaining"));
                    intent.putExtra("wage", new Constant("income", Float.valueOf(wageAmount.getText().toString()), wageSpinner.getSelectedItem().toString()));
                    intent.putExtra("other", new Constant("income", Float.valueOf(otherAmount.getText().toString()), otherSpinner.getSelectedItem().toString()));
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * Function to validate the input for incomes, checking they are not empty.
     *
     * @return - true if passes
     */
    private boolean validate() {
        if(balanceAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_income_balance), Toast.LENGTH_LONG).show();
            return false;
        }
        if(loanAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_income_remaining_loans), Toast.LENGTH_LONG).show();
            return false;
        }
        if(grantAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_income_remaining_grants), Toast.LENGTH_LONG).show();
            return false;
        }
        if(wageAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_income_salary), Toast.LENGTH_LONG).show();
            return false;
        }
        if(otherAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_income_other), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Function to recalculate the weekly expense balance as the spinners or values in text fields are changed.
     */
    public void itemChanged(){
        Float income = new Float(0);
        if(!balanceAmount.getText().toString().isEmpty()){
            income+= ((Float.valueOf(balanceAmount.getText().toString())/detail.getTotalWeeks())*7);
        }
        if(!loanAmount.getText().toString().isEmpty()){
            income += ((Float.valueOf(loanAmount.getText().toString())/detail.getTotalWeeks())*7);
        }
        if(!grantAmount.getText().toString().isEmpty()){
            income += ((Float.valueOf(grantAmount.getText().toString())/detail.getTotalWeeks())*7);
        }
        if(!wageAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(wageSpinner.getSelectedItem().toString(), wageAmount.getText().toString());
        }
        if(!otherAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(otherSpinner.getSelectedItem().toString(), otherAmount.getText().toString());
        }
        weeklyIncome.setText(BalanceUtilities.getValueAs2dpString(income));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        itemChanged();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        itemChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
