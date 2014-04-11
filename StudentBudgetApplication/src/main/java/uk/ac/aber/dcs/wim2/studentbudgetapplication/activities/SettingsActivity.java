package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.SettingsFragment;

public class SettingsActivity extends Activity {

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
