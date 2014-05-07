package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.app.ActionBar;
import android.content.res.TypedArray;
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

/**
 * This class contains the functionality for the managing expense screen of the application.
 *
 * @author wim2
 * @version 1.0
 */
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
    TypedArray categoryArray;

    /**
     * Called when creating a fragment inflating the view and instantiating objects
     *
     * @param inflater - layout inflater
     * @param container - container of the view
     * @param savedInstanceState - bundled state
     *
     * @return - view that has been inflated
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the fragment
        View inflate = inflater.inflate(R.layout.fragment_report, container, false);
        view = inflate;

        //setup the views on the screen
        date = (TextView) view.findViewById(R.id.dateForReport);
        next = (ImageButton) view.findViewById(R.id.nextDateButton);
        back = (ImageButton) view.findViewById(R.id.backDateButton);

        //setup the onclicklisteners
        next.setOnClickListener(this);
        back.setOnClickListener(this);

        //obtain category strings from strings file
        categoryArray = getResources().obtainTypedArray(R.array.categories);
        return inflate;
    }

    /**
     * Called when activity is created to ensure the setup of the screen is done before the main thread
     *
     * @param savedInstanceState - bundled state
     */
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

    /**
     * Creates a renderer object for creating a pie chart and sets up variables such as zoom, angle, label size,
     * and if legend will be displayed.
     */
    private void setupData() {
        //clears custom legend items
        clearLegend();

        //creates renderer and setup
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
        //loops through all transactions
        for(Transaction transaction : db.getAllTransactions()){
            String[] split = transaction.getDate().split("/");
            //if they are valid for this month, a new category and of the type expense,
            //the transaction category is added to the item names for display in the legend.
            if((today.monthOfYear().get()) == Integer.valueOf(split[1]) && today.year().get() == Integer.valueOf(split[2])){
                if(!itemNames.contains(categoryArray.getString(transaction.getCategory())) && transaction.getType().equalsIgnoreCase("minus")){
                    itemNames.add(categoryArray.getString(transaction.getCategory()));
                }
            }
        }

        //loops through all item names of categories
        for(String name : itemNames){
            float value = (float) 0;
            //loops through all transactions
            for (Transaction trans : db.getAllTransactions()){
                String[] split = trans.getDate().split("/");

                //if the transaction is for this month and matches the category then append to the total
                //value of the amount spent in that category
                if((today.monthOfYear().get()) == Integer.valueOf(split[1]) && today.year().get() == Integer.valueOf(split[2])){
                    if(categoryArray.getString(trans.getCategory()).equalsIgnoreCase(name) && trans.getType().equalsIgnoreCase("minus")){
                      value += trans.getAmount();
                    }
                }
            }
            total += value;
            itemValues.add(value);
        }


        //for all categories that occured this month
        for(String name : itemNames){
            //get the color associated with the category and add to a list
            if(name.equalsIgnoreCase("micro transaction")){
                itemColor.add(Color.rgb(235, 155, 59));
                continue;
            }
            for(Category cat : db.getAllCategories()){
                if(categoryArray.getString(cat.getPosition()).equalsIgnoreCase(name)){
                    selectColor(cat);
                    break;
                }
            }

        }

        //go through all values to be added to the chart
        mSeries = new CategorySeries("");
        for(int i = 0; i < itemValues.size(); i++){
            //calculate a percent being spent on this category to be displayed on the chart
            float percent = (itemValues.get(i)/total)*100;

            //add the category name and total spent in that category this month as a legend item
            mSeries.add(BalanceUtilities.getValueAs2dpString(percent)+"%   ", percent);
            setLegendText(legends[i], "• "+itemNames.get(i)+" "+ FragmentUtilities.getCurrency(getActivity())+
                    BalanceUtilities.getValueAs2dpString(itemValues.get(i)), itemColor.get(i));
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();

            //set the colour for the items
            renderer.setColor(itemColor.get(i));
            mRenderer.addSeriesRenderer(renderer);

        }

        //display a 'No data' message if no legend items appear
        if(itemNames.size()==0){
            TextView legendItem = (TextView)view.findViewById(legends[0]);
            legendItem.setText(R.string.report_no_data);
            legendItem.setTextColor(Color.BLACK);
            legendItem.setTextSize(20);
        }
        if(mChartView != null){
            mChartView.repaint();
        }super.onResume();
    }

    /**
     * Clear the custom legend ready for redrawing the chart
     */
    private void clearLegend() {
        for (int i : legends){
            TextView item = (TextView)view.findViewById(i);
            item.setText("");
        }
    }

    /**
     * Finds the correct colour for the given category
     * @param cat - category to select color
     */
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

    /**
     * Redraws chart and legend
     */
    private void invalidateChart() {
        setupData();
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.chart);
        layout.removeAllViews();
        mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
        mRenderer.setClickEnabled(false);
        layout.addView(mChartView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
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

    /**
     * Calculates the month and year for the date after the current one
     * @param current - current date, including month and year
     *
     * @return - month and year one month ahead of old date
     */
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

    /**
     * Calculates the month and year for the date before the current one
     * @param current - current date, including month and year
     *
     * @return - month and year one month before the old date
     */
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

    /**
     * Display the legent text for the custom legend
     *
     * @param legend - legend item position
     * @param text - text to contain
     * @param color - text colour
     */
    public void setLegendText(int legend, String text, int color){
        TextView legendItem = (TextView)view.findViewById(legend);
        legendItem.setText(text+"  ");
        legendItem.setTextColor(color);
        legendItem.setTextSize(15);
    }
}
