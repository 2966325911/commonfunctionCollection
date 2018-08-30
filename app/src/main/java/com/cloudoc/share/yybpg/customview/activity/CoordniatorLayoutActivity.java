package com.cloudoc.share.yybpg.customview.activity;

import android.graphics.Point;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cloudoc.share.yybpg.customview.R;

public class CoordniatorLayoutActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = CoordniatorLayoutActivity.class.getSimpleName();
    private TextView textView;
    private int screenWidth;
    private int screenHeight;
    DisplayMetrics displayMetrics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordniator_layout);

        getScreenSize();
        initViews();
    }

    private void initViews() {
        textView = findViewById(R.id.dependency);
        textView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.dependency:

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) (event.getX() + v.getLeft());
                        int y = (int) (event.getY() + v.getTop());
                        Log.d(TAG,"======x=====" + x);
                        Log.d(TAG,"======y=====" + y);
                        if(x > screenWidth) {
                            x = screenWidth;
                        }
                        if(y > screenHeight) {
                            y = screenHeight;
                        }
                        int left = x - v.getWidth() / 2;
                        int top = y - v.getHeight()/2;

                        setViewLocationInCoordinatorLayout(left,top,v);

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    public static void setViewLocationInCoordinatorLayout(int left, int top, View child) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if(lp == null) {
            lp = new CoordinatorLayout.LayoutParams(child.getWidth(),child.getHeight());
        }

        lp.setMargins(left,top,0,0);
        lp.gravity = Gravity.NO_GRAVITY;
        child.setLayoutParams(lp);
    }


    public void getScreenSize() {

        displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight= displayMetrics.heightPixels;

        Log.d(TAG,"===通过Resource获取===screenWidth===" +  screenWidth);
        Log.d(TAG,"===通过Resource获取==screenHeight==" + screenHeight);

        WindowManager manager = getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        Log.d(TAG,"=====screenWidth===" +  screenWidth);
        Log.d(TAG,"=====screenHeight==" + screenHeight);

        WindowManager windowManager = getWindowManager();
        screenHeight = windowManager.getDefaultDisplay().getHeight();
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        Log.d(TAG,"===通过windowManager获取==screenWidth===" +  screenWidth);
        Log.d(TAG,"===通过windowManager获取==screenHeight==" + screenHeight);

        //由于getWidth和getHeight方法过时 故官方推荐一下方式获取
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        Log.d(TAG,"===通过display point获取==screenWidth===" +  screenWidth);
        Log.d(TAG,"===通过display point获取==screenHeight==" + screenHeight);

    }
}
