package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;

/**
 * This class contains the functionality for the settings screen of the application.
 *
 * @author wim2
 * @version 1.0
 */
public class SettingsFragment extends PreferenceFragment {
    private String pin1;
    private String pin2;

    /**
     * Called when the activity is created to instantiate the objects required.
     *
     * @param savedInstanceState - saved bundled state including any variables passed from other activities.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //find preferences xml file for layout
        addPreferencesFromResource(R.xml.preferences);

        //add preferences along with click listeners
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

    /**
     * Opens the initial pin dialog for the user to enter the first pin for setting up pin lock protection
     */
    private void openInitialPinDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_pin_dialog);
        dialog.setTitle(getActivity().getString(R.string.enter_4_digit));

        final EditText pinField = (EditText)dialog.findViewById(R.id.pin_et);
        //add text changed lister to wait for 4 digits to be entered before opening the validation dialog
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

        //force on screen keyboard to be displayed
        pinField.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(pinField, 0);
            }
        }, 50);
    }

    /**
     * Opens the dialog for validating the initial pin entered was correct
     */
    private void openPinValidationDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_pin_dialog);
        dialog.setTitle(getActivity().getString(R.string.reenter_4_digit));

        final EditText pinField = (EditText)dialog.findViewById(R.id.pin_et);
        //add text changed listener to listen for 4 digit to be entered and compare the two pins to
        //validate if they are a match
        pinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(pinField.getText().toString().length()==4){
                    pin2 = pinField.getText().toString();
                    dialog.cancel();

                    //if the pins match, set the preference
                    if(pin1.equalsIgnoreCase(pin2)){
                        setPinPreference();
                    }
                    //else display error dialog
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

        //force on screen keyboard to be displayed
        pinField.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(pinField, 0);
            }
        },50);
    }

    /**
     * Set the pin preference in the applications shared preferences
     */
    private void setPinPreference() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pref_pin", pin2);
        editor.commit();

        //display pin set successful dialog
        showMessageDialog(getActivity().getString(R.string.pin_set), false);

    }

    /**
     * Message dialog builder to assist with the creation of popup dialogs for pin lock setup
     *
     * @param message - message to be displayed on the dialog
     * @param vibrate - boolean if device should vibrate
     */
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
