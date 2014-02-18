package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

public class TransactionsFragment extends Fragment implements View.OnClickListener{

    //views from the layout
    EditText amount;
    EditText shortDesc;
    ToggleButton type;
    EditText category;
    EditText date;
    Button clear;
    Button create;

    //saved instance account
    Account currentAcc;

    //values for new transaction
    String tmpType;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_transactions, container, false);
        registerViews(inflate);

        currentAcc = (Account) getArguments().getSerializable("ACCOUNT");
        return inflate;
    }

    private void registerViews(View inflate) {
        amount = (EditText) inflate.findViewById(R.id.amountField);
        shortDesc = (EditText) inflate.findViewById(R.id.descField);
        type = (ToggleButton) inflate.findViewById(R.id.typeButton);
        category = (EditText) inflate.findViewById(R.id.categoryField);
        date = (EditText) inflate.findViewById(R.id.dateField);

        clear = (Button) inflate.findViewById(R.id.clearButton);
        create = (Button) inflate.findViewById(R.id.createTransButton);

        create.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createTransButton:
                if(validateInput()){

                    Transaction newTrans =
                            new Transaction(currentAcc.getId(), Float.valueOf(amount.getText().toString()),
                                    shortDesc.getText().toString(), tmpType, category.getText().toString(), date.getText().toString());
                    SQLiteHelper db = new SQLiteHelper(getActivity());
                    db.addTransaction(newTrans);
                    if(tmpType.equalsIgnoreCase("minus")){
                        currentAcc.setBalance(currentAcc.getBalance()-Float.valueOf(amount.getText().toString()));

                    }
                    else{
                        currentAcc.setBalance(currentAcc.getBalance()+Float.valueOf(amount.getText().toString()));
                    }
                    db.updateAccount(currentAcc);
                    cleanForm();
                    Toast.makeText(getActivity(), "Transaction added", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.clearButton:
                cleanForm();
                break;
        }

    }

    public void cleanForm(){
        amount.setText("");
        shortDesc.setText("");
        type.setChecked(true);
        category.setText("");
        date.setText("");

    }

    private boolean validateInput() {
        if(type.isChecked()){
            tmpType = "plus";
        }
        else{
            tmpType = "minus";
        }

        if(amount.getText().toString().isEmpty()){
            return false;
        }
        if(shortDesc.getText().toString().isEmpty()){
            return false;
        }
        if(category.getText().toString().isEmpty()){
            return false;
        }
        if(date.getText().toString().isEmpty()){
            return false;
        }

        return true;
    }
}
