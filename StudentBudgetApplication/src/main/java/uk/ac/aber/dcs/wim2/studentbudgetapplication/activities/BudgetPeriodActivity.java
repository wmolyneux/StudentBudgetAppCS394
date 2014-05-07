package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

/**
 * This class contains the functionality for the budget period screen of the application.
 *
 * @author wim2
 * @version 1.0
 */
public class BudgetPeriodActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private int day;
    private int month;
    private int year;
    private EditText startDate;
    private EditText endDate;
    private Calendar cal;
    private String startOrEnd;
    private Button next;
    private DateTime start;
    private DateTime end;
    private TextView weeksText;
    private int weeks;
    private Context context;


    /**
     * Called when the activity is created to instantiate the objects required.
     *
     * @param savedInstanceState - saved bundled state including any variables passed from other activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_dates);
        context = this;

        //Instantiate views on screen
        startDate = (EditText) findViewById(R.id.termStartDate);
        endDate = (EditText) findViewById(R.id.termEndDate);

        //get today's date
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        //set the start date as today
        startDate.setText(day + "/" + (month + 1) + "/"+ year);
        start = new DateTime(year, month+1, day, 0, 0);

        endDate.setOnTouchListener(this);

        //Instantiate next button and attach onclicklistener
        next = (Button) findViewById(R.id.DateToIncomeButton);
        next.setOnClickListener(this);

        weeksText = (TextView) findViewById(R.id.weeks);
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
        Intent i = getIntent();
        finish();
        startActivity(i);
    }


    /**
     * Called on creation of the activity to create options menu.
     *
     * @param menu - Menu on the screen
     *
     * @return true if passed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return FragmentUtilities.menuItemSetup(menu, this);
    }

    /**
     * Called when an item with an OnClickListener is clicked.
     * Used for when the next button is pressed to redirect to the next screen.
     *
     * @param view - View that has been pressed.
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.DateToIncomeButton:
                if(validate()){
                    //create date object and set the values from the screen after validation has passed
                    Detail det = new Detail("", "", 0, 0, Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), Float.valueOf(0));
                    det.setStartDate(startDate.getText().toString());
                    det.setEndDate(endDate.getText().toString());
                    det.setTotalWeeks(weeks);

                    //start the next activity, passing the detail object
                    Intent intent = new Intent(this, IncomeActivity.class);
                    intent.putExtra("detail", det);
                    startActivity(intent);
                }
        }

    }

    /**
     * Validates that the end date is not empty and that the amount of days is at least 7.
     *
     * @return true if passed validation
     */
    private boolean validate() {
        if(endDate.getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_select)+" "+getString(R.string.msg_budget_end), Toast.LENGTH_LONG).show();
            return false;
        }
        if(weeks<=7){
            Toast.makeText(this, getString(R.string.please_select)+" "+getString(R.string.msg_budget_valid), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    /**
     * Creates a date picker dialog.
     *
     * @param id - id of the dialog.
     *
     * @return - true is passed.
     */
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    /**
     * Creates date picker dialog and attaches an OnDateSet listener to parse the income and
     */
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            endDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/"+ selectedYear);
            end = new DateTime(selectedYear, selectedMonth+1, selectedDay, 0, 0);
            calculateDays();
        }
    };

    /**
     * Function to calculate the dates between the start and end date.
     */
    private void calculateDays() {
        if(!startDate.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()){
            weeks = Days.daysBetween(start, end).getDays()+1;
            weeksText.setText(BalanceUtilities.getValueAs2dpString((float)weeks/7));
        }
    }

    /**
     * Called when an item with an OnTouchListener is touched.
     * Shows the date picker dialog when the term end date field is touched.
     *
     * @param view - View that has been touched.
     * @param motionEvent - motion event
     *
     * @return - true is passed.
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.termEndDate:
                showDialog(0);
                break;
        }
        return false;
    }

}
