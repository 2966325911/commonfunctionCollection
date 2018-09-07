package com.kangengine.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.kangengine.customview.R;

/**
 * @author : Vic
 * time   : 2018/09/07
 * desc   :
 */
public class CircleCountDownView extends View{
    private static final int MSG_UPDATE = 10;
    /**
     * 默认更新时间 隔多长时间更新一次
     */
    private static final int DEFAULT_TIME = 1000;
    /**
     * 倒计时 总时长 60s
     */
    private static final int DEFAULT_TOTAL_TIME =  60 * 1000;
    private static final int DEFAULT_STOKE_WIDTH = 8;
    /**
     * 绘制总次数
     */
    private static final int TOTAL_COUNT = 60;
    private static final int MAX_PROGRESS = 100;
    /**
     * 文字默认大小
     */
    private static final float DEFAULT_TEXT_SIZE = 20f;
    /**
     * 外圈背景色
     */
    private int mProgressHintColor;
    /**
     * circle颜色
     */
    private int mCircleColor;
    /**
     * circle 半径
     */
    private int mCircleRadius;
    /**
     * 文字颜色 默认白色
     */
    private int mTextColor;
    /**
     * 转圈的 progress 颜色
     */
    private int mProgressColor;
    /**
     * 转圈的 progress width
     */
    private float mProgressWidth;
    private int mProgress = 0;
    private CharSequence mText;

    private float mTextSize;
    private int mTotalTime;
    private int mUpdateTime;

    /**
     *  总的绘制次数
     */
    private int mDrawTimes;
    /**
     * 已经绘制的次数
     */

    private int mCurrentDrawTimes;
    /**
     * 每次绘制角度
     */

    private int mEachDrawAngle;

    private Paint mPaint;
    private TextPaint mTextPaint;
    private RectF mArcRectF;

    private Action mEndAction;

    private Handler mHandler;
    private boolean isStop = false;

    public CircleCountDownView(Context context) {
        this(context, null);
    }

    public CircleCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountdownView, 0, 0);

        try {

            mText = a.getText(R.styleable.CountdownView_text);
            mTextColor = a.getColor(R.styleable.CountdownView_text_color, Color.WHITE);
            mTextSize = a.getDimension(R.styleable.CountdownView_text_size, DEFAULT_TEXT_SIZE);
            mUpdateTime = a.getInteger(R.styleable.CountdownView_update_time, DEFAULT_TIME);
            mTotalTime = a.getInteger(R.styleable.CountdownView_total_time, DEFAULT_TOTAL_TIME);
            mProgressColor = a.getColor(R.styleable.CountdownView_progress_color, Color.RED);
            mProgressWidth = a.getDimension(R.styleable.CountdownView_progress_width, DEFAULT_STOKE_WIDTH);
            mProgressHintColor = a.getColor(R.styleable.CountdownView_progress_hint_color, Color.GRAY);
            mCircleColor = a.getColor(R.styleable.CountdownView_bg_color, Color.WHITE);
        } finally {
            a.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mArcRectF = new RectF();

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_UPDATE:
                        if(!isStop) {
                            int changePer = MAX_PROGRESS / mDrawTimes;
                            postInvalidate();
                            mCurrentDrawTimes++;

                            mText = String.valueOf(TOTAL_COUNT-mCurrentDrawTimes);

                            mProgress += changePer;
                            mTotalTime -= mUpdateTime;
                            if (mProgress == MAX_PROGRESS) {
                                mEndAction.onFinished();
                            } else {
                                if(mCurrentDrawTimes < TOTAL_COUNT) {
                                    sendEmptyMessageDelayed(MSG_UPDATE, mUpdateTime);
                                }
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mCircleRadius = width/2;
        setMeasuredDimension(mCircleRadius * 2, mCircleRadius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        //画大圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);
        canvas.drawCircle(centerX, centerY, mCircleRadius - 3*mProgressWidth, mPaint);

        //画外边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setColor(mProgressHintColor);
        canvas.drawCircle(centerX, centerY, mCircleRadius-mProgressWidth , mPaint);

        //画字
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        float textY = centerY - (mTextPaint.descent() + mTextPaint.ascent()) / 2;
        canvas.drawText(mText.toString(), centerX, textY, mTextPaint);

        //画进度条
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcRectF.set(mProgressWidth, getHeight()/2 - getWidth()/2 + mProgressWidth, getWidth()-mProgressWidth,
                getHeight()/2 + getWidth()/2 - mProgressWidth);

        canvas.drawArc(mArcRectF, -90,
                mCurrentDrawTimes   * mEachDrawAngle, false, mPaint);
    }

    public void setText(String text) {
        mText = text;
    }

    public void setTotalTime(int time) {
        mTotalTime = time;
    }

    public void setUpdateTime(int updateTime) {
        mUpdateTime = updateTime;
    }

    public int getTotalTime() {
        return mTotalTime;
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
    }

    public void setCircleBackgroundColor(int color) {
        mCircleColor = color;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public void startCountdownTime() {
        mDrawTimes = mTotalTime / mUpdateTime;
        mEachDrawAngle = 360 / mDrawTimes;
        mHandler.sendEmptyMessage(MSG_UPDATE);
    }

    /**
     * 倒计时正常结束
     * @param action
     */
    public void setOnFinishAction(Action action) {
        mEndAction = action;
    }
    public interface Action {
        void onFinished();
    }

    /**
     * 结束倒计时
     */
    public void endCountdownTime(){
        isStop = true;
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


}
