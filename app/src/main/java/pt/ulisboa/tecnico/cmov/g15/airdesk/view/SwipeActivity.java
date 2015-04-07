package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import pt.ulisboa.tecnico.cmov.g15.airdesk.R;


public class SwipeActivity extends FragmentActivity {

    MyAdapter mAdapter;
    ViewPager mPager;
    String[] tabs = {"Owner WS", "Foreign WS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
    }

}
