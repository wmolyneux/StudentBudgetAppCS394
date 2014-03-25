package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.BudgetsFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.HistoryFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.OverviewFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.ReportFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.TransactionsFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.ExpenseFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments.IncomeFragment;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

public class DetailActivity extends FragmentActivity {

    private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Context context;
    private Fragment currentFragment;
    private String pin;

    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean hasPin = prefs.getBoolean("pref_bool_pin", false);
        pin = prefs.getString("pref_pin", "");
        if(hasPin && pin.length()==4){
            activateLock();
        }
        context = this;
        setContentView(R.layout.activity_detail);

        manageFragments(new OverviewFragment(), R.id.content_frame);

        // get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.items);

        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.listview_navigator, drawerListViewItems));

        // App Icon
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );

        // Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        drawerListView.setOnItemClickListener(new DrawerItemClickListener());


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        // then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    public void manageFragments(Fragment newFrag, int oldFragId){
        currentFragment = newFrag;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(oldFragId, currentFragment);
        transaction.commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Fragment frag = null;
            switch(position){
                case 0:
                    frag = new OverviewFragment();
                    break;
                case 1:
                    frag = new TransactionsFragment();
                    break;
                case 2:
                    frag = new HistoryFragment();
                    break;
                case 3:
                    frag = new ReportFragment();
                    break;
                case 4:
                    frag = new BudgetsFragment();
                    break;
                case 5:
                    frag = new IncomeFragment();
                    break;
                case 6:
                    frag = new ExpenseFragment();
                    break;
            }
            manageFragments(frag, R.id.content_frame);


            drawerLayout.closeDrawer(drawerListView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        if(settings != null){
            settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(context, SettingsActivity.class);
                    startActivityForResult(intent, 0);
                    return true;
                }
            });
        }
        return true;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentUtilities.refreshPreferences(this);

        drawerListViewItems = getResources().getStringArray(R.array.items);
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.listview_navigator, drawerListViewItems));

        FragmentUtilities.reloadFragment(this, currentFragment);
        BalanceUtilities.updateWidget(this);
    }

    private void activateLock() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_pin_dialog);
        dialog.setTitle("Please enter 4-digit pin");
        dialog.setCancelable(false);

        final EditText pinField = (EditText)dialog.findViewById(R.id.pin_et);
        pinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(pinField.getText().toString().length()==4){
                    if(pinField.getText().toString().equalsIgnoreCase(pin)){
                        dialog.dismiss();
                    }
                    else{
                        pinField.setText("");
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog.show();
        pinField.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(pinField, 0);
            }
        }, 50);
    }

    
}
