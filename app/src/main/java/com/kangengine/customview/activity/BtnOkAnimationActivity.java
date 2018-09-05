package com.kangengine.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kangengine.customview.R;
import com.kangengine.customview.widget.AnimationButtonOk;

public class BtnOkAnimationActivity extends AppCompatActivity {

    private AnimationButtonOk animationOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn_ok_animation);

        initView();
    }

    private void initView() {
        animationOk = findViewById(R.id.animationOk);
        animationOk.setOnAnimationListener(new AnimationButtonOk.OnAnimationListener() {
            @Override
            public void onViewClick() {
                animationOk.start();
            }

            @Override
            public void onAnimationFinished() {

            }
        });

    }
}
