package com.kangengine.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.kangengine.retrofitlibrary2.util.httputil.HttpUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author : Vic
 * time    : 2018-11-07 16:21
 * desc    :
 */
public class SplashView extends FrameLayout {
    private static String IMG_PATH = null;
    public SplashView(@NonNull Context context) {
        this(context,null);
    }

    public SplashView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private static void getAndSaveNetWorkBitmap(final String urlStr) {
        Runnable getAndSaveImageRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection urlCon  = (HttpURLConnection) url.openConnection();
                    urlCon.setDoInput(true);
                    urlCon.connect();
                    InputStream inputStream = urlCon.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    saveBitmapFile(bitmap,IMG_PATH);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(getAndSaveImageRunnable).start();
    }

    private static void saveBitmapFile(Bitmap bitmap,String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        File file = new File(filePath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isFileExist(String filePath) {
        if(TextUtils.isEmpty(filePath)){
            return false;
        }
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }
}
