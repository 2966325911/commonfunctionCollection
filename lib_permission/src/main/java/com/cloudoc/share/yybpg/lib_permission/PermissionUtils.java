package com.cloudoc.share.yybpg.lib_permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;

import com.cloudoc.share.yybpg.lib_permission.setting.ISetting;
import com.cloudoc.share.yybpg.lib_permission.setting.Normal;
import com.cloudoc.share.yybpg.lib_permission.setting.OPPO;
import com.cloudoc.share.yybpg.lib_permission.setting.VIVO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author : Vic
 * time   : 2018/06/20
 * desc   :
 */
public class PermissionUtils {
    public static final int DEFAULT_REQUEST_CODE = 0xABC1994;
    private static SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }


    private static HashMap<String, Class<? extends ISetting>> permissionMenu = new HashMap<>();

    /**
     * 默认
     */
    private static final String MANUFACTURER_DEFAULT = "Default";
    /**
     * 华为
     */
    public static final String MANUFACTURER_HUAWEI = "huawei";
    /**
     * 魅族
     */
    public static final String MANUFACTURER_MEIZU = "meizu";
    /**
     * 小米
     */
    public static final String MANUFACTURER_XIAOMI = "xiaomi";
    /**
     * 索尼
     */
    public static final String MANUFACTURER_SONY = "sony";
    /**
     * oppo
     */
    public static final String MANUFACTURER_OPPO = "oppo";
    /**
     * lg
     */
    public static final String MANUFACTURER_LG = "lg";
    /**
     * vivo
     */
    public static final String MANUFACTURER_VIVO = "vivo";
    /**
     * 三星
     */
    public static final String MANUFACTURER_SAMSUNG = "samsung";
    /**
     *  乐视
     */
    public static final String MANUFACTURER_LETV = "letv";
    /**
     * 中兴
     */
    public static final String MANUFACTURER_ZTE = "zte";
    /**
     * 酷派
     */
    public static final String MANUFACTURER_YULONG = "yulong";
    /**
     * 联想
     */
    public static final String MANUFACTURER_LENOVO = "lenovo";

    static {
        permissionMenu.put(MANUFACTURER_DEFAULT, Normal.class);
        permissionMenu.put(MANUFACTURER_OPPO, OPPO.class);
        permissionMenu.put(MANUFACTURER_VIVO, VIVO.class);
    }

    /**
     * 检测时候有权限需要申请
     * @param context
     * @param permissions
     * @return true有  false 无
     */
    public static boolean hasPermission(Context context,String ... permissions) {
        if(permissions == null) {
            return false;
        }

        for(String permission : permissions) {
            if(permissionExists(permission) && !hasSelfPermission(context,permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检测某个权限是否可以授权，如果已授权则返回true,如果未安装则返回false
     * @param context
     * @param permission
     * @return
     */
    private static boolean hasSelfPermission(Context context,String permission) {
        try{
            return ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * @Description 如果在这个SDK版本存在的权限，则返回true
     * @version
     */
    private static boolean permissionExists(String permission) {
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }

    public static boolean verifyPermission(Context context, int ... gantedResults) {

        if (gantedResults == null || gantedResults.length == 0 ) {
            return false;
        }

        for (int ganted : gantedResults) {
            if (ganted != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 用于检查需要授权是否显示理由
     * @param context
     * @param permissions
     * @return
     */
    public static boolean shouldShowRequestPermissionRationale(Activity context,String... permissions) {
        if(permissions == null) {
            return false;
        }
        for(String permission : permissions) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(context,permission)){
                return true;
            }
        }
        return false;
    }


    public static void invokeAnnotation(Object object,Class annotationClass) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        if(methods != null && methods.length > 0) {
            for(Method method : methods) {
                boolean isHasAnnotation = method.isAnnotationPresent(annotationClass);
                if(isHasAnnotation) {
                    method.setAccessible(true);
                    try {
                        method.invoke(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 跳转至相应的设置页面
     * @param context
     */
    public static void gotoSetting(Context context) {
        Class clazz= permissionMenu.get(Build.MANUFACTURER.toLowerCase());
        if(clazz == null) {
            clazz = permissionMenu.get(MANUFACTURER_DEFAULT);
        }
        try {
            ISetting setting = (ISetting) clazz.newInstance();
            Intent intent = setting.getSettingIntent(context);
            if(intent == null) {
                return;
            }
            context.startActivity(intent);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
