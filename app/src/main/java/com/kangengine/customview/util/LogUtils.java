package com.kangengine.customview.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * @author : Vic
 * time    : 2018-11-09 15:01
 * desc    :
 */
public class LogUtils {

    public static boolean APP_DEBUG = false;

    public static void init(Context context) {
        APP_DEBUG = isApkDebuggable(context);
    }

    /**
     * 判断是否是debug模式
     * @param context
     * @return
     */
    private static boolean isApkDebuggable(Context context) {
        try{
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            return (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) !=0;
        }catch (Exception e) {

        }
        return false;
    }

    public static void d(String tag,String content){
        if(APP_DEBUG){
            Log.d(tag,content);
        }
    }

    public static void e(String tag,String content){
        if(APP_DEBUG){
            Log.e(tag, content);
        }
    }
    public static void v(String tag,String content){
        if(APP_DEBUG){
            Log.v(tag, content);
        }
    }

    public static void i(String tag,String content){
        if(APP_DEBUG){
            Log.i(tag, content);
        }
    }

    public static void w(String tag,String content){
        if(APP_DEBUG){
            Log.w(tag, content);
        }
    }
}
