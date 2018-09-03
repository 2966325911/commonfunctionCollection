package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.kangengine.customview.R;

/**
 * @author : Vic
 * time   : 2018/09/02
 * desc   : Shader 着色器，渲染器效果 包括以下几点
 * BitmapShader    位图Shader
 * LinerGradient   线性Shader
 * RadialGradient  光束Shader
 * SweepGradient   梯度Shader
 * ComposeShader   混合Shader
 */
public class ShaderView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private PorterDuffXfermode xfermode;
    private Bitmap mReflectBitmap;


    public ShaderView(Context context) {
        this(context,null);
    }

    public ShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bf);
//        //BitmapShader 中TileMode 三种 ClAMP 拉伸的是图片的最后一个像素，不断重复
//        // REPEAT 重复 横向，纵向不断重复
//        //MIRROR镜像 横向不断翻转重复，纵向不断翻转重复
//        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        mPaint.setShader(mBitmapShader);

        //LinearGradient
//        mPaint.setShader(new LinearGradient(0,0,400,400,
//                Color.BLUE,Color.YELLOW,Shader.TileMode.REPEAT));
        //利用LinearGradient PorterDuffXferMode创建一个具有倒影效果的图片

        Matrix matrix = new Matrix();
        //反向选装 将图片倒置
        matrix.setScale(1F,-1F);
        mReflectBitmap = Bitmap.createBitmap(mBitmap,0,0,mBitmap.getWidth(),mBitmap.getHeight(),matrix,true);
        mPaint.setShader(new LinearGradient(0,mBitmap.getHeight(),0,
                mBitmap.getHeight()+mBitmap.getHeight()/4,
                0XDD000000,0X60000000,Shader.TileMode.CLAMP));

        //这里的SRC是 ondraw中canvas绘制的黑色Rect DST是我们翻转过来的图片需要显示，
        //并需要和SRC融合在一起，有种倒影的mask渐变的效果，DST_IN  SRC_IN，SRC则是画出来的是Rect原形，
        //DST则只有倒影的图片，没有Rect渐变mask的效果，重点搞清那个是SRC 那个是DST
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //BitmapShader
//        canvas.drawCircle(300,300,100,mPaint);

        //LinearGradient
//        canvas.drawRect(0,0,400,400,mPaint);

        //绘制倒影像步骤 1；绘制原图 2 绘制倒影图  3 在倒影图上面绘制一个同样小的渐变矩形
        // 通过Mode.DST_IN模式绘制到倒影图上，从而形成一个具有过渡效果的渐变层

        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mBitmap,0,0,null);
        canvas.drawBitmap(mReflectBitmap,0,mBitmap.getHeight(),null);
        mPaint.setXfermode(xfermode);
        //绘制渐变效果矩形
        canvas.drawRect(0,mBitmap.getHeight(),mReflectBitmap.getWidth(),
                mBitmap.getHeight()*2,mPaint);
        mPaint.setXfermode(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mBitmap.getWidth(),mBitmap.getHeight()*2 + 20);
    }
}
