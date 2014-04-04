package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.Spinner;
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
public class IncomeActivityTest extends ActivityInstrumentationTestCase2<EnterActivity> {
    private Solo solo;
    private Activity activity;
    private Intent newIntent;

    public IncomeActivityTest(){
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
        solo.assertCurrentActivity("should be IncomeActivity", IncomeActivity.class);
        activity = solo.getCurrentActivity();
    }


    public void testContentsOfActivity(){
        getToTestState();
        solo.sleep(1000);
        EditText balanceField = (EditText)activity.findViewById(R.id.balanceAmount);//437.50
        EditText loanField = (EditText)activity.findViewById(R.id.loanAmount);//481.25
        EditText grantField = (EditText)activity.findViewById(R.id.grantAmount);//525
        EditText salaryField = (EditText)activity.findViewById(R.id.wageAmount);//550
        EditText otherField = (EditText)activity.findViewById(R.id.otherAmount);//575
        TextView weeklyIncome = (TextView)activity.findViewById(R.id.incomeWeeklyInc);

        solo.clearEditText(balanceField);
        solo.enterText(balanceField, "2000");
        assertEquals("2000", balanceField.getText().toString());

        solo.clearEditText(loanField);
        solo.enterText(loanField, "200");
        assertEquals("200", loanField.getText().toString());

        solo.clearEditText(grantField);
        solo.enterText(grantField, "200");
        assertEquals("200", grantField.getText().toString());

        solo.clearEditText(salaryField);
        solo.enterText(salaryField, "100");
        assertEquals("100", salaryField.getText().toString());
        solo.pressSpinnerItem(0, 1);
        solo.waitForDialogToClose();
        assertEquals("Spinner is not selected to monthly", true, solo.isSpinnerTextSelected(0, "Monthly"));


        solo.clearEditText(otherField);
        solo.enterText(otherField, "100");
        assertEquals("100", otherField.getText().toString());
        solo.pressSpinnerItem(1, 1);
        solo.waitForDialogToClose();
        assertEquals("Spinner is not selected to monthly", true, solo.isSpinnerTextSelected(1, "Monthly"));

    }

    public void testContentsValidation(){
        getToTestState();
        EditText balanceField = (EditText)activity.findViewById(R.id.balanceAmount);//437.50
        EditText loanField = (EditText)activity.findViewById(R.id.loanAmount);//481.25
        EditText grantField = (EditText)activity.findViewById(R.id.grantAmount);//525
        EditText salaryField = (EditText)activity.findViewById(R.id.wageAmount);//550
        EditText otherField = (EditText)activity.findViewById(R.id.otherAmount);//575
        solo.clearEditText(balanceField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(balanceField, "100");

        solo.clearEditText(loanField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(loanField, "100");

        solo.clearEditText(grantField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(grantField, "100");

        solo.clearEditText(salaryField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(salaryField, "100");

        solo.clearEditText(otherField);
        solo.clickOnButton(0);
        solo.waitForText(activity.getString(R.string.please_enter));
        solo.enterText(otherField, "100");

        solo.clickOnButton(0);
        solo.waitForActivity(ExpenseActivity.class);
        solo.assertCurrentActivity("Should be expense activity", ExpenseActivity.class);
    }


}
