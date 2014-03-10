package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

public class IncomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher, View.OnClickListener {

    private EditText balanceAmount;
    private Spinner loanSpinner;
    private EditText loanAmount;
    private Spinner grantSpinner;
    private EditText grantAmount;
    private Spinner wageSpinner;
    private EditText wageAmount;
    private Spinner otherSpinner;
    private EditText otherAmount;
    private TextView weeklyIncome;

    private SQLiteDatabaseHelper db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_income, container, false);
        registerViews(inflate);
        return inflate;
    }

    private void registerViews(View inflate) {

        balanceAmount = (EditText)inflate.findViewById(R.id.updateBalanceAmount);
        balanceAmount.addTextChangedListener(this);
//        loanSpinner = (Spinner) inflate.findViewById(R.id.updateLoanSpinner);
//        loanSpinner.setOnItemSelectedListener(this);
        loanAmount = (EditText) inflate.findViewById(R.id.updateLoanAmount);
        loanAmount.addTextChangedListener(this);

//        grantSpinner = (Spinner) inflate.findViewById(R.id.updateGrantSpinner);
//        grantSpinner.setOnItemSelectedListener(this);
        grantAmount = (EditText) inflate.findViewById(R.id.updateGrantAmount);
        grantAmount.addTextChangedListener(this);

        wageSpinner = (Spinner) inflate.findViewById(R.id.updateWageSpinner);
        wageSpinner.setOnItemSelectedListener(this);
        wageAmount = (EditText) inflate.findViewById(R.id.updateWageAmount);
        wageAmount.addTextChangedListener(this);

        otherSpinner = (Spinner) inflate.findViewById(R.id.updateOtherSpinner);
        otherSpinner.setOnItemSelectedListener(this);
        otherAmount = (EditText) inflate.findViewById(R.id.updateOtherAmount);
        otherAmount.addTextChangedListener(this);

        weeklyIncome = (TextView) inflate.findViewById(R.id.updateWeeklyInc);

        Button confirm = (Button) inflate.findViewById(R.id.ConfirmButton);
        confirm.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteDatabaseHelper(getActivity());

        ArrayList<Constant> tempIncomes = new ArrayList<Constant>();
        for(Constant con : db.getAllConstants()){
            if(con.getType().equalsIgnoreCase("income")){
                tempIncomes.add(con);
            }
        }

        //setup balance amount with value from constant
        balanceAmount.setText(tempIncomes.get(0).getAmount().toString());

        //setup loan amount with value from constant
        loanAmount.setText(tempIncomes.get(1).getAmount().toString());
//        FragmentUtilities.resetMonthYearSpinnerAndAmount(tempIncomes.get(0), loanSpinner, loanAmount);

        //setup grant spinner and amount with value from constant
        grantAmount.setText(tempIncomes.get(2).getAmount().toString());
//        FragmentUtilities.resetMonthYearSpinnerAndAmount(tempIncomes.get(1), grantSpinner, grantAmount);

        //setup wage spinner and amount with value from constant
        FragmentUtilities.resetWeekMonthYearSpinnerAndAmount(tempIncomes.get(3), wageSpinner, wageAmount);

        //setup other spinner and amount with value from constant
        FragmentUtilities.resetWeekMonthYearSpinnerAndAmount(tempIncomes.get(4), otherSpinner, otherAmount);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        itemChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        itemChanged();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        //on button click move back to overview fragment
        switch (view.getId()){
            case R.id.ConfirmButton:
                if(validate()){
                    addValuesToDatabase();
                    Toast.makeText(getActivity(), "Incomes updated", Toast.LENGTH_LONG).show();

                    //swap to overview fragment
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new OverviewFragment()).commit();
                }
                break;
        }
    }

    public void addValuesToDatabase(){
        Detail detail = db.getAllDetails().get(0);
        for(Constant con : db.getAllConstants()){
            if(con.getType().equalsIgnoreCase("income")){
                db.deleteConstant(con);
            }
        }

        detail.setWeeklyIncome(Float.valueOf(weeklyIncome.getText().toString()));
        Constant balance = new Constant("income", Float.valueOf(balanceAmount.getText().toString()), "balance");
        Constant loan = new Constant("income", Float.valueOf(loanAmount.getText().toString()), "remaining");
        Constant grant = new Constant("income", Float.valueOf(grantAmount.getText().toString()), "remaining");
        Constant wage = new Constant("income", Float.valueOf(wageAmount.getText().toString()), wageSpinner.getSelectedItem().toString());
        Constant other = new Constant("income", Float.valueOf(otherAmount.getText().toString()), otherSpinner.getSelectedItem().toString());
        db.addConstant(balance);
        db.addConstant(loan);
        db.addConstant(grant);
        db.addConstant(wage);
        db.addConstant(other);

        db.updateDetail(detail);
    }

    private boolean validate() {
        if(balanceAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter balance amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if(loanAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter loan amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if(grantAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter grant amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if(wageAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter wage amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if(otherAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter amount for 'other'", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void itemChanged(){
        Float income = new Float(0);
        if(!balanceAmount.getText().toString().isEmpty()){
            income += (Float.valueOf(balanceAmount.getText().toString())/db.getAllDetails().get(0).getTotalWeeks());
        }
        if(!loanAmount.getText().toString().isEmpty()){
//            income += FragmentUtilities.checkSpinner(loanSpinner.getSelectedItem().toString(), loanAmount.getText().toString());
            income += (Float.valueOf(loanAmount.getText().toString())/db.getAllDetails().get(0).getTotalWeeks());
        }
        if(!grantAmount.getText().toString().isEmpty()){
//            income += FragmentUtilities.checkSpinner(grantSpinner.getSelectedItem().toString(), grantAmount.getText().toString());
            income += (Float.valueOf(loanAmount.getText().toString())/db.getAllDetails().get(0).getTotalWeeks());
        }
        if(!wageAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(wageSpinner.getSelectedItem().toString(), wageAmount.getText().toString());
        }
        if(!otherAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(otherSpinner.getSelectedItem().toString(), otherAmount.getText().toString());
        }
        weeklyIncome.setText(BalanceUtilities.getValueAs2dpString(income));
    }

}
