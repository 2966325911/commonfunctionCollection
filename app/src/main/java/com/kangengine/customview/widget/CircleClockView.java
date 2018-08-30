package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : Vic
 * time   : 2018/08/23
 * desc   :
 */
public class CircleClockView extends View {
    private int mRadius;
    private int circleX;
    private int circleY;
    /**
     * 画圆的笔
     */
    Paint circlePaint;
    /**
     * 画文字的笔
     */
    Paint textPaint;
    /**
     * 画时针的笔
     */
    Paint paintHour;
    /**
     * 画分针的笔
     */
    Paint paintMin;
    /**
     * 画秒针的笔
     */
    Paint paintSecond;

    /**
     * 画小时的线
     */
    Paint paintMinLine;
    /**
     * 画分的线
     */
    Paint paintHourLine;

    public CircleClockView(Context context) {
        this(context,null);
    }

    public CircleClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        paintHour = new Paint();
        paintHour.setAntiAlias(true);
        paintHour.setColor(Color.RED);
        paintHour.setStrokeWidth(20);

        paintMin = new Paint();
        paintMin.setAntiAlias(true);
        paintMin.setColor(Color.RED);
        paintMin.setStrokeWidth(10);

        paintSecond = new Paint();
        paintSecond.setAntiAlias(true);
        paintSecond.setColor(Color.BLUE);
        paintSecond.setStrokeWidth(8);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(2);

        paintHourLine = new Paint();
        paintHourLine.setAntiAlias(true);
        paintHourLine.setColor(Color.RED);
        paintHourLine.setStyle(Paint.Style.STROKE);
        paintHourLine.setStrokeWidth(8);


        paintMinLine = new Paint();
        paintMinLine.setAntiAlias(true);
        paintMinLine.setColor(Color.RED);
        paintMinLine.setStyle(Paint.Style.STROKE);
        paintMinLine.setStrokeWidth(5);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLUE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(100);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawCircle(circleX,circleY,mRadius, circlePaint);
        drawLines(canvas);

        drawText(canvas);

        initCurrentTime(canvas);

        postInvalidateDelayed(1000);

    }

    private void drawLines(Canvas canvas) {
        for(int i =0; i < 60;i++) {
            if(i % 5 == 0) {
                canvas.drawLine(getWidth()/2,getHeight()/2 - getWidth()/2,getWidth()/2,getHeight()/2 - getWidth()/2 + 60,paintHourLine);
            }else {
                canvas.drawLine(getWidth()/2,getHeight()/2 - getWidth()/2,getWidth()/2,getHeight()/2 - getWidth()/2 + 30,paintMinLine);
            }
            canvas.rotate(6,getWidth()/2,getHeight()/2);

        }
    }

    private void drawText(Canvas canvas) {
        float textSize = textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top;

        int distance = getWidth()/2 - 60 - 100;
        float a ,b;
        for(int i = 0; i < 12;i++) {
            a = (float) (distance * Math.sin(i*30*Math.PI/180) + getWidth()/2);
            b = (float) (getHeight()/2 - distance * Math.cos(i*30 *Math.PI/180));
            if(i == 0) {
                canvas.drawText(String.valueOf(12),a - textPaint.measureText("12")/2 ,b + textSize/3, textPaint);
            } else {
                canvas.drawText(String.valueOf(i),a- textPaint.measureText(String.valueOf(i))/2,b+ textSize/3, textPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        mRadius = width/2;
        circleX = width/2;
        circleY = height/2;
    }

    /**
     * 获取当前系统时间
     *
     * @param canvas 画布
     */
    private void initCurrentTime(Canvas canvas) {
        canvas.drawCircle(circleX,circleY,30,paintSecond);
        //获取系统当前时间
        SimpleDateFormat format = new SimpleDateFormat("HH-mm-ss");
        String time = format.format(new Date(System.currentTimeMillis()));
        String[] split = time.split("-");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);
        int second = Integer.parseInt(split[2]);
        //时针走过的角度
        int hourAngle = hour * 30 + minute / 2;
        //分针走过的角度
        int minuteAngle = minute * 6 + second / 10;
        //秒针走过的角度
        int secondAngle = second * 6;

        //绘制时钟,以12整点为0°参照点
        canvas.rotate(hourAngle, circleX, circleY);
        canvas.drawLine(circleX, circleY, circleX, circleY - mRadius + 150, paintHour);
        canvas.save();
        canvas.restore();
        //这里画好了时钟，我们需要再将画布转回来,继续以12整点为0°参照点
        canvas.rotate(-hourAngle, circleX, circleY);

        //绘制分钟
        canvas.rotate(minuteAngle, circleX, circleY);
        canvas.drawLine(circleX, circleY, circleX, circleY - mRadius + 80, paintMin);
        canvas.save();
        canvas.restore();
        //这里同上
        canvas.rotate(-minuteAngle, circleX,circleY);

        //绘制秒钟
        canvas.rotate(secondAngle, circleX, circleY);
        canvas.drawLine(circleX, circleY, circleX, circleY - mRadius + 30, paintSecond);
    }

}
