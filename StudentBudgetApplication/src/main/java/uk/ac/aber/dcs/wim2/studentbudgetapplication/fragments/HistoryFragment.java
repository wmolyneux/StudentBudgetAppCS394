package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import org.joda.time.DateTime;
import org.joda.time.IllegalInstantException;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.HistoryArrayAdapter;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.TransactionAdapterListener;

/**
 * This class contains the functionality for the history screen of the application.
 *
 * @author wim2
 * @version 1.0
 */
public class HistoryFragment extends ListFragment implements TabHost.OnTabChangeListener {

    private TabHost tabHost;
    private List<Transaction> transactions = null;
    private SQLiteDatabaseHelper db;
    private HistoryArrayAdapter listAdapter;
    private View context;
    private ListView list;
    private Detail detail;
    private TransactionAdapterListener listen;

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
        View inflate = inflater.inflate(R.layout.fragment_history, container, false);
        context = inflate;
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
        detail = db.getAllDetails().get(0);
        setupTabHost();
        listen = new TransactionAdapterListener(getActivity(), transactions, db, listAdapter);
        list.setOnItemLongClickListener(listen);
        list.setOnItemClickListener(listen);
    }

    /**
     * Sets up the tab host in order to create two tabs with listviews for the income and expense tabs
     */
    private void setupTabHost() {
        tabHost = (TabHost)context.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec(getString(R.string.history_income_lc));
        TabHost.TabSpec tab2 = tabHost.newTabSpec(getString(R.string.history_expenses_lc));

        //setup onclick listeners using adapter listener.
        tab1.setIndicator(getString(R.string.history_income));
        tab1.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                list = (ListView)context.findViewById(android.R.id.list);
                return list;
            }
        });

        tab2.setIndicator(getString(R.string.history_expenses));
        tab2.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                list = (ListView)context.findViewById(android.R.id.list);
                return list;
            }
        });


        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.setOnTabChangedListener(this);

        //messy hack to get the tabs to display correct information
        tabHost.setCurrentTab(1);
    }

    /**
     * Called when tab is changed to change the content being diplayed
     *
     * @param s
     */
    @Override
    public void onTabChanged(String s) {
        //find only income or expense transactions depending on the current tab
        transactions = new ArrayList<Transaction>();
        if(tabHost.getCurrentTab()==0){
            filterIncomeTransactions();
        }
        else{
            filterExpenseTransactions();
        }

        //sort the transactions by most recent first
        sortTransactions();
        listAdapter = new HistoryArrayAdapter(getActivity(), transactions);

        //setup onclick listeners using adapter listener.
        listen = new TransactionAdapterListener(getActivity(), transactions, db, listAdapter);
        list.setOnItemLongClickListener(listen);
        list.setOnItemClickListener(listen);

        setListAdapter(listAdapter);
    }

    /**
     * Find all expense transactions saved in the database
     */
    private void filterExpenseTransactions() {
        for (Transaction transaction : db.getAllTransactions()){
            if(transaction.getType().equalsIgnoreCase("minus")){
                transactions.add(transaction);
            }
        }
    }

    /**
     * Find all income transaction saved in the database
     */
    private void filterIncomeTransactions() {
        for (Transaction transaction : db.getAllTransactions()){
            if(!transaction.getType().equalsIgnoreCase("minus")){
                transactions.add(transaction);
            }
        }
    }

    /**
     * Sort transaction by most recent
     */
    private void sortTransactions() {
        //create temp array for sorted list
        ArrayList<Transaction> temp = new ArrayList<Transaction>();

        //loop through all transactions
        for (Transaction transaction : transactions){
            //if no items in sorted list, add one and move to next
            if(temp.size() == 0){
                temp.add(transaction);
                continue;
            }

            //create a date object of the current transaction
            String[] transSplit = transaction.getDate().split("/");
            DateTime currentTrans;

            //try/catch block is to check for invalid dates that may occur due to daylight savings.
            //if the time 1am does not exist due to daylight savings moving the clocks forward at 12:59am then
            //use 2am instead.
            try{
                currentTrans = new DateTime(Integer.valueOf(transSplit[2]),
                    Integer.valueOf(transSplit[1]), Integer.valueOf(transSplit[0]), 1, 1);
            }
            catch (IllegalInstantException e){
                currentTrans = new DateTime(Integer.valueOf(transSplit[2]),
                        Integer.valueOf(transSplit[1]), Integer.valueOf(transSplit[0]), 2, 1);
            }

            //loop through all transactions in the new sorted list
            int position = 0;
            for (int i = 0; i < temp.size(); i++){
                String[] split = temp.get(i).getDate().split("/");
                DateTime current = new DateTime(Integer.valueOf(split[2]),
                        Integer.valueOf(split[1]), Integer.valueOf(split[0]), 0, 0);

                //compare dates, if the current transaction is after the one in the new list
                if(currentTrans.isAfter(current)){
                    break;
                }
                else{
                    position++;
                }
            }
            //add the transaction to the correct location in the new list
            temp.add(position, transaction);
        }

        //set the sorted list as the current list
        transactions = temp;
    }
}
