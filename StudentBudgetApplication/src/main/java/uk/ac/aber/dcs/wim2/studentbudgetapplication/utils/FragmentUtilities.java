package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Locale;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.LicenseActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.activities.SettingsActivity;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;

/**
 * This class contains the functionality for assisting with fragment related functionality
 *
 * @author wim2
 * @version 1.0
 */
public class FragmentUtilities {

    static Activity context;

    /**
     * Reset spinners that contain month and year to a given selection, for use with manage incomes/expenses
     *
     * @param constant - constant from the database containing the configuration the spinner should be
     * @param spinner - spinner to be configured
     * @param editText - text that should be displayed about a constant
     */
    public static void resetMonthYearSpinnerAndAmount(Constant constant, View spinner, View editText){
        Spinner spin = (Spinner)spinner;
        EditText text = (EditText) editText;
        if(constant.getRecurr().equalsIgnoreCase("monthly")){
            spin.setSelection(0);
        }
        else{
            spin.setSelection(1);
        }
        text.setText(constant.getAmount().toString());
    }

    /**
     * Reset spinners that contain week and month to a given selection, for use with manage incomes/expenses
     *
     * @param constant - constant from the database containing the configuration the spinner should be
     * @param spinner - spinner to be configured
     * @param editText - text that should be displayed about a constant
     */
    public static void resetWeekMonthSpinnerAndAmount(Constant constant, View spinner, View editText){
        Spinner spin = (Spinner)spinner;
        EditText text = (EditText) editText;
        if(constant.getRecurr().equalsIgnoreCase("weekly")){
            spin.setSelection(0);
        }
        else{
            spin.setSelection(1);
        }
        text.setText(constant.getAmount().toString());
    }

    /**
     * Reset spinners that contain week, month and year to a given selection, for use with manage incomes/expenses
     *
     * @param constant - constant from the database containing the configuration the spinner should be
     * @param spinner - spinner to be configured
     * @param editText - text that should be displayed about a constant
     */
    public static void resetWeekMonthYearSpinnerAndAmount(Constant constant, View spinner, View editText){
        Spinner spin = (Spinner)spinner;
        EditText text = (EditText) editText;
        if(constant.getRecurr().equalsIgnoreCase("weekly")){
            spin.setSelection(0);
        }
        else if(constant.getRecurr().equalsIgnoreCase("monthly")){
            spin.setSelection(1);
        }
        else{
            spin.setSelection(2);
        }
        text.setText(constant.getAmount().toString());
    }

    /**
     * Check a spinner to help calculate how it affect the balance
     *
     * @param spinnerText - selected text in the spinner
     * @param input - amount of the field the spinner refers to
     *
     * @return - calculated amount for the field
     */
    public static Float checkSpinner(String spinnerText, String input){
        Float value = (float) 0;
        if(spinnerText.equalsIgnoreCase("weekly")){
            value += Float.valueOf(input);
        }
        else if(spinnerText.equalsIgnoreCase("monthly")){
            value += (Float.valueOf(input)/4);
        }
        else if(spinnerText.equalsIgnoreCase("yearly")){
            value += (Float.valueOf(input)/52);
        }
        return value;
    }

    /**
     * Gets the currency selected in the preferences
     *
     * @param context - context
     *
     * @return - currency in string form
     */
    public static String getCurrency(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("pref_currency", "");
    }

    /**
     * Refresh preferences after they have been changed in the settings menu.
     *
     * @param activity - current activity
     */
    public static void refreshPreferences(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        //get the language preference, default english if doesnt exist
        String languageToLoad = prefs.getString("pref_language", "en");

        //set the locale of the application to the language preference value
        Locale locale = new Locale(languageToLoad);
        locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    /**
     * Reload a fragment
     *
     * @param activity - activity containing the fragment
     * @param current - current fragment
     */
    public static void reloadFragment(FragmentActivity activity, Fragment current){
        activity.getSupportFragmentManager().beginTransaction().detach(current).attach(current).commit();
    }

    /**
     * Setup menu items for an activity
     *
     * @param menu - menu
     * @param act - current activity
     *
     * @return true if passed
     */
    public static boolean menuItemSetup(Menu menu, Activity act) {
        // Inflate the menu; this adds items to the action bar if it is present.
        context = act;
        context.getMenuInflater().inflate(R.menu.detail, menu);
        //setup settings menu click listener
        MenuItem settings = menu.findItem(R.id.action_settings);
        if(settings != null){
            settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(context, SettingsActivity.class);

                    context.startActivityForResult(intent, 0);
                    return true;
                }
            });
        }
        //setup licenses menu click listener
        MenuItem license = menu.findItem(R.id.action_license);
        if(license != null){
            license.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(context, LicenseActivity.class);
                    context.startActivity(intent);
                    return true;
                }
            });
        }
        return true;
    }

}
