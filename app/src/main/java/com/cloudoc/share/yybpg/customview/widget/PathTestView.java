package com.cloudoc.share.yybpg.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author : Vic
 * time   : 2018/07/04
 * desc   :
 */
public class PathTestView extends View {
    Path path ;
    Paint mPaint;

    public PathTestView(Context context) {
        this(context,null);
    }

    public PathTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        path = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//
//        // 设置当前点位置
//        // 后面的路径会从该点开始画
//        moveTo(float x, float y) ；
//
//        // 当前点（上次操作结束的点）会连接该点
//        // 如果没有进行过操作则默认点为坐标原点。
//        lineTo(float x, float y)  ；
//
//        // 闭合路径，即将当前点和起点连在一起
//        // 注：如果连接了最后一个点和第一个点仍然无法形成封闭图形，则close什么也不做
//        close();
        // 添加圆弧
//// 方法1
//        public void addArc (RectF oval, float startAngle, float sweepAngle)
//
////  startAngle：确定角度的起始位置
////  sweepAngle ： 确定扫过的角度
//
//        // 方法2
//        // 与上面方法唯一不同的是：如果圆弧的起点和上次最后一个坐标点不相同，就连接两个点
//        public void arcTo (RectF oval, float startAngle, float sweepAngle)
//
//        // 方法3
//        // 参数forceMoveTo：是否将之前路径的结束点设置为圆弧起点
//        // true：在新的起点画圆弧，不连接最后一个点与圆弧起点，即与之前路径没有交集（同addArc（））
//        // false：在新的起点画圆弧，但会连接之前路径的结束点与圆弧起点，即与之前路径有交集（同arcTo（3参数））
//        public void arcTo (RectF oval, float startAngle, float sweepAngle, boolean forceMoveTo)
//// 下面会详细说明
//
//
//        // 加入圆形路径
//        // 起点：x轴正方向的0度
//        // 其中参数dir：指定绘制时是顺时针还是逆时针:CW为顺时针，  CCW为逆时针
//        // 路径起点变为圆在X轴正方向最大的点
//        addCircle(float x, float y, float radius, Path.Direction dir)
//
//        // 加入椭圆形路径
//        // 其中，参数oval作为椭圆的外切矩形区域
//        addOval(RectF oval, Path.Direction dir)
//
//        // 加入矩形路径
//        // 路径起点变为矩形的左上角顶点
//        addRect(RectF rect, Path.Direction dir)
//
//        //加入圆角矩形路径
//
//        addRoundRect(RectF rect, float rx, float ry, Path.Direction dir)

    //  注：添加图形路径后会改变路径的起点

        path.moveTo(10, 100);

        path.lineTo(70, 100);

        path.lineTo(40, 270);
        path.close();
        canvas.drawPath(path,mPaint);

//        canvas.translate(350,500);
//        path.addRect(0,0,400,400,Path.Direction.CW);
//
//        canvas.drawPath(path,mPaint);
    }
}
