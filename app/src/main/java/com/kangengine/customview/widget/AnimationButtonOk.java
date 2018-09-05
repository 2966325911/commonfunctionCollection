package com.kangengine.customview.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

/**
 * @author : Vic
 * time   : 2018/09/03
 * desc   :
 */
public class AnimationButtonOk extends View {

    private Paint mRectPaint;
    private RectF mRectF;
    private int mWidth;
    private int mHeight;

    private int mAngle;
    private int twoCircleDistance;
    private int defaultCircleDistance;

    private ValueAnimator rectAnimator;
    private ValueAnimator rectToCircleAnimator;

    private AnimatorSet animatorSet;

    public AnimationButtonOk(Context context) {
        this(context,null);
    }

    public AnimationButtonOk(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(Color.RED);

        mRectF = new RectF();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAnimationListener != null){
                    onAnimationListener.onViewClick();
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        defaultCircleDistance = (w-h)/2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawRoundRect(canvas);
    }

    private void mDrawRoundRect(Canvas canvas) {
        mRectF.left = twoCircleDistance;
        mRectF.top = 0;
        mRectF.right = mWidth - twoCircleDistance;
        mRectF.bottom = mHeight;
        canvas.drawRoundRect(mRectF,mAngle,mAngle,mRectPaint);
    }

    private void showRectAnimation(){
        rectAnimator = ValueAnimator.ofInt(0,mHeight/2);
        rectAnimator.setDuration(2000);
        rectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    private void rectToCircleAnimation(){
        rectToCircleAnimator = ValueAnimator.ofInt(0,defaultCircleDistance);
        rectToCircleAnimator.setDuration(2000);
        rectToCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                twoCircleDistance = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    public void start(){
        showRectAnimation();
        rectToCircleAnimation();
        animatorSet = new AnimatorSet();
        animatorSet.play(rectAnimator).before(rectToCircleAnimator);
        animatorSet.start();
    }

    public interface OnAnimationListener{
        void onViewClick();
        void onAnimationFinished();
    }

    public OnAnimationListener onAnimationListener;

    public void setOnAnimationListener(OnAnimationListener onAnimationListener){
        this.onAnimationListener = onAnimationListener;
    }

}
