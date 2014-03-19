package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        ImageView icon = (ImageView)row.findViewById(R.id.listIcon);

        category.setText(transactions.get(position).getCategory()+" - "+FragmentUtilities.getCurrency(context)+
                BalanceUtilities.getValueAs2dpString(Float.valueOf(transactions.get(position).getAmount().toString())));
        value.setText(transactions.get(position).getDate());
        icon.setImageResource(findImageId(transactions.get(position).getCategory()));

        return row;
    }

    public int findImageId(String cat){
        int image = 0;
        if(cat.equalsIgnoreCase("food")){
            image = R.drawable.ic_food;
        }
        else if(cat.equalsIgnoreCase("supermarket")){
            image = R.drawable.supermarket;
        }
        else if(cat.equalsIgnoreCase("university")){
            image = R.drawable.university;
        }
        else if(cat.equalsIgnoreCase("clothing")){
            image = R.drawable.clothing;
        }
        else if(cat.equalsIgnoreCase("other")){
            image = R.drawable.other;
        }
        else if(cat.equalsIgnoreCase("sport")){
            image = R.drawable.sports;
        }
        else if(cat.equalsIgnoreCase("travel")){
            image = R.drawable.travel;
        }
        else if(cat.equalsIgnoreCase("socialising")){
            image = R.drawable.booze;
        }
        else if(cat.equalsIgnoreCase("micro transaction")){
            image = R.drawable.microtrans;
        }
        return image;
    }


}
