package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import org.joda.time.Weeks;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;

public class AcademicYearActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

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
    TextView weeksText;
    int weeks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_year);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.term, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.DateToIncomeButton:
                if(validate()){
                    Detail det = new Detail("", "", 0, 0, Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), Float.valueOf(0), "Y");
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
        if(startDate.getText().toString().isEmpty()){
            Toast.makeText(this, "Please select term start date", Toast.LENGTH_LONG).show();
            return false;
        }
        if(endDate.getText().toString().isEmpty()){
            Toast.makeText(this, "Please select term end date", Toast.LENGTH_LONG).show();
            return false;
        }
        if(weeks<=0){
            Toast.makeText(this, "Please select valid term dates", Toast.LENGTH_LONG).show();
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
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            endDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/"+ selectedYear);
            end = new DateTime(selectedYear, selectedMonth+1, selectedDay, 0, 0);
            calculateWeeks();
        }
    };

    private void calculateWeeks() {
        if(!startDate.getText().toString().isEmpty() && !endDate.getText().toString().isEmpty()){
            weeks = Weeks.weeksBetween(start, end).getWeeks();
            weeksText.setText(weeks+"");
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
