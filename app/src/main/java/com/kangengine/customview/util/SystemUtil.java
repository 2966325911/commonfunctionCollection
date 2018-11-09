package com.kangengine.customview.util;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.kangengine.customview.AppContextAppliction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : Vic
 * time    : 2018-11-09 15:50
 * desc    :
 */

public class SystemUtil {
    public static final String CHANNEL_ID = "channelId";

    public static String getNetworkTypeName(Context context) {
        TelephonyManager telephonymanager;
        String s;
        NetworkInfo anetworkinfo[];
        int i;
        ConnectivityManager connectivitymanager = (ConnectivityManager) context.getSystemService("connectivity");
        telephonymanager = (TelephonyManager) context.getSystemService("phone");
        s = null;
        anetworkinfo = connectivitymanager.getAllNetworkInfo();
        i = 0;

        for (i = 0; i < anetworkinfo.length; i++) {
            if (anetworkinfo[i].isConnected()) {
                if (anetworkinfo[i].getTypeName().toUpperCase().contains("MOBILE") || (anetworkinfo[i].getTypeName().toUpperCase().contains("WIFI"))) {
                    s = String.valueOf(telephonymanager.getNetworkType());
                    break;
                }
            }
        }

        if (i >= anetworkinfo.length) {
            s = "UNKNOWN";
        }
        return s;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageinfo;
        PackageManager packagemanager = context.getPackageManager();
        String s = context.getPackageName();
        try {
            packageinfo = packagemanager.getPackageInfo(s, 0);
        } catch (PackageManager.NameNotFoundException e) {

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

    public static boolean CheckNetWork(Context context) {
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
     * 获取内网 ip地址
     *
     * @param context
     * @return
     */
    public static String getIpAddress(Context context) {
        String ip = "";
        ConnectivityManager conMana = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = conMana.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = conMana.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobileNetworkInfo.isConnected()) {
            ip = getLocalAddress();
        } else if (wifiNetworkInfo.isConnected()) {
            WifiManager wifiMana = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMana.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        }
        return ip;
    }

    /**
     * 将ip的整数形式转换成ip形式
     * 如1678485696  ---> 192.168.11.100
     *
     * @param ipAddress
     * @return
     */
    private static String intToIp(int ipAddress) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipAddress & 0xFF).append(".");
        sb.append((ipAddress >> 8) & 0xFF).append(".");
        sb.append((ipAddress >> 16) & 0xFF).append(".");
        sb.append((ipAddress >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取内网地址(局域网的)  192.168.11.100
     *
     * @return
     */
    public static String getLocalAddress() {
        try {
            String ipv4;
            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nilist) {
                ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address : ialist) {
                    //不是回送地址，且是ipv4 的
                    if (!address.isLoopbackAddress() && (address instanceof Inet4Address)) {
                        ipv4 = address.getHostAddress();
                        return ipv4;
                    }
                }
            }

        } catch (Exception e) {
            Log.e("localip", e.toString());
        }
        return null;
    }

    //获取渠道id，用于统计app
    public static String getChannelId(Context context) {
        ApplicationInfo appInfo;
        String channelId = "yybpg";//默认app是官网渠道
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != appInfo) {
                channelId = appInfo.metaData.getString(CHANNEL_ID);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelId;
    }


    /**
     * 根据Wifi信息获取本地的mac地址设别开通wifi连接 ，获取到网卡的MAC地址
     *
     * @param context
     * @return
     */
    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String getAdresseMAC(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if (wifiInf != null && marshmallowMacAddress.equals(wifiInf.getMacAddress())) {
            String result = null;
            try {
                result = getAdressMacByInterface();
                if (result != null) {
                    return result;
                } else {
                    result = getAddressMacByFile(wifiMan);
                    return result;
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        } else {
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                return wifiInf.getMacAddress();
            } else {
                return "";
            }
        }
        return marshmallowMacAddress;
    }

    private static String getAdressMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }

    /**
     * 判断是否在充电
     * @param context
     * @return
     */
    public static boolean isCharging(Context context) {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);
        // 是否在充电
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;
    }


    /**
     * 控制电源
     * @param context
     * @param value  0 表示关闭供电 1表示开启供电
     */
    public static void sendD1SMsg(Context context, int value) {
        File file = new File("/sys/medical_drv/medical_states");
        FileWriter mWriter = null;
        try {
            mWriter = new FileWriter(file);
            mWriter.write(String.valueOf(value));
            mWriter.flush();
            mWriter.close();
            mWriter = null;
        } catch (Exception e) {
            if (value == 1) {
                Toast.makeText(context, "血压计无法正常供电，请先结束测量再重新开始测量", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *  控制电源 0 表示关闭供电 1表示开启供电
     */
    public static int getDeviceState(Context context) {
        File file = new File("/sys/medical_drv/medical_states");
        FileReader mReader = null;
        int value = 0;
        BufferedReader br = null;
        String s = null;
        try {
            mReader = new FileReader(file);
            br = new BufferedReader(mReader);
            while ((s = br.readLine()) != null) {
                value = Integer.parseInt(s);
            }
            mReader.close();
            mReader = null;
        } catch (Exception e) {
            Toast.makeText(context, "机器无法供电", Toast.LENGTH_SHORT).show();
        }
        return value;
    }

    /**
     * 控制电源 板子供电
     * @param context
     * @param value  0 表示关闭供电 1表示开启供电
     */
    public static void glucoseSupplyPower(Context context, int value) {
        File file = new File("sys/medical_drv/sugar_states");
        FileWriter mWriter = null;
        try {
            mWriter = new FileWriter(file);
            mWriter.write(String.valueOf(value));
            mWriter.flush();
            mWriter.close();
            mWriter = null;
        } catch (Exception e) {
            if (value == 1) {
                Toast.makeText(context, "机器无法正常供电", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 获取设备唯一Id  deviceId
     * @return
     */
    public static String getDeviceId() {
        if (AppContextAppliction.getInstance() != null) {
            TelephonyManager tm = (TelephonyManager) AppContextAppliction.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                if (ActivityCompat.checkSelfPermission(AppContextAppliction.getInstance(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                return  tm.getDeviceId();
            }
        }
        return null;
    }

    /**
     * 判断屏幕是亮着还是熄灭的
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public static boolean screenOffOrOn(Context context) {
        PowerManager powerManager = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        boolean isOpen = powerManager.isInteractive();
        return  isOpen;
    }




    /**
     * 卸载APK
     * @param context
     * @param packageName 包名
     */
    public static synchronized  void unInstallApk(Context context,String packageName) {
        Intent intent = new Intent();
        Uri uri = Uri.parse("package:" + packageName);
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 调用系统安装APK
     * @param context
     * @param path
     */

    public static synchronized void install(Context context,String path) {
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
        } catch (PackageManager.NameNotFoundException e) {
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
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 判断该应用是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean appIsInstall(Context context,String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for(int i = 0 ; i < packageInfos.size();i++) {
                if(packageInfos.get(i).packageName.equalsIgnoreCase(packageName)) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
