package com.hzuhelper.activity.user;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.hzuhelper.R;

public class PhoneTypeChoice extends Activity {

    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_phone_type_choice);
        expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new Adapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.phone_type_choice,menu);
        return true;
    }

    private class Adapter implements ExpandableListAdapter {

        @Override
        public void registerDataSetObserver(DataSetObserver observer){

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer){

        }

        @Override
        public int getGroupCount(){
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition){
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition){
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getChild(int groupPosition,int childPosition){
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getGroupId(int groupPosition){
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getChildId(int groupPosition,int childPosition){
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasStableIds(){
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public View getGroupView(int groupPosition,boolean isExpanded,View convertView,ViewGroup parent){
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public View getChildView(int groupPosition,int childPosition,boolean isLastChild,View convertView,ViewGroup parent){
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition,int childPosition){
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean areAllItemsEnabled(){
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isEmpty(){
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition){
            // TODO Auto-generated method stub

        }

        @Override
        public void onGroupCollapsed(int groupPosition){
            // TODO Auto-generated method stub

        }

        @Override
        public long getCombinedChildId(long groupId,long childId){
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId){
            // TODO Auto-generated method stub
            return 0;
        }

    }

}
