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
/**
 * This class contains the functionality for displaying fragments of the application via the
 * navigation menu.
 *
 * @author wim2
 * @version 1.0
 */
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

    /**
     * Called when the activity is created to instantiate the objects needed for the screen.
     *
     * @param savedInstanceState - bundled state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check preferences for if the pin preference has been set and display the lock screen if so
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean hasPin = prefs.getBoolean("pref_bool_pin", false);
        pin = prefs.getString("pref_pin", "");
        if(hasPin && pin.length()==4){
            activateLock();
        }

        //setup the navigation menu
        context = this;
        setContentView(R.layout.activity_detail);

        manageFragments(new OverviewFragment(), R.id.content_frame, "overview");

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

    /**
     * called when configuration is changed on the navigation drawer, sliding the menu in or out.
     *
     * @param newConfig - configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Called when menu item is selected.
     *
     * @param item - menu item that was selected
     * @return true if passed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        // then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when menu is created to sync the state of the menu
     *
     * @param savedInstanceState - - bundled state
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    /**
     * Assist with managing fragments using fragment transactions.
     *
     * @param newFrag - new fragment to be displayed
     * @param oldFragId - old fragment's ID
     * @param tag - tag of the fragment
     */
    public void manageFragments(Fragment newFrag, int oldFragId, String tag){
        currentFragment = newFrag;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(oldFragId, currentFragment, tag);
        transaction.commit();
    }

    /**
     * Inner class for assisting with items being clicked in the menu.
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        /**
         * Called when item is clicked in the menu to swap current fragment for the existing one.
         *
         * @param parent - parent adapter view
         * @param view - view that was clicked
         * @param position - position in the list
         * @param id - id of the item clicked
         */
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Fragment frag = null;
            String tag = "";
            switch(position){
                case 0:
                    frag = new OverviewFragment();
                    tag = "overview";
                    break;
                case 1:
                    frag = new TransactionsFragment();
                    tag = "transaction";
                    break;
                case 2:
                    frag = new HistoryFragment();
                    tag = "history";
                    break;
                case 3:
                    frag = new ReportFragment();
                    tag = "report";
                    break;
                case 4:
                    frag = new BudgetsFragment();
                    tag = "budgets";
                    break;
                case 5:
                    frag = new IncomeFragment();
                    tag = "income";
                    break;
                case 6:
                    frag = new ExpenseFragment();
                    tag = "expense";
                    break;
            }
            manageFragments(frag, R.id.content_frame, tag);


            drawerLayout.closeDrawer(drawerListView);
        }
    }

    /**
     * Called when options menu is created to assist with attaching menu item click listeners.
     *
     * @param menu - Menu object
     *
     * @return - true if passed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return FragmentUtilities.menuItemSetup(menu, this);
    }

    /**
     * Called when a activity is finished with a result code sent to this activity.
     *
     * @param requestCode - request code
     * @param resultCode - result code
     * @param data - data sent from finishing activity
     */
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

    /**
     * Activates the lock screen dialog, promting the user to a 4 digit pin number.
     * Cannot be dismissed
     */
    private void activateLock() {
        //create dialog with title, content and un-cancelable
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_pin_dialog);
        dialog.setTitle("Please enter 4-digit pin");
        dialog.setCancelable(false);

        //attach TextChangedListener to check for the pin being entered
        final EditText pinField = (EditText)dialog.findViewById(R.id.pin_et);
        pinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //if the entered text is 4 digits long
                if(pinField.getText().toString().length()==4){
                    //if the text entered matches the pin then dismiss the dialog.
                    if(pinField.getText().toString().equalsIgnoreCase(pin)){
                        dialog.dismiss();
                    }
                    //else reset the dialog and vibrate for 0.5 of a second
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

        //display the dialog
        dialog.show();

        //force the onscreen keyboard to be displayed when dialog is displayed
        pinField.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(pinField, 0);
            }
        }, 50);
    }

    
}
