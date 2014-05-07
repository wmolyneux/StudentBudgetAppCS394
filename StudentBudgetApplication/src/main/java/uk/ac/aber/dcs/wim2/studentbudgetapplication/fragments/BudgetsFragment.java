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

/**
 * This class contains the functionality for the budget screen of the application.
 *
 * @author wim2
 * @version 1.0
 */
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
        View inflate = inflater.inflate(R.layout.fragment_budgets, container, false);

        registerView(inflate);
        return inflate;
    }

    /**
     * Registers views displayed on the screen, including displaying correct values, and setting up onClick listeners
     *
     * @param view - fragment view
     */
    public void registerView(View view){
        categories = (Spinner) view.findViewById(R.id.budgetCategorySpinner);
        currency = FragmentUtilities.getCurrency(getActivity());

        //get all categories from database to display in the spinner
        db = new SQLiteDatabaseHelper(getActivity());
        ArrayList<String> tempCategories = new ArrayList<String>();
        TypedArray categoryArray = getResources().obtainTypedArray(R.array.categories);
        for (Category cat : db.getAllCategories()){
            tempCategories.add(categoryArray.getString(cat.getPosition()));
        }
        detail = db.getAllDetails().get(0);

        //set categories in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, tempCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
        categories.setOnItemSelectedListener(this);

        weeklyBal = detail.getWeeklyBalance();

        //register views on screen
        weeklySlide = (SeekBar) view.findViewById(R.id.weeklyslide);
        weeklyText = (TextView) view.findViewById(R.id.weeklytext);
        currWeekMin = (TextView) view.findViewById(R.id.currentweekmin);
        currWeekMax = (TextView) view.findViewById(R.id.currentweekmax);
        remainingWeek = (TextView) view.findViewById(R.id.remainingweekly);

        clear = (Button) view.findViewById(R.id.clearBudget);
        create = (Button) view.findViewById(R.id.createBudget);

        //set on click listeners
        create.setOnClickListener(this);
        clear.setOnClickListener(this);

        //calculate weekly balance after all existing budgets are taken into consideration
        for(Budget budget : db.getAllBudgets()){
            weeklyBal -= budget.getWeekly();
        }

        remainingWeek.setText(currency+BalanceUtilities.getValueAs2dpString(weeklyBal));
        currWeekMax.setText(currency+Math.round(weeklyBal)+"");

        //setup slider
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
        weeklyText.setText(getActivity().getString(R.string.budget_amount)+" "+currency+amount);
        remainingWeek.setText(currency+BalanceUtilities.getValueAs2dpString(weeklyBal-amount));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
            //if clear budget button is pressed, clear the screen back to default values
            case R.id.clearBudget:
                weeklyText.setText(getActivity().getString(R.string.budget_amount)+" "+currency+"0");
                amount = 0;
                weeklySlide.setProgress(0);
                remainingWeek.setText(currency+BalanceUtilities.getValueAs2dpString(weeklyBal));
                break;
            //if create button is pressed, validate input then create budget if passes
            case R.id.createBudget:
                boolean flag = checkBudgets();
                if(flag){
                    if(weeklySlide.getProgress()!=0){
                        createBudget();
                    }
                    else{
                        Toast.makeText(getActivity(), getActivity().getString(R.string.budget_must_be_more_than)+" "+currency+"0", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), categories.getSelectedItem().toString()+" "+getActivity().getString(R.string.budget_exists),
                            Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    /**
     * Check that no budget already exists for the given category in the database
     *
     * @return true if no matching budgets exist
     */
    private boolean checkBudgets() {
        for(Budget bud : db.getAllBudgets()){
            if(bud.getCategory() == categories.getSelectedItemPosition()){
                return false;
            }
        }
        return true;
    }

    /**
     * Create budget object and add to Budget table in database
     */
    private void createBudget() {
        //get today's date
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DateTime today = new DateTime(year, month+1, day, 0, 0);
        today = today.withDayOfWeek(1);

        //create budget object with values from on screen form.
        String dateString = today.getDayOfMonth()+"/"+today.getMonthOfYear()+"/"+today.getYear();
        Budget budget = new Budget(categories.getSelectedItemPosition(), amount, (int)weeklyBal, dateString);

        //add to database and alert the user
        db.addBudget(budget);
        Toast.makeText(getActivity(), getActivity().getString(R.string.create_budget), Toast.LENGTH_LONG).show();

        //redirect to home screen
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new OverviewFragment(), "overview").commit();
    }

}
