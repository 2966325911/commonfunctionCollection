package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Modifier;

/**
 * @author : Vic
 * time   : 2018/07/03
 * desc   :
 */
public class SwitchView2 extends View {
    private int mRadius = 50;

    private int mCircleColor = Color.RED;
    private int mRectColor = Color.GRAY;
    private int mRectColor1 = Color.GREEN;

    private Paint mCirclePaint;
    private Paint mRectPaint;
    private Paint mRectPaint1;

    private Paint mRightCirclePaint;
    private Paint mLeftCirclePaint;
    private int mLeftX = 50;


    public SwitchView2(Context context) {
        this(context, null);
    }

    public SwitchView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(mRectColor);
        mRectPaint.setStyle(Paint.Style.FILL);

        mRectPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint1.setColor(mRectColor1);
        mRectPaint1.setStyle(Paint.Style.FILL);

        mLeftCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLeftCirclePaint.setColor(mRectColor);
        mLeftCirclePaint.setStyle(Paint.Style.FILL);

        mRightCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightCirclePaint.setColor(mRectColor);
        mRightCirclePaint.setStyle(Paint.Style.FILL);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                if(x > getWidth()/2) {
                    mLeftX = x - getWidth()/2;
                    if(mLeftX > 50 || mLeftX > 25) {
                        mLeftX = -50;
                    } else {
                        mLeftX = 50;
                    }

                } else {
                    mLeftX = getWidth()/2 - x;
                    if(mLeftX > 50 || mLeftX > 25) {
                        mLeftX = 50;
                    } else {
                        mLeftX = -50;
                    }
                }


                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        postInvalidate();

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mLeftX == 50) {
            canvas.drawRect((float) (getWidth() / 2 - 50), (float) (getHeight() / 2 - 50), (float) (getWidth() / 2 + 50), (float) (getHeight() / 2 + 50), mRectPaint);
            mLeftCirclePaint.setColor(mRectColor);
            mRightCirclePaint.setColor(mRectColor);
            canvas.drawCircle(getWidth() / 2 - 50, getHeight() / 2, 50, mLeftCirclePaint);
            canvas.drawCircle(getWidth() / 2 + 50, getHeight() / 2, 50, mRightCirclePaint);
        } else {
            canvas.drawRect((float) (getWidth() / 2 - 50), (float) (getHeight() / 2 - 50), (float) (getWidth() / 2 + 50), (float) (getHeight() / 2 + 50), mRectPaint1);
            mLeftCirclePaint.setColor(mRectColor1);
            mRightCirclePaint.setColor(mRectColor1);
            canvas.drawCircle(getWidth() / 2 - 50, getHeight() / 2, 50, mLeftCirclePaint);
            canvas.drawCircle(getWidth() / 2 + 50, getHeight() / 2, 50, mRightCirclePaint);
        }




        canvas.drawCircle(getWidth() / 2 - mLeftX, getHeight() / 2, mRadius, mCirclePaint);

    }
}
