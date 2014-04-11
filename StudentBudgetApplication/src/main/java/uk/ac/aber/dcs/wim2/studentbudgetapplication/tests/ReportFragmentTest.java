package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
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
 * Created by wim2 on 03/04/2014.
 */
public class ReportFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity>{
    private Solo solo;
    private Activity activity;

    public ReportFragmentTest(){
        super(EnterActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation());
        activity = getActivity();
    }

    public void tearDown(){
        solo.finishOpenedActivities();
    }

    public void getToTestState(){
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
        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(3));

        solo.waitForFragmentByTag("report");
    }

    public void testFragmentDisplaysCorrectCategories(){
        getToTestState();

        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);

        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(0));
        solo.waitForFragmentByTag("overview");

        SQLiteDatabaseHelper db = new SQLiteDatabaseHelper(activity);
        for(Transaction t : db.getAllTransactions()){
            db.deleteTransaction(t);
        }

        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(3));
        solo.waitForFragmentByTag("report");

        assertTrue(solo.searchText(activity.getString(R.string.report_no_data)));

        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(1));
        solo.waitForFragmentByTag("transaction");

        EditText amountEdit = (EditText)activity.findViewById(R.id.amountField);
        EditText descEdit = (EditText)activity.findViewById(R.id.descField);
        solo.enterText(amountEdit, "1");
        solo.enterText(descEdit, "test transaction 1");
        solo.clickOnButton(2);

        solo.waitForText(activity.getString(R.string.transaction_added));
        solo.waitForFragmentByTag("overview");

        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(3));
        solo.waitForFragmentByTag("report");

        TypedArray categoryArray = activity.getResources().obtainTypedArray(R.array.categories);

        assertTrue(solo.searchText(categoryArray.getString(db.getAllTransactions().get(0).getCategory())));

    }

    public void testOtherMonthsAlsoDisplayTransactions(){
        getToTestState();

        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);

        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(0));
        solo.waitForFragmentByTag("overview");

        SQLiteDatabaseHelper db = new SQLiteDatabaseHelper(activity);
        for(Transaction t : db.getAllTransactions()){
            db.deleteTransaction(t);
        }

        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(3));
        solo.waitForFragmentByTag("report");

        assertTrue(solo.searchText(activity.getString(R.string.report_no_data)));

        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(1));
        solo.waitForFragmentByTag("transaction");

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

        solo.waitForText(activity.getString(R.string.transaction_added));
        solo.waitForFragmentByTag("overview");

        solo.clickOnText(activity.getString(R.string.title_activity_detail));
        solo.clickOnText(typedArray.getString(3));
        solo.waitForFragmentByTag("report");

        ImageButton backMonthButton = (ImageButton)activity.findViewById(R.id.backDateButton);

        solo.clickOnView(backMonthButton);
        TypedArray categoryArray = activity.getResources().obtainTypedArray(R.array.categories);

        assertTrue(solo.searchText(categoryArray.getString(db.getAllTransactions().get(0).getCategory())));

    }

}
