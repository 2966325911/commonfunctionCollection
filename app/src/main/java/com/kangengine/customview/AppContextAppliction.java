package com.kangengine.customview;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.kangengine.customview.util.crash.CrashHandler;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import me.ele.uetool.UETool;


/**
 * @author : Vic
 * time   : 2018/06/26
 * desc   :
 */
public class AppContextAppliction extends Application {

    private static AppContextAppliction appliction;
    @Override
    public void onCreate() {
        super.onCreate();
        appliction = this;
        CrashHandler.getInstance().init(appliction);
//        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//        if(mode == Configuration.UI_MODE_NIGHT_YES) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else if(mode == Configuration.UI_MODE_NIGHT_NO) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(appliction,null);
        //日志打印，重写isLoggable，如果为true则打印，false不打印
        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority,String tag){
                return BuildConfig.DEBUG;
            }
        });


//        LogUtils.init(this);
//        LogUtils.d("Application","============Application");

        Logger.d("Application","AppCpmtesfasfs===========");
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            private int visibleActivityCount;
            private int uetoolDismissY = -1;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                visibleActivityCount ++;
                if(visibleActivityCount == 1 && uetoolDismissY >=0 ) {
                    UETool.showUETMenu(uetoolDismissY);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                visibleActivityCount --;
                if(visibleActivityCount == 0) {
                    uetoolDismissY = UETool.dismissUETMenu();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static AppContextAppliction getInstance() {
        return appliction;
    }
}
