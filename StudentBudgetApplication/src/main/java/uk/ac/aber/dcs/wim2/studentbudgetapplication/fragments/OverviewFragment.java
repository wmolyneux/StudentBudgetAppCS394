package uk.ac.aber.dcs.wim2.studentbudgetapplication.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;

public class OverviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_overview, container, false);
        return inflate;
    }


}
