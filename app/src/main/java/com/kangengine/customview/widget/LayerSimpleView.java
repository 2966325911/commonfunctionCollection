package com.kangengine.customview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author : Vic
 * time   : 2018/08/31
 * desc   :
 */
public class LayerSimpleView extends View {
    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
              | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                 | Canvas.CLIP_TO_LAYER_SAVE_FLAG;

    private Paint mPaint;

    public LayerSimpleView(Context context) {
        this(context,null);
    }

    public LayerSimpleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongConstant")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.translate(10,10);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(75,57,75,mPaint);

        //重新弄个图层，这样就让蓝色圆在红色的圆上面了，因为两个圆不在一个图层，如果两个圆是在一个图层就会出现遮盖的效果
        canvas.saveLayerAlpha(0,0,200,200,0x88,LAYER_FLAGS);
//        canvas.saveLayer(0,0,200,200,mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(125,125,75,mPaint);
        canvas.restore();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),300);
    }
}
