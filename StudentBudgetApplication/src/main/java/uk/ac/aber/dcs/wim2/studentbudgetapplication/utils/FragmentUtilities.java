package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Locale;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Constant;

/**
 * Created by wim2 on 03/03/2014.
 */
public class FragmentUtilities {

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

}
