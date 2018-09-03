package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * @author : Vic
 * time   : 2018/09/02
 * desc   : PathEffect是指 用各种笔触效果来绘制一个路径
 */
public class PathEffectView extends View {
    private Paint mPaint;
    private PathEffect[] mEffect;
    private Path mPath;
    private String[] mDesc;
    private Paint mTextPaint;

    public PathEffectView(Context context) {
        this(context,null);
    }

    public PathEffectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(50);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.RED);

        mPath = new Path();
        mPath.moveTo(0,0);
        for(int i = 0; i <=30 ;i++) {
            mPath.lineTo(i*35, (float) (Math.random()*100));
        }
        mEffect = new PathEffect[6];
        mDesc = new String[]{"没效果" ,"CornerPathEffect","DiscretePathEffect",
                "DashPathEffect","PathDashEffect","ComposePathEffect"};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //PathEffect 1 没效果
       mEffect[0] = null;
       //2 CornerPathEffect 拐角处变得圆滑，由参数决定平滑的力度
       mEffect[1] = new CornerPathEffect(30);
       // 3 DiscretePathEffect 使用这个线段上会产生许多杂点
       mEffect[2] = new DiscretePathEffect(3.0F,5.0F);
       // 4 DashPathEffect 绘制虚线效果 用一组数组来设置各个点之间的间隔 另一个参数phase则用来
        //控制绘制时数组的一个偏移量，通常可以通过设置值来实现路劲的动态效果
       mEffect[3] = new DashPathEffect(new float[]{20,10,5,10,},0);

       Path path = new Path();
       path.addRect(0,0,8,8,Path.Direction.CCW);
       // 5 方形点的虚线 同DashPathEffect 但更强大
       mEffect[4] = new PathDashPathEffect(path,12,0,PathDashPathEffect.Style.ROTATE);
       //6 组合实现
       mEffect[5] = new ComposePathEffect(mEffect[3],mEffect[1]);

       canvas.translate(0,100);
       for(int i = 0 ; i < mEffect.length;i++) {
           canvas.drawText(mDesc[i],500,0,mTextPaint);
           canvas.translate(0,50 );
           mPaint.setPathEffect(mEffect[i]);
           canvas.drawPath(mPath,mPaint);
           canvas.translate(0,200);
       }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredWidth()+getMeasuredWidth()/2);
    }
}
