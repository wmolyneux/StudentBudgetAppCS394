package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.joda.time.DateTime;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Category;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

public class ReportFragment extends Fragment {

    private ArrayList<String> itemNames;
    private ArrayList<Integer> itemColor;
    private ArrayList<Float> itemValues;
    private SQLiteDatabaseHelper db;


    private CategorySeries mSeries;

    private DefaultRenderer mRenderer;
    private GraphicalView mChartView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_report, container, false);
        view = inflate;
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteDatabaseHelper(getActivity());
//        setupData();
    }

    private void setupData() {
        mRenderer = new DefaultRenderer();
        mRenderer.setApplyBackgroundColor(false);
        mRenderer.setChartTitleTextSize(45);
        mRenderer.setLabelsTextSize(30);
        mRenderer.setLegendTextSize(30);
        mRenderer.setZoomEnabled(false);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setMargins(new int[] {20, 30, 15, 0});
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setStartAngle(90);
        mRenderer.setPanEnabled(false);

        itemNames = new ArrayList<String>();
        itemColor = new ArrayList<Integer>();
        itemValues = new ArrayList<Float>();

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        for(Transaction transaction : db.getAllTransactions()){
            String[] split = transaction.getDate().split("/");
            if((month+1) == Integer.valueOf(split[1]) && year == Integer.valueOf(split[2])){
                if(!itemNames.contains(transaction.getCategory()) && transaction.getType().equalsIgnoreCase("minus")){
                    itemNames.add(transaction.getCategory());
                }
            }
        }

        for(String name : itemNames){
            float value = (float) 0;
            for (Transaction trans : db.getAllTransactions()){
                if(trans.getCategory().equalsIgnoreCase(name)){
                    value += trans.getAmount();
                }
            }
            itemValues.add(value);
        }

        for(String name : itemNames){
            if(name.equalsIgnoreCase("micro transaction")){
                itemColor.add(Color.GRAY);
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
            mSeries.add(itemNames.get(i) +" " +itemValues.get(i)+"  ", itemValues.get(i));
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(itemColor.get(i));
            mRenderer.addSeriesRenderer(renderer);
        }

        if(mChartView != null){
            mChartView.repaint();
        }super.onResume();
    }

    private void selectColor(Category cat) {
        if(cat.getColor().equalsIgnoreCase("cyan")){
            itemColor.add(Color.CYAN);
        }
        else if(cat.getColor().equalsIgnoreCase("green")){
            itemColor.add(Color.GREEN);
        }
        else if(cat.getColor().equalsIgnoreCase("red")){
            itemColor.add(Color.RED);
        }
        else if(cat.getColor().equalsIgnoreCase("yellow")){
            itemColor.add(Color.YELLOW);
        }
        else if(cat.getColor().equalsIgnoreCase("blue")){
            itemColor.add(Color.BLUE);
        }
        else if(cat.getColor().equalsIgnoreCase("ltgray")){
            itemColor.add(Color.LTGRAY);
        }
        else if(cat.getColor().equalsIgnoreCase("magenta")){
            itemColor.add(Color.MAGENTA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        if(mChartView == null){
            setupData();
            LinearLayout layout = (LinearLayout)view.findViewById(R.id.chart);
            layout.removeAllViews();
            mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
            mRenderer.setClickEnabled(false);
            layout.addView(mChartView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//        }
//        else{
//            System.out.println("repainting");
//            mChartView.repaint();
//        }
    }

}
