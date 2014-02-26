package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.SQLiteDatabaseHelper;

public class TransactionsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    //views from the layout
    EditText amount;
    EditText shortDesc;
    ToggleButton type;
    Spinner category;
    EditText date;
    Button clear;
    Button create;

    //saved instance account
    Detail detail;

    //values for new transaction
    String tmpType;
    SQLiteDatabaseHelper db;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_transactions, container, false);
        registerViews(inflate);

        detail = (Detail) getArguments().getSerializable("detail");
        return inflate;
    }

    private void registerViews(View inflate) {
        amount = (EditText) inflate.findViewById(R.id.amountField);
        shortDesc = (EditText) inflate.findViewById(R.id.descField);
        type = (ToggleButton) inflate.findViewById(R.id.typeButton);
        category = (Spinner) inflate.findViewById(R.id.categorySpinner);

        db = new SQLiteDatabaseHelper(getActivity());
        ArrayList<String> tempCategories = new ArrayList<String>();
        for (Category cat : db.getAllCategories()){
            tempCategories.add(cat.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, tempCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(adapter);

        category.setOnItemSelectedListener(this);




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
                            new Transaction(Float.valueOf(amount.getText().toString()),
                                    shortDesc.getText().toString(), tmpType, category.getSelectedItem().toString(), date.getText().toString());
                    SQLiteDatabaseHelper db = new SQLiteDatabaseHelper(getActivity());
                    db.addTransaction(newTrans);
                    adjustBalance(db);
                    cleanForm();
                    Toast.makeText(getActivity(), "Transaction added", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.clearButton:
                cleanForm();
                break;
        }

    }

    private void adjustBalance(SQLiteDatabaseHelper db) {
        //needs to imeplement new things
//        if(tmpType.equalsIgnoreCase("minus")){
//            currentAcc.setBalance(currentAcc.getBalance()-Float.valueOf(amount.getText().toString()));
//
//        }
//        else{
//            currentAcc.setBalance(currentAcc.getBalance()+Float.valueOf(amount.getText().toString()));
//        }
//        db.updateAccount(currentAcc);
    }

    public void cleanForm(){
        amount.setText("");
        shortDesc.setText("");
        type.setChecked(true);
        category.setSelection(0);
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
        if(category.getSelectedItem().toString().isEmpty()){
            return false;
        }
        if(date.getText().toString().isEmpty()){
            return false;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
