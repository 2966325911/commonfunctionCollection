package com.cloudoc.share.yybpg.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * @author : Vic
 * time   : 2018/08/29
 * desc   :
 */
public class LockPatternView extends View {
    private static final String TAG = LockPatternView.class.getSimpleName();
    public OnPasswordChanged onPasswordChanged;
    /**
     * 画圆的画笔
     */
    private Paint mCirclePaint;
    /**
     * 圆的半径
     */
    private int mCircleRadius;
    /**
     * 画圆的颜色
     */
    private int mCircleColor = Color.GRAY;
    private int mCircleSelectedColor = Color.GREEN;
    /**
     * 画直线的画笔
     */
    private Paint mLinePaint;
    /**
     * 画直线的颜色
     */
    private int mLineColor = Color.GREEN;
    private int mCurrentX;
    private int mCurrentY;
    private boolean mIsSelected;
    private boolean mIsFinished;
    private List<PointView> mSelectedViewList = new ArrayList<>();
    private int index;
    private PointView[][] mCircleArray = new PointView[3][3];


    public LockPatternView(Context context) {
        this(context, null);
    }

    public LockPatternView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(20);
        mLinePaint.setColor(mLineColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //画圆
        drawCircle(canvas);

        for (int i = 0; i < mSelectedViewList.size(); i++) {
            mCirclePaint.setColor(mCircleSelectedColor);
            PointView pointView = mSelectedViewList.get(i);
            canvas.drawCircle(pointView.x, pointView.y, mCircleRadius, mCirclePaint);
            mCirclePaint.setColor(mCircleColor);
        }


        if (mSelectedViewList != null && mSelectedViewList.size() > 0) {
            PointView pointViewA = mSelectedViewList.get(0);
            for (int i = 0; i < mSelectedViewList.size(); i++) {
                PointView pointViewB = mSelectedViewList.get(i);
                drawLine(pointViewA.x, pointViewA.y, pointViewB.x, pointViewB.y, canvas);
                pointViewA = pointViewB;
            }

            if (!mIsFinished) {
                int statX = pointViewA.x;
                int statY = pointViewA.y;
                drawLine(statX, statY, mCurrentX, mCurrentY, canvas);
            }
        }


        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();

        mCircleRadius = width / 14;
    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {

        for (int i = 0; i < mCircleArray.length; i++) {
            int y = (i * 5 + 5) * mCircleRadius;
            for (int j = 0; j < mCircleArray.length; j++) {
                int x = (4 * j + 3) * mCircleRadius;
                canvas.drawCircle(x, y, mCircleRadius, mCirclePaint);
                PointView pointView = new PointView(x, y);
                pointView.setIndex(index);
                mCircleArray[i][j] = pointView;
                index++;
            }
        }
        index = 1;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurrentX = (int) event.getX();
        mCurrentY = (int) event.getY();
        PointView pointView = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSelectedViewList.clear();
                mIsFinished = false;
                pointView = getSelectedPointView();
                if(pointView != null) {
                    //如果PointView不为空，则说明此时被选中
                    mIsSelected = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mIsSelected) {
                    pointView = getSelectedPointView();
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsSelected = false;
                mIsFinished = true;
                break;
            default:
                break;
        }
        if(!mIsFinished && mIsSelected && pointView != null) {
            if(mSelectedViewList != null && !mSelectedViewList.contains(pointView)) {
                mSelectedViewList.add(pointView);
            }
        }

        if(mIsFinished) {
            if(mSelectedViewList != null) {
                if(mSelectedViewList.size() <5 && mSelectedViewList.size() >=0) {
                    mSelectedViewList.clear();
                    setOnPasswordFailed("图案密码不能少于5个");
                } else {
                    for(int i = 0 ; i < mSelectedViewList.size();i++) {
                        Log.d(TAG,"===index==" + mSelectedViewList.get(i).getIndex() );
                    }
                    StringBuilder sbPwd = new StringBuilder();
                    for(PointView pv : mSelectedViewList) {
                        sbPwd.append(pv.getIndex());
                    }
                    setOnPasswordSuccess(sbPwd.toString());
                }
            }
        }


        invalidate();
        return true;
    }

    private void drawLine(int startX, int startY, int stopX, int stopY, Canvas canvas) {
        canvas.drawLine(startX, startY, stopX, stopY, mLinePaint);

    }

    private PointView getSelectedPointView() {
        for (int i = 0; i < mCircleArray.length; i++) {
            for (int j = 0; j < mCircleArray.length; j++) {
                PointView pointView = mCircleArray[i][j];
                if (isWidthCircle(mCurrentX, mCurrentY, pointView.x, pointView.y)) {
                    return pointView;
                }
            }
        }
        return null;
    }

    /**
     * 触摸点是否在画的圆点内
     *
     * @param x
     * @param y
     * @param cx
     * @param cy
     * @return
     */
    private boolean isWidthCircle(int x, int y, int cx, int cy) {
        if (Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2)) <= mCircleRadius) {
            return true;
        }
        return false;
    }

    public void setOnPsswordChanged(OnPasswordChanged onPsswordChanged) {
        this.onPasswordChanged = onPsswordChanged;
    }

    public interface OnPasswordChanged {
        void onPasswordFailed(String errMsg);

        void onPasswordSuccess(String password);
    }

    /**
     * 密码错误时回调
     * @param errMsg
     */
    public void setOnPasswordFailed(String errMsg){
        if(onPasswordChanged != null) {
            onPasswordChanged.onPasswordFailed(errMsg);
        }
    }

    /**
     * 密码错误时候调用
     * @param password
     */
    public void setOnPasswordSuccess(String password) {
        if(onPasswordChanged != null) {
            onPasswordChanged.onPasswordSuccess(password);
        }
    }
}
