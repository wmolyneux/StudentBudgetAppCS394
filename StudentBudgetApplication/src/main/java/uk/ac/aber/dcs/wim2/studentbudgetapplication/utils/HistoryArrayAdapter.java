package uk.ac.aber.dcs.wim2.studentbudgetapplication.utils;

import android.content.Context;
import android.content.res.TypedArray;
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
 * This class contains the functionality for the list items in the history screen of the application
 *
 * @author wim2
 * @version 1.0
 */
public class HistoryArrayAdapter extends ArrayAdapter<Transaction> {
    private final Context context;
    private List<Transaction> transactions;

    public HistoryArrayAdapter(Context context, List<Transaction> transactions) {
        super(context, R.layout.listview_transactions, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    /**
     * Called when the list view is created to setup and inflate the list and it's items
     *
     * @param position - position in the list
     * @param convertView - view
     * @param parent - parent view group
     *
     * @return - list view item
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //instantiate views
        View row = inflater.inflate(R.layout.listview_transactions, parent, false);
        TextView category = (TextView) row.findViewById(R.id.listCategoryText);
        TextView value = (TextView) row.findViewById(R.id.listValueText);
        ImageView icon = (ImageView)row.findViewById(R.id.listIcon);
        TypedArray categoryArray = context.getResources().obtainTypedArray(R.array.categories);

        //set string values to the transaction category and amount
        String currentCat = categoryArray.getString(transactions.get(position).getCategory());
        category.setText(currentCat+" - "+FragmentUtilities.getCurrency(context)+
                BalanceUtilities.getValueAs2dpString(Float.valueOf(transactions.get(position).getAmount().toString())));
        value.setText(transactions.get(position).getDate());

        //set icon for the transaction
        icon.setImageResource(findImageId(transactions.get(position).getCategory()));

        return row;
    }

    /**
     * Finds the correct image for the given category
     *
     * @param cat - category number
     *
     * @return - image value
     */
    public int findImageId(int cat){
        int image = 0;
        if(cat == 0){
            image = R.drawable.ic_food;
        }
        else if(cat == 1){
            image = R.drawable.supermarket;
        }
        else if(cat == 2){
            image = R.drawable.booze;
        }
        else if(cat == 3){
            image = R.drawable.sports;
        }
        else if(cat == 4){
            image = R.drawable.university;
        }
        else if(cat == 5){
            image = R.drawable.travel;
        }
        else if(cat == 6){
            image = R.drawable.clothing;
        }
        else if(cat == 7){
            image = R.drawable.other;
        }
        else if(cat == 8){
            image = R.drawable.microtrans;
        }
        return image;
    }


}
