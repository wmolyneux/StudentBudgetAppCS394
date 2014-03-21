package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.FragmentUtilities;

public class ReportFragment extends Fragment implements View.OnClickListener {

    private ArrayList<String> itemNames;
    private ArrayList<Integer> itemColor;
    private ArrayList<Float> itemValues;
    private SQLiteDatabaseHelper db;
    private int[] legends = {R.id.legend, R.id.legend1, R.id.legend2, R.id.legend3, R.id.legend4, R.id.legend5, R.id.legend6, R.id.legend7, R.id.legend8};


    private CategorySeries mSeries;
    private DateTime today;
    private DefaultRenderer mRenderer;
    private GraphicalView mChartView;
    private View view;
    private TextView date;
    private ImageButton next;
    private ImageButton back;

    private int month;
    private int year;
    private Float total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_report, container, false);
        view = inflate;
        date = (TextView) view.findViewById(R.id.dateForReport);
        next = (ImageButton) view.findViewById(R.id.nextDateButton);
        back = (ImageButton) view.findViewById(R.id.backDateButton);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteDatabaseHelper(getActivity());
        Calendar cal = Calendar.getInstance();
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        today = new DateTime(year, month+1, day, 0, 0);

        date.setText(today.monthOfYear().getAsText() +" "+today.year().get());

    }

    private void setupData() {
        clearLegend();
        mRenderer = new DefaultRenderer();
        mRenderer.setApplyBackgroundColor(false);
        mRenderer.setLabelsTextSize(30);
        mRenderer.setZoomEnabled(false);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setMargins(new int[]{20, 30, 15, 0});
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setStartAngle(90);
        mRenderer.setPanEnabled(false);
        mRenderer.setShowLegend(false);



        itemNames = new ArrayList<String>();
        itemColor = new ArrayList<Integer>();
        itemValues = new ArrayList<Float>();

        total = (float) 0;
        for(Transaction transaction : db.getAllTransactions()){
            String[] split = transaction.getDate().split("/");
            if((today.monthOfYear().get()) == Integer.valueOf(split[1]) && today.year().get() == Integer.valueOf(split[2])){
                if(!itemNames.contains(transaction.getCategory()) && transaction.getType().equalsIgnoreCase("minus")){
                    itemNames.add(transaction.getCategory());
                }
            }
        }

        for(String name : itemNames){
            float value = (float) 0;
            for (Transaction trans : db.getAllTransactions()){
                String[] split = trans.getDate().split("/");
                if((today.monthOfYear().get()) == Integer.valueOf(split[1]) && today.year().get() == Integer.valueOf(split[2])){
                    if(trans.getCategory().equalsIgnoreCase(name) && trans.getType().equalsIgnoreCase("minus")){
                      value += trans.getAmount();

                    }
                }
            }
            total += value;
            itemValues.add(value);
        }



        for(String name : itemNames){
            if(name.equalsIgnoreCase("micro transaction")){
                itemColor.add(Color.rgb(235, 155, 59));
                continue;
            }
            for(Category cat : db.getAllCategories()){
                if(cat.getName().equalsIgnoreCase(name)){
                    selectColor(cat);
                    break;
                }
            }

        }

        mSeries = new CategorySeries("");
        for(int i = 0; i < itemValues.size(); i++){
            float percent = (itemValues.get(i)/total)*100;
            mSeries.add(BalanceUtilities.getValueAs2dpString(percent)+"%   ", percent);
            setLegendText(legends[i], "• "+itemNames.get(i)+" "+ FragmentUtilities.getCurrency(getActivity())+itemValues.get(i), itemColor.get(i));
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(itemColor.get(i));
            mRenderer.addSeriesRenderer(renderer);

        }

        if(mChartView != null){
            mChartView.repaint();
        }super.onResume();
    }

    private void clearLegend() {
        for (int i : legends){
            TextView item = (TextView)view.findViewById(i);
            item.setText("");
        }
    }

    private void selectColor(Category cat) {
        if(cat.getColor().equalsIgnoreCase("cyan")){
            itemColor.add(Color.rgb(34, 212, 200));
        }
        else if(cat.getColor().equalsIgnoreCase("green")){
            itemColor.add(Color.rgb(77, 204, 31));
        }
        else if(cat.getColor().equalsIgnoreCase("darkgreen")){
            itemColor.add(Color.rgb(0, 133, 0));
        }
        else if(cat.getColor().equalsIgnoreCase("red")){
            itemColor.add(Color.rgb(204, 10, 10));
        }
        else if(cat.getColor().equalsIgnoreCase("yellow")){
            itemColor.add(Color.rgb(219, 216, 37));
        }
        else if(cat.getColor().equalsIgnoreCase("blue")){
            itemColor.add(Color.rgb(28, 68, 230));
        }
        else if(cat.getColor().equalsIgnoreCase("purple")){
            itemColor.add(Color.rgb(148, 108, 196));
        }
        else if(cat.getColor().equalsIgnoreCase("magenta")){
            itemColor.add(Color.rgb(188, 25, 209));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateChart();
    }

    private void invalidateChart() {
        setupData();
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.chart);
        layout.removeAllViews();
        mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
        mRenderer.setClickEnabled(false);
        layout.addView(mChartView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.nextDateButton:
                today = nextMonth(today);
                invalidateChart();
                break;
            case R.id.backDateButton:
                today = previousMonth(today);
                invalidateChart();
                break;
        }
        date.setText(today.monthOfYear().getAsText() +" "+today.year().get());
    }

    public DateTime nextMonth(DateTime current){
        int currentMonth = current.monthOfYear().get();
        int currentYear = current.year().get();
        if((month+1) == currentMonth && year == currentYear){
            return current;
        }
        if(currentMonth == 12){
            current = current.plusYears(1);
            current = current.minusMonths(11);
        }
        else{
            current = current.plusMonths(1);
        }
        return current;
    }

    public DateTime previousMonth(DateTime current){
        int month = current.monthOfYear().get();
        if(month == 1){
            current = current.minusYears(1);
            current = current.plusMonths(11);
        }
        else{
            current = current.minusMonths(1);
        }
        return current;
    }

    public void setLegendText(int legend, String text, int color){
        TextView legendItem = (TextView)view.findViewById(legend);
        legendItem.setText(text+"  ");
        legendItem.setTextColor(color);
        legendItem.setTextSize(15);
    }
}
