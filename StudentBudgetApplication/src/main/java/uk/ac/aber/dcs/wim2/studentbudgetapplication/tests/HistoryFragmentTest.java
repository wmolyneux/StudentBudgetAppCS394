package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.BudgetPeriodActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.ExpenseActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.IncomeActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.TransactionActivity;

/**
 * This class contains the functionality for testing the history screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class HistoryFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity> {

    private Solo solo;
    private Activity activity;

    public HistoryFragmentTest(){
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
        //if database doesnt exist, setup an example budget period
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

        //change to history screen
        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(2));

        solo.waitForFragmentByTag("history");
    }

    /**
     * Test history displays income and expense transaction in tabs
     */
    public void testHistoryIsDisplaysIncomeAndExpenseTransactions(){
        getToTestState();

        //count initial items in the expense list
        ListView list = (ListView)activity.findViewById(android.R.id.list);
        int initialExpenses = list.getCount();

        //change to the income tab
        ViewGroup tabs = (ViewGroup)activity.findViewById(android.R.id.tabs);
        View incomeTab = tabs.getChildAt(0);
        solo.clickOnView(incomeTab);

        //count initial items in the income list
        list = (ListView)activity.findViewById(android.R.id.list);
        int initialIncomes = list.getCount();

        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);

        //change to new transaction screen
        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(1));
        solo.waitForFragmentByTag("transaction");

        //create new income transaction
        EditText amountEdit = (EditText)activity.findViewById(R.id.amountField);
        EditText descEdit = (EditText)activity.findViewById(R.id.descField);

        solo.enterText(amountEdit, "1");
        solo.enterText(descEdit, "test transaction 1");
        solo.clickOnButton(2);

        //wait for transaction added message and redirect to home screen
        solo.waitForText(activity.getString(R.string.transaction_added));
        solo.waitForFragmentByTag("overview");

        //change to new expense transaction screen
        solo.clickOnImage(0);
        solo.clickOnText(typedArray.getString(1));
        solo.waitForFragmentByTag("transaction");

        amountEdit = (EditText)activity.findViewById(R.id.amountField);
        descEdit = (EditText)activity.findViewById(R.id.descField);

        solo.enterText(amountEdit, "1");
        solo.enterText(descEdit, "test transaction 2");
        solo.clickOnToggleButton(activity.getString(R.string.transaction_expense));
        solo.clickOnButton(2);

        //wait for transaction added message and redirect to home screen
        solo.waitForText(activity.getString(R.string.transaction_added));
        solo.waitForFragmentByTag("overview");

        //change back to history page
        solo.clickOnImage(0);
        solo.clickOnText(typedArray.getString(2));
        solo.waitForFragmentByTag("history");

        //count list and assert that new items appear
        list = (ListView)activity.findViewById(android.R.id.list);
        int newExpenses = list.getCount();
        assertNotSame(initialExpenses, newExpenses);

        //change to income tab
        tabs = (ViewGroup)activity.findViewById(android.R.id.tabs);
        incomeTab = tabs.getChildAt(0);
        solo.clickOnView(incomeTab);

        //assert that new list contains more item than old list
        list = (ListView)activity.findViewById(android.R.id.list);
        int newIncomes = list.getCount();
        assertNotSame(initialIncomes, newIncomes);

        //assert that items in list display correct descriptions when selected
        solo.clickInList(0);
        solo.waitForActivity(TransactionActivity.class);
        assertTrue(solo.searchText("test transaction 2"));
    }

    /**
     * Test that transactions can be removed from the list
     */
    public void testDeleteTransactionRemovesFromList(){
        getToTestState();
        ListView list = (ListView)activity.findViewById(android.R.id.list);
        //if an item is in the list, attempt to remove it
        if(list.getCount()>0){
            removeItemFromList(list);
        }
        //else create and transaction and remove it from the history list
        else{
            TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
            //change to new transaction screen
            solo.clickOnText(activity.getString(R.string.title_activity_detail));
            solo.clickOnText(typedArray.getString(1));
            solo.waitForFragmentByTag("transaction");

            EditText amountEdit = (EditText)activity.findViewById(R.id.amountField);
            EditText descEdit = (EditText)activity.findViewById(R.id.descField);

            //enter transaction information
            solo.enterText(amountEdit, "1");
            solo.enterText(descEdit, "test transaction 1");
            solo.clickOnButton(2);

            //submit transaction
            solo.waitForText(activity.getString(R.string.transaction_added));
            solo.waitForFragmentByTag("overview");

            //check history for new item
            solo.clickOnText(activity.getString(R.string.title_activity_detail));
            solo.clickOnText(typedArray.getString(2));
            solo.waitForFragmentByTag("history");
            list = (ListView)activity.findViewById(android.R.id.list);
            assertTrue(list.getCount()!=0);
            //remove the transaction from the list
            removeItemFromList(list);
        }

    }

    /**
     * Removes first item from a listview using long click
     *
     * @param list - listview containing item to be removed
     */
    private void removeItemFromList(ListView list) {
        int listLength  = list.getCount();
        solo.clickLongInList(0);
        solo.waitForDialogToOpen();
        solo.clickOnText(activity.getString(R.string.yes));
        solo.waitForDialogToClose();
        int newListLength = list.getCount();
        assertNotSame(listLength, newListLength);
    }
}
