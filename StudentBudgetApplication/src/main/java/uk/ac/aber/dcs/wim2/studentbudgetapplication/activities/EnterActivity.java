package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

public class EnterActivity extends Activity implements View.OnClickListener{

    private Button entryButton;
    private SQLiteDatabaseHelper db;
    private Button clear;
    String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtilities.refreshPreferences(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean hasPin = prefs.getBoolean("pref_bool_pin", false);
        pin = prefs.getString("pref_pin", "");
        if(hasPin && pin.length()==4){
            activateLock();
        }

        setContentView(R.layout.activity_enter);
        entryButton = (Button)findViewById(R.id.enterButton);
        entryButton.setOnClickListener(this);

        //testing purposes remove!!!!
        clear = (Button) findViewById(R.id.clearDB);
        clear.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.enter, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.enterButton:
                //temp code REMOVE!!!

//                AccountDataSQLHelper db = new AccountDataSQLHelper(this);
//                db.addAccount(new Account("awdwadwdwd", Float.valueOf(2000), Float.valueOf(200)));
//                Toast.makeText(this, db.getAllAccounts().size()+"", Toast.LENGTH_LONG).show();
//
                db = new SQLiteDatabaseHelper(this);
                populateCategoryTable();
                if(db.getAllDetails().size()!=0){
                    Intent intent = new Intent(this, DetailActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(this, BudgetPeriodActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.clearDB:
                db = new SQLiteDatabaseHelper(this);
                for (Detail det : db.getAllDetails()){
                    db.deleteDetail(det);
                }
                for (Constant con : db.getAllConstants()){
                    db.deleteConstant(con);
                }
                for (Transaction trans : db.getAllTransactions()){
                    db.deleteTransaction(trans);
                }
        }
    }


    private void populateCategoryTable() {
        if(db.getAllCategories().size()==0){
            db.addCategory(new Category(0, "cyan"));
            db.addCategory(new Category(1, "darkGreen"));
            db.addCategory(new Category(2, "green"));
            db.addCategory(new Category(3, "magenta"));
            db.addCategory(new Category(4, "yellow"));
            db.addCategory(new Category(5, "red"));
            db.addCategory(new Category(6, "blue"));
            db.addCategory(new Category(7, "purple"));
        }
    }

    private void activateLock() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_pin_dialog);
        dialog.setTitle("Please enter 4-digit pin");
        dialog.setCancelable(false);

        final EditText pinField = (EditText)dialog.findViewById(R.id.pin_et);
        pinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(pinField.getText().toString().length()==4){
                    if(pinField.getText().toString().equalsIgnoreCase(pin)){
                        dialog.dismiss();
                    }
                    else{
                        pinField.setText("");
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog.show();
        pinField.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(pinField, 0);
            }
        }, 50);
    }


}
