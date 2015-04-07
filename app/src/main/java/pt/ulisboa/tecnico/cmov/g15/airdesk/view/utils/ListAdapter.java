package pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by joao on 07-04-2015.
 */
public abstract class ListAdapter<Item> extends BaseAdapter {

    private List<Item> mItemList;
    private int mItemLayout;
    private Context mContext;

    public ListAdapter(Context context, int itemLayout, List<Item> itemList) {
        super();
        mContext = context;
        mItemLayout = itemLayout;
        mItemList = itemList;
    }

    @Override
    public int getCount() { return mItemList.size(); }

    @Override
    public Item getItem(int position) { return mItemList.get(position); }

    @Override
    public long getItemId(int index) { return index; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mItemLayout, null);
        }

        initItemView(getItem(position), convertView);

        return convertView;
    }

    public abstract void initItemView(Item item, View view);
}
