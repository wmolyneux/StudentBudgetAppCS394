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
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.TransactionAdapterListener;

public class HistoryFragment extends ListFragment implements TabHost.OnTabChangeListener {

    TabHost tabHost;
    List<Transaction> transactions = null;
    SQLiteHelper db;
    ArrayAdapter<String> adapter;
    View context;
    ListView list;
    ArrayList<String> values;
    Account currentAcc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_history, container, false);
        context = inflate;
        db = new SQLiteHelper(getActivity());
        currentAcc = (Account) getArguments().getSerializable("ACCOUNT");
        setupTabHost();

        return inflate;
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
                if(transaction.getAccountId()==currentAcc.getId() && !transaction.getType().equalsIgnoreCase("minus")){
                    values.add(transaction.getAmount().toString());
                    transactions.add(transaction);
                }
            }
        }
        else{
            transactions = new ArrayList<Transaction>();
            for (Transaction transaction : db.getAllTransactions()){
                if(transaction.getAccountId()==currentAcc.getId() && transaction.getType().equalsIgnoreCase("minus")){
                    values.add("-"+transaction.getAmount().toString());
                    transactions.add(transaction);
                }
            }
        }

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_accounts, values);
        //setup onclick listeners using adapter listener.
        TransactionAdapterListener listen = new TransactionAdapterListener(getActivity(), transactions, db, adapter, values);
        list.setOnItemLongClickListener(listen);
        list.setOnItemClickListener(listen);


        setListAdapter(adapter);
    }
}
