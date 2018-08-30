package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author : Vic
 * time   : 2018/08/30
 * desc   :
 */
public class CustomHeadView extends View {

    private Bitmap dBitmap;
    private Bitmap sBitmap;
    private Paint mPaint;

    private PorterDuffXfermode xfermode;

    public CustomHeadView(Context context) {
        this(context,null);
    }

    public CustomHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sBitmap.getWidth(),sBitmap.getHeight());
    }

    public void drawHeader(int src,int dst) {
        sBitmap = BitmapFactory.decodeResource(getResources(),src,null);
        dBitmap = BitmapFactory.decodeResource(getResources(),dst,null);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int layerId = canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(sBitmap,0,0,mPaint);
        mPaint.setXfermode(xfermode);
        canvas.drawBitmap(dBitmap,0,0,mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }
}
