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
 * Created by wim2 on 03/04/2014.
 */
public class BudgetFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity> {
    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public BudgetFragmentTest(){
        super(EnterActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation());
        activity = getActivity();
        newIntent = new Intent(activity, EnterActivity.class);
    }

    public void tearDown() throws Exception{
        solo.finishOpenedActivities();

    }

    public void getToTestState(){
        if(solo.waitForActivity(DetailActivity.class, 1000)){
            TestingUtilities.checkDatabase(solo);
            solo.finishOpenedActivities();
            solo = new Solo(getInstrumentation());
            activity.startActivity(newIntent);
        }

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

        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(4));

        solo.waitForFragmentByTag("budget");
    }

    public void testBudgetCanBeCreated(){
        getToTestState();
        solo.pressSpinnerItem(0, 0);
        solo.waitForDialogToClose();
        solo.setProgressBar(0, 30);

        solo.clickOnButton(1);
        solo.waitForText(activity.getString(R.string.create_budget));
        assertTrue(solo.searchText("30"));

        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.categories);
        assertTrue(solo.searchText(typedArray.getString(0)));
    }

    public void testBudgetsCanBeDeletedFromOverviewFragment(){
        getToTestState();
        solo.pressSpinnerItem(0, 0);
        solo.waitForDialogToClose();
        solo.setProgressBar(0, 30);

        solo.clickOnButton(1);
        solo.waitForText(activity.getString(R.string.create_budget));
        solo.clickLongInList(0);
        solo.waitForDialogToOpen();
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.categories);
        assertTrue(solo.searchText(typedArray.getString(0)));

        solo.clickOnText(activity.getString(R.string.yes));
        solo.waitForDialogToClose();

        assertFalse(solo.searchText(typedArray.getString(0)));

    }
}
