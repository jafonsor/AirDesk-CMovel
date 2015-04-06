package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

/**
 * Created by MSC on 06/04/2015.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import pt.ulisboa.tecnico.cmov.g15.airdesk.R;

public class OwnerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.owner_layout, container, false);
        return rootView;
    }
}
