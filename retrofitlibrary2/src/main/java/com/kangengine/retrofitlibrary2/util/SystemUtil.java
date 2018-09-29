package com.kangengine.retrofitlibrary2.util;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.util.List;


/**
 * @author Vic
 * desc : 系统常用方法
 */
public class SystemUtil {

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageinfo;
        PackageManager packagemanager = context.getPackageManager();
        String s = context.getPackageName();
        try {
            packageinfo = packagemanager.getPackageInfo(s, 0);
        } catch (NameNotFoundException e) {
            packageinfo = null;
        }
        return packageinfo;
    }

    public static int getSoftwareVersionCode(Context context) {
        PackageInfo packageinfo = getPackageInfo(context);
        int i;
        if (packageinfo == null) {
            i = 0;
        } else {
            i = packageinfo.versionCode;
        }
        return i;
    }

    public static String getSoftwareVersionName(Context context) {
        PackageInfo packageinfo = getPackageInfo(context);
        String s;
        if (packageinfo == null) {
            s = "";
        } else {
            s = packageinfo.versionName;
        }
        return s;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }


    public static boolean checkNetWork(Context context) {
        ConnectivityManager localConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (localConnectivityManager != null) {
            NetworkInfo[] arrayOfNetworkInfo = localConnectivityManager
                    .getAllNetworkInfo();
            if (arrayOfNetworkInfo != null) {
                int k = arrayOfNetworkInfo.length;
                int i = 0;
                for (i = 0; i < k; i++) {
                    if (arrayOfNetworkInfo[i].isConnected()) {
                        break;
                    }
                }
                if (i >= k) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 调用系统安装APK
     * @param context
     * @param path
     */

    public static synchronized void install(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri
                .parse((new StringBuilder("file://")).append(path).toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 通过包名获取应用程序的名称。
     *
     * @param context
     *            Context对象。
     * @param packageName
     *            包名。
     * @return 返回包名所对应的应用程序的名称。
     */
    public static String getProgramNameByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }


    /**
     * 获取程序 图标
     * @param context
     * @param packName
     * @return
     */
    public static Drawable getAppIcon(Context context, String packName){
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packName, 0);
            return info.loadIcon(pm);
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        /**
         * 获取Android设备中所有正在运行的App
         */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


}