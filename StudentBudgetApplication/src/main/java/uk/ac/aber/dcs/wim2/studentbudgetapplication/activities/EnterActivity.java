package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

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
                SQLiteHelper db = new SQLiteHelper(this);
                if(db.getAllAccounts().size() == 0){
                    db.addAccount(new Account("Savings", Float.valueOf(2000), Float.valueOf(200)));
                }
                if(db.getAllTransactions().size()==0){
                    db.addTransaction(new Transaction(0, Float.valueOf("1"), "minus1", "minus", "food", "12/12/2001"));
                    db.addTransaction(new Transaction(0, Float.valueOf("2"), "minus2", "minus", "food", "12/12/2001"));
                    db.addTransaction(new Transaction(0, Float.valueOf("3"), "minus3", "minus", "food", "12/12/2001"));
                    db.addTransaction(new Transaction(0, Float.valueOf("4"), "minus4", "minus", "food", "12/12/2001"));
                    db.addTransaction(new Transaction(0, Float.valueOf("5"), "minus5", "minus", "food", "12/12/2001"));
                    db.addTransaction(new Transaction(0, Float.valueOf("1"), "plus1", "plus", "birthday", "12/12/2001"));
                    db.addTransaction(new Transaction(0, Float.valueOf("2"), "plus2", "plus", "birthday", "12/12/2001"));
                    db.addTransaction(new Transaction(0, Float.valueOf("3"), "plus3", "plus", "birthday", "12/12/2001"));
                    db.addTransaction(new Transaction(0, Float.valueOf("4"), "plus4", "plus", "birthday", "12/12/2001"));
                    db.addTransaction(new Transaction(0, Float.valueOf("5"), "plus5", "plus", "birthday", "12/12/2001"));

                }

                Intent intent = new Intent(this, AccountsActivity.class);
                startActivity(intent);
        }
    }
}
