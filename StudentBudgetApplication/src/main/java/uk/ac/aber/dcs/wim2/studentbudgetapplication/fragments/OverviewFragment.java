package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;

public class OverviewFragment extends Fragment {

    Account currentAccount = null;
    TextView balance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_overview, container, false);

        Bundle bundle = getArguments();
        currentAccount = (Account)bundle.getSerializable("ACCOUNT");
        balance = (TextView)inflate.findViewById(R.id.balanceNum);


        balance.setText("£"+currentAccount.getBalance().toString());
        return inflate;
    }


}
