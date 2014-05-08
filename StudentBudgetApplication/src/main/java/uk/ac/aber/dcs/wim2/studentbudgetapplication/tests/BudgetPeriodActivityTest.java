package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.BudgetPeriodActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.IncomeActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.TestingUtilities;

/**
 * This class contains the functionality for testing the budget period screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class BudgetPeriodActivityTest extends ActivityInstrumentationTestCase2<BudgetPeriodActivity> {
    private Solo solo;
    private Activity activity;

    public BudgetPeriodActivityTest(){
        super(BudgetPeriodActivity.class);
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
     * check activity contents are correct
     */
    public void testContentsOfActivity(){
        Resources resources = activity.getResources();
        solo.assertCurrentActivity("Should be budget period Activity", BudgetPeriodActivity.class);
        solo.getText(resources.getString(R.string.budgetperiod_weeks));
        solo.clickOnEditText(1);
        solo.clickOnButton(0);
    }

    /**
     * Set dates for budget period
     */
    public void testSettingDatePicker(){
        solo.assertCurrentActivity("Should be budget period Activity", BudgetPeriodActivity.class);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        EditText endDate = (EditText)activity.findViewById(R.id.termEndDate);
        TextView weeks = (TextView)activity.findViewById(R.id.weeks);
        String weekText = weeks.getText().toString();
        //open date picker dialog for selecting an end date
        solo.clickOnView(endDate);

        //set date picker
        solo.setDatePicker(0, year+1, month, 1);
        solo.clickOnButton(0);
        solo.waitForActivity(BudgetPeriodActivity.class);
        EditText editText = solo.getEditText(1);
        assertNotSame("", editText.getText().toString());


        assertNotSame(weekText, weeks.getText().toString());
    }

    /**
     * test that validation of dates is correct
     */
    public void testValidationOfDates(){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        EditText endDate = (EditText)activity.findViewById(R.id.termEndDate);
        TextView weeks = (TextView)activity.findViewById(R.id.weeks);
        String weekText = weeks.getText().toString();

        solo.clickOnButton(activity.getString(R.string.next_button));
        solo.waitForText(activity.getString(R.string.please_select));

        //enter date using date picker entering invalid date
        solo.clickOnView(endDate);
        solo.setDatePicker(0, year, month, day);
        solo.clickOnButton(0);

        solo.waitForActivity(BudgetPeriodActivity.class);
        solo.getEditText(day + "/" + (month+1) + "/" + year);

        solo.clickOnButton(activity.getString(R.string.next_button));

        //check for error message
        solo.waitForText(activity.getString(R.string.please_select)+" "+activity.getString(R.string.msg_budget_valid));

        //enter correct budget dates and attempt to proceed
        solo.clickOnView(endDate);
        solo.setDatePicker(0, year+1, month, day);
        solo.clickOnButton(0);
        solo.waitForActivity(BudgetPeriodActivity.class);
        solo.clickOnButton(activity.getString(R.string.next_button));
        solo.waitForActivity(IncomeActivity.class);
        solo.assertCurrentActivity("Should now be on income activity", IncomeActivity.class);

    }

}
