package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.Account;
import uk.ac.aber.dcs.wim2.studentbudgetapplication.database.SQLiteHelper;

public class BudgetsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_budgets, container, false);
        createDatabase();
        return inflate;
    }


    public void createDatabase(){
        SQLiteHelper db = new SQLiteHelper(getActivity());


        List<Account> list = db.getAllAccounts();
        String example = "";
        for (int i = 0; i< list.size(); i++){
            example += list.get(i).getAccountName() +" + ";
        }
//        Toast.makeText(getActivity(), example, Toast.LENGTH_LONG).show();


    }
}
