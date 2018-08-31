package com.kangengine.customview.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cloudoc.share.yybpg.lib_permission.annotation.PermissionCanceled;
import com.cloudoc.share.yybpg.lib_permission.annotation.PermissionDenied;
import com.kangengine.customview.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PermissionActivity.class.getSimpleName();
    private Button button1;
    private Button button2;
    private Button button3;
    private RxPermissions mPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        initViews();
        initRxPermission();

    }

    private void initViews() {
        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    private void initRxPermission() {
        mPermissions = new RxPermissions(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn1:
                if (isNeedPermission()) {
//                    //需要申请权限
//                    mPermissions.request(Manifest.permission.CALL_PHONE,
//                            Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            .subscribe(new Consumer<Boolean>() {
//                                @Override
//                                public void accept(Boolean aBoolean) throws Exception {
//                                    if(aBoolean){
//                                        Log.d(TAG,"===========已经授权");
//                                    } else {
//                                        Log.d(TAG,"===========未授权");
//                                    }
//                                }
//                            });
                    mPermissions.requestEach(Manifest.permission.CAMERA,
                            Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Permission>() {
                                @Override
                                public void accept(Permission permission) throws Exception {
                                    if (permission.granted) {
                                        Log.d(TAG, permission.name + "=====权限被允许==========");
                                    } else if (permission.shouldShowRequestPermissionRationale) {
                                        Log.d(TAG, permission.name + "=====权限被拒，但不是永久被拒绝=====权限被拒绝");
                                    } else {
                                        Log.d(TAG, permission.name + "===== 永久被拒绝，不在询问=====权限被拒绝");
                                    }
                                }
                            });

                } else {


                }
                break;
            case R.id.btn2:
                permissionSetting();
                break;
            case R.id.btn3:
                permissionTest();
                break;
            default:
                break;
        }
    }



    @com.cloudoc.share.yybpg.lib_permission.annotation.Permission(value = {
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    public void permissionTest() {
        Toast.makeText(this, "申请权限", Toast.LENGTH_SHORT).show();
    }
    @PermissionCanceled(requestCode = 200)
    private void cancelCode200(){
        Toast.makeText(this, "cancel__200", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(requestCode = 200)
    private void denyCode200(){
        Toast.makeText(this, "deny__200", Toast.LENGTH_SHORT).show();
    }

    @PermissionCanceled()
    private void cancel() {
        Log.i(TAG, "writeCancel: " );
        Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied()
    private void deny() {
        Log.i(TAG, "writeDeny:");
        Toast.makeText(this, "deny", Toast.LENGTH_SHORT).show();
    }

    public boolean isNeedPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    private void permissionSetting() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }
}
