package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_dates);
        context = this;

        startDate = (EditText) findViewById(R.id.termStartDate);
        endDate = (EditText) findViewById(R.id.termEndDate);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        startDate.setText(day + "/" + (month + 1) + "/"+ year);
        start = new DateTime(year, month+1, day, 0, 0);

        endDate.setOnTouchListener(this);

        next = (Button) findViewById(R.id.DateToIncomeButton);
        next.setOnClickListener(this);

        weeksText = (TextView) findViewById(R.id.weeks);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentUtilities.refreshPreferences(this);
        Intent i = getIntent();
        finish();
        startActivity(i);
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
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.DateToIncomeButton:
                if(validate()){
                    Detail det = new Detail("", "", 0, 0, Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), Float.valueOf(0));
                    det.setStartDate(startDate.getText().toString());
                    det.setEndDate(endDate.getText().toString());
                    det.setTotalWeeks(weeks);

                    Intent intent = new Intent(this, IncomeActivity.class);
                    intent.putExtra("detail", det);
                    startActivity(intent);
                }
        }

    }

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

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            endDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/"+ selectedYear);
            end = new DateTime(selectedYear, selectedMonth+1, selectedDay, 0, 0);
//            calculateWeeks();
            calculateDays();
        }
    };

    private void calculateDays() {
        if(!startDate.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()){
            weeks = Days.daysBetween(start, end).getDays()+1;
            weeksText.setText(BalanceUtilities.getValueAs2dpString((float)weeks/7));
        }
    }

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
