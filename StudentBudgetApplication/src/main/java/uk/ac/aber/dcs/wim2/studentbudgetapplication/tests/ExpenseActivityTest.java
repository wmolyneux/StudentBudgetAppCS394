package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
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
 * Created by wim2 on 28/03/2014.
 */
public class ExpenseActivityTest extends ActivityInstrumentationTestCase2<EnterActivity>{

    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public ExpenseActivityTest(){
        super(EnterActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation());
        activity = getActivity();
        newIntent = new Intent(activity, EnterActivity.class);
    }

    public void tearDown(){
        solo.finishOpenedActivities();
    }

    public void getToTestState(){
        if(solo.waitForActivity(DetailActivity.class, 1000)){
            solo.assertCurrentActivity("Should be DetailActivity", DetailActivity.class);
            TestingUtilities.checkDatabase(solo);
            activity.startActivity(newIntent);
//            solo.finishOpenedActivities();
//            this.launchActivity("uk.ac.aber.dcs.wim2.studentbudgetapplication", EnterActivity.class, null);
//
//            solo.sleep(1000);
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
        activity = solo.getCurrentActivity();
        solo.clickOnButton(activity.getString(R.string.next_button));
        solo.waitForActivity(ExpenseActivity.class);
        activity = solo.getCurrentActivity();
    }

    public void testContentsOfActivity(){
        getToTestState();
        EditText rent = (EditText)activity.findViewById(R.id.rentAmount);
        EditText electricity = (EditText)activity.findViewById(R.id.electricityAmount);
        EditText heating = (EditText)activity.findViewById(R.id.heatingAmount);
        EditText internet = (EditText)activity.findViewById(R.id.internetAmount);
        EditText transport = (EditText)activity.findViewById(R.id.transportAmount);
        EditText mobile = (EditText)activity.findViewById(R.id.mobileAmount);
        EditText other = (EditText)activity.findViewById(R.id.otherAmount);
        TextView weeklyExpense = (TextView)activity.findViewById(R.id.ExpenseWeeklyExp);
        solo.waitForText("Rent");
        solo.clearEditText(rent);
        solo.enterText(rent, "100");
        assertEquals("100", rent.getText().toString());
        solo.pressSpinnerItem(0, 1);
        solo.waitForDialogToClose();


        solo.clearEditText(electricity);
        solo.enterText(electricity, "100");
        assertEquals("100", electricity.getText().toString());
        solo.pressSpinnerItem(1, 1);
        solo.waitForDialogToClose();


        solo.clearEditText(heating);
        solo.enterText(heating, "100");
        assertEquals("100", heating.getText().toString());
        solo.pressSpinnerItem(2, 1);
        solo.waitForDialogToClose();

        solo.clearEditText(internet);
        solo.enterText(internet, "100");
        assertEquals("100", internet.getText().toString());
        solo.pressSpinnerItem(3, 1);
        solo.waitForDialogToClose();

        solo.clearEditText(transport);
        solo.enterText(transport, "100");
        assertEquals("100", transport.getText().toString());
        solo.pressSpinnerItem(4, 2);
        solo.waitForDialogToClose();

        solo.clearEditText(mobile);
        solo.enterText(mobile, "100");
        assertEquals("100", mobile.getText().toString());
        solo.pressSpinnerItem(5, 1);
        solo.waitForDialogToClose();

        solo.clearEditText(other);
        solo.enterText(other, "100");
        assertEquals("100", other.getText().toString());
        solo.pressSpinnerItem(6, 1);
        solo.waitForDialogToClose();
    }

    public void testContentsValidation(){
        getToTestState();
        EditText rent = (EditText)activity.findViewById(R.id.rentAmount);
        EditText electricity = (EditText)activity.findViewById(R.id.electricityAmount);
        EditText heating = (EditText)activity.findViewById(R.id.heatingAmount);
        EditText internet = (EditText)activity.findViewById(R.id.internetAmount);
        EditText transport = (EditText)activity.findViewById(R.id.transportAmount);
        EditText mobile = (EditText)activity.findViewById(R.id.mobileAmount);
        EditText other = (EditText)activity.findViewById(R.id.otherAmount);
        TextView weeklyExpense = (TextView)activity.findViewById(R.id.ExpenseWeeklyExp);

        solo.clearEditText(rent);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(rent, "100");

        solo.clearEditText(electricity);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(electricity, "100");

        solo.clearEditText(heating);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(heating, "100");

        solo.clearEditText(internet);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(internet, "100");

        solo.clearEditText(transport);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(transport, "100");

        solo.clearEditText(mobile);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(mobile, "100");

        solo.clearEditText(other);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(other, "100");

        solo.clickOnButton(0);
        solo.waitForActivity(DetailActivity.class);
        solo.assertCurrentActivity("Should now be on Detail activity", DetailActivity.class);
    }
}
