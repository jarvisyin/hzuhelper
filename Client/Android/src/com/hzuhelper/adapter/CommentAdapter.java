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
import com.hzuhelper.model.receive.ARRAY_CT0001;
import com.hzuhelper.model.receive.ARRAY_CT0002;
import com.hzuhelper.utils.ConstantStrUtil;
import com.hzuhelper.utils.DateUtil;
import com.hzuhelper.utils.StringUtils;

public class CommentAdapter extends BaseAdapter {

    private Context             context;
    ArrayList<ARRAY_CT0002> list;
    ARRAY_CT0001                chat_tweetInfo;

    public CommentAdapter(Context context,ArrayList<ARRAY_CT0002> list,ARRAY_CT0001 chat_tweetInfo){
        this.context = context;
        this.list = list;
        this.chat_tweetInfo = chat_tweetInfo;
    }

    class ListItemView {
        LinearLayout lly_info;
        TextView     tvDate;
        TextView     tvContent;
        TextView     tv_author;
        Button       btnComment;
    }

    public int getCount(){
        return list.size();
    }

    public Object getItem(int position){
        return list.get(position);
    }

    public long getItemId(int position){
        return list.get(position).getId();
    }

    public View getView(int position,View convertView,ViewGroup parent){
        ListItemView listItemView = null;
        if (convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_chat_comment1,null);
            listItemView = new ListItemView();
            listItemView.tvDate = (TextView)convertView.findViewById(R.id.tv_date);
            listItemView.tvContent = (TextView)convertView.findViewById(R.id.tv_content);
            listItemView.btnComment = (Button)convertView.findViewById(R.id.btn_comment);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView)convertView.getTag();
        }
        ARRAY_CT0002 model = list.get(position);
        listItemView.tvDate.setText(DateUtil.friendly_time(model.getPublishDate()));
        listItemView.tvContent.setText((Html.fromHtml(StringUtils.tweetContentTran(model.getContent()))));
        listItemView.btnComment.setTag(list.get(position));
        listItemView.btnComment.setOnClickListener(btn_commentOnClickListener);
        return convertView;
    }

    private OnClickListener btn_commentOnClickListener = new OnClickListener() {
                                                           public void onClick(View v){
                                                               ARRAY_CT0002 chat_commentInfo = (ARRAY_CT0002)v.getTag();
                                                               Intent intent = new Intent(context,CommentCommit.class);
                                                               intent.putExtra("tweetId",chat_tweetInfo.getId());
                                                               intent.putExtra(ConstantStrUtil.COMMENT_ID,chat_commentInfo.getId());
                                                               context.startActivity(intent);
                                                           }
                                                       };
}
