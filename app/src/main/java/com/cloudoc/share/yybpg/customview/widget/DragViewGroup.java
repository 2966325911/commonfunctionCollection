package com.cloudoc.share.yybpg.customview.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author : Vic
 * time   : 2018/08/21
 * desc   :
 */
public class DragViewGroup extends FrameLayout {

    private static final String TAG = "SidePullLayout";

    private ViewDragHelper mViewDragHelper;
    private View mMenuView;
    private View mMainView;
    private int mLeft; // 主View的左边框距离，随拖拽而改变
    private int mWidth; // 菜单View的宽度

    private DrawerListener mDrawerListener;
    private int mMinLeft; // 当菜单打开时，主View最小的左边距离

    public void setDrawerListener(DrawerListener mDrawerListener) {
        this.mDrawerListener = mDrawerListener;
    }

    public DragViewGroup(@NonNull Context context) {
        this(context,null);
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this,mCallback);
    }



    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if(mDrawerListener != null){
                mDrawerListener.onDrawerStateChanged(state);
            }
        }

        /**
         * 判断什么时候开始检测触摸事件
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // 当触摸的View是MainView时开始检测
            return mMainView == child;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if(mDrawerListener !=null) {
                float alpha;
                if(left>mMinLeft){
                    alpha = 0.6f;
                }else{
                    alpha = (mMinLeft-left)*1.0f/mMinLeft;
                    if(alpha < 0.6f){
                        alpha = 0.6f;
                    }
                }
                mDrawerListener.onDrawerSlide(changedView,alpha);
            }
        }

        /**
         * 拖拽结束后回调
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            // 当手指抬起时，我们让菜单慢慢滑动到合适位置（平滑）
            if(mMainView.getLeft() < mMinLeft){
                // 关闭菜单
                mViewDragHelper.smoothSlideViewTo(mMainView,0,0);
                ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
                if(mDrawerListener != null){
                    mDrawerListener.onDrawerClosed(releasedChild);
                }
            }else{
                // 打开菜单
                mViewDragHelper.smoothSlideViewTo(mMainView, mMinLeft, 0);
                ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
                if(mDrawerListener != null){
                    mDrawerListener.onDrawerOpened(releasedChild);
                }
            }
        }

        /**
         * 水平滑动回调方法
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if(left <0){
                mLeft = 0;
                return 0;
            }
            mLeft = left;
            return left;
        }

        /**
         * 垂直滑动回调方法
         * @param child
         * @param top
         * @param dy
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
//            if(top <0 && mLeft <0 || top > mLeft){
//                return 0;
//            }else {
//                return mLeft / 2;
//            }
            return 0;
        }
    };

    /**
     * 给mViewDragHelper判断是否拦截事件
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将触摸事件交给mViewDragHelper处理
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * View尺寸发送改变时回调
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMenuView.getMeasuredWidth();
        mMinLeft = mWidth/2;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public interface DrawerListener{
        void onDrawerSlide(View drawerView, float alpha);
        void onDrawerOpened(View drawerView);
        void onDrawerClosed(View drawerView);
        void onDrawerStateChanged(int newState);
    }
}
