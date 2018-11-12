package com.kangengine.customview;

import android.os.Bundle;
import android.view.View;

import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastBlackStyle;
import com.hjq.toast.style.ToastQQStyle;
import com.hjq.toast.style.ToastWhiteStyle;

/**
 * @author Vic
 * ToastUitl git 地址 ：https://github.com/getActivity/ToastUtils
 */
public class ToastAtivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toastctivity);
    }


    public void show1(final View v) {
        ToastUtils.show("我是吐司");
    }

    public void show2(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show("我是子线程中弹出的吐司");
            }
        }).start();
    }

    public void show3(View v) {
        ToastUtils.initStyle(new ToastWhiteStyle());
        ToastUtils.show("动态切换吐司样式成功");
    }

    public void show4(View v) {
        ToastUtils.initStyle(new ToastBlackStyle());
        ToastUtils.show("动态切换吐司样式成功");
    }

    public void show5(View v) {
        ToastUtils.initStyle(new ToastQQStyle());
        ToastUtils.show("QQ样式那种还不简单，分分钟的事");
    }

    public void show6(View v) {
        ToastUtils.setView(this, R.layout.toast_custom_view);
        ToastUtils.show("我是自定义Toast");
    }

    public void show7(View v) {
        ToastUtils.show(ToastUtils.isNotificationEnabled(this));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 如果通知栏的权限被手动关闭了
        if (!ToastUtils.isNotificationEnabled(this) && "XToast".equals(ToastUtils.getToast().getClass().getSimpleName())) {
            // 因为吐司只有初始化的时候才会判断通知权限有没有开启，根据这个通知开关来显示原生的吐司还是兼容的吐司
            ToastUtils.init(getApplication());
            recreate();
            ToastUtils.show("检查到你手动关闭了通知权限，现在只能通过重启应用，吐司才能正常显示出来");
        }
    }
}
