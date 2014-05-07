package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.app.DatePickerDialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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

/**
 * This class contains the functionality for the budget screen of the application.
 *
 * @author wim2
 * @version 1.0
 */
public class TransactionsFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    //views from the layout
    private EditText amount;
    private EditText shortDesc;
    private ToggleButton type;
    private Spinner category;
    private EditText date;
    private Button clear;
    private Button create;

    //saved instance account
    private Detail detail;

    //values for new transaction
    private String tmpType;
    private SQLiteDatabaseHelper db;

    private int day;
    private int month;
    private int year;
    private String todaysDate;

    private DatePickerDialog dateDialog;
    private String description;


    /**
     * Called when creating a fragment inflating the view and instantiating objects
     *
     * @param inflater - layout inflater
     * @param container - container of the view
     * @param savedInstanceState - bundled state
     *
     * @return - view that has been inflated
     */
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

    /**
     * Called when activity is created to ensure the setup of the screen is done before the main thread
     *
     * @param savedInstanceState - bundled state
     */
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

    /**
     * Registers views displayed on the screen, including displaying correct values, and setting up onClick listeners
     *
     * @param view - fragment view
     */
    private void registerViews(View inflate) {
        amount = (EditText) inflate.findViewById(R.id.amountField);
        shortDesc = (EditText) inflate.findViewById(R.id.descField);
        type = (ToggleButton) inflate.findViewById(R.id.typeButton);
        category = (Spinner) inflate.findViewById(R.id.categorySpinner);

        db = new SQLiteDatabaseHelper(getActivity());

        detail = db.getAllDetails().get(0);
        TypedArray categoryArray = getResources().obtainTypedArray(R.array.categories);

        ArrayList<String> tempCategories = new ArrayList<String>();
        for (Category cat : db.getAllCategories()){
            tempCategories.add(categoryArray.getString(cat.getPosition()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, tempCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(adapter);

        category.setOnItemSelectedListener(this);

        date = (EditText) inflate.findViewById(R.id.dateField);
        date.setOnTouchListener(this);
        type.setOnCheckedChangeListener(this);


        clear = (Button) inflate.findViewById(R.id.clearButton);
        create = (Button) inflate.findViewById(R.id.createTransButton);

        create.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    /**
     * Called when an item with an OnClickListener is clicked.
     * Used for when the next button is pressed to redirect to the next screen.
     *
     * @param view - View that has been pressed.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createTransButton:
                if(validateInput()){
                    Transaction newTrans =
                            new Transaction(Float.valueOf(amount.getText().toString()),
                                    description, tmpType, category.getSelectedItemPosition(), date.getText().toString());
                    SQLiteDatabaseHelper db = new SQLiteDatabaseHelper(getActivity());
                    db.addTransaction(newTrans);
                    BalanceUtilities.updateWidget(getActivity());

                    Toast.makeText(getActivity(), getString(R.string.transaction_added), Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new OverviewFragment(), "overview").commit();
                }
                break;
            case R.id.clearButton:
                cleanForm();
                break;
        }

    }

    /**
     * clean the form back to its default values
     */
    public void cleanForm(){
        amount.setText("");
        shortDesc.setText("");
        type.setChecked(true);
        category.setSelection(0);
        date.setText(todaysDate);

    }


    /**
     * Validate the input in the form
     *
     * @return - true if passes
     */
    private boolean validateInput() {
        if(type.isChecked()){
            tmpType = "plus";
        }
        else{
            tmpType = "minus";
        }
        if(shortDesc.getText().toString().isEmpty()){
            description = getString(R.string.no_desc);
        }
        else{
            description = shortDesc.getText().toString();
        }
        if(amount.getText().toString().isEmpty() || Float.valueOf(amount.getText().toString()) == 0){
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

    /**
     * called when the toggle button is changed, changing the default category depending on the state
     * @param compoundButton - toggle button
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()){
            case R.id.typeButton:
                if(compoundButton.isChecked()){
                    category.setSelection(7);
                }
                else{
                    category.setSelection(0);
                }
                break;
        }
    }
}
