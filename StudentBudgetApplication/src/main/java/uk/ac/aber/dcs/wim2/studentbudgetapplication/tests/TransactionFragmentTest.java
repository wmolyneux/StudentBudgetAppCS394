package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.res.TypedArray;
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

/**
 * Created by wim2 on 31/03/2014.
 */
public class TransactionFragmentTest extends ActivityInstrumentationTestCase2<EnterActivity> {

    private Solo solo;
    private Activity activity;

    public TransactionFragmentTest(){
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
        solo.clickOnText(typedArray.getString(1));

        solo.waitForFragmentByTag("transaction");
    }

    public void testTransactionIsSuccessfullyCreatedAndAppendsBalance(){
        getToTestState();

        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.items);
        solo.clickOnImage(0);
        solo.clickOnText(typedArray.getString(0));
        solo.waitForFragmentByTag("overview");
        TextView weeklyBalance = (TextView)activity.findViewById(R.id.overWeeklyBalance);
        Float beforeBalance = Float.valueOf(weeklyBalance.getText().toString().substring(1, weeklyBalance.getText().toString().length()));

        solo.clickOnImage(0);
        solo.clickOnText(typedArray.getString(1));
        solo.waitForFragmentByTag("transaction");

        EditText amountEdit = (EditText)activity.findViewById(R.id.amountField);
        EditText descEdit = (EditText)activity.findViewById(R.id.descField);

        solo.enterText(amountEdit, "10");
        solo.enterText(descEdit, "Iceland");
        solo.pressSpinnerItem(0, 1);
        solo.clickOnEditText(2);
        solo.waitForDialogToOpen();
        solo.clickOnButton(0);
        solo.waitForDialogToClose();
        solo.clickOnButton(2);


        solo.waitForText(activity.getString(R.string.transaction_added));
        solo.waitForFragmentByTag("overview");

        weeklyBalance = (TextView)activity.findViewById(R.id.overWeeklyBalance);
        Float afterBalance = Float.valueOf(weeklyBalance.getText().toString().substring(1, weeklyBalance.getText().toString().length()));

        assertEquals((beforeBalance-10), afterBalance);
    }

    public void testTransactionValidation(){
        getToTestState();
        EditText amountEdit = (EditText)activity.findViewById(R.id.amountField);
        EditText descEdit = (EditText)activity.findViewById(R.id.descField);

        solo.enterText(descEdit, "Iceland");
        solo.pressSpinnerItem(0, 1);
        solo.clickOnEditText(2);
        solo.waitForDialogToOpen();
        solo.clickOnButton(0);
        solo.waitForDialogToClose();
        solo.clickOnButton(2);

        solo.enterText(amountEdit, "5");
        solo.clickOnToggleButton(activity.getString(R.string.transaction_expense));
        solo.clickOnButton(2);
    }

}
