package com.hzuhelper.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hzuhelper.R;
import com.hzuhelper.activity.chat.CommentCommit;
import com.hzuhelper.activity.chat.TweetShow;
import com.hzuhelper.model.receive.P6005;
import com.hzuhelper.utils.ConstantStrUtil;
import com.hzuhelper.utils.DateUtil;

public class TweetMsgAdapter extends BaseAdapter {
	private ArrayList<P6005> list = null;
	private LayoutInflater listContainer;
	private Context context;

	class ListItemView { // 自定义控件集合
		public View lly_info;
		public ImageButton btn_comment;
		public TextView tv_author;
		public TextView tv_date;
		public TextView tv_content;
		public TextView tv_yourcontent;
	}

	public TweetMsgAdapter(ArrayList<P6005> list,
			Context context) {
		this.context = context;
		this.list = list;
		listContainer = LayoutInflater.from(context);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int index) {
		return list.get(index);
	}

	public long getItemId(int index) {
		return list.get(index).getId();
	}

	public View getView(int index, View view, ViewGroup viewGroup) {
		ListItemView listItemView = null;
		if (view == null) {
			view = listContainer
					.inflate(R.layout.list_item_chat_comment2, null);
			listItemView = new ListItemView();

			listItemView.lly_info = view.findViewById(R.id.lly_info);
			listItemView.tv_author = (TextView) view
					.findViewById(R.id.tv_author);
			listItemView.tv_date = (TextView) view.findViewById(R.id.tv_date);
			listItemView.tv_content = (TextView) view
					.findViewById(R.id.tv_content);
			listItemView.tv_yourcontent = (TextView) view
					.findViewById(R.id.tv_yourcontent);
			listItemView.btn_comment = (ImageButton) view
					.findViewById(R.id.btn_comment);

			view.setTag(listItemView);
		} else {
			listItemView = (ListItemView) view.getTag();
		}
		P6005 model = list.get(index);

		if (model.getResponseCommentId() < 1)
			listItemView.tv_yourcontent.setText("回复你的树洞："
					+ model.getYourContent() + " ......");
		else
			listItemView.tv_yourcontent.setText("回复你的评论："
					+ model.getYourContent() + " ......");
		listItemView.tv_date.setText(DateUtil.friendly_time(model
				.getPublishDate()));
		listItemView.tv_content.setText(model.getContent());
		listItemView.tv_author.setText("同学甲乙丙丁");
		listItemView.btn_comment.setTag(model);
		listItemView.btn_comment.setOnClickListener(bcOnclickListerer);
		listItemView.lly_info.setTag(model);
		listItemView.lly_info.setOnClickListener(lioOnclickListerer);
		return view;
	}

	OnClickListener bcOnclickListerer = new OnClickListener() {
		public void onClick(View v) {
			P6005 model = (P6005) v.getTag();
			Intent intent = new Intent(context, CommentCommit.class);
			intent.putExtra("tweetId", model.getTweetId());
			intent.putExtra(ConstantStrUtil.COMMENT_ID, model.getId());
			context.startActivity(intent);
		}
	};

	OnClickListener lioOnclickListerer = new OnClickListener() {
		public void onClick(View v) {
			P6005 model = (P6005) v.getTag();
			Intent intent = new Intent(context, TweetShow.class);
			intent.putExtra("tweetId", model.getTweetId());
			context.startActivity(intent);
		}
	};
}