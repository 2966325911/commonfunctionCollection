package com.kangengine.customview.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cloudoc.share.yybpg.customview.R;
import com.kangengine.customview.service.FloatButtonService;
import com.kangengine.customview.service.ImageSettingService;
import com.kangengine.customview.service.MediaPlayService;


public class FloatActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x11;
    private int mTouchSlop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);
    }


    public boolean applyPermissionOver(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(this)) {
                toast("授权成功===");
                return true;
            }  else {
                toast(" 当前无权限，请授权");
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())),REQUEST_CODE);
            }
        } else {
            toast("此版本低于6.0无法展示");
        }
        return false;

    }

    /**
     * 进入悬浮窗口Button的service
     * @param view
     */
    public void startFloatingButtonService(View view) {
        if(applyPermissionOver()) {
            startService(new Intent(this,FloatButtonService.class));
        }
    }

    /**
     * 进入悬浮窗口图片展示的Service
     * @param view
     */
    public void startFloatingImageDisplayService(View view) {
        if(applyPermissionOver()) {
            startService(new Intent(this, ImageSettingService.class));
        }
    }


    /**
     * 进入悬浮窗口的视频展示的Service
     * @param view
     */
    public void startFloatingVideoService(View view) {
        if(applyPermissionOver()) {
            startService(new Intent(this, MediaPlayService.class));
        }
    }


    private void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode == RESULT_OK) {
           if(requestCode == REQUEST_CODE) {
               applyPermissionOver();
           }
       }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, FloatButtonService.class));
        stopService(new Intent(this,MediaPlayService.class));
        stopService(new Intent(this,ImageSettingService.class));
    }
}
