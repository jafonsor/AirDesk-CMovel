package pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;

import java.security.acl.Group;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.R;

/**
 * Created by ist169408 on 06-04-2015.
 */
public abstract class ListExpandableListAdapter<GroupClass, ChildClass> extends BaseExpandableListAdapter {

    private Activity context;
    private List<Pair<GroupClass,List<ChildClass>>> elements;
    private int groupLayout;
    private int childLayout;

    public ListExpandableListAdapter(Activity context, List<Pair<GroupClass, List<ChildClass>>> elements, int groupLayout, int childLayout) {
        this.context = context;
        this.elements = elements;
        this.groupLayout = groupLayout;
        this.childLayout = childLayout;
    }

    public Activity getContext() { return context; }

    public ChildClass getChildT(int groupPosition, int childPosition) {
        return (ChildClass) getChild(groupPosition, childPosition);
    }

    public GroupClass getGroupT(int groupPosition) {
        return (GroupClass) getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return elements.get(groupPosition).second.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return elements.get(groupPosition).second.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return elements.get(groupPosition).first;
    }

    @Override
    public int getGroupCount() {
        return elements.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildClass child = getChildT(groupPosition, childPosition);

        if(convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.file_item, null);
        }

        editChildView(child, convertView);

        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupClass group = getGroupT(groupPosition);
        if(convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.workspace_item, null);
        }

        editGroupView(group, convertView);

        return convertView;
    }

    public abstract void editChildView(ChildClass child, View convertView);
    public abstract void editGroupView(GroupClass group, View convertView);

}
