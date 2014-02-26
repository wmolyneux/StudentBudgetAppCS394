package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.AcademicYearActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.SQLiteDatabaseHelper;

public class EnterActivity extends Activity implements View.OnClickListener{

    Button entryButton;
    SQLiteDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        entryButton = (Button)findViewById(R.id.enterButton);
        entryButton.setOnClickListener(this);



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



//code before changes
//                SQLiteHelper db = new SQLiteHelper(this);
//                if(db.getAllAccounts().size() == 0){
//                    db.addAccount(new Account("Savings", Float.valueOf(2000), Float.valueOf(200)));
//                }
//
//
//                Intent intent = new Intent(this, AccountsActivity.class);
//                startActivity(intent);


                //new code

//                SQLiteHelper db = new SQLiteHelper(this);

//                if(db.getAllAccounts().size()==0){
                db = new SQLiteDatabaseHelper(this);
                populateCategoryTable();

                Detail detail = null;
                for(Detail det : db.getAllDetails()){
                    detail = det;
                }
                if(detail==null){
                    Intent intent = new Intent(this, AcademicYearActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(this, DetailActivity.class);
                    startActivity(intent);
                }
//                }

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
