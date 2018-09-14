package com.kangengine.customview.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author : Vic
 * time   : 2018/09/14
 * desc   :  利用hook技术 本质是反射，来处理在Activity中需要有回传至的startActivityForResult()
 * step 1:在自定义的Applicaiton中调用HookUtil.hookActivityThreadHandler()
 * step 2: OnResultManager onResultManager = new OnResultManager(this);
 * Intent intent = new Intent(this, NotificationActivity.class);
 * onResultManager.startForResult(intent, 1, new OnResultManager.Callback() {
 * @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
 * <p>
 * }
 * });
 *
 * 借鉴别人的整合了，只是一种思路，但是本人认为这种并不如原生的好用，
 * 依然不能节省什么，甚至还用了一大堆的反射
 *
 */
public class HookResultUtil {
    public static void hookActivityThreadHandler() throws Exception {
        final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field currentActivityThreadFiled = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        currentActivityThreadFiled.setAccessible(true);
        //静态无参数 get(null)调用
        final Object currentActivityThread = currentActivityThreadFiled.get(null);

        //获取ActivityThread中的mH即Handler
        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Handler mH = (Handler) mHField.get(currentActivityThread);

        Handler.Callback mHCallback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    //这里108 = SEND_RESULT 源码中ActivityThread
                    if (msg.what == 108) {
                        Object resultData = msg.obj;
                        Field mActivitiesField = activityThreadClass.getDeclaredField("mActivities");
                        mActivitiesField.setAccessible(true);
                        ArrayMap mActivities = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            mActivities = (ArrayMap) mActivitiesField.get(currentActivityThread);
                        } else {
                            return false;
                        }

                        Class<?> resultDataClass = Class.forName("android.app.ActivityThread$ResultData");
                        Field tokeField = resultDataClass.getDeclaredField("token");
                        tokeField.setAccessible(true);
                        IBinder token = (IBinder) tokeField.get(resultData);

                        //r 是ActivityClientRecord类型
                        Object r = mActivities.get(tokeField);
                        Class<?> ActivityClientRecordClass = Class.forName("android.app.ActivityThread$ActivityClientRecord");
                        Field activityField = ActivityClientRecordClass.getDeclaredField("activity");
                        activityField.setAccessible(true);

                        //拿到activity
                        Activity activity = (Activity) activityField.get(r);

                        Field resultsField = resultDataClass.getDeclaredField("results");
                        resultsField.setAccessible(true);
                        List results = (List) resultsField.get(resultData);

                        Object resultInfo = results.get(0);

                        Class<?> resultInfoClass = Class.forName("android.app.ResultInfo");
                        Field mRequestCodeField = resultInfoClass.getDeclaredField("mRequestCode");
                        mRequestCodeField.setAccessible(true);
                        int mRequestCode = (int) mRequestCodeField.get(resultInfo);

                        Field mResultCodeFiled = resultDataClass.getDeclaredField("mResultCode");
                        mRequestCodeField.setAccessible(true);
                        int mResultCode = (int) mResultCodeFiled.get(resultInfo);

                        Field mDataField = resultDataClass.getDeclaredField("mData");
                        mDataField.setAccessible(true);
                        Intent mData = (Intent) mDataField.get(resultInfo);

                        new OnResultManager(activity).trigger(mRequestCode, mResultCode, mData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        };

        Field mCallBackField = Handler.class.getDeclaredField("mCallback");
        mCallBackField.setAccessible(true);
        mCallBackField.set(mH, mHCallback);
    }

}
