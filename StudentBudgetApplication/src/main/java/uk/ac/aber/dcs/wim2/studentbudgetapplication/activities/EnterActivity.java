package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.SQLCipher.AccountDataSQLHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;

public class EnterActivity extends Activity implements View.OnClickListener{

    Button entryButton;

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

                SQLiteHelper db = new SQLiteHelper(this);
                if(db.getAllAccounts().size() == 0){
                    db.addAccount(new Account("Savings", Float.valueOf(2000), Float.valueOf(200)));
                }


                Intent intent = new Intent(this, AccountsActivity.class);
                startActivity(intent);
        }
    }





}
