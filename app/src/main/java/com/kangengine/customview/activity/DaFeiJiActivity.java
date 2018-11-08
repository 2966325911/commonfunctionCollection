package com.kangengine.customview.activity;

import android.content.DialogInterface;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.widget.DaFeiJiGameView;

public class DaFeiJiActivity extends BaseActivity {

    private DaFeiJiGameView daFeiJiGameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        daFeiJiGameView = new DaFeiJiGameView(this);
        setContentView(daFeiJiGameView);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        daFeiJiGameView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        daFeiJiGameView.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            daFeiJiGameView.stop();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("确定要退出游戏吗");
            alertDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Process.killProcess(Process.myPid());
                }
            });

            alertDialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    daFeiJiGameView.start();
                }
            });
            alertDialog.show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
