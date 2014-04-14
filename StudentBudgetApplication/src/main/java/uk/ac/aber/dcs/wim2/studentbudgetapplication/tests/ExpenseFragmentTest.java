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
 * Created by wim2 on 14/04/2014.
 */
public class ExpenseFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity> {
    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public ExpenseFragmentTest(){
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
        solo.clickOnText(typedArray.getString(6));

        solo.waitForFragmentByTag("expense");
    }

    public void testIncomesChangeAndSaveInDatabase(){
        getToTestState();
        EditText rentAmount = (EditText) activity.findViewById(R.id.updateRentAmount);
        EditText electricityAmount = (EditText) activity.findViewById(R.id.updateElectricityAmount);
        EditText heatingAmount = (EditText) activity.findViewById(R.id.updateHeatingAmount);
        EditText internetAmount = (EditText) activity.findViewById(R.id.updateInternetAmount);
        EditText transportAmount = (EditText) activity.findViewById(R.id.updateTransportAmount);
        EditText mobileAmount = (EditText) activity.findViewById(R.id.updateMobileAmount);
        EditText otherAmount = (EditText) activity.findViewById(R.id.updateOtherAmount);

        solo.clearEditText(rentAmount);
        solo.enterText(rentAmount, "11");

        solo.clearEditText(electricityAmount);
        solo.enterText(electricityAmount, "22");

        solo.clearEditText(heatingAmount);
        solo.enterText(heatingAmount, "33");

        solo.clearEditText(internetAmount);
        solo.enterText(internetAmount, "44");

        solo.clearEditText(transportAmount);
        solo.enterText(transportAmount, "55");

        solo.clearEditText(mobileAmount);
        solo.enterText(mobileAmount, "66");

        solo.clearEditText(otherAmount);
        solo.enterText(otherAmount, "77");

        solo.clickOnButton(activity.getString(R.string.update_button));

        solo.clickOnImage(0);
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnText(typedArray.getString(5));

        solo.waitForFragmentByTag("income");

        solo.searchText("111");
        solo.searchText("222");
        solo.searchText("333");
        solo.searchText("444");
        solo.searchText("555");
    }
}
