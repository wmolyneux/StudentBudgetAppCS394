package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        detail = (Detail) getIntent().getSerializableExtra("detail");
        registerViews();
    }

    public void registerViews(){
        balanceAmount = (EditText)findViewById(R.id.balanceAmount);
        balanceAmount.addTextChangedListener(this);

//        loanSpinner = (Spinner) findViewById(R.id.loanSpinner);
//        loanSpinner.setOnItemSelectedListener(this);
        loanAmount = (EditText) findViewById(R.id.loanAmount);
        loanAmount.addTextChangedListener(this);

//        grantSpinner = (Spinner) findViewById(R.id.grantSpinner);
//        grantSpinner.setOnItemSelectedListener(this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.income, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.incomeToExpenseButton:
                if(validate()){
                    addValuesToDatabase();
                    Intent intent = new Intent(this, ExpenseActivity.class);
                    intent.putExtra("detail", detail);
                    startActivity(intent);
                }
                break;
        }
    }

    private void addValuesToDatabase() {
        detail.setWeeklyIncome(Float.valueOf(weeklyIncome.getText().toString()));
        Constant balance = new Constant("income", Float.valueOf(balanceAmount.getText().toString()), "balance");
        Constant loan = new Constant("income", Float.valueOf(loanAmount.getText().toString()), "remaining");
        Constant grant = new Constant("income", Float.valueOf(grantAmount.getText().toString()), "remaining");
        Constant wage = new Constant("income", Float.valueOf(wageAmount.getText().toString()), wageSpinner.getSelectedItem().toString());
        Constant other = new Constant("income", Float.valueOf(otherAmount.getText().toString()), otherSpinner.getSelectedItem().toString());
        db = new SQLiteDatabaseHelper(this);
        db.addConstant(balance);
        db.addConstant(loan);
        db.addConstant(grant);
        db.addConstant(wage);
        db.addConstant(other);

    }

    private boolean validate() {
        if(balanceAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter balance amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if(loanAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter loan amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if(grantAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter grant amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if(wageAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter salary amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if(otherAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter amount for 'other'", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void itemChanged(){
        Float income = new Float(0);
        if(!balanceAmount.getText().toString().isEmpty()){
            income+= ((Float.valueOf(balanceAmount.getText().toString())/detail.getTotalWeeks())*7);
        }
        if(!loanAmount.getText().toString().isEmpty()){
            income += ((Float.valueOf(loanAmount.getText().toString())/detail.getTotalWeeks())*7);
//            income += FragmentUtilities.checkSpinner("remaining", loanAmount.getText().toString());
        }
        if(!grantAmount.getText().toString().isEmpty()){
            income += ((Float.valueOf(grantAmount.getText().toString())/detail.getTotalWeeks())*7);
//            income += FragmentUtilities.checkSpinner("remaining", grantAmount.getText().toString());
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
