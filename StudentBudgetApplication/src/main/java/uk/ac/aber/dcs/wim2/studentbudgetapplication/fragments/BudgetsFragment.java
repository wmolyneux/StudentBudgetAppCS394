package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Budget;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

public class BudgetsFragment extends Fragment implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private Spinner categories;
    private SeekBar weeklySlide;
    private TextView currWeekMin;
    private TextView weeklyText;
    private TextView currWeekMax;
    private TextView remainingWeek;
    private Button create;
    private Button clear;
    private SQLiteDatabaseHelper db;

    private Detail detail;
    private float weeklyBal;
    private int amount;
    private String currency;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_budgets, container, false);

        registerView(inflate);
        return inflate;
    }

    public void registerView(View view){
        categories = (Spinner) view.findViewById(R.id.budgetCategorySpinner);
        currency = FragmentUtilities.getCurrency(getActivity());
        db = new SQLiteDatabaseHelper(getActivity());
        ArrayList<String> tempCategories = new ArrayList<String>();
        TypedArray categoryArray = getResources().obtainTypedArray(R.array.categories);
        for (Category cat : db.getAllCategories()){
            tempCategories.add(categoryArray.getString(cat.getPosition()));
        }
        detail = db.getAllDetails().get(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, tempCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
        categories.setOnItemSelectedListener(this);

        weeklyBal = detail.getWeeklyBalance();

        weeklySlide = (SeekBar) view.findViewById(R.id.weeklyslide);
        weeklyText = (TextView) view.findViewById(R.id.weeklytext);
        currWeekMin = (TextView) view.findViewById(R.id.currentweekmin);
        currWeekMax = (TextView) view.findViewById(R.id.currentweekmax);


        remainingWeek = (TextView) view.findViewById(R.id.remainingweekly);

        clear = (Button) view.findViewById(R.id.clearBudget);
        create = (Button) view.findViewById(R.id.createBudget);
        create.setOnClickListener(this);
        clear.setOnClickListener(this);

        for(Budget budget : db.getAllBudgets()){
            weeklyBal -= budget.getWeekly();
        }

        remainingWeek.setText(currency+BalanceUtilities.getValueAs2dpString(weeklyBal));
        currWeekMax.setText(currency+Math.round(weeklyBal)+"");

        weeklySlide.setOnSeekBarChangeListener(this);
        weeklySlide.setMax((int) weeklyBal);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        categories.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        amount = i;
        weeklyText.setText("Amount of weekly budget: "+currency+amount);
        remainingWeek.setText(currency+BalanceUtilities.getValueAs2dpString(weeklyBal-amount));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.clearBudget:
                weeklyText.setText("Amount of weekly budget: "+currency+"0");
                amount = 0;
                weeklySlide.setProgress(0);
                remainingWeek.setText(currency+BalanceUtilities.getValueAs2dpString(weeklyBal));
                break;
            case R.id.createBudget:
                boolean flag = checkBudgets();
                if(flag){
                    if(weeklySlide.getProgress()!=0){
                        createBudget();
                    }
                    else{
                        Toast.makeText(getActivity(), "Budget must be more than "+currency+"0", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Budget for "+categories.getSelectedItem().toString()+" already exists!",
                            Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    private boolean checkBudgets() {
        for(Budget bud : db.getAllBudgets()){
            if(bud.getCategory() == categories.getSelectedItemPosition()){
                return false;
            }
        }
        return true;
    }

    private void createBudget() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);
        today = today.withDayOfWeek(1);


        String dateString = today.getDayOfMonth()+"/"+today.getMonthOfYear()+"/"+today.getYear();
        Budget budget = new Budget(categories.getSelectedItemPosition(), amount, (int)weeklyBal, dateString);
        db.addBudget(budget);
        Toast.makeText(getActivity(), getActivity().getString(R.string.create_budget), Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new OverviewFragment(), "overview").commit();
    }

}
