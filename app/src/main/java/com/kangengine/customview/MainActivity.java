package com.kangengine.customview;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;


import com.cloudoc.share.yybpg.customview.R;
import com.kangengine.customview.activity.CircleMenuLayoutActivity;
import com.kangengine.customview.activity.LoginActivity;
import com.kangengine.customview.activity.UiTestActivity;
import com.kangengine.customview.ui.FlowLayoutActivity;
import com.kangengine.customview.ui.PermissionActivity;
import com.kangengine.customview.ui.ShoppingActvity;
import com.kangengine.customview.activity.CanvasActivity;
import com.kangengine.customview.activity.CoordniatorLayoutActivity;
import com.kangengine.customview.activity.CustomActivity;
import com.kangengine.customview.activity.DaFeiJiActivity;
import com.kangengine.customview.activity.FloatActivity;
import com.kangengine.customview.activity.PathAcitivty;
import com.kangengine.customview.activity.PicPasswordActivity;
import com.kangengine.customview.activity.PreferenceActivity;
import com.kangengine.customview.activity.ScratchCardActivity;

import me.ele.uetool.UETool;

public class MainActivity extends BaseActivity implements LifecycleOwner {

    private static final String PACKAGENAME_CLASSNAME = "MainActivity";
    private LifecycleRegistry mLifecycle = new LifecycleRegistry(this);

    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UETool.showUETMenu();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void toFlowActivity(View view) {
        startActivity(FlowLayoutActivity.class, null);
    }

    private void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);


    }

    public void toShoppingActivity(View vew) {
        startActivity(ShoppingActvity.class, null);
    }

    public void toPermissionActivity(View view) {
        startActivity(PermissionActivity.class, null);
    }

    public void toFloatActivity(View view) {
        startActivity(FloatActivity.class, null);
    }

    public void toCustomView(View view) {
        startActivity(CustomActivity.class, null);
    }

    public void switchDayOrNight(View view) {
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (mode) {
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        recreate();
    }

    /**
     * 跳转到相应的自定义圆形菜单页面
     * @param view
     */
    public void circleMenuLayout(View view) {
        startActivity(CircleMenuLayoutActivity.class,null);
    }


    private void createShortCut(){
        Intent shortcutIntent = new Intent();
        shortcutIntent.setComponent(new ComponentName(this.getPackageName(),PACKAGENAME_CLASSNAME));
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS|Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(this,R.mipmap.ic_launcher));
        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"快捷键名称");
        resultIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        sendBroadcast(resultIntent);

    }

    public void UiAutoTest(View view) {
//        startActivity();
        startActivity(UiTestActivity.class,null);
    }

    public void preferenceOnClick(View view) {
        startActivity(PreferenceActivity.class,null);
    }


    public void CoordniatorLayoutOnClick(View view) {
        startActivity(CoordniatorLayoutActivity.class,null);
    }

    public void dafeijiActivity(View view) {
        startActivity(DaFeiJiActivity.class,null);
    }


    /**
     * 图案密码页面
     * @param view
     */
    public void gotoPasswordActivity(View view) {
        toNextActivity(PicPasswordActivity.class,null);
    }

    /**
     * 刮刮卡效果页面
     * @param view
     */
    public void gotoScratchActivity(View view) {
        toNextActivity(ScratchCardActivity.class,null);
    }


    public void toCanvasActivity(View view) {
        startActivity(new Intent(this, CanvasActivity.class));
    }

    public void toPathActivity(View view) {
        startActivity(new Intent(this, PathAcitivty.class));
    }

    /**
     * 进入指纹认证界面
     * @param view
     */
    public void toFingerAutActivity(View view) {
        toNextActivity(LoginActivity.class,null);
    }
}
