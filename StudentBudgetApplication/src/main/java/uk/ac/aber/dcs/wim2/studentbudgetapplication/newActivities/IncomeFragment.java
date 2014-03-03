package uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities;

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
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.OverviewFragment;

public class IncomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher, View.OnClickListener {

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
        loanSpinner = (Spinner) inflate.findViewById(R.id.updateLoanSpinner);
        loanSpinner.setOnItemSelectedListener(this);
        loanAmount = (EditText) inflate.findViewById(R.id.updateLoanAmount);
        loanAmount.addTextChangedListener(this);

        grantSpinner = (Spinner) inflate.findViewById(R.id.updateGrantSpinner);
        grantSpinner.setOnItemSelectedListener(this);
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

        //setup loan spinner and amount with value from constant
        if(tempIncomes.get(0).getRecurr().equalsIgnoreCase("monthly")){
            loanSpinner.setSelection(0);
        }
        else{
            loanSpinner.setSelection(1);
        }
        loanAmount.setText(tempIncomes.get(0).getAmount().toString());

        //setup grant spinner and amount with value from constant
        if(tempIncomes.get(1).getRecurr().equalsIgnoreCase("monthly")){
            grantSpinner.setSelection(0);
        }
        else{
            grantSpinner.setSelection(1);
        }
        grantAmount.setText(tempIncomes.get(1).getAmount().toString());

        //setup wage spinner and amount with value from constant
        if(tempIncomes.get(2).getRecurr().equalsIgnoreCase("weekly")){
            wageSpinner.setSelection(0);
        }
        else if(tempIncomes.get(2).getRecurr().equalsIgnoreCase("monthly")){
            wageSpinner.setSelection(1);
        }
        else{
            wageSpinner.setSelection(2);
        }
        wageAmount.setText(tempIncomes.get(2).getAmount().toString());

        //setup other spinner and amount with value from constant
        if(tempIncomes.get(3).getRecurr().equalsIgnoreCase("weekly")){
            otherSpinner.setSelection(0);
        }
        else if(tempIncomes.get(3).getRecurr().equalsIgnoreCase("monthly")){
            otherSpinner.setSelection(1);
        }
        else{
            otherSpinner.setSelection(2);
        }
        otherAmount.setText(tempIncomes.get(3).getAmount().toString());

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
        Constant loan = new Constant("income", Float.valueOf(loanAmount.getText().toString()), loanSpinner.getSelectedItem().toString());
        Constant grant = new Constant("income", Float.valueOf(grantAmount.getText().toString()), grantSpinner.getSelectedItem().toString());
        Constant wage = new Constant("income", Float.valueOf(wageAmount.getText().toString()), wageSpinner.getSelectedItem().toString());
        Constant other = new Constant("income", Float.valueOf(otherAmount.getText().toString()), otherSpinner.getSelectedItem().toString());
        db.addConstant(loan);
        db.addConstant(grant);
        db.addConstant(wage);
        db.addConstant(other);

        db.updateDetail(detail);
    }

    private boolean validate() {
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
        if(!loanAmount.getText().toString().isEmpty()){
            income += checkSpinner(loanSpinner.getSelectedItem().toString(), loanAmount.getText().toString());
        }
        if(!grantAmount.getText().toString().isEmpty()){
            income += checkSpinner(grantSpinner.getSelectedItem().toString(), grantAmount.getText().toString());
        }
        if(!wageAmount.getText().toString().isEmpty()){
            income += checkSpinner(wageSpinner.getSelectedItem().toString(), wageAmount.getText().toString());
        }
        if(!otherAmount.getText().toString().isEmpty()){
            income += checkSpinner(otherSpinner.getSelectedItem().toString(), otherAmount.getText().toString());
        }
        weeklyIncome.setText(""+income);
    }

    public Float checkSpinner(String spinnerText, String input){
        Float value = (float) 0;
        if(spinnerText.equalsIgnoreCase("weekly")){
            value += Float.valueOf(input);
        }
        else if(spinnerText.equalsIgnoreCase("monthly")){
            value += (Float.valueOf(input)/4);
        }
        else{
            value += (Float.valueOf(input)/52);
        }
        return value;
    }
}
