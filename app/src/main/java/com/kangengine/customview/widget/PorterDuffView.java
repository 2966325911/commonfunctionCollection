package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author : Vic
 * time   : 2018/08/31
 * desc   : PorterDuffXfermode 测试
 * clear  Src Dst SrcOver DstOver SrcIn DstIn SrcOut DstOut SrcATop
 * DstAtop Xor Darken Lighten Multiply Screen
 * 详细参考 https://www.jianshu.com/p/3feaa8b347f2
 */
public class PorterDuffView extends View {

    private Paint mPaint;

    public PorterDuffView(Context context) {
        this(context,null);
    }

    public PorterDuffView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint();
}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap dst = getDstCircleBitmap();
        Bitmap src = getSrcRetangleBitmap();

        //开启硬件离屏缓存（顺便说一下它的好处）
        //- 1.解决xfermode黑色问题。
        //- 2.效率比关闭硬件加速高3倍以上
        setLayerType(LAYER_TYPE_HARDWARE,null);
        canvas.drawBitmap(src,100,100,mPaint);
//        //白底
//        canvas.drawColor(Color.BLACK);
//        int canvasWidth= canvas.getWidth();
//        int r= canvasWidth/ 10;
//        //正常绘制黄色的圆形
//        mPaint.setColor(0xFFFFCC44);
//        //Dst
//        canvas.drawCircle(r,r,r,mPaint);

        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST));
        //SRC_OVER src浮在dst的上面
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        //DST_OVER dst浮在src的上面
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));



//        mPaint.setColor(0xFF66AAFF);
//        //Src
//        canvas.drawRect(r,r,r* 2.7f,r* 2.7f,mPaint);

        canvas.drawBitmap(dst,0,0,mPaint);
//        //最后将画笔去除Xfermode
//        mPaint.setXfermode(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredWidth()/3);
    }


    private Bitmap getSrcRetangleBitmap() {
        /**
         * bm1 在bitmap上面画正方形
         */
        Bitmap rectangle = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas c1 = new Canvas(rectangle);
        Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(0xFF66AAFF);
        /**
         * 设置透明
         */
        c1.drawARGB(0, 0, 0, 0);
        c1.drawRect(0, 0, 200, 200, p1);
        return rectangle;
    }


    private Bitmap getDstCircleBitmap() {
        /**
         * bm 在bitmap上面画圆
         */
        Bitmap circle = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(circle);
        /**
         * 设置透明
         */
        c.drawARGB(0, 0, 0, 0);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCC44);
        c.drawCircle(100, 100, 100, p);
        return circle;
    }
}
