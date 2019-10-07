package com.example.obeo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.ObjectStreamException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ClientServer.ClientRequests.ObeoClient;
import interests.UserInterest;
import user.ObeoUser;

public class ELAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> categories;
    Map<String, List<String>> specifics;
    List<String> userInterests;

    public ELAdapter(Context context, List<String> categories, Map<String, List<String>> specifics) {
        this.context = context;
        this.categories = categories;
        this.specifics = specifics;
        userInterests = ObeoClient.getInstance().getClientUser().getUserInterests().stream().map(UserInterest::getInterest).collect(Collectors.toList());
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return specifics.get(categories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return specifics.get(categories.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String category = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.interests_parent, null);
        }

        TextView textParent = (TextView) convertView.findViewById(R.id.textParent);
        textParent.setText(category);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String specific = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.interests_child, null);
        }

        CheckBox checkBox = convertView.findViewById(R.id.textChild);
        checkBox.setText(specific);

        if (ObeoClient.getInstance().getClientUser().getUserInterests().contains(new UserInterest(ObeoClient.getInstance().getClientUser().getId(), specific))) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CheckBoxListener());



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            System.out.println("Checking");

            ObeoClient.getInstance().createInterest(buttonView.getText().toString());
        } else {
            System.out.println("Unchecking");
            ObeoClient.getInstance().deleteInterest(new UserInterest(ObeoClient.getInstance().getClientUser().getId(), buttonView.getText().toString()));
            System.out.println(ObeoClient.getInstance().getClientUser().getUserInterests().size());
        }
    }

}