package com.kangengine.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kangengine.customview.R;
import com.kangengine.customview.widget.CircleCountDownView;

/**
 * @author Vic
 * desc 圆形倒计时
 */
public class CircleCountdownTimeActivity extends AppCompatActivity {

    private CircleCountDownView mCountdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_countdown_time);
        initView();
    }

    private void initView() {
        mCountdown = findViewById(R.id.countdown);

    }

    public void startCountdown(View view){
        mCountdown.startCountdownTime();
    }

    public void endCountdown(View view) {
        mCountdown.endCountdownTime();
    }
}
