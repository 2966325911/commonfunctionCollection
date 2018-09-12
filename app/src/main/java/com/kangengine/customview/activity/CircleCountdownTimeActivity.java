package com.kangengine.customview.activity;

import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.kangengine.customview.R;
import com.kangengine.customview.widget.CircleCountDownView;
import com.kangengine.customview.widget.CircleCountDownView1;

/**
 * @author Vic
 * desc 圆形倒计时 add tint 着色器 和 clipping 裁剪
 */
public class CircleCountdownTimeActivity extends AppCompatActivity {

    private CircleCountDownView mCountdown;
    private View v1;
    private View v2;
    private CircleCountDownView1 mCountdown1;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_countdown_time);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mCountdown = findViewById(R.id.countdown);
        mCountdown1 = findViewById(R.id.countdown1);
        v1 = findViewById(R.id.tv_rect);
        v2 = findViewById(R.id.tv_circle);
        // 使用Clipping裁剪 首先要使用ViewOutlinProvider 来修改outline，然后再通过
        // setOutlinProvider 将outline作用给视图
        ViewOutlineProvider viewOutlineProvider1 = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //修改outline为特定形状
                outline.setRoundRect(0,0,view.getWidth(),
                        view.getHeight(),30);
            }
        };

        ViewOutlineProvider viewOutlineProvider2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0,0,view.getWidth(),view.getHeight());
            }
        };

        v1.setOutlineProvider(viewOutlineProvider1);
        v2.setOutlineProvider(viewOutlineProvider2);

        mCountdown1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCountdown1.getIsStop()) {
                    mCountdown1.startCountdownTime();
                } else {
                    mCountdown1.endCountdownTime();
                }
            }
        });
    }

    public void startCountdown(View view){
        mCountdown.startCountdownTime();
    }

    public void endCountdown(View view) {
        mCountdown.endCountdownTime();
    }

}
