package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.BudgetPeriodActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.ExpenseActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.IncomeActivity;

/**
 * This class contains the functionality for testing the manage expense screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class ExpenseFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity> {
    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public ExpenseFragmentTest(){
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
        //setup initial budget if it does not already exist in the database
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

        //change to the manage expenses screen
        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(6));
        solo.waitForFragmentByTag("expense");
    }

    /**
     * Test Expenses can be updated in the database
     */
    public void testExpensesChangeAndSaveInDatabase(){
        getToTestState();

        //initiate views
        EditText rentAmount = (EditText) activity.findViewById(R.id.updateRentAmount);
        EditText electricityAmount = (EditText) activity.findViewById(R.id.updateElectricityAmount);
        EditText heatingAmount = (EditText) activity.findViewById(R.id.updateHeatingAmount);
        EditText internetAmount = (EditText) activity.findViewById(R.id.updateInternetAmount);
        EditText transportAmount = (EditText) activity.findViewById(R.id.updateTransportAmount);
        EditText mobileAmount = (EditText) activity.findViewById(R.id.updateMobileAmount);
        EditText otherAmount = (EditText) activity.findViewById(R.id.updateOtherAmount);

        //update rent
        solo.clearEditText(rentAmount);
        solo.enterText(rentAmount, "11");

        //update electricity
        solo.clearEditText(electricityAmount);
        solo.enterText(electricityAmount, "22");

        //update heating
        solo.clearEditText(heatingAmount);
        solo.enterText(heatingAmount, "33");

        //update internet
        solo.clearEditText(internetAmount);
        solo.enterText(internetAmount, "44");

        //update transport
        solo.clearEditText(transportAmount);
        solo.enterText(transportAmount, "55");

        //update mobile
        solo.clearEditText(mobileAmount);
        solo.enterText(mobileAmount, "66");

        //update other
        solo.clearEditText(otherAmount);
        solo.enterText(otherAmount, "77");

        //update
        solo.clickOnButton(activity.getString(R.string.update_button));

        //return to expense page
        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(5));

        solo.waitForFragmentByTag("expense");

        //check that new values exist
        solo.searchText("111");
        solo.searchText("222");
        solo.searchText("333");
        solo.searchText("444");
        solo.searchText("555");
    }
}
