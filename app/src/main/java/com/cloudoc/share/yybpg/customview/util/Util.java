package com.cloudoc.share.yybpg.customview.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by ${GongWenbo} on 2018/5/18 0018.
 */
public class Util {

    // 下划线
    public static void drawUnderline(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    // 中划线
    public static void drawStrikethrough(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    // dp2px
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    /**
     * 头条屏幕适配原理
     * @param activity
     * @param application
     */
    private static void setCustomDensity(Activity activity, final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if(sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.scaledDensity;
            // 防止系统切换后不起作用
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if(newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        //这里假设 360刚好是是设计稿的宽度，得到的就是目标所需要的密度大小 density = 屏幕宽度/设计稿宽度
        float targetDensity = appDisplayMetrics.widthPixels/360;
        // 防止字体变小
        float targetScaledDensity = targetDensity*(sNoncompatScaledDensity/sNoncompatDensity);
        // 在 像素密度是160的设备上 1dp = 1px dpi = density
        int targetDensityDpi = (int) (160 * targetDensity);

        //计算好密度之后 分别给Application和Activity设置
        appDisplayMetrics.density = targetDensity;
        //scaledDensity 字体的缩放因子 字体的缩放因子，正常情况下和density相等，
        // 但是调节系统字体大小后会改变这个值
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }
}
