package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ImageButton;

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

/**
 * This class contains the functionality for testing the report screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class ReportFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity>{
    private Solo solo;
    private Activity activity;

    public ReportFragmentTest(){
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
        //if database doesnt exist, setup budget with dates, incomes and expenses
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

        //change to the report screen
        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(3));

        solo.waitForFragmentByTag("report");
    }

    /**
     * Test that the report fragment displays correct categories
     */
    public void testFragmentDisplaysCorrectCategories(){
        getToTestState();

        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);

        //change to home screen
        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(0));
        solo.waitForFragmentByTag("overview");

        //remove all transactions from database
        SQLiteDatabaseHelper db = new SQLiteDatabaseHelper(activity);
        for(Transaction t : db.getAllTransactions()){
            db.deleteTransaction(t);
        }

        //change to report screen
        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(3));
        solo.waitForFragmentByTag("report");

        //check no data message is displayed
        assertTrue(solo.searchText(activity.getString(R.string.report_no_data)));

        //change to new transaction screen
        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(1));
        solo.waitForFragmentByTag("transaction");

        //create an test transaction
        EditText amountEdit = (EditText)activity.findViewById(R.id.amountField);
        EditText descEdit = (EditText)activity.findViewById(R.id.descField);
        solo.enterText(amountEdit, "1");
        solo.enterText(descEdit, "test transaction 1");
        solo.clickOnButton(2);

        //submit transaction
        solo.waitForText(activity.getString(R.string.transaction_added));
        solo.waitForFragmentByTag("overview");

        //change to report screen
        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(3));
        solo.waitForFragmentByTag("report");

        TypedArray categoryArray = activity.getResources().obtainTypedArray(R.array.categories);

        //assert that the new transaction displays on the key of the chart
        assertTrue(solo.searchText(categoryArray.getString(db.getAllTransactions().get(0).getCategory())));

    }

    /**
     * test other months also display transactions
     */
    public void testOtherMonthsAlsoDisplayTransactions(){
        getToTestState();

        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);

        //change to home screen
        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(0));
        solo.waitForFragmentByTag("overview");

        //remove all transactions in the database
        SQLiteDatabaseHelper db = new SQLiteDatabaseHelper(activity);
        for(Transaction t : db.getAllTransactions()){
            db.deleteTransaction(t);
        }

        //change to report screen
        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(3));
        solo.waitForFragmentByTag("report");

        //check that no data message is displayed
        assertTrue(solo.searchText(activity.getString(R.string.report_no_data)));

        //change to new transaction screen
        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(1));
        solo.waitForFragmentByTag("transaction");

        //enter in transaction for last month
        EditText amountEdit = (EditText)activity.findViewById(R.id.amountField);
        EditText descEdit = (EditText)activity.findViewById(R.id.descField);
        solo.enterText(amountEdit, "1");
        solo.enterText(descEdit, "test transaction 1");
        solo.clickOnEditText(2);
        solo.waitForDialogToOpen();
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        solo.setDatePicker(0, year, month - 1, day);
        solo.clickOnButton(0);
        solo.waitForDialogToClose();
        solo.clickOnButton(2);

        //submit transaction
        solo.waitForText(activity.getString(R.string.transaction_added));
        solo.waitForFragmentByTag("overview");

        //change to report screen
        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(3));
        solo.waitForFragmentByTag("report");

        ImageButton backMonthButton = (ImageButton)activity.findViewById(R.id.backDateButton);

        //click on back month button
        solo.clickOnView(backMonthButton);
        TypedArray categoryArray = activity.getResources().obtainTypedArray(R.array.categories);

        //assert that transaction is displayed
        assertTrue(solo.searchText(categoryArray.getString(db.getAllTransactions().get(0).getCategory())));

    }

}
