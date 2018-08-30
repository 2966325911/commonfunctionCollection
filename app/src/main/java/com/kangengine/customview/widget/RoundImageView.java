package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.cloudoc.share.yybpg.customview.R;

/**
 * @author : Vic
 * time   : 2018/08/29
 * desc   :
 */
public class RoundImageView extends View {
    private Bitmap mBitmap;
    private Bitmap mOut;
    private Paint mPaint;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundImageView(Context context) {
        this(context,null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bf);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(getmBitmap(),0,0,mPaint);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap getmBitmap(){

        mOut = Bitmap.createBitmap(mBitmap.getWidth(),mBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Canvas canvas = new Canvas(mOut);
        canvas.drawRoundRect(0,0,mBitmap.getWidth(),mBitmap.getHeight(),
                80,80,mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(mBitmap,0,0,mPaint);
        return mOut;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(mBitmap.getWidth(),mBitmap.getHeight());
    }

}
