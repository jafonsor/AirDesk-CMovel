package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

/**
 * Created by MSC on 06/04/2015.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.ulisboa.tecnico.cmov.g15.airdesk.R;

public class ForeignFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.foreign_layout, container, false);
        return rootView;
    }
}
