package com.hzuhelper.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hzuhelper.R;
import com.hzuhelper.activity.chat.CommentCommit;
import com.hzuhelper.activity.chat.TweetShow;
import com.hzuhelper.model.ChatTweetInfo;
import com.hzuhelper.tools.DateUtil;
import com.hzuhelper.tools.StringUtils;

public class TweetAdapter extends BaseAdapter {
	private ArrayList<ChatTweetInfo> list = null;
	private Context context;

	class ListItemView {
		LinearLayout lly_info;
		TextView tv_date;
		TextView tv_content;
		TextView tv_author;
		Button btn_comment;
	}

	public TweetAdapter(ArrayList<ChatTweetInfo> list, Context context) {
		this.context = context;
		this.list = list;
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
			view = LayoutInflater.from(context).inflate(
					R.layout.list_item_chat_tweet, null);
			listItemView = new ListItemView();
			listItemView.lly_info = (LinearLayout) view
					.findViewById(R.id.lly_info);
			listItemView.tv_date = (TextView) view.findViewById(R.id.tv_date);
			listItemView.tv_content = (TextView) view
					.findViewById(R.id.tv_content);
			listItemView.tv_author = (TextView) view
					.findViewById(R.id.tv_author);
			listItemView.btn_comment = (Button) view
					.findViewById(R.id.llo_foot).findViewById(R.id.btn_comment);
			view.setTag(listItemView);
		} else {
			listItemView = (ListItemView) view.getTag();
		}
		ChatTweetInfo model = list.get(index);

		listItemView.lly_info.setTag(model);
		listItemView.lly_info.setOnClickListener(liOnclickListerer);
		listItemView.tv_date.setText(DateUtil.friendly_time(model
				.getPublishDate()));
		listItemView.tv_content.setText(Html.fromHtml(StringUtils
				.tweetContentTran(model.getContent())));
		listItemView.tv_author.setText("同学甲乙丙丁");
		listItemView.btn_comment.setTag(model.getId());
		listItemView.btn_comment.setOnClickListener(bcOnclickListerer);
		if (model.getComment_count() > 0)
			listItemView.btn_comment.setText(String.valueOf(model
					.getComment_count()));
		else
			listItemView.btn_comment.setText("评论");
		return view;
	}

	OnClickListener bcOnclickListerer = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(context, CommentCommit.class);
			intent.putExtra("tweetId", (Integer) (v.getTag()));
			context.startActivity(intent);
		}
	};

	OnClickListener liOnclickListerer = new OnClickListener() {
		public void onClick(View v) {
			ChatTweetInfo model = (ChatTweetInfo) v.getTag();
			Intent intent = new Intent(context, TweetShow.class);
			intent.putExtra("tweetId", model.getId());
			context.startActivity(intent);
		}
	};
}
