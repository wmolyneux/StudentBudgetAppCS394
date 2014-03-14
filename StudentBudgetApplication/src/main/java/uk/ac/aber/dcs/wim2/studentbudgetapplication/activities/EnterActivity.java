package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;

public class EnterActivity extends Activity implements View.OnClickListener{

    private Button entryButton;
    private SQLiteDatabaseHelper db;
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                try{
                    Detail detail = db.getAllDetails().get(0);
                    Intent intent = new Intent(this, DetailActivity.class);
                    startActivity(intent);
                }
                catch(IndexOutOfBoundsException e){
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
            db.addCategory(new Category("Food", "cyan"));
            db.addCategory(new Category("Supermarket", "darkGreen"));
            db.addCategory(new Category("Socialising", "green"));
            db.addCategory(new Category("Sport", "magenta"));
            db.addCategory(new Category("University", "yellow"));
            db.addCategory(new Category("Travel", "red"));
            db.addCategory(new Category("Clothing", "blue"));
            db.addCategory(new Category("Other", "purple"));
        }
    }


}
