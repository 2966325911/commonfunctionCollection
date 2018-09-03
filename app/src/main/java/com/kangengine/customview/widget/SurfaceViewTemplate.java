package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author : Vic
 * time   : 2018/09/02
 * desc   : SurfaceView 实例 套路模板
 * 1 : 实例一个SurfaceHolder 通过getHolder()获得
 * 2：SurfaceHolder addCallback(this) 实现 SurfaceHolder.Callback 中的
 * surfaceCreated sufaceChanged surfaceDestoryed
 */
public class SurfaceViewTemplate extends SurfaceView implements
        SurfaceHolder.Callback, Runnable {

    /**
     * SurfaceHolder
     */
    private SurfaceHolder mHolder;
    /**
     * 用于绘图的Canvas
     */
    private Canvas mCanvas;
    /**
     * 子线程标志位
     */
    private boolean mIsDrawing;

    private int x;
    private int y;
    private Path mPath;
    private Paint mPaint;

    public SurfaceViewTemplate(Context context) {
        this(context, null);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (mIsDrawing) {

            //TODO somethings
            draw();
//            drawSinLine();

        }

        long end = System.currentTimeMillis();
        if (end - start < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw() {
        try {
            //获取当期那Canvas的绘图对象
            mCanvas = mHolder.lockCanvas();
            //draw somethings
            //surfaceView 背景
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                //如果要清屏则需要drawColor 调用unlockCanvasAndPost将画布内容进行提交
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

    /**
     * 绘制一个正弦曲线
     */
    private void drawSinLine() {
        x += 1;
        y = (int) (100 * Math.sin(x * 2 * Math.PI / 180) + 400);
        mPath.lineTo(x, y);
        if (x == getMeasuredWidth() - 50) {
            mIsDrawing = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int  y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 清空画布
     */
    public void clearCanvas(){
        //清除Path中的内容
        //reset不保留内部数据结构，但会保留FillType.
        //rewind会保留内部的数据结构，但不保留FillType
        //由于这里只是用path绘制，清楚path即可
        mPath.rewind();
    }

}
