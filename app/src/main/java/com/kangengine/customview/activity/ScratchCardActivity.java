package com.kangengine.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kangengine.customview.R;
import com.kangengine.customview.widget.CustomHeadView;

public class ScratchCardActivity extends AppCompatActivity {

    private CustomHeadView one,two,three;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);

        initView();
    }

    private void initView() {
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);

        one.drawHeader(R.mipmap.head_s, R.mipmap.head_d);
        two.drawHeader(R.mipmap.wbx, R.mipmap.head_d);
        three.drawHeader(R.mipmap.love, R.mipmap.head_d);
    }
}
