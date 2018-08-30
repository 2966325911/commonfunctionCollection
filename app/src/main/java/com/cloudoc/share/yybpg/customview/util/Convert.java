package com.cloudoc.share.yybpg.customview.util;

import android.content.Context;
import android.util.TypedValue;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author : Vic
 * time   : 2018/07/03
 * desc   :
 */
public class Convert {

    /**
     * px = dp* density density = dpi/160 dpi(像素密度)为160的设备上1dp = 1px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px (Context context,float dpValue) {
//        int scale = (int) context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue*scale + 0.5f);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());

    }

    /**
     * px2dp dp = px/density
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context,float pxValue) {
        int scale = (int) context.getResources().getDisplayMetrics().density;
//        return (int) (pxValue/context.getResources().getDisplayMetrics().density+0.5);
        return (int)(pxValue/scale + 0.5f);

    }
}
