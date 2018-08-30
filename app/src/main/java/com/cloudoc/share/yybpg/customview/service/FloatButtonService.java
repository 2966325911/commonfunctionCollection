package com.cloudoc.share.yybpg.customview.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

public class FloatButtonService extends Service {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button button;

    public FloatButtonService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initWindowManager();
    }

    private void initWindowManager() {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.format = PixelFormat.RGB_565;
        layoutParams.gravity = Gravity.CENTER | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            button = new Button(this);
            button.setText("Floating button");
            button.setBackgroundColor(Color.RED);
            mWindowManager.addView(button, layoutParams);

           button.setOnTouchListener(new FloatingOnTouchListener(mWindowManager,layoutParams,button));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mWindowManager != null && button != null) {
            mWindowManager.removeView(button);
        }

    }
}
