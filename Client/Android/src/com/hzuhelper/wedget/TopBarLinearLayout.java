package com.hzuhelper.wedget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.hzuhelper.R;

public class TopBarLinearLayout extends LinearLayout implements OnClickListener  {

	private Button btn_left;
	private Button btn_right;
	private TextView tv_center;
	private Context context;

	public TopBarLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.linear_topbar, this);

		TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.MyAttr);
		
		String centerTextViewText = ta.getString(R.styleable.MyAttr_centerTextViewText);
		String rightButtonText = ta.getString(R.styleable.MyAttr_rightButtonText);
		String leftButtonText = ta.getString(R.styleable.MyAttr_leftButtonText);

		int rbg = ta.getResourceId(R.styleable.MyAttr_rightButtonBg, 0);
		int lbg = ta.getResourceId(R.styleable.MyAttr_leftButtonBg, 0);

		btn_left = (Button) this.findViewById(R.id.btn_left);
		btn_right = (Button) this.findViewById(R.id.btn_right);
		tv_center = (TextView) this.findViewById(R.id.tv_center);

		if (leftButtonText != null)
			btn_left.setText(leftButtonText);
		else if (lbg != 0) {
			android.view.ViewGroup.LayoutParams params = btn_left
					.getLayoutParams();
			params.width = params.height;
			btn_left.setLayoutParams(params);
			btn_left.setBackgroundResource(lbg);
		} else
			btn_left.setVisibility(View.GONE);

		if (rightButtonText != null)
			btn_right.setText(rightButtonText);
		else if (rbg != 0) {
			android.view.ViewGroup.LayoutParams params = btn_right
					.getLayoutParams();
			params.width = params.height;
			btn_right.setLayoutParams(params);
			btn_right.setBackgroundResource(rbg);
		} else
			btn_right.setVisibility(View.GONE);

		if (centerTextViewText != null)
			tv_center.setText(centerTextViewText);
		
		ta.recycle();		
		btn_left.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		context.fileList();
	}
}
