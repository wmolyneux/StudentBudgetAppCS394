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
 * Created by wim2 on 28/03/2014.
 */
public class BudgetPeriodActivityTest extends ActivityInstrumentationTestCase2<BudgetPeriodActivity> {
    private Solo solo;
    private Activity activity;

    public BudgetPeriodActivityTest(){
        super(BudgetPeriodActivity.class);
    }

    public void setUp(){
        solo = new Solo(getInstrumentation());
        activity = getActivity();
    }

    public void tearDown(){
        solo.finishOpenedActivities();
    }

    public void testContentsOfActivity(){
        Resources resources = activity.getResources();
        solo.assertCurrentActivity("Should be budget period Activity", BudgetPeriodActivity.class);
        solo.getText(resources.getString(R.string.budgetperiod_weeks));
        solo.clickOnEditText(1);
        solo.clickOnButton(0);
    }

    public void testSettingDatePicker(){
        solo.assertCurrentActivity("Should be budget period Activity", BudgetPeriodActivity.class);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        EditText endDate = (EditText)activity.findViewById(R.id.termEndDate);
        TextView weeks = (TextView)activity.findViewById(R.id.weeks);
        String weekText = weeks.getText().toString();

        solo.clickOnView(endDate);

        solo.setDatePicker(0, year+1, month+2, day+1);

        solo.clickOnButton(0);
        solo.waitForActivity(BudgetPeriodActivity.class);
        solo.getEditText((day+1) + "/" + (month + 3) + "/" + (year+1));

        assertNotSame(weekText, weeks.getText().toString());
    }

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


        solo.clickOnView(endDate);
        solo.setDatePicker(0, year, month, day);
        solo.clickOnButton(0);

        solo.waitForActivity(BudgetPeriodActivity.class);
        solo.getEditText(day + "/" + (month+1) + "/" + year);

        solo.clickOnButton(activity.getString(R.string.next_button));
        solo.waitForText(activity.getString(R.string.please_select)+" "+activity.getString(R.string.msg_budget_valid));

        solo.clickOnView(endDate);
        solo.setDatePicker(0, year+1, month, day);
        solo.clickOnButton(0);
        solo.waitForActivity(BudgetPeriodActivity.class);
        solo.clickOnButton(activity.getString(R.string.next_button));
        solo.waitForActivity(IncomeActivity.class);
        solo.assertCurrentActivity("Should now be on income activity", IncomeActivity.class);

    }

}
