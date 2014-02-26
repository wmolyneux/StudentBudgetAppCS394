package uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;

public class AcademicYearActivity extends Activity implements View.OnClickListener {

    private int day;
    private int month;
    private int year;
    private EditText startDate;
    private EditText endDate;
    private Calendar cal;
    private String startOrEnd;
    private Button next;
    private SQLiteDatabaseHelper db;


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

        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

        next = (Button) findViewById(R.id.DateToIncomeButton);
        next.setOnClickListener(this);
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
            case R.id.termStartDate:
                startOrEnd = "start";
                showDialog(0);
                break;
            case R.id.termEndDate:
                startOrEnd = "end";
                showDialog(0);
                break;
            case R.id.DateToIncomeButton:
                if(validate()){
                    Detail det = new Detail("", "", 0, Float.valueOf(0), Float.valueOf(0), Float.valueOf(0));
                    det.setStartDate(startDate.getText().toString());
                    det.setEndDate(endDate.getText().toString());


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
            if(startOrEnd.equalsIgnoreCase("start")){
                startDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);
            }
            else{
                endDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);
            }
        }
    };

}
