package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;
import java.util.zip.Inflater;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;

public class SettingsFragment extends PreferenceFragment {
    private String pin1;
    private String pin2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference pinPreference = getPreferenceScreen().findPreference("pref_pin");
        pinPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openInitialPinDialog();
                return true;
            }
        });

        CheckBoxPreference pinOnPreference =
                (CheckBoxPreference) getPreferenceScreen().findPreference("pref_bool_pin");
        pinOnPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("pref_pin");
                editor.commit();
                return true;
            }
        });
    }

    private void openInitialPinDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_pin_dialog);
        dialog.setTitle(getActivity().getString(R.string.enter_4_digit));

        final EditText pinField = (EditText)dialog.findViewById(R.id.pin_et);
        pinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(pinField.getText().toString().length()==4){
                    pin1 = pinField.getText().toString();
                    dialog.cancel();
                    openPinValidationDialog();
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
                InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(pinField, 0);
            }
        }, 50);
    }

    private void openPinValidationDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_pin_dialog);
        dialog.setTitle(getActivity().getString(R.string.reenter_4_digit));

        final EditText pinField = (EditText)dialog.findViewById(R.id.pin_et);
        pinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(pinField.getText().toString().length()==4){
                    pin2 = pinField.getText().toString();
                    dialog.cancel();
                    if(pin1.equalsIgnoreCase(pin2)){
                        setPinPreference();
                    }
                    else{
                        showMessageDialog(getActivity().getString(R.string.pin_doesnt_match), true);
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
                InputMethodManager keyboard = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(pinField, 0);
            }
        },50);
    }

    private void setPinPreference() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pref_pin", pin2);
        editor.commit();

        showMessageDialog(getActivity().getString(R.string.pin_set), false);

    }

    private void showMessageDialog(String message, boolean vibrate) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title
        alertDialogBuilder.setTitle(message);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getActivity().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        if(vibrate){
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
    }

}
