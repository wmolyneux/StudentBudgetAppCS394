package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;

public class NewAccountActivity extends Activity implements View.OnClickListener{

    Button create;
    Button cancel;

    EditText newAccName;
    EditText newAccBal;
    EditText newAccOverD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        //find views and assign them
        create = (Button)findViewById(R.id.createAccountButton);
        cancel = (Button)findViewById(R.id.cancelButton);

        newAccName = (EditText)findViewById(R.id.newNameField);
        newAccBal = (EditText)findViewById(R.id.newBalanceField);
        newAccOverD = (EditText)findViewById(R.id.newOverdraftField);

        //set on click listeners
        create.setOnClickListener(this);
        create.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_account, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.createAccountButton:
                if(valudateInput()){
                    Account newAcc =
                            new Account(newAccName.getText().toString(),
                                    Float.parseFloat(newAccBal.getText().toString()),
                                    Float.parseFloat(newAccOverD.getText().toString()));
                    Intent data = new Intent();
                    data.putExtra("newAcc", newAcc);
                    setResult(0, data);
                    this.finish();
                }
                break;

            case R.id.cancelButton:
                setResult(1);
                this.finish();
                break;
        }
    }

    private boolean valudateInput() {


        return true;
    }
}
