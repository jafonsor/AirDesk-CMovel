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
        switch(0){
            case 0: return new OwnerFragment();
            case 1: return new ForeignFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(0){
            case 0: return "Owner WS";
            case 1: return "Foreign WS";
            default: return "Invalid Title";
        }
    }
}
