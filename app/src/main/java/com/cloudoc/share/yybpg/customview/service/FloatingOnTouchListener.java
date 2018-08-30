package com.cloudoc.share.yybpg.customview.service;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * @author : Vic
 * time   : 2018/06/21
 * desc   :
 */
public class FloatingOnTouchListener implements View.OnTouchListener {
    private int  x;
    private int y;
    private WindowManager manager;
    private WindowManager.LayoutParams layoutParams;
    private View view;
    public FloatingOnTouchListener(WindowManager manager,
                                   WindowManager.LayoutParams layoutParams,View view) {
        this.manager = manager;
        this.layoutParams = layoutParams;
        this.view = view;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getRawX();
                int nowY = (int) event.getRawY();

                int moveX = nowX - x;
                int moveY = nowY - y;

                layoutParams.x += moveX;
                layoutParams. y += moveY;
                manager.updateViewLayout(view,layoutParams);

                x = nowX;
                y = nowY;


                break;
            default:
                break;
        }
        return false;
    }
}
