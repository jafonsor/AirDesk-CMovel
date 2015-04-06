package pt.ulisboa.tecnico.cmov.g15.airdesk.view;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

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
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "Owner WS";
            case 1: return "Foreign WS";
            case 2: return "Settings";
            default: return "Invalid Title";
        }
    }
}
