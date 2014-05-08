package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.Spinner;

import com.robotium.solo.Solo;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.BudgetPeriodActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.ExpenseActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.IncomeActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.TestingUtilities;

/**
 * This class contains the functionality for testing the manage income screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class IncomeFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity> {
    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public IncomeFragmentTest(){
        super(EnterActivity.class);
    }

    /**
     * Setup objects required for testing and prepares the activity to be opened
     */
    public void setUp(){
        solo = new Solo(getInstrumentation());
        activity = getActivity();
        newIntent = new Intent(activity, EnterActivity.class);
    }

    /**
     * Tears down the test and any objects or activities created during the test
     */
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    /**
     * Completes the necessary setup in order to get to the test state for this class
     */
    public void getToTestState(){
        //if the database does not exist, setup the budget with dates, incomes and expenses
        if(!solo.waitForActivity(DetailActivity.class, 1000)){
            solo.clickOnButton(0);
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            solo.clickOnEditText(1);
            solo.setDatePicker(0, year, month + 1, day);
            solo.clickOnButton(0);
            solo.waitForActivity(BudgetPeriodActivity.class);
            activity = solo.getCurrentActivity();
            solo.clickOnButton(activity.getString(R.string.next_button));
            solo.waitForActivity(IncomeActivity.class);
            solo.clearEditText(0);
            solo.enterText(0, "2000");
            solo.clickOnButton(0);
            solo.waitForActivity(ExpenseActivity.class);
            solo.clickOnButton(0);
            solo.waitForActivity(DetailActivity.class);
            activity = solo.getCurrentActivity();
        }
        else{
            activity = solo.getCurrentActivity();
        }
        //redirect to manage incomes screen
        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(5));

        solo.waitForFragmentByTag("income");
    }

    /**
     * Test the incomes can be updated in the database
     */
    public void testIncomesChangeAndSaveInDatabase(){
        getToTestState();

        //initiate views
        EditText balanceAmount = (EditText)activity.findViewById(R.id.updateBalanceAmount);
        EditText loanAmount = (EditText) activity.findViewById(R.id.updateLoanAmount);
        EditText grantAmount = (EditText) activity.findViewById(R.id.updateGrantAmount);
        EditText wageAmount = (EditText) activity.findViewById(R.id.updateWageAmount);
        EditText otherAmount = (EditText) activity.findViewById(R.id.updateOtherAmount);

        //update balance
        solo.clearEditText(balanceAmount);
        solo.enterText(balanceAmount, "111");

        //update loan
        solo.clearEditText(loanAmount);
        solo.enterText(loanAmount, "222");

        //update grant
        solo.clearEditText(grantAmount);
        solo.enterText(grantAmount, "333");

        //update wage
        solo.clearEditText(wageAmount);
        solo.enterText(wageAmount, "444");

        //update other
        solo.clearEditText(otherAmount);
        solo.enterText(otherAmount, "555");

        //press update button
        solo.clickOnButton(activity.getString(R.string.update_button));
        solo.waitForFragmentByTag("overview");

        //change back to manage income screen
        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(5));

        solo.waitForFragmentByTag("income");

        //check updated values are displayed
        solo.searchText("111");
        solo.searchText("222");
        solo.searchText("333");
        solo.searchText("444");
        solo.searchText("555");

    }
}
