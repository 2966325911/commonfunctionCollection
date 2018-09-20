package com.kangengine.customview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @author : Vic
 * time   : 2018/09/03
 * desc   :
 */
public class AnimationButtonOk extends View {
    private static final String TAG = AnimationButtonOk.class.getSimpleName();
    private OnAnimationListener listener;

    /**
     * 矩形转化为圆角矩形
     */
    private ValueAnimator rectToRadiusAnimator;
    /**
     * 圆角矩形转化为圆
     */
    private ValueAnimator rectToCircleAnimator;

    private ObjectAnimator translateAnimator;

    private ValueAnimator drawOkAnimator;

    private PathEffect pathEffect;
    private PathMeasure mPathMeasure;

    private Path mPath;

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int mAngle;

    private int defaultTwoCircleDistance;
    private RectF mRectF;
    private int two_circle_distance;

    private int mCurrentX;
    private int mCurrentY;
    AnimatorSet animatorSet = new AnimatorSet();
    private boolean isStartDrawOk = false;

    private Paint mOkPaint;
    private static final int DURATION = 2000;

    public AnimationButtonOk(Context context) {
        this(context, null);
    }

    public AnimationButtonOk(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);

        mOkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOkPaint.setColor(Color.WHITE);

        mRectF = new RectF();

        mPath = new Path();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onViewClick();
                }
            }
        });

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(listener != null) {
                    listener.onAnimationFinished();
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        defaultTwoCircleDistance = (w - h) / 2;
        Log.d(TAG,"w==" + w + "==h==" + h + "== defaultTwoCircleDistance==" + defaultTwoCircleDistance);
        initOk();

    }

    private void initOk() {
        //对勾的路径
        mPath.moveTo(defaultTwoCircleDistance + mHeight / 8 * 3, mHeight / 2);
        mPath.lineTo(defaultTwoCircleDistance + mHeight / 2, mHeight / 5 * 3);
        mPath.lineTo(defaultTwoCircleDistance + mHeight / 3 * 2, mHeight / 5 * 2);

        mPathMeasure = new PathMeasure(mPath, true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAnimationRect(canvas);
        if(isStartDrawOk){
            canvas.drawPath(mPath,mOkPaint);
        }
    }

    private void drawAnimationRect(Canvas canvas) {
        mRectF.left = two_circle_distance;
        mRectF.top = 0;
        mRectF.right = mWidth - two_circle_distance;
        mRectF.bottom = mHeight;
        canvas.drawRoundRect(mRectF, mAngle, mAngle, mPaint);
    }

    /**
     * 开启动画
     */
    public void start() {
        startRectToRadius();
        startRadiusRectToCircle();
        startTranslate();
        startDrawOkAnimator();

        //play 播放 after 在...之后  before 在...之前  在rectToRaiusAnimator之后，再translateAnimator之前播放
        //rectToCircleAnimator动画
//        play（a1）.with（a2）    a1和a2同时开始
//        play（a1）.before（a2）   先a1后a2
//        play（a1）.after（a2）  先a2后a1
//        animatorSet.play(rectToCircleAnimator).before(translateAnimator).after(rectToRadiusAnimator);

        animatorSet
                .play(translateAnimator)
                .before(drawOkAnimator)
                .after(rectToCircleAnimator)
                .after(rectToRadiusAnimator);
        animatorSet.start();

    }

    private void startDrawOkAnimator(){
        drawOkAnimator = ValueAnimator.ofFloat(1,0);
        drawOkAnimator.setDuration(DURATION);
        drawOkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                isStartDrawOk = true;
                float animationValue = (float) animation.getAnimatedValue();

                pathEffect = new DashPathEffect(new float[]{mPathMeasure.getLength(),mPathMeasure.getLength()
                },animationValue * mPathMeasure.getLength());
                mOkPaint.setPathEffect(pathEffect);
                invalidate();
            }
        });

    }

    private void startTranslate(){

        mCurrentY = (int) getTranslationY();
        translateAnimator = ObjectAnimator.ofFloat(this,"translationY",mCurrentY,mCurrentY - 300);
        translateAnimator.setDuration(DURATION);
        translateAnimator.setInterpolator(new AccelerateInterpolator());

    }

    private void startRectToRadius() {
        rectToRadiusAnimator = ValueAnimator.ofInt(0, mHeight / 2);
        rectToRadiusAnimator.setDuration(DURATION);
        rectToRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAngle = (int) animation.getAnimatedValue();
                Log.d(TAG,"==mAngle==" + mAngle);
                invalidate();
            }
        });
    }

    private void startRadiusRectToCircle(){
        rectToCircleAnimator = ValueAnimator.ofInt(0,defaultTwoCircleDistance);
        rectToCircleAnimator.setDuration(DURATION);
        rectToCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                two_circle_distance = (int) animation.getAnimatedValue();
                Log.d(TAG,"==two_circle_distance==" + two_circle_distance);
                invalidate();
            }
        });


    }


    public void setOnAnimationListener(OnAnimationListener listener) {
        this.listener = listener;
    }

    public interface OnAnimationListener {
        void onViewClick();

        void onAnimationFinished();
    }
}
