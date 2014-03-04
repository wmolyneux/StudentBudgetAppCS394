package uk.ac.aber.dcs.wim2.studentbudgetapplication.oldCode;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;

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
        cancel.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
        this.finish();
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
                if(validateInput()){
                    Account newAcc =
                            new Account(newAccName.getText().toString(),
                                    Float.parseFloat(newAccBal.getText().toString()),
                                    Float.parseFloat(newAccOverD.getText().toString()));
                    Intent data = new Intent();
                    data.putExtra("newAcc", newAcc);
                    setResult(1, data);
                    this.finish();
                }
                break;

            case R.id.cancelButton:
                setResult(0);
                this.finish();
                break;
        }
    }

    private boolean validateInput() {
        if(newAccName.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a valid account name!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(newAccBal.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a valid balance!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(newAccOverD.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a valid balance!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(Float.valueOf(newAccOverD.getText().toString()) + Float.valueOf(newAccBal.getText().toString()) < 0){
            Toast.makeText(this, "Balance is lower than overdraft allows!", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }
}
