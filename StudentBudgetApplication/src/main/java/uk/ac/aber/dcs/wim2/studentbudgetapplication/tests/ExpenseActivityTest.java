package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.Intent;
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
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.TestingUtilities;

/**
 * This class contains the functionality for testing the expense screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class ExpenseActivityTest extends ActivityInstrumentationTestCase2<EnterActivity>{

    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public ExpenseActivityTest(){
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
    public void tearDown(){
        solo.finishOpenedActivities();
    }

    /**
     * Completes the necessary setup in order to get to the test state for this class
     */
    public void getToTestState(){
        //if the database exists, remove it and start application again
        if(solo.waitForActivity(DetailActivity.class, 1000)){
            solo.assertCurrentActivity("Should be DetailActivity", DetailActivity.class);
            TestingUtilities.checkDatabase(solo);
            activity.startActivity(newIntent);
        }

        //setup the budget period dates and incomes ready for the test
        solo.clickOnButton(0);
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        solo.clickOnEditText(1);
        solo.setDatePicker(0, year, month + 1, day);
        solo.clickOnButton(0);
        solo.waitForDialogToClose();
        solo.clickOnButton(activity.getString(R.string.next_button));
        solo.waitForActivity(IncomeActivity.class);
        activity = solo.getCurrentActivity();
        solo.clickOnButton(activity.getString(R.string.next_button));
        solo.waitForActivity(ExpenseActivity.class);
        activity = solo.getCurrentActivity();
    }

    /**
     * Test that the contents of activity are correct
     */
    public void testContentsOfActivity(){
        getToTestState();

        //instantiate objects
        EditText rent = (EditText)activity.findViewById(R.id.rentAmount);
        EditText electricity = (EditText)activity.findViewById(R.id.electricityAmount);
        EditText heating = (EditText)activity.findViewById(R.id.heatingAmount);
        EditText internet = (EditText)activity.findViewById(R.id.internetAmount);
        EditText transport = (EditText)activity.findViewById(R.id.transportAmount);
        EditText mobile = (EditText)activity.findViewById(R.id.mobileAmount);
        EditText other = (EditText)activity.findViewById(R.id.otherAmount);
        TextView weeklyExpense = (TextView)activity.findViewById(R.id.ExpenseWeeklyExp);

        //setup rent expenses
        solo.waitForText("Rent");
        solo.clearEditText(rent);
        solo.enterText(rent, "100");
        assertEquals("100", rent.getText().toString());
        solo.pressSpinnerItem(0, 1);
        solo.waitForDialogToClose();

        //setup electricity expenses
        solo.clearEditText(electricity);
        solo.enterText(electricity, "100");
        assertEquals("100", electricity.getText().toString());
        solo.pressSpinnerItem(1, 1);
        solo.waitForDialogToClose();

        //setup heating expenses
        solo.clearEditText(heating);
        solo.enterText(heating, "100");
        assertEquals("100", heating.getText().toString());
        solo.pressSpinnerItem(2, 1);
        solo.waitForDialogToClose();

        //setup internet expenses
        solo.clearEditText(internet);
        solo.enterText(internet, "100");
        assertEquals("100", internet.getText().toString());
        solo.pressSpinnerItem(3, 1);
        solo.waitForDialogToClose();

        //setup transport expenses
        solo.clearEditText(transport);
        solo.enterText(transport, "100");
        assertEquals("100", transport.getText().toString());
        solo.pressSpinnerItem(4, 2);
        solo.waitForDialogToClose();

        //setup mobile expenses
        solo.clearEditText(mobile);
        solo.enterText(mobile, "100");
        assertEquals("100", mobile.getText().toString());
        solo.pressSpinnerItem(5, 1);
        solo.waitForDialogToClose();

        //setup other expenses
        solo.clearEditText(other);
        solo.enterText(other, "100");
        assertEquals("100", other.getText().toString());
        solo.pressSpinnerItem(6, 1);
        solo.waitForDialogToClose();
    }

    /**
     * Tests the validation works for the contents provided
     */
    public void testContentsValidation(){
        getToTestState();

        //instantiate views
        EditText rent = (EditText)activity.findViewById(R.id.rentAmount);
        EditText electricity = (EditText)activity.findViewById(R.id.electricityAmount);
        EditText heating = (EditText)activity.findViewById(R.id.heatingAmount);
        EditText internet = (EditText)activity.findViewById(R.id.internetAmount);
        EditText transport = (EditText)activity.findViewById(R.id.transportAmount);
        EditText mobile = (EditText)activity.findViewById(R.id.mobileAmount);
        EditText other = (EditText)activity.findViewById(R.id.otherAmount);
        TextView weeklyExpense = (TextView)activity.findViewById(R.id.ExpenseWeeklyExp);


        //validate rent expense
        solo.clearEditText(rent);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(rent, "100");

        //validate electricity expense
        solo.clearEditText(electricity);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(electricity, "100");

        //validate heating expense
        solo.clearEditText(heating);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(heating, "100");

        //validate internet expense
        solo.clearEditText(internet);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(internet, "100");

        //validate transport expense
        solo.clearEditText(transport);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(transport, "100");

        //validate mobile expense
        solo.clearEditText(mobile);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(mobile, "100");

        //validate other expense
        solo.clearEditText(other);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(other, "100");

        //check all validation passes
        solo.clickOnButton(0);
        solo.waitForActivity(DetailActivity.class);
        solo.assertCurrentActivity("Should now be on Detail activity", DetailActivity.class);
    }
}
