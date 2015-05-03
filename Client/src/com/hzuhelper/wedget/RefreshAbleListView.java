package com.hzuhelper.wedget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hzuhelper.R;

public class RefreshAbleListView extends ListView implements OnScrollListener{

  private static final String   TAG                = "listview";

  private final static int      RELEASE_To_REFRESH = 0;
  private final static int      PULL_To_REFRESH    = 1;
  private final static int      REFRESHING         = 2;

  private final static int      GETTING            = 4;

  private final static int      DONE               = 7;
  private final static int      LOADING            = 8;

  // 实际的padding的距离与界面上偏移距离的比例
  private final static int      RATIO              = 3;

  private LayoutInflater        inflater;

  private LinearLayout          headView;
  private LinearLayout          footView;

  private TextView              tv_head_tips;
  private TextView              tv_foot_tips;
  private ProgressBar           pb_head;
  private ProgressBar           pb_foot;
  private ImageView             aiv_head;

  private RotateAnimation       animation;
  private RotateAnimation       reverseAnimation;

  // 用于保证startY的值在一个完整的touch事件中只被记录一次
  private boolean               isRecored;

  // private int headContentWidth;
  private int                   headContentHeight;

  private int                   startY;
  private int                   firstItemIndex;

  private int                   changeHeader_State;
  private int                   changeFooter_State;

  private boolean               isBack;

  private OnRefreshListener     refreshListener;
  private OnGetMoreDateListener getMoreDateListener;

  private boolean               isRefreshable;
  private boolean               isGetMoreDataable;

  public RefreshAbleListView(Context context){
    super(context);
    init(context);
  }

  public RefreshAbleListView(Context context,AttributeSet attrs){
    super(context, attrs);
    init(context);
  }

  private void init(Context context){
    setCacheColorHint(context.getResources().getColor(R.color.colorF5F5F5));
    inflater = LayoutInflater.from(context);

    // 设置 listview 的 header
    headView = (LinearLayout)inflater.inflate(R.layout.chat_tweet_listview_head,null);

    aiv_head = (ImageView)headView.findViewById(R.id.aiv_head);
    aiv_head.setMinimumWidth(70);
    aiv_head.setMinimumHeight(50);
    pb_head = (ProgressBar)headView.findViewById(R.id.pb_head);
    tv_head_tips = (TextView)headView.findViewById(R.id.tv_head_tips);

    measureView(headView);
    headContentHeight = headView.getMeasuredHeight();
    // headContentWidth = headView.getMeasuredWidth();

    headView.setPadding(0,-1*headContentHeight,0,0);
    headView.invalidate();

    addHeaderView(headView,null,false);

    animation = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
    animation.setInterpolator(new LinearInterpolator());
    animation.setDuration(250);
    animation.setFillAfter(true);

    reverseAnimation = new RotateAnimation(-180,0,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
    reverseAnimation.setInterpolator(new LinearInterpolator());
    reverseAnimation.setDuration(200);
    reverseAnimation.setFillAfter(true);

    // 设置 listview 的 footer
    footView = (LinearLayout)inflater.inflate(R.layout.chat_tweet_listview_foot,null);
    pb_foot = (ProgressBar)footView.findViewById(R.id.pb_foot);
    addFooterView(footView,null,false);
    tv_foot_tips = (TextView)footView.findViewById(R.id.tv_foot_tips);
    footView.setVisibility(View.GONE);

    // 初始化设置
    setOnScrollListener(this);

    changeHeader_State = DONE;
    isRefreshable = false;
    isGetMoreDataable = false;
  }

  // 无须下拉进行刷新
  public void refresh(){
    changeHeader_State = REFRESHING;
    setSelection(0);
    changeHeaderViewByState();
    onRefresh();
  }

  // 设置是否可以下拉刷新和获取更多数据
  public void setIsGetMoreDataable(boolean bool){
    isGetMoreDataable = bool;
    if (isGetMoreDataable) {
      changeFooter_State = DONE;
      changeFooterViewByState();
      footView.setVisibility(View.VISIBLE);
    } else {
      footView.setVisibility(View.GONE);
    }
  }

  // 下拉刷新操作
  public interface OnRefreshListener{
    public void onRefresh();
  }

  public void setonRefreshListener(OnRefreshListener refreshListener){
    this.refreshListener = refreshListener;
    isRefreshable = true;
  }

  public void onRefreshComplete(){
    changeHeader_State = DONE;
    changeHeaderViewByState();
  }

  private void onRefresh(){
    if (refreshListener!=null) {
      refreshListener.onRefresh();
    }
  }

  private void changeHeaderViewByState(){
    switch (changeHeader_State) {
    case RELEASE_To_REFRESH:
      aiv_head.setVisibility(View.VISIBLE);
      pb_head.setVisibility(View.GONE);
      tv_head_tips.setVisibility(View.VISIBLE);

      aiv_head.clearAnimation();
      aiv_head.startAnimation(animation);

      tv_head_tips.setText("松开刷新");
      // 当前状态，松开刷新
      break;
    case PULL_To_REFRESH:
      pb_head.setVisibility(View.GONE);
      tv_head_tips.setVisibility(View.VISIBLE);
      aiv_head.clearAnimation();
      aiv_head.setVisibility(View.VISIBLE);
      // 是由RELEASE_To_REFRESH状态转变来的
      if (isBack) {
        isBack = false;
        aiv_head.clearAnimation();
        aiv_head.startAnimation(reverseAnimation);

        tv_head_tips.setText("下拉刷新");
      } else {
        tv_head_tips.setText("下拉刷新");
      }
      // 当前状态，下拉刷新
      break;

    case REFRESHING:

      headView.setPadding(0,0,0,0);

      pb_head.setVisibility(View.VISIBLE);
      aiv_head.clearAnimation();
      aiv_head.setVisibility(View.GONE);
      tv_head_tips.setText("正在刷新...");
      // 当前状态,正在刷新...
      break;
    case DONE:
      headView.setPadding(0,-1*headContentHeight,0,0);

      pb_head.setVisibility(View.GONE);
      aiv_head.clearAnimation();
      aiv_head.setImageResource(R.drawable.img_arrow);
      tv_head_tips.setText("下拉刷新");
      // 当前状态，done
      break;
    }
  }

  public void onScroll(AbsListView arg0,int firstVisiableItem,int arg2,int arg3){
    firstItemIndex = firstVisiableItem;
  }

  public boolean onTouchEvent(MotionEvent event){
    if (isRefreshable) {
      switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (firstItemIndex==0&&!isRecored) {
          isRecored = true;
          startY = (int)event.getY();
          Log.v(TAG,"在down时候记录当前位置‘");
        }
        break;
      case MotionEvent.ACTION_UP:
        if (changeHeader_State!=REFRESHING&&changeHeader_State!=LOADING) {
          if (changeHeader_State==DONE) {
            // 什么都不做
          }
          if (changeHeader_State==PULL_To_REFRESH) {
            changeHeader_State = DONE;
            changeHeaderViewByState();
            // 由下拉刷新状态，到done状态");
          }
          if (changeHeader_State==RELEASE_To_REFRESH) {
            changeHeader_State = REFRESHING;
            changeHeaderViewByState();
            onRefresh();
            // 由松开刷新状态，到done状态
          }
        }
        isRecored = false;
        isBack = false;
        break;
      case MotionEvent.ACTION_MOVE:
        int tempY = (int)event.getY();
        if (!isRecored&&firstItemIndex==0) {
          Log.v(TAG,"在move时候记录下位置");
          isRecored = true;
          startY = tempY;
        }
        if (changeHeader_State!=REFRESHING&&isRecored&&changeHeader_State!=LOADING) {
          // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
          // 可以松手去刷新了
          if (changeHeader_State==RELEASE_To_REFRESH) {
            setSelection(0);
            // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
            if (((tempY-startY)/RATIO<headContentHeight)&&(tempY-startY)>0) {
              changeHeader_State = PULL_To_REFRESH;
              changeHeaderViewByState();
              // 由松开刷新状态转变到下拉刷新状态
            }
            // 一下子推到顶了
            else if (tempY-startY<=0) {
              changeHeader_State = DONE;
              changeHeaderViewByState();
              // 由松开刷新状态转变到done状态
            }
            // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
            else {
              // 不用进行特别的操作，只用更新paddingTop的值就行了
            }
          }
          // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
          if (changeHeader_State==PULL_To_REFRESH) {
            setSelection(0);
            // 下拉到可以进入RELEASE_TO_REFRESH的状态
            if ((tempY-startY)/RATIO>=headContentHeight) {
              changeHeader_State = RELEASE_To_REFRESH;
              isBack = true;
              changeHeaderViewByState();
              // 由done或者下拉刷新状态转变到松开刷新
            }
            // 上推到顶了
            else if (tempY-startY<=0) {
              changeHeader_State = DONE;
              changeHeaderViewByState();
              // 由DOne或者下拉刷新状态转变到done状态
            }
          }
          // done状态下
          if (changeHeader_State==DONE) {
            if (tempY-startY>0) {
              changeHeader_State = PULL_To_REFRESH;
              changeHeaderViewByState();
            }
          }
          // 更新headView的size
          if (changeHeader_State==PULL_To_REFRESH) {
            headView.setPadding(0,-1*headContentHeight+(tempY-startY)/RATIO,0,0);
          }
          // 更新headView的paddingTop
          if (changeHeader_State==RELEASE_To_REFRESH) {
            headView.setPadding(0,(tempY-startY)/RATIO-headContentHeight,0,0);
          }
        }
        break;
      }
    }
    return super.onTouchEvent(event);
  }

  // 获取更多数据操作
  public interface OnGetMoreDateListener{
    public void onGetMoreDate();
  }

  public void setonGetMoreDateListene(OnGetMoreDateListener getMoreDateListener){
    this.getMoreDateListener = getMoreDateListener;
  }

  public void onGetMoreDateComplete(){
    changeFooter_State = DONE;
    changeFooterViewByState();
  }

  private void OnGetFormalDate(){
    if (getMoreDateListener!=null) {
      getMoreDateListener.onGetMoreDate();
    }
  }

  private void changeFooterViewByState(){
    switch (changeFooter_State) {
    case GETTING:
      pb_foot.setVisibility(View.VISIBLE);
      tv_foot_tips.setText("加载中...");
      break;
    case DONE:
      pb_foot.setVisibility(View.GONE);
      tv_foot_tips.setText("更新");
      break;
    }
  }

  public void onScrollStateChanged(AbsListView view,int scrollState){
    if (isGetMoreDataable) {
      switch (scrollState) {
      case OnScrollListener.SCROLL_STATE_IDLE:
        if (changeFooter_State==DONE&&getLastVisiblePosition()>view.getCount()-2) {
          changeFooter_State = GETTING;
          changeFooterViewByState();
          OnGetFormalDate();
        } else {
          Log.i("jarvisyin","getLastVisiblePosition() - view.getCount() + 2 = "+(getLastVisiblePosition()-view.getCount()+2));
        }
        break;
      default:
        break;
      }
    }
  }

  // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
  private void measureView(View child){
    ViewGroup.LayoutParams p = child.getLayoutParams();
    if (p==null) {
      p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    int childWidthSpec = ViewGroup.getChildMeasureSpec(0,0+0,p.width);
    int lpHeight = p.height;
    int childHeightSpec;
    if (lpHeight>0) {
      childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,MeasureSpec.EXACTLY);
    } else {
      childHeightSpec = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
    }
    child.measure(childWidthSpec,childHeightSpec);
  }

  // public void setAdapter(BaseAdapter adapter) {
  // lastUpdatedTextView.setText("最近更新:" + sdf.format(new Date()));
  // super.setAdapter(adapter);
  // }

}