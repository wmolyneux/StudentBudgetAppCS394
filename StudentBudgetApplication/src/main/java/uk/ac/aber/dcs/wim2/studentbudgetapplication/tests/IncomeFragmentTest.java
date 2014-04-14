package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.Spinner;

import com.robotium.solo.Solo;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.BudgetPeriodActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.ExpenseActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.IncomeActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.TestingUtilities;

/**
 * Created by wim2 on 14/04/2014.
 */
public class IncomeFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity> {
    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public IncomeFragmentTest(){
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
        solo.clickOnText(typedArray.getString(5));

        solo.waitForFragmentByTag("income");
    }

    public void testIncomesChangeAndSaveInDatabase(){
        getToTestState();
        EditText balanceAmount = (EditText)activity.findViewById(R.id.updateBalanceAmount);
        EditText loanAmount = (EditText) activity.findViewById(R.id.updateLoanAmount);
        EditText grantAmount = (EditText) activity.findViewById(R.id.updateGrantAmount);
        EditText wageAmount = (EditText) activity.findViewById(R.id.updateWageAmount);
        EditText otherAmount = (EditText) activity.findViewById(R.id.updateOtherAmount);

        solo.clearEditText(balanceAmount);
        solo.enterText(balanceAmount, "111");

        solo.clearEditText(loanAmount);
        solo.enterText(loanAmount, "222");

        solo.clearEditText(grantAmount);
        solo.enterText(grantAmount, "333");

        solo.clearEditText(wageAmount);
        solo.enterText(wageAmount, "444");

        solo.clearEditText(otherAmount);
        solo.enterText(otherAmount, "555");

        solo.clickOnButton(activity.getString(R.string.update_button));
        solo.waitForFragmentByTag("overview");

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
