package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;

/**
 * This class contains the functionality for displaying the licenses.
 *
 * @author wim2
 * @version 1.0
 */
public class LicenseActivity extends Activity {

    /**
     * Called when the activity is created to instantiate the objects required.
     *
     * @param savedInstanceState - saved bundled state including any variables passed from other activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
    }

    /**
     * Called on creation of the activity to create options menu.
     *
     * @param menu - Menu on the screen
     *
     * @return true if passed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.license, menu);
        return true;
    }
    
}
