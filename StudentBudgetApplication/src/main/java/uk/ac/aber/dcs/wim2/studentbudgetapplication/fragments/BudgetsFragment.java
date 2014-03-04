package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.oldCode.SQLiteHelper;

public class BudgetsFragment extends Fragment implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    Spinner categories;
    SeekBar weeklySlide;
    SeekBar monthlySlide;
    TextView currWeekMin;
    TextView currWeekMax;
    TextView currMonthMin;
    TextView currMonthMax;
    TextView remainingWeek;
    TextView remainingMonth;
    SQLiteHelper db;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_budgets, container, false);
        registerView(inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void registerView(View view){
        categories = (Spinner) view.findViewById(R.id.budgetCategorySpinner);
        db = new SQLiteHelper(getActivity());
        ArrayList<String> tempCategories = new ArrayList<String>();
        for (Category cat : db.getAllCategories()){
            tempCategories.add(cat.getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, tempCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
        categories.setOnItemSelectedListener(this);

        weeklySlide = (SeekBar) view.findViewById(R.id.weeklyslide);
        monthlySlide = (SeekBar) view.findViewById(R.id.monthlyslide);
        currWeekMin = (TextView) view.findViewById(R.id.currentweekmin);
        //example code!!! remove!!!
        currWeekMin.setText("£20");
        currWeekMax = (TextView) view.findViewById(R.id.currentweekmax);

        //example code!!! remove!!!
        currWeekMax.setText("£50");
        currMonthMin = (TextView) view.findViewById(R.id.currentMonthMin);
        currMonthMax = (TextView) view.findViewById(R.id.currentmonthmax);
        remainingWeek = (TextView) view.findViewById(R.id.remainingweekly);
        remainingMonth = (TextView) view.findViewById(R.id.remainingmonthly);

        weeklySlide.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        categories.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    int min = 20;
    int max = 50;
    int diff = 30;
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        weeklySlide.setMax(diff);
        remainingWeek.setText("£"+(min+i));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
