package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.widget.AppWidgetProvider;

public class ExpenseActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher {

    private Spinner rentSpinner;
    private EditText rentAmount;
    private Spinner electricitySpinner;
    private EditText electricityAmount;
    private Spinner heatingSpinner;
    private EditText heatingAmount;
    private Spinner internetSpinner;
    private EditText internetAmount;
    private Spinner foodSpinner;
    private EditText foodAmount;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        detail = (Detail) getIntent().getSerializableExtra("detail");
        registerViews();

    }

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

        foodSpinner = (Spinner) findViewById(R.id.foodSpinner);
        foodSpinner.setOnItemSelectedListener(this);
        foodAmount = (EditText) findViewById(R.id.foodAmount);
        foodAmount.addTextChangedListener(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.expense, menu);
        return true;
    }

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

    private void addValuesToDatabase() {
        Constant rent = new Constant("expense", Float.valueOf(rentAmount.getText().toString()), rentSpinner.getSelectedItem().toString());
        Constant electricity = new Constant("expense", Float.valueOf(electricityAmount.getText().toString()), electricitySpinner.getSelectedItem().toString());
        Constant heating = new Constant("expense", Float.valueOf(heatingAmount.getText().toString()), heatingSpinner.getSelectedItem().toString());
        Constant internet = new Constant("expense", Float.valueOf(internetAmount.getText().toString()), internetSpinner.getSelectedItem().toString());
        Constant food = new Constant("expense", Float.valueOf(foodAmount.getText().toString()), foodSpinner.getSelectedItem().toString());
        Constant transport = new Constant("expense", Float.valueOf(transportAmount.getText().toString()), transportSpinner.getSelectedItem().toString());
        Constant mobile = new Constant("expense", Float.valueOf(mobileAmount.getText().toString()), mobileSpinner.getSelectedItem().toString());
        Constant other = new Constant("expense", Float.valueOf(otherAmount.getText().toString()), otherSpinner.getSelectedItem().toString());
        db = new SQLiteDatabaseHelper(this);
        db.addConstant(rent);
        db.addConstant(electricity);
        db.addConstant(heating);
        db.addConstant(internet);
        db.addConstant(food);
        db.addConstant(transport);
        db.addConstant(mobile);
        db.addConstant(other);

        detail.setWeeklyExpense(Float.valueOf(weeklyExpense.getText().toString()));
        db.addDetail(detail);

        Intent intent = new Intent(this, AppWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), AppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

    }



    private boolean validate() {
        if(rentAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an amount for rent", Toast.LENGTH_LONG).show();
            return false;
        }
        if(electricityAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an amount for electricity", Toast.LENGTH_LONG).show();
            return false;
        }
        if(heatingAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an amount for heating", Toast.LENGTH_LONG).show();
            return false;
        }
        if(internetAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an amount for internet", Toast.LENGTH_LONG).show();
            return false;
        }
        if(foodAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an amount for food", Toast.LENGTH_LONG).show();
            return false;
        }
        if(transportAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an amount for transport", Toast.LENGTH_LONG).show();
            return false;
        }
        if(mobileAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an amount for mobile", Toast.LENGTH_LONG).show();
            return false;
        }
        if(otherAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an amount for other", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public void itemChanged(){
        Float income = new Float(0);
        if(!rentAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(rentSpinner.getSelectedItem().toString(), rentAmount.getText().toString());
        }
        if(!electricityAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(electricitySpinner.getSelectedItem().toString(), electricityAmount.getText().toString());
        }
        if(!heatingAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(heatingSpinner.getSelectedItem().toString(), heatingAmount.getText().toString());
        }
        if(!internetAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(internetSpinner.getSelectedItem().toString(), internetAmount.getText().toString());
        }
        if(!foodAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(foodSpinner.getSelectedItem().toString(), foodAmount.getText().toString());
        }
        if(!transportAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(transportSpinner.getSelectedItem().toString(), transportAmount.getText().toString());
        }
        if(!mobileAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(mobileSpinner.getSelectedItem().toString(), mobileAmount.getText().toString());
        }
        if(!otherAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(otherSpinner.getSelectedItem().toString(), otherAmount.getText().toString());
        }
        weeklyExpense.setText(""+income);
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
