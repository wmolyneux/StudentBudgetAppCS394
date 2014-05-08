package uk.ac.aber.dcs.wim2.studentbudgetapplication.tests;

import android.app.Activity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.robotium.solo.Solo;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.BudgetPeriodActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.DetailActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.EnterActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Budget;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.TestingUtilities;

/**
 * This class contains the functionality for testing the enter screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class EnterActivityTest extends ActivityInstrumentationTestCase2<EnterActivity> {

    private Solo solo;
    private Activity activity;

    public EnterActivityTest(){
        super(EnterActivity.class);
    }

    /**
     * Setup objects required for testing and prepares the activity to be opened
     */
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation());
        System.out.println("setup is called");
        activity = getActivity();
    }

    /**
     * Tears down the test and any objects or activities created during the test
     */
    public void tearDown(){
        solo.finishOpenedActivities();
        System.out.println("tear down is called");

    }

    /**
     * Test that database can be cleared
     */
    public void testAlwaysClearDatabase(){
        if(solo.waitForActivity(DetailActivity.class, 1000)){
            solo.assertCurrentActivity("Should be DetailActivity", DetailActivity.class);
            TestingUtilities.checkDatabase(solo);
        }
    }

    /**
     * Test correct activity is being displayed
     */
    public void testCorrectActivity(){
        solo.assertCurrentActivity("Incorrect Activity", EnterActivity.class);
    }

    /**
     * Test contents of activity to be tested are correct
     */
    public void testContentsOfActivity(){
        Resources resources = solo.getCurrentActivity().getResources();
        solo.getText(resources.getString(R.string.enter_welcome));
        solo.getButton(resources.getString(R.string.enter_enterbutton));
    }

    /**
     * Test enter button works correctly
     */
    public void testEnterButton(){
        solo.clickOnButton(activity.getString(R.string.enter_enterbutton));
        solo.waitForActivity(BudgetPeriodActivity.class);
        solo.assertCurrentActivity("Should be budget period activity", BudgetPeriodActivity.class);
    }

}
