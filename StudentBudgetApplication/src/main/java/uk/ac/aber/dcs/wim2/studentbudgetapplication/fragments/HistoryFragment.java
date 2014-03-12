package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

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
    private ArrayList<String> values;
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

        listen = new TransactionAdapterListener(getActivity(), detail, transactions, db, listAdapter, values);
        list.setOnItemLongClickListener(listen);
        list.setOnItemClickListener(listen);
    }

    private void setupTabHost() {
        tabHost = (TabHost)context.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("income");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("expenses");

        //setup onclick listeners using adapter listener.


        tab1.setIndicator("Income");
        tab1.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String s) {
                list = (ListView)context.findViewById(android.R.id.list);
                return list;
            }
        });

        tab2.setIndicator("Expenses");
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
        tabHost.setCurrentTab(0);



    }



    @Override
    public void onTabChanged(String s) {
        values = new ArrayList<String>();

        if(tabHost.getCurrentTab()==0){
            transactions = new ArrayList<Transaction>();
            for (Transaction transaction : db.getAllTransactions()){
                if(!transaction.getType().equalsIgnoreCase("minus")){
                    values.add(BalanceUtilities.getValueAs2dpString(transaction.getAmount()));
                    transactions.add(transaction);
                }
            }
        }
        else{
            transactions = new ArrayList<Transaction>();
            for (Transaction transaction : db.getAllTransactions()){
                if(transaction.getType().equalsIgnoreCase("minus")){
                    values.add("-"+BalanceUtilities.getValueAs2dpString(transaction.getAmount()));
                    transactions.add(transaction);
                }
            }
        }

//        adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_accounts, values);
        listAdapter = new HistoryArrayAdapter(getActivity(), transactions);
        //setup onclick listeners using adapter listener.
        listen = new TransactionAdapterListener(getActivity(), detail, transactions, db, listAdapter, values);
        list.setOnItemLongClickListener(listen);
        list.setOnItemClickListener(listen);


        setListAdapter(listAdapter);
    }
}
