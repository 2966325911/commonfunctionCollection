package com.kangengine.customview.util.crash;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangengine.customview.AppContextAppliction;
import com.kangengine.customview.MainActivity;
import com.kangengine.retrofitlibrary2.util.SystemUtil;
import com.newservice.peanut_android.ConstHttpProp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author : Vic
 * time    : 2018-11-09 15:35
 * desc    :
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    /**
     * 随机实例
     */
    private static final Random DEFULT_RANDOM = new Random();

    private static CrashHandler INSTANCE = null;
    /**
     *   系统默认的UncaughtException处理类
      */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private AppContextAppliction mContext;
    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> infos = new HashMap<String, String>();
    /**
     *   用于格式化日期,作为日志文件名的一部分
     */

    @SuppressLint("SimpleDateFormat")
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        INSTANCE = InnerCrash.crashHandler;
        return INSTANCE;
    }

    private static class InnerCrash{
        private static CrashHandler crashHandler = new CrashHandler();
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(AppContextAppliction context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        showToast(mContext.getApplicationContext(), "抱歉,程序运行异常,稍后将会重启");
        restartApp();
        if (mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }

    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        String file_path = saveCrashInfo2File(ex);
        if (file_path != null && !"".equals(file_path)
                && new File(file_path).exists()) {//
            try {
                final String zip_file_path = zipFile(new File(file_path));
                if (zip_file_path != null && !"".equals(zip_file_path)
                        && new File(zip_file_path).exists()) {//
                    //  上传到服务器
                    Thread t1 = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            sendToServer(zip_file_path);
                        }

                    });
                    t1.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void showToast(final Context context, final String msg) {
        try {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 1,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            });

            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 当应用crash后，应用自动重启
     */
    private void restartApp() {
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent);
        Log.d(TAG, "===========应用重启了====");

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        System.gc();
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        String file_path = "";
        CrashInfo info = new CrashInfo();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // sb.append(key + "=" + value + "\n");
            info.setPlatform("android");
            info.setOsversion(android.os.Build.VERSION.RELEASE);
            info.setManufacturer(android.os.Build.MANUFACTURER);

            info.setModel(android.os.Build.MODEL);
            if ("DISPLAY".equals(key)) {
                info.setDisplay(value);
            }
            info.setTime(formatter.format(new Date()));
            if ("versionName".equals(key)) {
                info.setVersionName(value);
            }
            if ("versionCode".equals(key)) {
                info.setVersionCode(value);
            }
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        info.setContent(result);
        // sb.append(result);
        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + makeRandom(10) + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory() + "/yybpg/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                file_path = path + fileName;
                FileOutputStream fos = new FileOutputStream(file_path);
                String crashJson = new Gson().toJson(info);
                fos.write(crashJson.getBytes());
                fos.close();
            }
            return file_path;
        } catch (Exception e) {
        }
        return file_path;
    }

    //压缩文件
    private String zipFile(File resFile) throws FileNotFoundException,
            IOException {

        int BUFF_SIZE = 1024 * 1024;
        String zip_path = resFile.getAbsolutePath();
        String zip_name = zip_path.substring(0, zip_path.lastIndexOf(".") + 1) + "zip";
        File zipFile = new File(zip_name);

        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
                new FileOutputStream(zipFile), BUFF_SIZE));
        zipout.putNextEntry(new ZipEntry(resFile.getName()));
        byte buffer[] = new byte[BUFF_SIZE];
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                resFile), BUFF_SIZE);
        int realLength = 0;
        while ((realLength = in.read(buffer)) != -1) {
            zipout.write(buffer, 0, realLength);
        }
        zipout.flush();
        in.close();
        zipout.closeEntry();
        zipout.close();
        return zip_name;
    }

    // send file in background
    private void sendToServer(String file_path) {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--";
        String LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024;// 10KB
        File uploadFile = new File(file_path);
        try {
            URL url = new URL(ConstHttpProp.base_url +"服务器上传日志的地址");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(3600);
            conn.setConnectTimeout(3600);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);
            conn.setRequestProperty("Accept", "application/json;charset=utf-8");
            if (uploadFile != null) {
                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());
                StringBuffer sb = new StringBuffer();

                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\""
                        + "platform" + "\"" + LINE_END);
                sb.append(LINE_END);
                sb.append("android" + LINE_END);

                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\""
                        + "mobile_num" + "\"" + LINE_END);
                sb.append(LINE_END);
//                if (mContext.getAhi() != null) {
//                    sb.append("" + mContext.getAhi().getPhoneNumber() + LINE_END);
//                } else {
//                    sb.append("no_name" + LINE_END);
//                }


                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\""
                        + "audience" + "\"" + LINE_END);
                sb.append(LINE_END);
                sb.append("patient" + LINE_END);

                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);

                sb.append("Content-Disposition: form-data; name=\""
                        + "log"
                        + "\"; filename=\""
                        + java.net.URLEncoder.encode(uploadFile.getName(),
                        "UTF-8") + "\"" + LINE_END);
                sb.append("Content-Type: text/plain" + LINE_END);
                sb.append(LINE_END);

                dos.write(sb.toString().getBytes());

                FileInputStream in = new FileInputStream(file_path);

                bytesAvailable = in.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);// 设置每次写入的大小
                buffer = new byte[bufferSize];
                // Read file
                bytesRead = in.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = in.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = in.read(buffer, 0, bufferSize);
                }
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = in.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                in.close();
                dos.write(LINE_END.getBytes());
                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();

                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                String result = "";
                int res = conn.getResponseCode();
                if (res == 201) {
                    BufferedReader result_buffer = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(),
                                    "UTF-8"));
                    StringBuffer localStringBuffer = new StringBuffer();
                    while (true) {
                        String str4 = result_buffer.readLine();
                        if (str4 != null) {
                            localStringBuffer.append(str4);

                        } else {
                            result = localStringBuffer.toString().trim();
                            break;
                        }
                    }
                    System.out.println(result);
                } else {
                    Log.e("FileUploadTask", "request error");
                }
            }
        } catch (Exception e) {
            Log.e("FileUploadTask", e.toString());
        }
    }

    private String makeRandom(int len) {

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < len; i++) {
            buffer.append(DEFULT_RANDOM.nextInt(10));
        }
        return buffer.toString();
    }

}
