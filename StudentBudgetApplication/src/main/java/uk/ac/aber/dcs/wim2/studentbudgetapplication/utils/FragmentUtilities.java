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
 * Created by wim2 on 03/03/2014.
 */
public class FragmentUtilities {

    static Activity context;

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

    public static String getCurrency(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("pref_currency", "");
    }

    public static void refreshPreferences(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        String languageToLoad = prefs.getString("pref_language", "en");
        Locale locale = new Locale(languageToLoad);
        locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public static void reloadFragment(FragmentActivity activity, Fragment current){
        activity.getSupportFragmentManager().beginTransaction().detach(current).attach(current).commit();
    }

    public static boolean menuItemSetup(Menu menu, Activity act) {
        // Inflate the menu; this adds items to the action bar if it is present.
        context = act;
        context.getMenuInflater().inflate(R.menu.detail, menu);
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
