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
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.OverviewFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

public class ExpenseFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher {

    private Spinner rentSpinner;
    private EditText rentAmount;
    private Spinner electricitySpinner;
    private EditText electricityAmount;
    private Spinner heatingSpinner;
    private EditText heatingAmount;
    private Spinner internetSpinner;
    private EditText internetAmount;
    private Spinner foodSpinner;
    private EditText foodAmount;
    private Spinner transportSpinner;
    private EditText transportAmount;
    private Spinner mobileSpinner;
    private EditText mobileAmount;
    private Spinner otherSpinner;
    private EditText otherAmount;
    private TextView weeklyExpense;

    private SQLiteDatabaseHelper db;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_expense, container, false);
        registerViews(inflate);
        return inflate;
    }

    private void registerViews(View inflate) {
        rentSpinner = (Spinner) inflate.findViewById(R.id.updateRentSpinner);
        rentSpinner.setOnItemSelectedListener(this);
        rentAmount = (EditText) inflate.findViewById(R.id.updateRentAmount);
        rentAmount.addTextChangedListener(this);

        electricitySpinner = (Spinner) inflate.findViewById(R.id.updateElectricitySpinner);
        electricitySpinner.setOnItemSelectedListener(this);
        electricityAmount = (EditText) inflate.findViewById(R.id.updateElectricityAmount);
        electricityAmount.addTextChangedListener(this);

        heatingSpinner = (Spinner) inflate.findViewById(R.id.updateHeatingSpinner);
        heatingSpinner.setOnItemSelectedListener(this);
        heatingAmount = (EditText) inflate.findViewById(R.id.updateHeatingAmount);
        heatingAmount.addTextChangedListener(this);

        internetSpinner = (Spinner) inflate.findViewById(R.id.updateInternetSpinner);
        internetSpinner.setOnItemSelectedListener(this);
        internetAmount = (EditText) inflate.findViewById(R.id.updateInternetAmount);
        internetAmount.addTextChangedListener(this);

        foodSpinner = (Spinner) inflate.findViewById(R.id.updateFoodSpinner);
        foodSpinner.setOnItemSelectedListener(this);
        foodAmount = (EditText) inflate.findViewById(R.id.updateFoodAmount);
        foodAmount.addTextChangedListener(this);

        transportSpinner = (Spinner) inflate.findViewById(R.id.updateTransportSpinner);
        transportSpinner.setOnItemSelectedListener(this);
        transportAmount = (EditText) inflate.findViewById(R.id.updateTransportAmount);
        transportAmount.addTextChangedListener(this);

        mobileSpinner = (Spinner) inflate.findViewById(R.id.updateMobileSpinner);
        mobileSpinner.setOnItemSelectedListener(this);
        mobileAmount = (EditText) inflate.findViewById(R.id.updateMobileAmount);
        mobileAmount.addTextChangedListener(this);

        otherSpinner = (Spinner) inflate.findViewById(R.id.updateOtherSpinner);
        otherSpinner.setOnItemSelectedListener(this);
        otherAmount = (EditText) inflate.findViewById(R.id.updateOtherAmount);
        otherAmount.addTextChangedListener(this);

        weeklyExpense = (TextView) inflate.findViewById(R.id.updateWeeklyExp);

        Button update = (Button) inflate.findViewById(R.id.updateExpenseButton);
        update.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteDatabaseHelper(getActivity());

        ArrayList<Constant> tempExpenses = new ArrayList<Constant>();
        for(Constant con : db.getAllConstants()){
            if(con.getType().equalsIgnoreCase("expense")){
                tempExpenses.add(con);
            }
        }



        //setup the rent spinner and amount
        FragmentUtilities.resetMonthYearSpinnerAndAmount(tempExpenses.get(0), rentSpinner, rentAmount);

        //setup the Electricity spinner and amount
        FragmentUtilities.resetWeekMonthSpinnerAndAmount(tempExpenses.get(1), electricitySpinner, electricityAmount);

        //setup the heating spinner and amount
        FragmentUtilities.resetWeekMonthSpinnerAndAmount(tempExpenses.get(2), heatingSpinner, heatingAmount);

        //setup the internet spinner and amount
        FragmentUtilities.resetMonthYearSpinnerAndAmount(tempExpenses.get(3), internetSpinner, internetAmount);

        //setup the food spinner and amount
        FragmentUtilities.resetWeekMonthYearSpinnerAndAmount(tempExpenses.get(4), foodSpinner, foodAmount);

        //setup the transport spinner and amount
        FragmentUtilities.resetWeekMonthYearSpinnerAndAmount(tempExpenses.get(5), transportSpinner, transportAmount);

        //setup the mobile spinner and amount
        FragmentUtilities.resetMonthYearSpinnerAndAmount(tempExpenses.get(6), mobileSpinner, mobileAmount);

        //setup the other spinner and amount
        FragmentUtilities.resetWeekMonthYearSpinnerAndAmount(tempExpenses.get(7), otherSpinner, otherAmount);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.updateExpenseButton:
                if(validate()){
                    addValuesToDatabase();
                    Toast.makeText(getActivity(), "Expenses updated", Toast.LENGTH_LONG).show();

                    //swap to overview fragment
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new OverviewFragment()).commit();
                }
                break;
        }
    }

    private void addValuesToDatabase() {
        Detail detail = db.getAllDetails().get(0);
        for(Constant con : db.getAllConstants()){
            if(con.getType().equalsIgnoreCase("expense")){
                db.deleteConstant(con);
            }
        }

        Constant rent = new Constant("expense", Float.valueOf(rentAmount.getText().toString()), rentSpinner.getSelectedItem().toString());
        Constant electricity = new Constant("expense", Float.valueOf(electricityAmount.getText().toString()), electricitySpinner.getSelectedItem().toString());
        Constant heating = new Constant("expense", Float.valueOf(heatingAmount.getText().toString()), heatingSpinner.getSelectedItem().toString());
        Constant internet = new Constant("expense", Float.valueOf(internetAmount.getText().toString()), internetSpinner.getSelectedItem().toString());
        Constant food = new Constant("expense", Float.valueOf(foodAmount.getText().toString()), foodSpinner.getSelectedItem().toString());
        Constant transport = new Constant("expense", Float.valueOf(transportAmount.getText().toString()), transportSpinner.getSelectedItem().toString());
        Constant mobile = new Constant("expense", Float.valueOf(mobileAmount.getText().toString()), mobileSpinner.getSelectedItem().toString());
        Constant other = new Constant("expense", Float.valueOf(otherAmount.getText().toString()), otherSpinner.getSelectedItem().toString());

        db.addConstant(rent);
        db.addConstant(electricity);
        db.addConstant(heating);
        db.addConstant(internet);
        db.addConstant(food);
        db.addConstant(transport);
        db.addConstant(mobile);
        db.addConstant(other);

        detail.setWeeklyExpense(Float.valueOf(weeklyExpense.getText().toString()));
        db.updateDetail(detail);

    }

    private boolean validate() {
        if(rentAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter an amount for rent", Toast.LENGTH_LONG).show();
            return false;
        }
        if(electricityAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter an amount for electricity", Toast.LENGTH_LONG).show();
            return false;
        }
        if(heatingAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter an amount for heating", Toast.LENGTH_LONG).show();
            return false;
        }
        if(internetAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter an amount for internet", Toast.LENGTH_LONG).show();
            return false;
        }
        if(foodAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter an amount for food", Toast.LENGTH_LONG).show();
            return false;
        }
        if(transportAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter an amount for transport", Toast.LENGTH_LONG).show();
            return false;
        }
        if(mobileAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter an amount for mobile", Toast.LENGTH_LONG).show();
            return false;
        }
        if(otherAmount.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter an amount for other", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void itemChanged(){
        Float income = new Float(0);
        if(!rentAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(rentSpinner.getSelectedItem().toString(), rentAmount.getText().toString());
        }
        if(!electricityAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(electricitySpinner.getSelectedItem().toString(), electricityAmount.getText().toString());
        }
        if(!heatingAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(heatingSpinner.getSelectedItem().toString(), heatingAmount.getText().toString());
        }
        if(!internetAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(internetSpinner.getSelectedItem().toString(), internetAmount.getText().toString());
        }
        if(!foodAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(foodSpinner.getSelectedItem().toString(), foodAmount.getText().toString());
        }
        if(!transportAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(transportSpinner.getSelectedItem().toString(), transportAmount.getText().toString());
        }
        if(!mobileAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(mobileSpinner.getSelectedItem().toString(), mobileAmount.getText().toString());
        }
        if(!otherAmount.getText().toString().isEmpty()){
            income += FragmentUtilities.checkSpinner(otherSpinner.getSelectedItem().toString(), otherAmount.getText().toString());
        }
        weeklyExpense.setText(""+income);
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
}