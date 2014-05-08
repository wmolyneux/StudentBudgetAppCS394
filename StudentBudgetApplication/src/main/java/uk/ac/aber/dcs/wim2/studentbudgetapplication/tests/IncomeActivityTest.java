package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.Spinner;
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
 * This class contains the functionality for testing the income screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class IncomeActivityTest extends ActivityInstrumentationTestCase2<EnterActivity> {
    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public IncomeActivityTest(){
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
        //check if database exists and remove it if it does
        if(solo.waitForActivity(DetailActivity.class, 1000)){
            solo.assertCurrentActivity("Should be DetailActivity", DetailActivity.class);
            TestingUtilities.checkDatabase(solo);
            activity.startActivity(newIntent);
        }
        //setup budget period dates and move to income screen
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
        solo.assertCurrentActivity("should be IncomeActivity", IncomeActivity.class);
        activity = solo.getCurrentActivity();
    }

    /**
     * Test that the contents of the income activity are correct
     */
    public void testContentsOfActivity(){
        getToTestState();
        solo.sleep(1000);
        //instantiate views
        EditText balanceField = (EditText)activity.findViewById(R.id.balanceAmount);//437.50
        EditText loanField = (EditText)activity.findViewById(R.id.loanAmount);//481.25
        EditText grantField = (EditText)activity.findViewById(R.id.grantAmount);//525
        EditText salaryField = (EditText)activity.findViewById(R.id.wageAmount);//550
        EditText otherField = (EditText)activity.findViewById(R.id.otherAmount);//575
        TextView weeklyIncome = (TextView)activity.findViewById(R.id.incomeWeeklyInc);

        //Enter balance and check is correctly displayed
        solo.clearEditText(balanceField);
        solo.enterText(balanceField, "2000");
        assertEquals("2000", balanceField.getText().toString());

        //Enter loan and check is correctly displayed
        solo.clearEditText(loanField);
        solo.enterText(loanField, "200");
        assertEquals("200", loanField.getText().toString());

        //Enter grant and check is correctly displayed
        solo.clearEditText(grantField);
        solo.enterText(grantField, "200");
        assertEquals("200", grantField.getText().toString());

        //Enter salary and check is correctly displayed, check spinner is correct
        solo.clearEditText(salaryField);
        solo.enterText(salaryField, "100");
        assertEquals("100", salaryField.getText().toString());
        solo.pressSpinnerItem(0, 1);
        solo.waitForDialogToClose();
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.weekly_monthly_yearly);
        assertEquals("Spinner is not selected to monthly", true, solo.isSpinnerTextSelected(0, typedArray.getString(1)));

        //Enter other and check is correctly displayed, check spinner is correct
        solo.clearEditText(otherField);
        solo.enterText(otherField, "100");
        assertEquals("100", otherField.getText().toString());
        solo.pressSpinnerItem(1, 1);
        solo.waitForDialogToClose();
        assertEquals("Spinner is not selected to monthly", true, solo.isSpinnerTextSelected(1, typedArray.getString(1)));

    }

    /**
     * Test contents validation of the income form
     */
    public void testContentsValidation(){
        getToTestState();

        //instantiate views
        EditText balanceField = (EditText)activity.findViewById(R.id.balanceAmount);//437.50
        EditText loanField = (EditText)activity.findViewById(R.id.loanAmount);//481.25
        EditText grantField = (EditText)activity.findViewById(R.id.grantAmount);//525
        EditText salaryField = (EditText)activity.findViewById(R.id.wageAmount);//550
        EditText otherField = (EditText)activity.findViewById(R.id.otherAmount);//575

        //validate balance is correct
        solo.clearEditText(balanceField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(balanceField, "100");

        //validate loan is correct
        solo.clearEditText(loanField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(loanField, "100");

        //validate grant is correct
        solo.clearEditText(grantField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(grantField, "100");

        //validate salary is correct
        solo.clearEditText(salaryField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(salaryField, "100");

        //validate other is correct
        solo.clearEditText(otherField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(otherField, "100");

        //assert that the next button redirects to expense activity when all incomes pass validation
        solo.clickOnButton(0);
        solo.waitForActivity(ExpenseActivity.class);
        solo.assertCurrentActivity("Should be expense activity", ExpenseActivity.class);
    }


}
