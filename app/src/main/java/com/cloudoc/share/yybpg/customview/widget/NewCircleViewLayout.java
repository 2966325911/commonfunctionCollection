package com.cloudoc.share.yybpg.customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.cloudoc.share.yybpg.customview.R;

/**
 * @author : Vic
 * time   : 2018/07/30
 * desc   :
 */
public class NewCircleViewLayout  extends ViewGroup {

    private int mRadius;
    /**
     * 该容器内child item的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
    /**
     * 菜单的中心child的默认尺寸
     */
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private static final int FLINGABLE_VALUE = 300;

    /**
     * 如果移动角度达到该值，则屏蔽点击
     */
    private static final int NOCLICK_VALUE = 3;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private int mFlingableValue = FLINGABLE_VALUE;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private float mPadding;
    /**
     * 布局时的开始角度
     */
    private double mStartAngle = 0;
    /**
     * 菜单项的文本
     */
    private String[] mItemTexts;
    /**
     * 菜单项的图标
     */
    private int[] mItemImgs;

    /**
     * 菜单的个数
     */
    private int mMenuItemCount;

    /**
     * 检测按下到抬起时旋转的角度
     */
    private float mTmpAngle;
    /**
     * 检测按下到抬起时使用的时间
     */
    private long mDownTime;

    /**
     * 判断是否正在自动滚动
     */
    private boolean isFling;
    private int mMenuItemLayoutId = R.layout.circle_menu_item;

    public NewCircleViewLayout(Context context) {
        this(context,null);
    }

    public NewCircleViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //do not care padding
        setPadding(0,0,0,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureMyself(widthMeasureSpec,heightMeasureSpec);
        measureChildView();

    }

    private void measureChildView() {
        mRadius = Math.max(getMeasuredWidth(),getMeasuredHeight());

        final int count = getChildCount();
        //menu item size
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        int childMode = MeasureSpec.EXACTLY;

        for(int i = 0 ; i < count; i++) {
            View childView = getChildAt(i);
            if(childView.getVisibility() == GONE) {
                continue;
            }

            int makeMeasureSpec = -1;
            if(childView.getId() == R.id.id_circle_menu_item_center) {
                //中心圆的菜单
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION),
                        childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,childMode);
            }

            childView.measure(makeMeasureSpec,makeMeasureSpec);
        }

        mPadding = RADIO_PADDING_LAYOUT * mRadius;

    }

    private void measureMyself(int widthMeasureSpec,int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            resWidth = getSuggestedMinimumWidth();

            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

            resHeight = getSuggestedMinimumHeight();
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {
            //如果设置为的具体的值，则直接去最小的值即可
            resWidth = resHeight = Math.min(width,height);
        }

        //设置整体的宽高
        setMeasuredDimension(resWidth,resHeight);
    }




    private int getDefaultWidth(){
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return Math.min(displayMetrics.widthPixels,displayMetrics.heightPixels);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutRadius = mRadius;
        final int childCount = getChildCount();
        int left = 0 ,top = 0,right = 0,bottom = 0;

        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        int angleDelay = 360 /(getChildCount() - 1);

        for(int i = 0 ; i < childCount;i++) {
            View child = getChildAt(i);

            if(child.getId() == R.id.id_circle_menu_item_center) {
                continue;
            }
            if(child.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;
            //计算中心点到menu item中心的距离
            float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;

            left = layoutRadius/2 + (int)Math.round(tmp * Math.cos(Math.toRadians(mStartAngle)) - 1/2f * cWidth);
            top = layoutRadius/2 + (int)Math.round(tmp * Math.sin(Math.toRadians(mStartAngle))- 1/2f * cWidth);

            right = left + cWidth;
            bottom = top + cWidth;
            child.layout(left,top,right,bottom);

            mStartAngle += angleDelay;
        }

        View cView = findViewById(R.id.id_circle_menu_item_center);

        if(cView != null) {
            cView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemClick(v);
                    }
                }
            });
        }

        int cl = layoutRadius / 2 - cView.getMeasuredWidth() /2;
        int cr = cl + cView.getMeasuredWidth();

        cView.layout(cl,cl,cr,cr);
    }

    public interface OnMenuItemClickListener {
        void itemClick(View view,int position);
        void itemClick(View view);
    }

    public void setOnItemClickListener(OnMenuItemClickListener listener){
        this.mOnMenuItemClickListener= listener;
    }

    private OnMenuItemClickListener mOnMenuItemClickListener;


    private ListAdapter mAdapter;
    public void setAdapter(ListAdapter mAdapter){
        this.mAdapter = mAdapter;
    }


    public void buildMenuItems(){
        for(int i = 0 ; i < mAdapter.getCount();i++) {
            final View itemView = mAdapter.getView(i,null,this);
            final int position = i;
            itemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemClick(itemView, position);
                    }
                }
            });
            // 添加view到容器中
            addView(itemView);
        }
    }



    @Override
    protected void onAttachedToWindow() {
        if(mAdapter != null) {
            buildMenuItems();
        }
        super.onAttachedToWindow();
    }
}


