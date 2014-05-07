package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
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
 * This class contains the functionality for setting up reoccurring expenses.
 *
 * @author wim2
 * @version 1.0
 */
public class ExpenseActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher {

    private Spinner rentSpinner;
    private EditText rentAmount;
    private Spinner electricitySpinner;
    private EditText electricityAmount;
    private Spinner heatingSpinner;
    private EditText heatingAmount;
    private Spinner internetSpinner;
    private EditText internetAmount;
    private Spinner transportSpinner;
    private EditText transportAmount;
    private Spinner mobileSpinner;
    private EditText mobileAmount;
    private Spinner otherSpinner;
    private EditText otherAmount;
    private TextView weeklyExpense;
    private Button finish;


    private Detail detail;
    private SQLiteDatabaseHelper db;
    private Context context;


    /**
     * Called when the activity is created to instantiate the objects required.
     *
     * @param savedInstanceState - saved bundled state including any variables passed from other activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        context = this;
        detail = (Detail) getIntent().getSerializableExtra("detail");
        registerViews();

    }

    /**
     * Register views with onClick and OnTextChanged listeners
     */
    public void registerViews(){
        rentSpinner = (Spinner) findViewById(R.id.rentSpinner);
        rentSpinner.setOnItemSelectedListener(this);
        rentAmount = (EditText) findViewById(R.id.rentAmount);
        rentAmount.addTextChangedListener(this);

        electricitySpinner = (Spinner) findViewById(R.id.electricitySpinner);
        electricitySpinner.setOnItemSelectedListener(this);
        electricityAmount = (EditText) findViewById(R.id.electricityAmount);
        electricityAmount.addTextChangedListener(this);

        heatingSpinner = (Spinner) findViewById(R.id.heatingSpinner);
        heatingSpinner.setOnItemSelectedListener(this);
        heatingAmount = (EditText) findViewById(R.id.heatingAmount);
        heatingAmount.addTextChangedListener(this);

        internetSpinner = (Spinner) findViewById(R.id.internetSpinner);
        internetSpinner.setOnItemSelectedListener(this);
        internetAmount = (EditText) findViewById(R.id.internetAmount);
        internetAmount.addTextChangedListener(this);

        transportSpinner = (Spinner) findViewById(R.id.transportSpinner);
        transportSpinner.setOnItemSelectedListener(this);
        transportAmount = (EditText) findViewById(R.id.transportAmount);
        transportAmount.addTextChangedListener(this);

        mobileSpinner = (Spinner) findViewById(R.id.mobileSpinner);
        mobileSpinner.setOnItemSelectedListener(this);
        mobileAmount = (EditText) findViewById(R.id.mobileAmount);
        mobileAmount.addTextChangedListener(this);

        otherSpinner = (Spinner) findViewById(R.id.otherSpinner);
        otherSpinner.setOnItemSelectedListener(this);
        otherAmount = (EditText) findViewById(R.id.otherAmount);
        otherAmount.addTextChangedListener(this);

        weeklyExpense = (TextView) findViewById(R.id.ExpenseWeeklyExp);

        finish = (Button) findViewById(R.id.finishButton);
        finish.setOnClickListener(this);
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
            case R.id.finishButton:
                if(validate()){
                    addValuesToDatabase();
                    Intent intent = new Intent(this, DetailActivity.class);
                    startActivity(intent);

                }

        }
    }

    /**
     * Function to add the values to the database once the setup process has been completed.
     */
    private void addValuesToDatabase() {
        //Construct the expense objects from fields on this activity
        Constant rent = new Constant("expense", Float.valueOf(rentAmount.getText().toString()), rentSpinner.getSelectedItem().toString());
        Constant electricity = new Constant("expense", Float.valueOf(electricityAmount.getText().toString()), electricitySpinner.getSelectedItem().toString());
        Constant heating = new Constant("expense", Float.valueOf(heatingAmount.getText().toString()), heatingSpinner.getSelectedItem().toString());
        Constant internet = new Constant("expense", Float.valueOf(internetAmount.getText().toString()), internetSpinner.getSelectedItem().toString());
        Constant transport = new Constant("expense", Float.valueOf(transportAmount.getText().toString()), transportSpinner.getSelectedItem().toString());
        Constant mobile = new Constant("expense", Float.valueOf(mobileAmount.getText().toString()), mobileSpinner.getSelectedItem().toString());
        Constant other = new Constant("expense", Float.valueOf(otherAmount.getText().toString()), otherSpinner.getSelectedItem().toString());

        //Add the 'expense' objects to the Constants table in the database
        db = new SQLiteDatabaseHelper(this);
        db.addConstant(rent);
        db.addConstant(electricity);
        db.addConstant(heating);
        db.addConstant(internet);
        db.addConstant(transport);
        db.addConstant(mobile);
        db.addConstant(other);

        //Add the 'income' objects, previously passed from the setup of incomes, to the Constants table in the database
        db.addConstant((Constant)getIntent().getSerializableExtra("balance"));
        db.addConstant((Constant)getIntent().getSerializableExtra("loan"));
        db.addConstant((Constant)getIntent().getSerializableExtra("grant"));
        db.addConstant((Constant)getIntent().getSerializableExtra("wage"));
        db.addConstant((Constant)getIntent().getSerializableExtra("other"));

        //weekly expenses are added to the Detail object and then the object is added to the Detail table
        detail.setWeeklyExpense(Float.valueOf(weeklyExpense.getText().toString()));
        db.addDetail(detail);

        //Widget is updated with new values
        BalanceUtilities.updateWidget(this);
    }


    /**
     * Function to validate the input for expenses, checking they are not empty.
     *
     * @return - true if passes
     */
    private boolean validate() {
        if(rentAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_expense_rent), Toast.LENGTH_LONG).show();
            return false;
        }
        if(electricityAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_expense_electricity), Toast.LENGTH_LONG).show();
            return false;
        }
        if(heatingAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_expense_heating), Toast.LENGTH_LONG).show();
            return false;
        }
        if(internetAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_expense_internet), Toast.LENGTH_LONG).show();
            return false;
        }
        if(transportAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_expense_transport), Toast.LENGTH_LONG).show();
            return false;
        }
        if(mobileAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter) +" "+ getString(R.string.msg_expense_mobile), Toast.LENGTH_LONG).show();
            return false;
        }
        if(otherAmount.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter)+" "+getString(R.string.msg_expense_other), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Function to recalculate the weekly expense balance as the spinners or values in text fields are changed.
     */
    public void itemChanged(){
        Float expense = new Float(0);
        if(!rentAmount.getText().toString().isEmpty()){
            expense += FragmentUtilities.checkSpinner(rentSpinner.getSelectedItem().toString(), rentAmount.getText().toString());
        }
        if(!electricityAmount.getText().toString().isEmpty()){
            expense += FragmentUtilities.checkSpinner(electricitySpinner.getSelectedItem().toString(), electricityAmount.getText().toString());
        }
        if(!heatingAmount.getText().toString().isEmpty()){
            expense += FragmentUtilities.checkSpinner(heatingSpinner.getSelectedItem().toString(), heatingAmount.getText().toString());
        }
        if(!internetAmount.getText().toString().isEmpty()){
            expense += FragmentUtilities.checkSpinner(internetSpinner.getSelectedItem().toString(), internetAmount.getText().toString());
        }
        if(!transportAmount.getText().toString().isEmpty()){
            expense += FragmentUtilities.checkSpinner(transportSpinner.getSelectedItem().toString(), transportAmount.getText().toString());
        }
        if(!mobileAmount.getText().toString().isEmpty()){
            expense += FragmentUtilities.checkSpinner(mobileSpinner.getSelectedItem().toString(), mobileAmount.getText().toString());
        }
        if(!otherAmount.getText().toString().isEmpty()){
            expense += FragmentUtilities.checkSpinner(otherSpinner.getSelectedItem().toString(), otherAmount.getText().toString());
        }
        weeklyExpense.setText(BalanceUtilities.getValueAs2dpString(expense));
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        itemChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
}
