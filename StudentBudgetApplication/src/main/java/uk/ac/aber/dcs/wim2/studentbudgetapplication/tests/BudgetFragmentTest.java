package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.robotium.solo.Solo;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.BudgetPeriodActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.ExpenseActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.IncomeActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.TestingUtilities;

/**
 * This class contains the functionality for testing the budget screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class BudgetFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity> {
    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public BudgetFragmentTest(){
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
     *
     * @throws Exception
     */
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();

    }

    /**
     * Completes the necessary setup in order to get to the test state for this class
     */
    public void getToTestState(){
        //if database exists, clear the database and reopen the application
        if(solo.waitForActivity(DetailActivity.class, 1000)){
            TestingUtilities.checkDatabase(solo);
            solo.finishOpenedActivities();
            solo = new Solo(getInstrumentation());
            activity.startActivity(newIntent);
        }

        //setup a budget with dates, incomes and expenses
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
        solo.clearEditText(0);
        solo.enterText(0, "2000");
        solo.clickOnButton(0);
        solo.waitForActivity(ExpenseActivity.class);
        solo.clickOnButton(0);
        solo.waitForActivity(DetailActivity.class);
        activity = solo.getCurrentActivity();

        //Change to budget screen using navigation menu
        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(4));

        solo.waitForFragmentByTag("budget");
    }

    /**
     * Tests that budgets can be created
     */
    public void testBudgetCanBeCreated(){
        getToTestState();

        //fill in fields for creating a budget
        solo.pressSpinnerItem(0, 0);
        solo.waitForDialogToClose();
        solo.setProgressBar(0, 30);

        solo.clickOnButton(1);
        solo.waitForText(activity.getString(R.string.create_budget));

        //check that budget appears on home screen
        assertTrue(solo.searchText("30"));
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.categories);
        assertTrue(solo.searchText(typedArray.getString(0)));
    }

    /**
     * Tests that budgets can be deleted from the home page
     */
    public void testBudgetsCanBeDeletedFromOverviewFragment(){
        getToTestState();

        //fill in fields for creating a budget
        solo.pressSpinnerItem(0, 0);
        solo.waitForDialogToClose();
        solo.setProgressBar(0, 30);

        solo.clickOnButton(1);
        solo.waitForText(activity.getString(R.string.create_budget));

        //long click budget on home screen to open delete dialog
        solo.clickLongInList(0);
        solo.waitForDialogToOpen();
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.categories);
        assertTrue(solo.searchText(typedArray.getString(0)));

        //select yes to delete
        solo.clickOnText(activity.getString(R.string.yes));
        solo.waitForDialogToClose();

        //check that it no longer exists
        assertFalse(solo.searchText(typedArray.getString(0)));

    }
}
