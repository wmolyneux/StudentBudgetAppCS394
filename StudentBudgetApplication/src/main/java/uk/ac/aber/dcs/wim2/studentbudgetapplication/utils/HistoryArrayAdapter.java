package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;

/**
 * Created by wim2 on 12/03/2014.
 */
public class HistoryArrayAdapter extends ArrayAdapter<Transaction> {
    private final Context context;
    private List<Transaction> transactions;



    public HistoryArrayAdapter(Context context, List<Transaction> transactions) {
        super(context, R.layout.listview_transactions, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.listview_transactions, parent, false);
        TextView category = (TextView) row.findViewById(R.id.listCategoryText);
        TextView value = (TextView) row.findViewById(R.id.listValueText);

        category.setText(transactions.get(position).getCategory().toString()
                +" - "+transactions.get(position).getAmount().toString());
        value.setText(transactions.get(position).getDate().toString());

        return row;
    }
}
