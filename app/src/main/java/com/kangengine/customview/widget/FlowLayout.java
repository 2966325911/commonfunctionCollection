package com.kangengine.customview.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Vic
 * time   : 2018/06/15
 * desc   :
 */
public class FlowLayout extends ViewGroup {

    // 每一行view的集合
    private List<List<View>> mAllViews   = new ArrayList<>();
    // 每一行的高度
    private List<Integer>    mLineHeight = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // 如果是wrap_content
        int width = 0;
        int height = 0;
        // 记录每一行的宽度与高度
        int lineWidth = 0;
        int lineHeight = 0;
        // view个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //测量每个子View的宽高
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            //获取子view的margin值大小
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            //获取childView的总宽高
            int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            // 换行 判断 当前的宽度大于 开辟新行
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                // 对比得到最大的宽度
                width = Math.max(width, lineWidth);
                // 重置lineWidth
                lineWidth = childWidth;
                // 记录行高
                height += lineHeight;
                // 重置lineHeight
                lineHeight = childHeight;
            } else {
                // 宽度累加
                lineWidth += childWidth;
                // 得到当前行最大的高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 最后一个的时候，不管是换行，还是未换行，前面都没有处理
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
            setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                    modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int childCount = getChildCount();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> viewList = new ArrayList<>();
        for(int i = 0 ; i < childCount;i++) {
            View childView = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if(lineWidth + childWidth > width - getPaddingLeft() - getPaddingRight()) {
                //换行
                mAllViews.add(viewList);
                mLineHeight.add(lineHeight);

                viewList = new ArrayList<>();
                lineHeight = childHeight;
                lineWidth = childWidth;

            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight,childHeight);
            }
            viewList.add(childView);
        }
        mAllViews.add(viewList);
        mLineHeight.add(lineHeight);

        int top = getPaddingTop();
        int left = getPaddingLeft();
        int lineNums = mAllViews.size();
        for(int i = 0 ; i < lineNums;i++) {
            viewList = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            for(View view : viewList) {
                if(view.getVisibility() == GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                int ll = left + lp.leftMargin;
                int lt = top + lp.topMargin;
                int lr = ll + view.getMeasuredWidth();
                int lb = lt + view.getMeasuredHeight();
                view.layout(ll,lt,lr,lb);

                left += view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            top += lineHeight;
            left = getPaddingLeft();
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
