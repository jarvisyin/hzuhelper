package com.hzuhelper.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hzuhelper.R;
import com.hzuhelper.model.ScoreInfo;

public class ScoreAdapter extends BaseAdapter {
	private	ArrayList<ScoreInfo> sList;
	private Context context;
	public boolean isFail;
	public ScoreAdapter(Context context,ArrayList<ScoreInfo> sList){
		this.sList=sList;
		this.context=context;
	}

	public int getCount() {
		return sList.size();
	}

	@Override
	public Object getItem(int position) {
		return sList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_score, null);
			listItemView = new ListItemView();
			listItemView.tv_kcmc = (TextView) convertView.findViewById(R.id.tv_kcmc);
			listItemView.tv_xn = (TextView) convertView.findViewById(R.id.tv_xn);
			listItemView.tv_xq = (TextView) convertView.findViewById(R.id.tv_xq);
			listItemView.tv_kclx = (TextView) convertView.findViewById(R.id.tv_kclx);
			listItemView.tv_khfs = (TextView) convertView.findViewById(R.id.tv_khfs);
			listItemView.tv_pscj = (TextView) convertView.findViewById(R.id.tv_pscj);
			listItemView.tv_qzcj = (TextView) convertView.findViewById(R.id.tv_qzcj);
			listItemView.tv_sycj = (TextView) convertView.findViewById(R.id.tv_sycj);
			listItemView.tv_qmcj = (TextView) convertView.findViewById(R.id.tv_qmcj);
			listItemView.tv_zpcj = (TextView) convertView.findViewById(R.id.tv_zpcj);
			listItemView.tv_cxcj = (TextView) convertView.findViewById(R.id.tv_cxcj);
			listItemView.tv_jd = (TextView) convertView.findViewById(R.id.tv_jd);
			listItemView.tv_xf = (TextView) convertView.findViewById(R.id.tv_xf);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		ScoreInfo model = sList.get(position);

		listItemView.tv_kcmc.setText(model.getKcmc());
		listItemView.tv_xn.setText(model.getXn() + "学年 ");
		listItemView.tv_xq.setText("第" + model.getXq() + "学期");
		listItemView.tv_kclx.setText("课程类型:" + model.getKclx());
		listItemView.tv_khfs.setText("考核方式:" + model.getKhfs());
		listItemView.tv_pscj.setText("平时成绩:" + model.getPscj());
		listItemView.tv_qzcj.setText("期中成绩:" + model.getQzcj());
		listItemView.tv_sycj.setText("实验成绩:" + model.getSycj());
		listItemView.tv_qmcj.setText("期末成绩:" + model.getQmcj());
		listItemView.tv_zpcj.setTextColor(Color.BLACK);
		listItemView.tv_zpcj.setText(model.getZpcj());
		listItemView.tv_cxcj.setText("重修成绩:" + model.getCxcj());
		listItemView.tv_jd.setText("绩点:" + model.getJd());
		listItemView.tv_xf.setText("学分:" + model.getXf());

		if (model.getXn().equals("2013-2014"))
			try {
				float zpcj = Float.parseFloat(model.getZpcj());
				if (zpcj < 60) {
					listItemView.tv_zpcj.setTextColor(Color.RED);
					isFail=true;
				}
			} catch (Exception e) {
			}

		return convertView;
	}
	
	private final class ListItemView {
		TextView tv_kcmc;
		TextView tv_xn;
		TextView tv_xq;
		TextView tv_kclx;
		TextView tv_khfs;
		TextView tv_pscj;
		TextView tv_qzcj;
		TextView tv_sycj;
		TextView tv_qmcj;
		TextView tv_zpcj;
		TextView tv_cxcj;
		TextView tv_jd;
		TextView tv_xf;
	}
}
