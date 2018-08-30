package com.cloudoc.share.yybpg.customview.widget;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.cloudoc.share.yybpg.customview.R;
import com.cloudoc.share.yybpg.customview.adapter.BaseAdapter;
import com.cloudoc.share.yybpg.customview.adapter.OnItemClickListener;

import java.util.List;

/**
 * @author : Vic
 * time   : 2018/07/30
 * desc   :
 */
public class CircleMenuLayout extends ViewGroup {

    /**
     * 该容器内child item的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
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
    private int mRadius;
    /**
     * 菜单的中心child的默认尺寸
     */
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;
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
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private float mLastX;
    private float mLastY;
    private AutoFlingRunnable mFlingRunnable;

    public CircleMenuLayout(Context context) {
        this(context, null);
    }


    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //do not care padding
        setPadding(0, 0, 0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureMyself(widthMeasureSpec, heightMeasureSpec);
        measureChildView();

    }

    private void measureMyself(int widthMeasureSpec, int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            resWidth = getSuggestedMinimumWidth();

            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

            resHeight = getSuggestedMinimumHeight();
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {
            //如果设置为的具体的值，则直接去最小的值即可
            resWidth = resHeight = Math.min(width, height);
        }

        //设置整体的宽高
        setMeasuredDimension(resWidth, resHeight);
    }

    private void measureChildView() {
        mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());

        final int count = getChildCount();
        //menu item size
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        int childMode = MeasureSpec.EXACTLY;

        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

            int makeMeasureSpec = -1;
            if (childView.getId() == R.id.id_circle_menu_item_center) {
                //中心圆的菜单
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION),
                        childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
            }

            childView.measure(makeMeasureSpec, makeMeasureSpec);
        }

        mPadding = RADIO_PADDING_LAYOUT * mRadius;

    }

    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public void setOnItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    public void setMenuItemIconsAndTexts(int[] resIds, String[] texts) {
        mItemImgs = resIds;
        mItemTexts = texts;

        if (resIds == null && texts == null) {
            throw new IllegalArgumentException("menu or text need to set");
        }

        mMenuItemCount = resIds == null ? texts.length : resIds.length;

        if (resIds != null && texts != null) {
            mMenuItemCount = Math.min(resIds.length, texts.length);
        }

        addMenuItems();
    }

    private void addMenuItems() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;
            View view = inflater.inflate(mMenuItemLayoutId, null);
            ImageView iv = (ImageView) view
                    .findViewById(R.id.id_circle_menu_item_image);
            TextView tv = (TextView) view
                    .findViewById(R.id.id_circle_menu_item_text);
            TextView msgTv = view.findViewById(R.id.id_circle_menu_item_msg_num_text);

            if (iv != null) {
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(mItemImgs[i]);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemClick(v, j);
                        }
                    }
                });
            }
            if (tv != null) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(mItemTexts[i]);
            }
            if (msgTv != null) {
                msgTv.setVisibility(VISIBLE);
                msgTv.setText("111");
            }
            addView(view);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngle = 0;
                if(isFling) {
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                float start = getAngle(mLastX,mLastY);
                float end = getAngle(x,y);

                //1 4 象限 正值 2,3象限 负值
                if(getQuadrant(x,y) == 1 || getQuadrant(x,y) == 4) {
                    mStartAngle += end - start;
                    mTmpAngle += end -start;
                } else {
                    mStartAngle += start - end;
                    mTmpAngle += start - end;
                }

                requestLayout();
                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:
                float anglePerSecond = mTmpAngle * 100 /(System.currentTimeMillis() - mDownTime);
                if(Math.abs(anglePerSecond) > mFlingableValue && !isFling) {
                    post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));
                    return true;
                }

                if(Math.abs(mTmpAngle) > NOCLICK_VALUE) {
                    return true;
                }
                break;
            default:
                break;

        }

        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private float getAngle(float xTouch,float yTouch) {
        double x = xTouch - (mRadius/2d);
        double y = yTouch - (mRadius/2d);
        return (float) (Math.asin(y / Math.hypot(x,y))* 180 / Math.PI);
    }

    private int getQuadrant(float xTouch,float yTouch) {
        int tmpX = (int) (xTouch - (mRadius/2));
        int tmpY = (int) (yTouch - (mRadius/2));
        if(tmpX >= 0) {
            return tmpY >=0 ? 4 : 1;
        } else {
            return tmpY >=0 ? 3 : 2;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutRadius = mRadius;
        final int childCount = getChildCount();
        int left = 0, top = 0, right = 0, bottom = 0;

        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        int angleDelay = 360 / (getChildCount() - 1);

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getId() == R.id.id_circle_menu_item_center) {
                continue;
            }
            if (child.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;
            //计算中心点到menu item中心的距离
            float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;

            left = layoutRadius / 2 + (int) Math.round(tmp * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f * cWidth);
            top = layoutRadius / 2 + (int) Math.round(tmp * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f * cWidth);

            right = left + cWidth;
            bottom = top + cWidth;
            child.layout(left, top, right, bottom);

            mStartAngle += angleDelay;
        }

        View cView = findViewById(R.id.id_circle_menu_item_center);

        if (cView != null) {
            cView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemClick(v);
                    }
                }
            });
        }

        int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
        int cr = cl + cView.getMeasuredWidth();

        cView.layout(cl, cl, cr, cr);
    }

    public interface OnMenuItemClickListener {
        void itemClick(View view, int position);

        void itemClick(View view);
    }

    private class AutoFlingRunnable implements Runnable {

        private float angelPerSecond;

        public AutoFlingRunnable(float velocity) {
            this.angelPerSecond = velocity;
        }

        @Override
        public void run() {
            if (Math.abs(angelPerSecond) < 20) {
                isFling = false;
                return;
            }

            isFling = true;
            mStartAngle += angelPerSecond / 30;
            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);
            requestLayout();
        }
    }
}


