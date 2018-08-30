package com.cloudoc.share.yybpg.lib_permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.cloudoc.share.yybpg.lib_permission.core.IPermission;

/**
 * @author : Vic
 * time   : 2018/06/20
 * desc   :
 */
public class PermissionActivity extends Activity {

    private static final String PARAM_PERMISSION = "param_permission";
    private static final String PARAM_REQUEST_CODE = "param_request_code";

    private String[] mPermissions;
    private int mRequestCode;
    private static IPermission permissionListener;

    public static void requestPermission(Context context,String[] permissions,
                                         int requestCode,IPermission iPermission) {
        permissionListener = iPermission;
        Intent intent = new Intent(context,PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(PARAM_PERMISSION,permissions);
        bundle.putInt(PARAM_REQUEST_CODE,requestCode);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if(context instanceof Activity) {
            ((Activity)context).overridePendingTransition(0,0);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPermissions = getIntent().getStringArrayExtra(PARAM_PERMISSION);
        this.mRequestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE,-1);

        if(mPermissions == null || mRequestCode < 0 || permissionListener == null) {
            this.finish();
            return;
        }

        if(PermissionUtils.hasPermission(this,mPermissions)) {
            permissionListener.granted();
            finish();
            return;
        }
        ActivityCompat.requestPermissions(this,this.mPermissions,this.mRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //请求权限成功
        if (PermissionUtils.verifyPermission(this, grantResults)) {
            permissionListener.granted();
            finish();
            return;
        }

        //用户点击了不再显示
        if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
//            if (permissions.length != grantResults.length) {
//                return;
//            }
//            List<String> deinedList = new ArrayList<>();
//            for (int i = 0; i < grantResults.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                    deinedList.add(permissions[i]);
//                }
//            }
            permissionListener.denied();
            finish();
            return;
        }

        //用户取消
        permissionListener.canceled();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
