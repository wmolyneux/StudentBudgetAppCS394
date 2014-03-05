package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Detail;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteDatabaseHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.utils.BalanceUtilities;

public class OverviewFragment extends Fragment {

    Detail detail = null;
    SQLiteDatabaseHelper db;
    TextView weeklyIncome;
    TextView weeklyExpense;
    TextView weeklyBalance;
    TextView totalBalance;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_overview, container, false);
        registerViews(inflate);

        return inflate;
    }

    private void registerViews(View view) {
        weeklyIncome = (TextView) view.findViewById(R.id.overWeeklyIncome);
        weeklyExpense = (TextView) view.findViewById(R.id.overWeeklyExpense);
        weeklyBalance = (TextView) view.findViewById(R.id.overWeeklyBalance);
        totalBalance = (TextView) view.findViewById(R.id.totalBalance);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new SQLiteDatabaseHelper(getActivity());
        detail = db.getAllDetails().get(0);
        setup();
    }

    private void setup() {
        weeklyIncome.setText(BalanceUtilities.getValueAs2dpString(detail.getWeeklyIncome()));
        weeklyExpense.setText(BalanceUtilities.getValueAs2dpString(detail.getWeeklyExpense()));

        detail = BalanceUtilities.recalculateBalance(detail, db);

        totalBalance.setText(BalanceUtilities.getValueAs2dpString(detail.getBalance()));
        weeklyBalance.setText(BalanceUtilities.getValueAs2dpString(detail.getWeeklyBalance()));
        db.updateDetail(detail);
    }
    
}
