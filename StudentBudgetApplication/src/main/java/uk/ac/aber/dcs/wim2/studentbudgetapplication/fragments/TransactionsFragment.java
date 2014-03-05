package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.widget.AppWidgetProvider;

public class TransactionsFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, AdapterView.OnItemSelectedListener{

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

    int day;
    int month;
    int year;
    String todaysDate;

    DatePickerDialog dateDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_transactions, container, false);
        registerViews(inflate);

        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        todaysDate = day+"/"+(month+1)+"/"+year;
        date.setText(todaysDate);
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selYear, int selMonth, int selDay) {
                date.setText(selDay+"/"+(1+selMonth)+"/"+selYear);
            }
        };

        dateDialog = new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    private void registerViews(View inflate) {
        amount = (EditText) inflate.findViewById(R.id.amountField);
        shortDesc = (EditText) inflate.findViewById(R.id.descField);
        type = (ToggleButton) inflate.findViewById(R.id.typeButton);
        category = (Spinner) inflate.findViewById(R.id.categorySpinner);

        db = new SQLiteDatabaseHelper(getActivity());
        detail = db.getAllDetails().get(0);
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
        date.setOnTouchListener(this);


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
                    BalanceUtilities.updateWidget(getActivity());

                    Toast.makeText(getActivity(), "Transaction added", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new OverviewFragment()).commit();
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
        category.setSelection(0);
        date.setText(todaysDate);

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
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.dateField:
                dateDialog.show();
                break;
        }

        return false;
    }


}
