package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.AcademicYearActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.SQLiteDatabaseHelper;

public class EnterActivity extends Activity implements View.OnClickListener{

    Button entryButton;
    SQLiteDatabaseHelper db;
    Button clear;

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
                    Intent intent = new Intent(this, AcademicYearActivity.class);
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
            db.addCategory(new Category("Food"));
            db.addCategory(new Category("Booze"));
            db.addCategory(new Category("Sport"));
            db.addCategory(new Category("University"));
            db.addCategory(new Category("Travel"));
            db.addCategory(new Category("Clothing"));
            db.addCategory(new Category("Other"));
        }
    }


}
