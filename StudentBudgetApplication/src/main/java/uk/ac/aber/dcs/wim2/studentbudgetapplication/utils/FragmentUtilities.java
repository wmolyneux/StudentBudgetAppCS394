package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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
        else{
            value += (Float.valueOf(input)/52);
        }
        return value;
    }

}
