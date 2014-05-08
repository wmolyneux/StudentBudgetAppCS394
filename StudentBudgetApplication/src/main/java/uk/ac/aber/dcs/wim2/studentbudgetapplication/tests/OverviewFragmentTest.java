package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.BudgetPeriodActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.ExpenseActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.IncomeActivity;

/**
 * This class contains the functionality for testing the home screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class OverviewFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity>{
    private Solo solo;
    private Activity activity;

    public OverviewFragmentTest(){
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

    }

    /**
     * Test that the home page is displayed correctly
     */
    public void testCheckCorrectActivityAndFragmentAreDisplayed(){
        getToTestState();
        solo.assertCurrentActivity("Should be the detail activity", DetailActivity.class);
        DetailActivity det = (DetailActivity)solo.getCurrentActivity();
        assertNotNull(det.getSupportFragmentManager().findFragmentByTag("overview"));

        //check that navigation menu correctly displays
        solo.clickOnImage(0);
        TypedArray itemArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.waitForText(itemArray.getString(0));
        solo.clickOnText(itemArray.getString(0));

    }


}
