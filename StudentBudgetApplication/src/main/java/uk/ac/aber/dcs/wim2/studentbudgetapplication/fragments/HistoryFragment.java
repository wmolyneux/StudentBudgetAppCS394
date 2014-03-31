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
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.HistoryArrayAdapter;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.TransactionAdapterListener;

public class HistoryFragment extends ListFragment implements TabHost.OnTabChangeListener {

    private TabHost tabHost;
    private List<Transaction> transactions = null;
    private SQLiteDatabaseHelper db;
    private HistoryArrayAdapter listAdapter;
    private View context;
    private ListView list;
    private Detail detail;
    private TransactionAdapterListener listen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_history, container, false);
        context = inflate;
        return inflate;
    }

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


    @Override
    public void onTabChanged(String s) {
        transactions = new ArrayList<Transaction>();
        if(tabHost.getCurrentTab()==0){
            filterIncomeTransactions();
        }
        else{
            filterExpenseTransactions();
        }

        sortTransactions();
        listAdapter = new HistoryArrayAdapter(getActivity(), transactions);

        //setup onclick listeners using adapter listener.
        listen = new TransactionAdapterListener(getActivity(), transactions, db, listAdapter);
        list.setOnItemLongClickListener(listen);
        list.setOnItemClickListener(listen);

        setListAdapter(listAdapter);
    }

    private void filterExpenseTransactions() {
        for (Transaction transaction : db.getAllTransactions()){
            if(transaction.getType().equalsIgnoreCase("minus")){
                transactions.add(transaction);
            }
        }
    }

    private void filterIncomeTransactions() {
        for (Transaction transaction : db.getAllTransactions()){
            if(!transaction.getType().equalsIgnoreCase("minus")){
                transactions.add(transaction);
            }
        }
    }

    private void sortTransactions() {
        ArrayList<Transaction> temp = new ArrayList<Transaction>();
        for (Transaction transaction : transactions){
            if(temp.size() == 0){
                temp.add(transaction);
                continue;
            }
            String[] transSplit = transaction.getDate().split("/");
            DateTime currentTrans;
            try{
                currentTrans = new DateTime(Integer.valueOf(transSplit[2]),
                    Integer.valueOf(transSplit[1]), Integer.valueOf(transSplit[0]), 1, 1);
            }
            catch (IllegalInstantException e){
                currentTrans = new DateTime(Integer.valueOf(transSplit[2]),
                        Integer.valueOf(transSplit[1]), Integer.valueOf(transSplit[0]), 2, 1);
            }
            int position = 0;
            for (int i = 0; i < temp.size(); i++){
                String[] split = temp.get(i).getDate().split("/");
                DateTime current = new DateTime(Integer.valueOf(split[2]),
                        Integer.valueOf(split[1]), Integer.valueOf(split[0]), 0, 0);
                if(currentTrans.isAfter(current)){
                    break;
                }
                else{
                    position++;
                }
            }
            temp.add(position, transaction);
        }
        transactions = temp;
    }
}
