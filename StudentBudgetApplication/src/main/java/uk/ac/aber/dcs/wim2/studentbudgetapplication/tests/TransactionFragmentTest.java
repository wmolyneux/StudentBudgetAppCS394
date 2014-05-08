package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.BudgetPeriodActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.ExpenseActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.IncomeActivity;

/**
 * This class contains the functionality for testing the new transaction screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class TransactionFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity> {

    private Solo solo;
    private Activity activity;

    public TransactionFragmentTest(){
        super(EnterActivity.class);
    }

    /**
     * Setup objects required for testing and prepares the activity to be opened
     */
    public void setUp(){
        solo = new Solo(getInstrumentation());
        activity = getActivity();
    }

    /**
     * Tears down the test and any objects or activities created during the test
     */
    public void tearDown(){
        solo.finishOpenedActivities();
    }

    /**
     * Completes the necessary setup in order to get to the test state for this class
     */
    public void getToTestState(){
        //if database doesnt exist, create a budget with dates, incomes and expenses
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

        //change to new transaction page
        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(1));

        solo.waitForFragmentByTag("transaction");
    }

    /**
     * Test transaction is successfully created and appends balance
     */
    public void testTransactionIsSuccessfullyCreatedAndAppendsBalance(){
        getToTestState();

        //change to home screen
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnImage(0);
        solo.clickOnText(typedArray.getString(0));
        solo.waitForFragmentByTag("overview");

        //check weekly balance before transaction
        TextView weeklyBalance = (TextView)activity.findViewById(R.id.overWeeklyBalance);
        Float beforeBalance = Float.valueOf(weeklyBalance.getText().toString().substring(1, weeklyBalance.getText().toString().length()));

        //change to new transaction page
        solo.clickOnImage(0);
        solo.clickOnText(typedArray.getString(1));
        solo.waitForFragmentByTag("transaction");

        EditText amountEdit = (EditText)activity.findViewById(R.id.amountField);
        EditText descEdit = (EditText)activity.findViewById(R.id.descField);

        //create test transaction
        solo.enterText(amountEdit, "10");
        solo.enterText(descEdit, "Iceland");
        solo.pressSpinnerItem(0, 1);
        solo.clickOnEditText(2);
        solo.waitForDialogToOpen();
        solo.clickOnButton(0);
        solo.waitForDialogToClose();
        solo.clickOnButton(2);

        //submit transaction
        solo.waitForText(activity.getString(R.string.transaction_added));
        solo.waitForFragmentByTag("overview");

        //check weekly balance after transaction
        weeklyBalance = (TextView)activity.findViewById(R.id.overWeeklyBalance);
        Float afterBalance = Float.valueOf(weeklyBalance.getText().toString().substring(1, weeklyBalance.getText().toString().length()));

        //assert that there is a change of the amount of the transaction
        assertEquals((beforeBalance-10), afterBalance);
    }

    /**
     * Test transaction validation
     */
    public void testTransactionValidation(){
        getToTestState();
        EditText amountEdit = (EditText)activity.findViewById(R.id.amountField);
        EditText descEdit = (EditText)activity.findViewById(R.id.descField);

        //attempt to enter new transaction with no amount
        solo.enterText(descEdit, "Iceland");
        solo.pressSpinnerItem(0, 1);
        solo.clickOnEditText(2);
        solo.waitForDialogToOpen();
        solo.clickOnButton(0);
        solo.waitForDialogToClose();
        solo.clickOnButton(2);

        //Enter valid transaction and submit
        solo.enterText(amountEdit, "5");
        solo.clickOnToggleButton(activity.getString(R.string.transaction_expense));
        solo.clickOnButton(2);
    }

}
