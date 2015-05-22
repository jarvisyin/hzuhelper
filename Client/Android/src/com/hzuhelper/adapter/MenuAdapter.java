package com.hzuhelper.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hzuhelper.R;
import com.hzuhelper.model.receive.ARRAY_P6002;

public class MenuAdapter extends BaseAdapter {

	private	List<ARRAY_P6002> list;
	private	Context context;
	public MenuAdapter(Context context,List<ARRAY_P6002> list){
		this.list=list;
		this.context=context;
	}
	
	class ListItemView {
		TextView name;
		TextView intro;
		TextView price;
	}

	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return list.get(arg0).getId();
	}

	@Override
	public View getView(int position, View cView, ViewGroup viewGroup) {
		ListItemView listItemView;
		if (cView == null) {
			cView = LayoutInflater.from(context).inflate(R.layout.list_item_takeout_menu, null);
			listItemView = new ListItemView();
			listItemView.name = (TextView) cView.findViewById(R.id.name);
			listItemView.price = (TextView) cView.findViewById(R.id.price);
			listItemView.intro = (TextView) cView.findViewById(R.id.intro);
			cView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) cView.getTag();
		}

		ARRAY_P6002 model = list.get(position);
		listItemView.name.setText(model.getName());
		listItemView.price.setText(String.valueOf(model.getPrice()));
		listItemView.intro.setText(model.getIntro());
		return cView;
	}
}