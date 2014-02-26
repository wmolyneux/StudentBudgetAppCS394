package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Transaction;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.newActivities.Detail;

public class OverviewFragment extends Fragment {

    Account currentAccount = null;
    TextView balanceText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_overview, container, false);
        Bundle bundle = getArguments();
        Detail detail = (Detail) bundle.getSerializable("detail");
        balanceText = (TextView) inflate.findViewById(R.id.incomeNum);
        balanceText.setText(detail.getWeeklyIncome().toString());
//
//        Bundle bundle = getArguments();
//        currentAccount = (Account)bundle.getSerializable("ACCOUNT");
//
//        balanceText = (TextView)inflate.findViewById(R.id.balanceNum);
//
//        balanceText.setText("£"+currentAccount.getBalance().toString());
        return inflate;
    }





}
