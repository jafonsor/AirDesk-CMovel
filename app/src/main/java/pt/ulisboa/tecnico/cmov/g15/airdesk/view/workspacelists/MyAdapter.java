package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by MSC on 06/04/2015.
 */
public class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new OwnerFragment();
            case 1: return new ForeignFragment();
            case 2: return new SettingsFragment();
            case 3: return new AuxiliaryFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "Owner WS";
            case 1: return "Foreign WS";
            case 2: return "Settings";
            case 3: return "Auxiliary";
            default: return "Invalid Title";
        }
    }
}
