package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author : Vic
 * time   : 2018/07/04
 * desc   :
 */
public class Canvastest extends View {

    private Paint mPaint;
    private static final String TEXT = "绘制文字";

    public Canvastest(Context context) {
        this(context,null);
    }

    public Canvastest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPoint(100,100,mPaint);
        canvas.drawPoints(new float[]{
                150f,150f,
                200f,200f
        },mPaint);

        canvas.drawLine(100,110,150,110,mPaint);

        canvas.drawLines(new float[]{
                100,120,150,120,
                100,130,150,130
        },mPaint);

        canvas.drawRoundRect(100,150,200,200,30,30,mPaint);

        RectF rectF = new RectF(100,200,200,300);
        canvas.drawRoundRect(rectF,30,30,mPaint);

        canvas.drawOval(100,400,300,500,mPaint);
        canvas.drawCircle(100,600,50,mPaint);


        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        canvas.drawArc(200,100,300,200,0,60,true,mPaint);
        canvas.drawArc(350,100,450,200,180,270,true,mPaint);
        canvas.drawArc(500,100,600,200,270,360,true,mPaint);
        canvas.drawArc(650,100,750,200,360,0,true,mPaint);

        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(TEXT,300,300,mPaint);

        canvas.drawText(TEXT,0,TEXT.length(),300,340,mPaint);
        char[] chars = TEXT.toCharArray();
        canvas.drawPosText(TEXT,new float[]{
                400,400,
                400,440,
                400,480,
                400,520
        },mPaint);


//        Path path = new Path();
//        // 2. 设置路径轨迹
//        path.cubicTo(540, 750, 640, 450, 840, 600);
//        // 3. 画路径
//        canvas.drawPath(path,mPaint);
//        // 4. 画出在路径上的字
//        canvas.drawTextOnPath("在Path上写的字:Carson_Ho", path, 50, 0, mPaint);

        Picture picture = new Picture();
        picture.beginRecording(100,100);
        canvas.drawCircle(300,800,50,mPaint);
        picture.endRecording();


        PictureDrawable drawable = new PictureDrawable(picture);
        drawable.setBounds(0,0,picture.getWidth(),picture.getHeight());
        drawable.setBounds(300,900,600,picture.getHeight());

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.android_pic);
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = getResources().getAssets().open("android_pic.png");
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        canvas.drawBitmap(bitmap,new Matrix(),new Paint());

        canvas.rotate(90);
        canvas.skew(0,1f);
        canvas.clipRect(300,500,600,800);
        canvas.translate(300, 500);

        //原来画布设置为灰色
        canvas.drawColor(Color.GRAY);

        //第一次裁剪
        canvas.clipRect(0, 0, 600, 600);

        //将第一次裁剪后的区域设置为红色
        canvas.drawColor(Color.RED);

        //第二次裁剪,并显示第一次裁剪与第二次裁剪不重叠的区域
        canvas.clipRect(0, 200, 600, 400, Region.Op.DIFFERENCE);
//        canvas.clipOutRect(0, 200, 600, 400);

        //将第一次裁剪与第二次裁剪不重叠的区域设置为黑色
        canvas.drawColor(Color.BLACK);


    }
}
