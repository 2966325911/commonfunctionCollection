package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kangengine.customview.R;


/**
 * @author : Vic
 * time   : 2018/08/30
 * desc   : 类似于刮刮乐效果
 */
public class TearOffGirlsClotheView extends View implements View.OnTouchListener {

    private Bitmap sBitmap;
    private Bitmap dBitmap;

    private Paint mPaint;
    private Bitmap bitmap;
    private PorterDuffXfermode xfermode;

    private Path mPath = new Path();
    private float startX;
    private float startY;

    private Canvas bitCanvas;

    public TearOffGirlsClotheView(Context context) {
        this(context,null);
    }

    public TearOffGirlsClotheView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(45);

        sBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.a914);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.b914);
        dBitmap = Bitmap.createBitmap(sBitmap.getWidth(),sBitmap.getHeight(),Bitmap.Config.ARGB_8888);
        bitCanvas = new Canvas(dBitmap);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);

        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sBitmap.getWidth(),sBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,0,0,mPaint);
        int layerId = canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);

        bitCanvas.drawPath(mPath,mPaint);
        canvas.drawBitmap(dBitmap,0,0,mPaint);
        mPaint.setXfermode(xfermode);
        canvas.drawBitmap(sBitmap,0,0,mPaint);
        mPaint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                mPath.moveTo(startX,startY);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();

                mPath.quadTo(startX,startY,endX,endY);
                startX = event.getX();
                startY = event.getY();
                break;
                default:
                    break;
        }
        postInvalidate();
        return true;
    }
}
