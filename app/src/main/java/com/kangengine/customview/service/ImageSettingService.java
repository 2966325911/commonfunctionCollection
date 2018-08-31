package com.kangengine.customview.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.kangengine.customview.R;


public class ImageSettingService extends Service {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams layoutParams;

    private View displayView;

    public ImageSettingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initWindowManager();

    }

    private void initWindowManager() {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = PixelFormat.RGB_888;

        layoutParams.gravity = Gravity.CENTER | Gravity.TOP;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showMediaFloating();
        return super.onStartCommand(intent, flags, startId);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showMediaFloating() {
        if(Settings.canDrawOverlays(this)) {
            displayView = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.service_img_layout,null
            );

            mWindowManager.addView(displayView,layoutParams);


        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayAndRemove();
    }

    private void stopPlayAndRemove() {
        if(mWindowManager != null && displayView != null) {
            mWindowManager.removeView(displayView);
        }
    }
}
