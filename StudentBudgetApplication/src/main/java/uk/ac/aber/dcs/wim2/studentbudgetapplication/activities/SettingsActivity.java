package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.os.Bundle;
import android.app.Activity;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.SettingsFragment;

/**
 * This class contains the functionality for displaying the settings fragment.
 *
 * @author wim2
 * @version 1.0
 */
public class SettingsActivity extends Activity {

    /**
     * Called when the activity is created to instantiate the objects required.
     *
     * @param savedInstanceState - saved bundled state including any variables passed from other activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(1);
        this.finish();
    }
}
